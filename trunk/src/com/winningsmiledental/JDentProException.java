package com.winningsmiledental;

public class JDentProException extends Exception {
    public JDentProException(String message) {
	super(message);
    }
    public JDentProException(String message, Throwable e) {
	super(message, e);
    }
}