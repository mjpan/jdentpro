package com.winningsmiledental;

public class DatabaseConnectionException extends JDentProException {
    public DatabaseConnectionException(String message) {
	super(message);
    }

    public DatabaseConnectionException(String message, Throwable e) {
	super(message, e);
    }
}