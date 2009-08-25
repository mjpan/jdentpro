package com.winningsmiledental;

public class NoRecordsRetrievedException extends JDentProException {
    public NoRecordsRetrievedException(String message) {
	super(message);
    }

    public NoRecordsRetrievedException(String message, Throwable e) {
	super(message, e);
    }
}