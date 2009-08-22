package com.winningsmiledental;

public class UserAccountManagerFactory {
    public static UserAccountManager getUserAccountManager(String dbname) {
	UserAccountManager manager =
	    new DBUserAccountManager(dbname);
	return manager;
    }
}