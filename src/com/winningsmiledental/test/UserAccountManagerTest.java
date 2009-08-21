package com.winningsmiledental.test;

import com.winningsmiledental.*;

import java.util.*;
import java.net.*;
import java.io.*;

import junit.framework.*;

public class UserAccountManagerTest extends TestCase {
    /* instance variables for the test fixtures */


    /** this method returns a test suite to the test runner */
    public static Test suite() { 
	TestSuite suite= new TestSuite(); 

	suite.addTest(new UserAccountManagerTest("createUsers"));
	suite.addTest(new UserAccountManagerTest("createExistingUser"));
	suite.addTest(new UserAccountManagerTest("removeExistingUser"));
	suite.addTest(new UserAccountManagerTest("removeNonExistingUser"));
	suite.addTest(new UserAccountManagerTest("authenticateUser"));
	return suite;
    }
    

    public UserAccountManagerTest(String test) {
	super(test);
    }

    /* write test methods 
     * the names of the methods will be passed into the constructor */
    public void createUsers() throws Exception {
	UserAccountManager userAccountManager =
	    new XMLUserAccountManager();
	userAccountManager.addUser("a", "p1", 1);
	userAccountManager.addUser("b", "p2", 1);
	userAccountManager.addUser("c", "p3", 1);
    }

    public void createExistingUser() {
	UserAccountManager userAccountManager =
	    new XMLUserAccountManager();

	boolean b = false;

	try {
	    userAccountManager.addUser("a", "p3", 1);
	}
	catch (Exception e) {
	    if (e instanceof UserAlreadyExistsException) {
		b = true;
	    }
	}
	assertTrue(b);
    }

   public void authenticateUser() throws Exception {
	UserAccountManager userAccountManager =
	    new XMLUserAccountManager();

	userAccountManager.authenticateUser("b", "p2");
    }

    public void removeExistingUser() throws Exception {
	UserAccountManager userAccountManager =
	    new XMLUserAccountManager();

	userAccountManager.removeUser("a");
    }

    public void removeNonExistingUser() throws Exception {
	UserAccountManager userAccountManager =
	    new XMLUserAccountManager();

	userAccountManager.removeUser("a");
    }

    /** setup all the class fields */
    protected void setUp() {
    }

    /** remove any permanent instances */
    protected void tearDown() {
    }

    public static void main(String args[]) { 
	junit.textui.TestRunner.run(suite());
    }

}
