#!/usr/bin/perl

use DBI;
use XBase;

#my $rootdir = "/Users/mjpan/dental";
my $homedir = `echo \$HOME`;
chomp($homedir);
my $rootdir = "$homedir/dental";
my $origdir = "$rootdir/DBFS";
my $convdir = "$rootdir/MySQL";
my $columnsDir = "$rootdir/columns";

#note: cannot end select statement with ";" for xbase

#If you are converting from another program, there are certain things you need to do. To group family members, simply give them the same patient.Guarantor number. To do this, you must have a way to uniquely identify each patient and guarantor from the old system. If the export features of the old program are inadequate, you might have to just use patient names. Each patient name will have to be unique if you are using them to group families (you can add periods to duplicate names to make them unique). To establish balances, add an adjustment to each patient. Setting any of the fields in the patient table will not work because those fields are recomputed every time the patient's account is opened. One last thing you should do is run UPDATE patient SET BillingType=40, PriProv=1; which will set those column value for every patient in the database.

#to setup mysql accounts
##set the root password
#shell> mysql -u root
#mysql> UPDATE mysql.user SET Password = PASSWORD('newpwd')
#    ->     WHERE User = 'root';
#mysql> FLUSH PRIVILEGES;
##set the user accounts
#mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'localhost'
#    ->     IDENTIFIED BY 'some_pass' WITH GRANT OPTION;
#mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'%'
#    ->     IDENTIFIED BY 'some_pass' WITH GRANT OPTION;

my %mysqlParamsHash =();
$mysqlParamsHash{"dbhost"} = "localhost";
$mysqlParamsHash{"dbname"} = "dental";
$mysqlParamsHash{"dbuser"} = "apache";
$mysqlParamsHash{"dbpwd"} = "apache";
$mysqlParamsHash{"dbport"} = 5432;
$mysqlParamsHash{"dbms"} = "mysql";

#map xbase fields to new db fields

my %xbasecodes =();
$xbasecodes{"C"} = "text";
$xbasecodes{"M"} = "text";
$xbasecodes{"X"} = "text";
$xbasecodes{"N"} = "text";
$xbasecodes{"L"} = "boolean";
$xbasecodes{"D"} = "date";

#map of [human relation name][xbase column name]->[mysql column names]
my %columns =();
$columns{"patient"}{"PRCN"} = ["RCN"];#, "Guarantor"];
$columns{"patient"}{"PLAST"} = ["LName"];
$columns{"patient"}{"PFIRST"} = ["FName"];
$columns{"patient"}{"PMIDDLE"} = ["MiddleI"];
$columns{"patient"}{"PNICKNAME"} = ["Preferred"];
$columns{"patient"}{"PTITLE"} = ["Salutation"];
$columns{"patient"}{"PMARITAL"} = ["PatStatus"];
$columns{"patient"}{"PGENDER"} = ["Gender"];
$columns{"patient"}{"PGUAR"} = ["Guarantor"];
$columns{"patient"}{"PBDATE"} = ["Birthdate"];
$columns{"patient"}{"PSSN"} = ["SSN"];
$columns{"patient"}{"PSTREET"} = ["Address"];
$columns{"patient"}{"PADDR2"} = ["Address2"];
$columns{"patient"}{"PCITY"} = ["City"];
$columns{"patient"}{"PSTATE"} = ["State"];
$columns{"patient"}{"PZIP"} = ["Zip"];
$columns{"patient"}{"PHPHONE"} = ["HmPhone"];
$columns{"patient"}{"PWPHONE"} = ["WkPhone"];
$columns{"patient"}{"PEPHONE"} = ["WirelessPhone"];
$columns{"patient"}{"PINSPLAN1"} = ["PriPlanNum"];
$columns{"patient"}{"RECALLINT"} = ["RecallInterval"];


#map of [human relation name][xbase column name]->[mysql column names]
#$columns{"guarantor"}{"GRCN"} = ["GRCN"];
$columns{"guarantor"}{"GLAST"} = ["LName"];
$columns{"guarantor"}{"GFIRST"} = ["FName"];
$columns{"guarantor"}{"GTITLE"} = ["Salutation"];
$columns{"guarantor"}{"GGENDER"} = ["Gender"];
$columns{"guarantor"}{"GBDATE"} = ["Birthdate"];
$columns{"guarantor"}{"GSSN"} = ["SSN"];
$columns{"guarantor"}{"GSTREET"} = ["Address"];
$columns{"guarantor"}{"GADDR2"} = ["Address2"];
$columns{"guarantor"}{"GCITY"} = ["City"];
$columns{"guarantor"}{"GSTATE"} = ["State"];
$columns{"guarantor"}{"GZIP"} = ["Zip"];
$columns{"guarantor"}{"GHPHONE"} = ["HmPhone"];
$columns{"guarantor"}{"GWPHONE"} = ["WkPhone"];



#these are map of human->xbase relation names
my %tables =();
$tables{"patient"} = "PATIENT";
$tables{"provider"} = "DOCTOR";
$tables{"insurance"} = "INSPLAN";
$tables{"appointment"} = "APPTBOOK";
$tables{"guarantor"} = "GUARANTO";





my %dirs;
$dirs{"dbfs"} = $origdir;#"/Users/mjpan/dental/DBFS";

#    my $drh = DBI->install_driver("mysql");
#    $drh->func("dropdb", $mysqlParamsHash{"dbname"}, 
#		  [$mysqlParamsHash{"dbhost"}, 
#		   $mysqlParamsHash{"dbuser"}, 
#		   $mysqlParamsHash{"dbpwd"},], 'admin');

#load from dentpro.sql database dump
#assumes that database tables does not exist
print "about to load opendental.sql... assuming that the database does not yet exist\n";
my $command = "mysql -u ".$mysqlParamsHash{"dbuser"}." -p < opendental.sql";
`$command`;

my $mysqlh = mysqlHandle();
my $xbaseh = xbaseHandle($dirs{"dbfs"});

#alter the employee database
#what is the userpermission table for?
#alter table employee add column (integer access);

convertData($xbaseh, $mysqlh);

#need to load guarantor data file
print "remember to connect patients to guarantors by loading $convdir/guarantor\n";
print "this can be done by issuing the command...\n";
my $guarantorUpdateCommand =
    "mysql -u ".$mysqlParamsHash{"dbuser"}." -p -D ".
    $mysqlParamsHash{"dbname"}." < $convdir/guarantorUpdates";
print "\"$guarantorUpdateCommand\"\n";

my $response;
do {
    print "would you like me to do this now? (y/n)\n";

    #get the input from STDIN (presumably the keyboard)
    $response = <STDIN>;
    #remove the newline character
    chomp($response);
    if ($response =~ m/^y$/) {
	`$guarantorUpdateCommand`;
    }
} until ($response =~ m/^(y|n)$/i);

#how to dump database?
print "if you need to dump the database, you can type... \n";
print "\"mysqldump -u ".$mysqlParamsHash{"dbuser"}.
    " -p --opt ".
    $mysqlParamsHash{"dbname"}." > \$outputfile\"\n";

sub xbaseHandle {
    #the directory where the original dbfs files are located
    my $dir = shift @_;
    my $xbaseh = DBI->connect("DBI:XBase:$dir")
	or die $DBI::errstr;
    return $xbaseh;
}

sub mysqlHandle {
    my $mysqlh = DBI->connect("dbi:".
			      $mysqlParamsHash{"dbms"}.":".
			      $mysqlParamsHash{"dbname"},
			      $mysqlParamsHash{"dbuser"},
			      $mysqlParamsHash{"dbpwd"},
			      {'RaiseError' => 1});
    return $mysqlh;
}

sub extractColumns {
    foreach my $relation (keys %tables) {
	print "extracting columns from $relation...\n";
	extractColumns($xbaseh, $tables{$relation});
    }
}

sub extractColumnsFromRelation {
    my $dbh = shift @_;
    my $table = shift @_;

    unless (open FILE, ">$columnsDir/$table") {
	die "could not open $columnsDir/$table!\n";
    }

    #cannot end select statement with ";" for xbase
    my $statement = "select * from $table";
    my $sth = $dbh->prepare($statement);
    $sth->execute();
    my $numColumns = $sth->{NUM_OF_FIELDS};
    foreach my $i (0..$numColumns) {
	my $column = $sth->{NAME}->[$i];

	#write to file
	print FILE $column, "\n";
    }
    $sth->finish();

    close FILE;
}

sub convertData {
    my $xbaseh = shift @_;
    my $mysqlh = shift @_;

    convertPatientData($xbaseh, $mysqlh);
    #convertAppointmentData($xbaseh, $mysqlh);
    convertRecallData($xbaseh, $mysqlh);
}

sub convertRecallData {
    my $xbaseh = shift;
    my $mysqlh = shift;

    print "converting recall data...\n";

    #replace the recall table
    $mysqlh->do("drop table recall");
    $mysqlh->do("CREATE TABLE `recall` (
  `RecallNum` mediumint(8) unsigned NOT NULL auto_increment,
  `PatNum` mediumint(8) unsigned NOT NULL default '0',
  `DateDueCalc` date NOT NULL default '0001-01-01',
  `DateDue` date NOT NULL default '0001-01-01',
  `DatePrevious` date NOT NULL default '0001-01-01',
  `RecallInterval` int(11) NOT NULL default '0',
  `RecallStatus` smallint(5) unsigned NOT NULL default '0',".
 #add recalltype
 "`RecallType` varchar(255),".

 "`Note` text NOT NULL,
  `IsDisabled` tinyint(3) unsigned NOT NULL default '0',
  PRIMARY KEY  (`RecallNum`),
  KEY `PatNum` (`PatNum`),
  KEY `DateDue` (`DateDue`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;");


    #create map (rcn->patnum)
    my %patnumByRCN;
    my $tuples =
	$mysqlh->selectall_arrayref("SELECT RCN, PatNum from patient;");
    foreach my $row (@{$tuples}) {
	my $patnum = ${$row}[1];
	$patnumByRCN{${$row}[0]} = $patnum;
    }

    #get patient, last prophy
    $tuples = $xbaseh->selectall_arrayref("SELECT PRCN, DLPROP, RECRSN1, RECRSN2, DREC1, DREC2, RECALLINT FROM ".$tables{"patient"}." order by PRCN");

    unless (open FILE, ">$convdir/recall") {
	print "cannot open recall!";
	die;
    }

    my %recallTypes;
    #$recallType{"Prophylaxis"} = 1;
    #$recallType{"OfficeReline"} = 3;
    #foreach my $recallType (keys %recallTypes) {
    #my $quotedProphy = $mysqlh->quote("Prophylaxis");
    #my $quotedReline = $mysqlh->quote("OfficeReline");
    $recallTypes{"1"} = $mysqlh->quote("Prophylaxis");
    $recallTypes{"3"} = $mysqlh->quote("OfficeReline");
	foreach my $row (@{$tuples}) {
	    unless (${$row}[1]=~m/\d/ ||
		    ${$row}[0]>0) {
		next;
	    }

	    my @values;
	    
	    #convert prcn to patnum
	    push @values, $patnumByRCN{${$row}[0]};

	    #convert date last prophy
	    ${$row}[1]=~m/(\d{4})(\d*)(\d{2})/;
	    my $datePrevious = createValidDate(${$row}[1], "past");
	    push @values, $datePrevious;

	    my @recallDates;
	    foreach my $recallDateIdx (4..5) {
		#${$row}[$recallDateIdx]=~m/(\d{4})(\d*)(\d{2})/;
		my $date = createValidDate(${$row}[$recallDateIdx], "future");
		push @recallDates, $date;
	    }

	    #convert recall interval
	    my $recallInterval = ${$row}[6];
	    push @values, $recallInterval;

	    #map { $_ = $mysqlh->quote($_); } @{$row};
	    map { $_ = $mysqlh->quote($_); } @values;

	    #
	    #my $string = 
		#"insert into recall (PatNum, DatePrevious, RecallType) ".
		#"values (".join(", ", (@{$row}, $quotedProphy)).");\n";

	    #recall index is the index of the field in the previous query
	    for my $recallIndex (2..3) {
		my $recallID = ${$row}[$recallIndex];

		unless($recallID>0) {
		    next;
		}

		my $recallType = $recallTypes{"$recallID"};

		unless($recallType=~m/.+/) {
		    next;
		}

		#this is a hack
		#office relines are always 1 year
		if ($recallType=~m/Reline/i) {
		    $recallInterval = 365;
		    $values[2] = $mysqlh->quote($recallInterval);
		}

		#need to do update dataduecalc because
		#current mysql distribution (4.1.13)
		#does not yet support triggers
		my $string = 
		    "insert into recall (PatNum, DatePrevious, RecallInterval, DateDueCalc, RecallType) ".
		    "values (".join(", ", 
				    (@values, 
				     "ADDDATE('$datePrevious', INTERVAL $recallInterval DAY)",
				     $recallType)).");\n";
	    
		dbQuery(FILE, $mysqlh, $string);

		#update recall set dateduecalc='' where patnum=$values[0] and recalltype=$recallType
		if ($recallDates[0] =~ m/\d+/) {
		    $string =
			"update recall set dateduecalc='".(shift @recallDates).
			"' where patnum=$values[0] and recallType=$recallType;\n";
		    dbQuery(FILE, $mysqlh, $string);
		}
	    }


	}
    #}
    close FILE;

}

sub createValidDate {
    my $date = shift;
    my $relative = shift;

    $date=~m/(\d{4})(\d*)(\d{2})/;
    my $year = $1;
    my $month = $2;
    my $day = $3;

    #ensure valid month
    unless ($month > 0 && $month <= 12) {
	$month = 1;
    }
    
    #max recall year to this year 
    #(cant be 2041 if it is currently 2005)
    use POSIX;
    my $thisyear = strftime("%Y", localtime);
    if ($year > $thisyear+1) {
	if ($relative =~ m/future/i) {
	    $year = $thisyear+1;
	}
	else {
	    $year = $thisyear;
	}
    }

    return "$year-$month-$day";
}

sub convertAppointmentData {
    my $xbaseh = shift;
    my $mysqlh = shift;

    #replace the appointment table
    $mysqlh->do("drop table appointment");
    $mysqlh->do("CREATE TABLE `appointment` (
  `AptNum` mediumint(8) unsigned NOT NULL auto_increment,
  `PatNum` mediumint(8) unsigned NOT NULL default '0',
  `AptStatus` tinyint(3) unsigned NOT NULL default '0',
  `Pattern` varchar(255) NOT NULL default '',
  `Confirmed` smallint(5) unsigned NOT NULL default '0',
  `AddTime` tinyint(3) NOT NULL default '0',
  `Op` smallint(5) unsigned NOT NULL default '0',
  `Note` text NOT NULL,
  `ProvNum` smallint(5) unsigned NOT NULL default '0',
  `ProvHyg` smallint(5) unsigned NOT NULL default '0',".
 #this needs to be updated every time recall is set
 "`AptDateTime` datetime NOT NULL default '0001-01-01 00:00:00',
  `NextAptNum` mediumint(8) unsigned NOT NULL default '0',
  `UnschedStatus` smallint(5) unsigned NOT NULL default '0',
  `Lab` tinyint(3) unsigned NOT NULL default '0',
  `IsNewPatient` tinyint(1) unsigned NOT NULL default '0',
  `ProcDescript` varchar(255) NOT NULL default '',
  `Assistant` smallint(5) unsigned NOT NULL default '0',
  PRIMARY KEY  (`AptNum`),
  KEY `indexPatNum` (`PatNum`),
  KEY `indexNextAptNum` (`NextAptNum`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;");

    #get patient, last prophy
    my $tuples = $xbaseh->selectall_arrayref("SELECT PRCN, DLPROP FROM ".$tables{"patient"}." order by PRCN");

    unless (open FILE, ">$convdir/appointment") {
	print "cannot open appointment!";
	die;
    }
    foreach my $row (@{$tuples}) {
	unless (${$row}[1]=~m/\d/ ||
		${$row}[0]>0) {
	    next;
	}
	
	${$row}[1]=~m/(\d{4})(\d*)(\d{2})/;
	${$row}[1]="$1-$2-$3";

	map { $_ = $mysqlh->quote($_); } @{$row};

	my $string = 
	    "insert into appointment (PatNum, AptDateTime, ProcDescript) ".
	    "values (".join(", ", (@{$row}, "Prophylaxis")).");\n";

	dbQuery(FILE, $mysqlh, $string);
	#print FILE $string;
	#$mysqlh->do($string);
    }
    close FILE;
}


#this is processed before patient data
#as each patient is processed, remove from guarantor map if ssn is the same
#after patients are completed, write out guarantors to file
#and make sure to load guarantors before patients
sub convertGuarantorData {
    my $xbaseh = shift @_;
    my $mysqlh = shift @_;

    print "converting guarantor data...\n";

    #foreach my $relation (keys %columns) {
    #print "querying $relation...\n";

    my $relation = "guarantor";
    my $patientRelation = "patient";
    
    my %guarantorSQLBySSN;
    my %guarantorSSNByRCN;
    #hash [guarantor rcn]->[guarantor ssn]
    #my $tuples = 
	#$xbaseh->selectall_arrayref
	#("SELECT GRCN, GSSN".
	# " FROM ".$tables{"guarantor"});
    #foreach my $guarantorRow (@{$tuples}) {
	#my $gRCN = ${$guarantorRow}[0];
	#my $gSSN = ${$guarantorRow}[1];
	#if ($gRCN > 0 &&
	#    $gSSN =~ m/^\d{9}$/) {
	#    $guarantorSSNByRCN{$gRCN} = $gSSN;
	#}
    #}


    my @array = calculateColumnIndices($columns{$relation});
    my @original = @{shift @array};
    my @converted = @{shift @array};
    my %indexes = %{shift @array};

    $tuples = 
	$xbaseh->selectall_arrayref("SELECT GRCN, ".join(", ", @original).
				    " FROM ".$tables{$relation}.
				    " order by GRCN");

    foreach my $relationRow (@{$tuples}) {
	my $rcn = shift @{$relationRow};

	#get ssn index
	my $ssnIndex = ${$indexes{"GSSN"}}[0];

	#put into hashes
	my $ssn = ${$relationRow}[$ssnIndex];
	$ssn = formatSSNForDB($ssn);

	unless ($rcn > 0) {
	    #print "ignoring row ", @{$relationRow}, " because rcn for is $rcn\n";
	    next;
	}

	#unless ($ssn =~ m/\d{9}/) {
	#    #print "ignoring row ", @{$relationRow}, 
	#    " because ssn $ssn is improperly formatted\n";
	#    next;
	#}

	if ($ssn =~ m/^0*$/) {
	    next;
	}

	#reformat the patient birthdate
	foreach ("GBDATE") {
	    my @indices = @{$indexes{$_}};
	    foreach my $index (@indices) {
		my $date = ${$relationRow}[$index];
		${$relationRow}[$index] = formatDateForDB($date);
	    }
	}


	#quote every column
	map { $_ = $mysqlh->quote($_); } @{$relationRow};
	
	#construct query string
	my $string = 
	    "insert into $patientRelation (".
	    join(", ",  @converted).") ".
	    "values (".
	    join(", ", @{$relationRow}).");\n";
	#replace null values with spaces
	$string =~ s/\'\'/\' \'/g;

	#execute sql string
	#dbQuery(FILE, $mysqlh, $string);

	#print $string, "\n";

	$guarantorSQLBySSN{$ssn} = $string;
	$guarantorSSNByRCN{$rcn} = $ssn;
    }

    print "returning guarantorSSNByRCN with ", 
    scalar(keys %guarantorSSNByRCN), " values\n";

    print "returning guarantorSQLBySSN with ",
    scalar(keys %guarantorSQLBySSN), " values\n";

    return (\%guarantorSSNByRCN, \%guarantorSQLBySSN);
}

sub calculateColumnIndices {
    my %columns = %{shift @_};

    #original column names
    my @original = ();
    
    #converted column names
    my @converted = ();

    my %indexes = ();
    
    my $i=0;
    #foreach my $column (keys %{$columns{$relation}}) {	    
    foreach my $column (keys %columns) {
	#my $opendentColumnRef = $columns{$relation}{$column};
	my $opendentColumnRef = $columns{$column};
	foreach my $opendentColumn (@{$opendentColumnRef}) {
	    push @original, $column;
	    push @converted, $opendentColumn;
	    push @{$indexes{$column}}, $i;

	    $i++;
	}
    }
    return (\@original, \@converted, \%indexes);
}

#process patients
sub convertPatientData {
    my $xbaseh = shift @_;
    my $mysqlh = shift @_;

    print "converting patient data...\n";

    my $relation = "patient";
    unless (open FILE, ">$convdir/$relation") {
	die "could not open $convdir/$relation!\n";
    }
    my $guarantorRelation = "guarantor";
    unless (open GUARANTOR, ">$convdir/$guarantorRelation") {
	die "could not open $convdir/$relation!\n";
    }
    my $guarantorUpdates = "guarantorUpdates";
    unless (open GUARANTOR_UPDATES, ">$convdir/$guarantorUpdates") {
	die "could not open $convdir/$guarantorUpdates!\n";
    }

    my @array = calculateColumnIndices($columns{$relation});
    my @original = @{shift @array};
    my @converted = @{shift @array};
    my %indexes = %{shift @array};

    my @gArray = convertGuarantorData($xbaseh, $mysqlh);
    my %guarantorSSNByRCN = %{shift @gArray};
    my %guarantorSQLBySSN = %{shift @gArray};
    #print "retrieved guarantorSSNByRCN with ", scalar(keys %guarantorSSNByRCN), " values\n";
    #print "retrieved guarantoSQLBySSN with ", scalar(keys %guarantorSQLBySSN), " values\n";

    print "recreating patient relation...\n";

    #replace the patient table
    $mysqlh->do("drop table patient");
    $mysqlh->do("CREATE TABLE `patient` (
  `PatNum` mediumint(8) unsigned NOT NULL auto_increment,
  `RCN` mediumint(8) unsigned,
  `LName` varchar(100) NOT NULL default '',
  `FName` varchar(100) NOT NULL default '',
  `MiddleI` varchar(100) NOT NULL default '',
  `Preferred` varchar(100) NOT NULL default '',
  `PatStatus` tinyint(3) unsigned NOT NULL default '0',
  `Gender` tinyint(3) unsigned NOT NULL default '0',
  `Position` tinyint(3) unsigned NOT NULL default '0',
  `Birthdate` date NOT NULL default '0001-01-01',
  `SSN` varchar(100) NOT NULL default '000000000',
  `Address` varchar(100) NOT NULL default '',
  `Address2` varchar(100) NOT NULL default '',
  `City` varchar(100) NOT NULL default '',
  `State` varchar(100) NOT NULL default '',
  `Zip` varchar(100) NOT NULL default '',
  `HmPhone` varchar(30) NOT NULL default '',
  `WkPhone` varchar(30) NOT NULL default '',
  `WirelessPhone` varchar(30) NOT NULL default '',".
 #need to resolve guarantor, default to self
 "`Guarantor` mediumint(8) unsigned NOT NULL default '0',
  `CreditType` char(1) NOT NULL default '',
  `Email` varchar(100) NOT NULL default '',
  `Salutation` varchar(100) NOT NULL default '',
  `PriPlanNum` mediumint(8) unsigned NOT NULL default '0',
  `PriRelationship` tinyint(3) unsigned NOT NULL default '0',
  `SecPlanNum` mediumint(8) unsigned NOT NULL default '0',
  `SecRelationship` tinyint(3) unsigned NOT NULL default '0',
  `EstBalance` double NOT NULL default '0',
  `NextAptNum` mediumint(8) NOT NULL default '0',
  `PriProv` smallint(5) unsigned NOT NULL default '0',
  `SecProv` smallint(5) unsigned NOT NULL default '0',
  `FeeSched` smallint(5) unsigned NOT NULL default '0',
  `BillingType` smallint(5) unsigned NOT NULL default '0',".
 #need to find out how recall interval is combined
 #changed size to 8 bits to 
 "`RecallInterval` integer NOT NULL default '0',".
 #need to find out what are the values for RecallStatus 
 #possible to deactivate patient using this value?
 "`RecallStatus` smallint(5) unsigned NOT NULL default '0',
  `ImageFolder` varchar(100) NOT NULL default '',
  `AddrNote` text NOT NULL,
  `FamFinUrgNote` text NOT NULL,
  `MedUrgNote` varchar(255) NOT NULL default '',
  `ApptModNote` varchar(255) NOT NULL default '',
  `StudentStatus` char(1) NOT NULL default '',
  `SchoolName` varchar(30) NOT NULL default '',
  `ChartNumber` varchar(20) NOT NULL default '',
  `MedicaidID` varchar(20) NOT NULL default '',
  `Bal_0_30` double NOT NULL default '0',
  `Bal_31_60` double NOT NULL default '0',
  `Bal_61_90` double NOT NULL default '0',
  `BalOver90` double NOT NULL default '0',
  `InsEst` double NOT NULL default '0',
  `PrimaryTeeth` varchar(255) NOT NULL default '',
  `BalTotal` double NOT NULL default '0',
  `EmployerNum` mediumint(8) unsigned NOT NULL default '0',
  `EmploymentNote` varchar(255) NOT NULL default '',
  `Race` tinyint(4) NOT NULL default '0',
  `County` varchar(255) NOT NULL default '',
  `GradeSchool` varchar(255) NOT NULL default '',
  `GradeLevel` tinyint(4) NOT NULL default '0',
  `Urgency` tinyint(4) NOT NULL default '0',".
 #need to set date first visit
 "`DateFirstVisit` date NOT NULL default '0001-01-01',
  `PriPending` tinyint(1) unsigned NOT NULL default '0',
  `SecPending` tinyint(1) unsigned NOT NULL default '0',
  `ClinicNum` smallint(5) unsigned NOT NULL default '0',".
 #this will inform as to who made the last change
 "`LastUpdatedBy` smallint(5) unsigned NOT NULL default '0',
  PRIMARY KEY  (`PatNum`),
  KEY `indexLName` (`LName`(10)),
  KEY `indexFName` (`FName`(10)),
  KEY `indexLFName` (`LName`,`FName`),
  KEY `indexGuarantor` (`Guarantor`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;");
    
    my @rowsWithBadKeys;
    my %keysProcesses;
    
    $tuples = 
	$xbaseh->selectall_arrayref("SELECT ".join(", ", @original).
				    " FROM ".$tables{$relation}.
				    " order by PRCN");
    
    
    
    
    foreach my $relationRow (@{$tuples}) {
	
	#reformat the patient birthdate
	foreach ("PBDATE") {
	    my @indices = @{$indexes{$_}};
	    foreach my $index (@indices) {
		my $date = ${$relationRow}[$index];
		${$relationRow}[$index] = formatDateForDB($date);
	    }
	}
#select prcn, plast, pfirst, prel1, prel2 from patient   	
	#reformat patient emergency phone #
	foreach ("PEPHONE") {
	    my @indices = @{$indexes{$_}};
	    foreach my $index (@indices) {
		my $number = ${$relationRow}[$index];
		${$relationRow}[$index] = formatPhoneNumberForDB($number);
	    }
	}

	#if guarantor is already a patient, dont add as guarantor
	foreach ("PSSN") {
	    my @indices = @{$indexes{$_}};

	    my $index = $indices[0];

	    my $ssn = ${$relationRow}[$index];

	    ${$relationRow}[$index] = formatSSNForDB($ssn);

	    #print "deleting from guarantorSQLBySSN $ssn\n";
	    delete $guarantorSQLBySSN{$ssn};
	}

	foreach ("PGENDER") {
	    my @indices = @{$indexes{$_}};
	    my $index = $indices[0];
	    my $gender = ${$relationRow}[$index];
	    ${$relationRow}[$index] = formatGenderForDB($gender);	    
	}
	
	#resolve guarantor for each patient
	foreach ("PGUAR") {
	    my @rcnIndices = @{$indexes{"PRCN"}};
	    my $rcn = ${$relationRow}[$rcnIndices[0]];
	    
	    my @guarantorIndices = @{$indexes{$_}};
	    foreach my $guarantorIndex (@guarantorIndices) {
		my $guarantorRCN = ${$relationRow}[$guarantorIndex];
		
		#print "processing patient ".$rcn
		#    ." with guarantor ".$guarantorRCN."\n";
		
		my $guarantorSQL;
		
		if (exists $guarantorSSNByRCN{$guarantorRCN}) {
		    my $guarantorSSN = 
			$guarantorSSNByRCN{$guarantorRCN};
		    
		    #if the ssn is properly formatted
		    #if ($guarantorSSN =~ m/^\d{9}$/) {
		    
		    $guarantorSQL = 
			"update patient as a, ".
			"patient as b set a.guarantor=b.patnum ".
			"where b.ssn=$guarantorSSN and ".
			"a.rcn=".$rcn.";\n"; 
		}
		else {
		    #no need to do this, presumably already done
		    
		    #print "setting patient $rcn to having self as guarantor\n";
		    #$guarantorSQL =
		    #    "update patient as a ".
		    #    "set a.guarantor=a.patnum ".
		    #    "where a.rcn=".$rcn.";\n";

		    next;
		}
		
		#need to find guarantors that are not patients
		# and add them in
		
		#write update sql query to file
		print GUARANTOR_UPDATES $guarantorSQL;		
		#print $guarantorSQL;
	    }
	}
	
	my @rcnIndices = @{$indexes{"PRCN"}};
	foreach my $rcnIndex (@rcnIndices) {
	    my $valueAtIndex = ${$relationRow}[$rcnIndex];
	    
		#filter only those with rcn > 0
	    if ($valueAtIndex > 0) {
		#quote every column
		map { $_ = $mysqlh->quote($_); } @{$relationRow};
		    
		#construct query string
		my $string = 
		    "insert into $relation (".join(", ", @converted).") ".
		    "values (".join(", ", @{$relationRow}).");\n";
		#replace null values with spaces
		$string =~ s/\'\'/\' \'/g;
		
		#handle db corruption with duplicate id
		if ($keysProcessed{$valueAtIndex} > 0) {
		    push @rowsWithBadKeys, $string;
		    last;
		}

		dbQuery(FILE, $mysqlh, $string);
		#print FILE $string;
		#$mysqlh->do($string);
		
		#set id to processed
		$keysProcessed{$valueAtIndex} = 1;
		
		last;
	    }
	}
	
	#maintain a map of (patient id->guarantor name)
	#need to query dentpro.guarantor for name/ssn
	###code here
    }

    outputGuarantorInfo(\%guarantorSQLBySSN);
    
    #set every patient to have themselves as guarantor first
    $mysqlh->do("update patient as a set a.guarantor=a.patnum");

	#for every patient
	#try to map the guarantor by querying for name 
	#(or ssn if available)
	###code here

	#process rows with bad keys
	print FILE "*******************\n";
	print FILE "bad keys\n";
	print FILE "*******************\n";
	foreach my $string (@rowsWithBadKeys) {
	    print FILE $string;
	}

    close FILE;
    close GUARANTOR;
    close GUARANTOR_UPDATES;
}

#right now everything is commented out
#because the number of guarantors that are left
#after removing duplicates with existing patients 
#based upon ssn match, is just 4178-4112
#which would be useful, except that 
#many of them are patients as well,
#just that the patient info does not have ssn
sub outputGuarantorInfo {
    my %guarantorSQLBySSN = %{shift @_};

    #output all guarantor info to file
    #print "outputting guarantors to file...\n";
    #foreach my $guarantorSSN (keys %guarantorSQLBySSN) {
	#dbQuery(GUARANTOR, $mysqlh, $guarantorSQLBySSN{$guarantorSSN});
    #}
}

sub dbQuery {
    my $fileh = shift;
    my $dbh = shift;
    my $query = shift;

    #write to file
    #print FILE $string;
    print $fileh $query;
    
    #write to database
    #$mysqlh->do($string);
    $dbh->do($query);
}

sub formatSSNForDB {
    my $ssn = shift;

    #prepend 0's
    while (length($ssn) < 9) {
	$ssn = "0".$ssn;
    }

    return $ssn;
}

sub formatGenderForDB {
    my $gender = shift;;

    if ($gender =~ m/M/i) {
	$gender = "0";
    }
    elsif ($gender =~ m/F/i) {
	$gender = "1";
    }

    return $gender;
}

sub formatDateForDB {
    my $date = shift;

    if ($date =~ m/\d/ ) {
	$date =~ m/(\d{4})(\d*)(\d{2})/;
	$date = "$1-$2-$3";
    }
    else {
	$date = "0001-01-01";
    }
    
    return $date;
}

sub formatPhoneNumberForDB {
    my $number = shift;
    unless ($number =~ m/\d/ ) {
	$number = "0000000000";
    }
    $number;
}
