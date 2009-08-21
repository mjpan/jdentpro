package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;

public class DBUserAccountManager extends AbstractUserAccountManager {

    public DBUserAccountManager(String databaseName) {	
	super(databaseName);
    }
   
}
