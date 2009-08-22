#!/usr/bin/perl

use POSIX qw(strftime);

my $date = strftime("%Y%m%d", localtime);
my $user = "apache";
my $dbname = "dental";

#print $date;
#`mysqldump -u apache -p --opt dental > dentpro.060423.sql

my $command = 
    "mysqldump -u $user -p --opt $dbname > dentpro.$date.sql";

#print $command;
print qq/press any key to execute "$command"\n/;
<STDIN>;
`$command`;
