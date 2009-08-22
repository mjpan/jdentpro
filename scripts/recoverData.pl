#!/usr/bin/perl

use DBI;
use XBase;

#recover data by taking data from a backup database and copy over to production
#ensures that does not overwrite data since the backup


#load from dentpro.sql database dump
#assumes that database tables does not exist
#print "about to load opendental.sql... assuming that the database does not yet exist\n";
#my $command = "mysql -u ".$mysqlParamsHash{"dbuser"}." -p < opendental.sql";
#`$command`;

#need to load databases first

recoverData();

sub loadData {
#almond:~/dental/data mjpan$ mysql -u apache -p -D dental < dentpro.20060907.sql 
#almond:~/dental/data mjpan$ mysql -u apache -p -D dental_20060423 < dentpro.060423.sql 
#almond:~/dental/data mjpan$ mysql -u apache -p -D dental_20060129 < dentpro.060129.sql 
}

sub recoverData {

  my @mysqlHandles;
  for my $dbIdx (0..2) {
    my %mysqlParamsHash = mysqlParams($dbIdx);
    my $mysqlh = mysqlHandle(\%mysqlParamsHash);
    push @mysqlHandles, $mysqlh;
  }
  
  #find all patnums with duplicate rcns in database0
  my %corruptedRecords = findCorruptedRecords($mysqlHandles[0]);
  print "retrieved ids for ".(scalar keys %corruptedRecords)." records\n";

  #pull data from database1
  #tally number updated from database1
  #if not in database2, pull from database2
  #tally number updated from database2
  for my $dbh ($mysqlHandles[1], $mysqlHandles[2]) {
    retrieveBackup($mysqlHandles[0], $dbh, \%corruptedRecords);
  }
  
  #output which were not updated
  for my $patnum (keys %corruptedRecords) {
    #print "could not fix patient record with patnum $patnum\n";
  }
  print "total ".(scalar keys %corruptedRecords)." records not fixed\n";
}

sub retrieveBackup {
  my $dbh1 = shift;
  my $dbh2 = shift;
  my %corruptedRecords = %{shift @_};

  #(String last, String first, String middle, String salutation, 
  #			   String nickname, String address, String address2, String city, String state, 
  #			   String zip, String hmPhone, String mobile, String wkPhone, 
  #			 String ssn, int gender, String bday, int employee)
  my @columns = ("patnum",
		 "rcn",
		 "lname",
		 "fname",
		 "middlei",
		 "preferred",
		 "birthdate",
		 "ssn",
		 "address",
		 "address2",
		 "city",
		 "state",
		 "zip",
		 "hmphone",
		 "wkphone",
		 "wirelessphone",
		 "salutation",
		 "patstatus",
		 "gender",
		 "guarantor");

  my $queryString = 
    "select ".join(", ", @columns).
      " from patient where patnum in (".
	join(", ",(keys %corruptedRecords)).")";

  print "querying $queryString\n";

  my $tuples =
    $dbh2->selectall_arrayref($queryString);

  foreach my $row(@{$tuples}) {
    #my $queryString = "insert into patient (".join(", ",@columns).") ".
    #  "values (".join("','",@{$row}).")";
    foreach my $value (@{$row}) {
      $value =~ s/\'//g;
    }
    $queryString = "update patient ".
      "set rcn = ".${$row}[1].",".
	"lname='".${$row}[2]."',".
	  "fname='".${$row}[3]."',".
	    "middlei='".${$row}[4]."',".
	      "preferred='".${$row}[5]."',".
		"birthdate='".${$row}[6]."',".
		  "ssn='".${$row}[7]."',".
		    "address='".${$row}[8]."',".
		      "address2='".${$row}[9]."',".
			"city='".${$row}[10]."',".
			  "state='".${$row}[11]."',".
			    "zip='".${$row}[12]."',".
			      "hmphone='".${$row}[13]."',".
				"wkphone='".${$row}[14]."',".
				  "wirelessphone='".${$row}[15]."',".
				    "salutation='".${$row}[16]."',".
				      "patstatus = ".${$row}[17].",".
					"gender = ".${$row}[18].",".
					  "guarantor = ".${$row}[19].
					    " where patnum=".${$row}[0];

    dbQuery(0, $dbh1, $queryString);

    delete $corruptedRecords{${$row}[0]};
  }

  
}

sub dbQuery {
    my $fileh = shift;
    my $dbh = shift;
    my $query = shift;

    #write to file
    #print FILE $string;
    if ($fileh) {
      print $fileh $query;
    }

    #write to database
    #$mysqlh->do($string);
    $dbh->do($query);
}

sub findCorruptedRecords {
  my $dbh = shift;
  my %corruptedRecords;
  my $tuples =
    $dbh->selectall_arrayref("SELECT patnum from patient where rcn in (5401);");
  foreach my $row (@{$tuples}) {
    my $patnum = ${$row}[0];
    $corruptedRecords{$patnum} = 1;
  }
  return %corruptedRecords;
}

sub mysqlParams {
  my $dbIdx = shift;

  my %mysqlParamsHash =();
  $mysqlParamsHash{"dbhost"} = "localhost";

  #$mysqlParamsHash{"dbname"} = "dental";
  if ($dbIdx == 0) {
    $mysqlParamsHash{"dbname"} = "dental";
  }
  elsif ($dbIdx == 1) {
    $mysqlParamsHash{"dbname"} = "dental_20060423";
  }
  elsif ($dbIdx == 2) {
    $mysqlParamsHash{"dbname"} = "dental_20060129";
  }

  $mysqlParamsHash{"dbuser"} = "apache";
  $mysqlParamsHash{"dbpwd"} = "apache";
  $mysqlParamsHash{"dbport"} = 5432;
  $mysqlParamsHash{"dbms"} = "mysql";

  return %mysqlParamsHash;
}

sub mysqlHandle {
  my %mysqlParamsHash = %{shift @_};

    my $mysqlh = DBI->connect("dbi:".
			      $mysqlParamsHash{"dbms"}.":".
			      $mysqlParamsHash{"dbname"},
			      $mysqlParamsHash{"dbuser"},
			      $mysqlParamsHash{"dbpwd"},
			      {'RaiseError' => 1});
    return $mysqlh;
}
