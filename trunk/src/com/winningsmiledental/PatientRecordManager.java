package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PatientRecordManager extends AbstractRecordManager {

    public PatientRecordManager() {
	super();
    }

    public PatientRecordManager(String databaseName) {
	super(databaseName);
    }

}