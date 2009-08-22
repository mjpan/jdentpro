package com.winningsmiledental;

public class InvalidSSNException extends JDentProException {
    public InvalidSSNException(String message) {
	super(message);
    }

    public InvalidSSNException(String message, Throwable e) {
	super(message, e);
    }
}