package com.winningsmiledental;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String s) {
	super(s);
    }
}