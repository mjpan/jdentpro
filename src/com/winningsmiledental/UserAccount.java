package com.winningsmiledental;

import java.util.*;

public class UserAccount {
    private String login;
    private String password;
    private int accessLevel;

    protected UserAccount(String s) {
	setLogin(s);
    }

    private void setLogin(String s) {
	login = s;
    }

    protected String getLogin() {
	return login;
    }

    protected void setPassword(String s) {
	password = s;
    }

    protected String getPassword() {
	return password;
    }

    protected void setAccessLevel(int i) {
	accessLevel = i;
    }

    protected int getAccessLevel() {
	return accessLevel;
    }    
}