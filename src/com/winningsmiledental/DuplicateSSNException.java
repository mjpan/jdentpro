package com.winningsmiledental;

public class DuplicateSSNException extends JDentProException {
    public DuplicateSSNException(String message) {
	super(message);
    }

    public DuplicateSSNException(String message, Throwable e) {
	super(message, e);
    }
}