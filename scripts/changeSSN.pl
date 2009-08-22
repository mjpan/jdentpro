#!/usr/bin/perl

#go through and change duplicate ssns into unique identifiers

use DBI;

changeSSN();

sub changeSSN {
  my @mysqlHandles;
  for my $dbIdx (0..2) {
    my %mysqlParamsHash = mysqlParams($dbIdx);
    my $mysqlh = mysqlHandle(\%mysqlParamsHash);
    push @mysqlHandles, $mysqlh;
  }

  #get all patnums with ssn duplicates
  my %corruptedRecords = findCorruptedRecords($mysqlHandles[0]);

  #foreach patnum, update to 
  my $i = 1000;
  foreach my $patnum (keys %corruptedRecords) {
    my $ssn = getSSNPrefix()."$i"; $i++;
    my $queryString = "update patient set ssn='$ssn' where patnum=$patnum";
    dbQuery(0, $mysqlHandles[0], $queryString);
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

  my %corruptedSSNs;
  my $tuples =
    $dbh->selectall_arrayref("select ssn, count(*) from patient group by ssn");
  foreach my $row (@{$tuples}) {
    if (${$row}[1] > 1) {
      $corruptedSSNs{${$row}[0]} = 1;
    }
  }

  foreach my $corruptedSSN (keys %corruptedSSNs) {
    my $queryString =
      "SELECT patnum from patient where ssn = '$corruptedSSN';";
      #"SELECT patnum from patient where ssn > 'v'";
    $tuples =
      $dbh->selectall_arrayref($queryString);
    foreach my $row (@{$tuples}) {
      my $patnum = ${$row}[0];
      $corruptedRecords{$patnum} = 1;
    }
  }
  return %corruptedRecords;
}

$serial = 1000;
sub getNextSSN {
  $serial +=1;
  return "wsd:000000000".$serial;
}

sub getSSNPrefix {
  return "wsd:000000000";
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
