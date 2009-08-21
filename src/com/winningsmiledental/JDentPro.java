package com.winningsmiledental;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.sql.*;




public class JDentPro implements GraphicalApplication {

    public ApplicationFrame applicationFrame;
    public JDentProExecutioner jdpExecutioner;
    public int currentEmployeeID;
    public String database;

    protected static Properties configuration;

    protected static Map dbConnections = new Hashtable();

    public static boolean staticRunningOnMacOSX() {
	return System.getProperty("os.name").
	    toLowerCase().startsWith("mac os x");
    }


    public static String getProperty(String key) {
	return configuration.getProperty(key);
    }

    public static void loadProperties(URL url) {
	if (configuration == null) {
	    configuration = new Properties();
	}
	try {
	    configuration.load(url.openStream());
	}
	catch(Exception e) {
	}
    }

    public static java.net.URL getApplicationResource
	(String defaultResourcePath, String filename) {
	
	java.net.URL url = null;

	String urlString;

	    urlString = "/" + defaultResourcePath;
	    if (!urlString.endsWith("/")) {
		urlString += "/";
	    }
	    urlString += filename;

	    url = 
		JDentPro.class.getResource(urlString);

	return url;
    }

    public static Connection getConnection(String dbname) {
	Connection c = (Connection) dbConnections.get(dbname);
	if (c==null) {
	    c= establishConnection(getProperty("database.host"),
				   getProperty("database.user"),
				   getProperty("database.password"),
				dbname);
	    dbConnections.put(dbname, c);
	}
	return c;
    }

    public static Connection establishConnection(String dbhost,
						 String dbuser,
						 String dbpassword,
						 String dbname) {
	Connection c =null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            c = 
		DriverManager.getConnection("jdbc:mysql://"+dbhost+"/" + 
					    dbname + "?user="+dbuser+"&password="+dbpassword);
        } 
	catch (Exception e) {
	    e.printStackTrace();
        }
	return c;
    }

    public static void main(String[] args) {
	URL applicationConfigURL = 
	    getApplicationResource("config", "application.config");
	loadProperties(applicationConfigURL);
	String applicationClass =
	    getProperty("application.class");
	try {
	    createObject(applicationClass);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * create anonymous objects with empty constructors
     */
    public static Object createObject(String className)
    throws Exception {
	Object object = null;

	Class classDefinition = Class.forName(className);
	object = classDefinition.newInstance();

	return object;
    }

    public JDentPro() {
	jdpExecutioner = new JDentProExecutioner(this);
	applicationFrame = ApplicationFrameFactory.createFrame(this);
	new Thread(applicationFrame).start();
    }

    public boolean runningOnMacOSX() {
	return staticRunningOnMacOSX();
    }

    public ApplicationFrame getAppFrame() {
	return applicationFrame;
    }

    public Executioner getExecutioner() {
	return jdpExecutioner;
    }

    public void setCurrentEmployee(int id) {
	currentEmployeeID = id;
    }

    public int getCurrentEmployeeID() {
	return currentEmployeeID;
    }

    public void setDatabase(String database) {
	this.database = database;
    }

    public String getDatabase() {
	return database;
    }

    public void about() {
	jdpExecutioner.about();
    }

    public void exit(int i) {
	System.exit(i);
    }
}