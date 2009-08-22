package com.winningsmiledental;

public class UnknownSSNException extends JDentProException {
    public UnknownSSNException(String message) {
	super(message);
    }

    public UnknownSSNException(String message, Throwable e) {
	super(message, e);
    }
}