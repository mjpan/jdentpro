package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.swixml.SwingEngine;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

public class LoginScreenListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;

    public LoginScreenListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();
	LoginScreenGUI temp = (LoginScreenGUI)gui;
	if (command.equals("AC_LOGIN")) {
	    //authenticate user and load main menu if user is authenticated
	    //else notify that login invalid and clear fields
	    String db = temp.getDatabase();
	    /*
	    String db = database.getText();
	    if (database == 0) {
		new ErrorMessage("Please Select Office");
		return;
	    }
	    else if (database == 1) {
		//set to temple city database
		db = "dental";
	    }
	    else {
		//set to cerritos database
		db = "dental";
	    }
	    */
	    temp.setUserAccountManager(db);
	    getExecutioner().setDatabase(db);
	    int employeeID = temp.getUserAccountManager().authenticateUser(temp.getUserName(), temp.getPassword());
	    if (employeeID < 0) {
		new ErrorMessage("Invalid UserName & Password.");
		return;
	    }
	    else {
		getExecutioner().setCurrentEmployee(employeeID);
		getExecutioner().loadMainMenu();
	    }
	}
	if (command.equals("AC_QUIT")) {
	    //quit jDentPro
	    getExecutioner().exit(0);
	}
    }

}

