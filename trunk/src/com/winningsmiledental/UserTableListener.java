package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.io.*;
import java.util.*;
import java.net.*;
import org.jdom.*;
import java.sql.*;

public class UserTableListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;
    //private UserAccountManager manager;

    public UserTableListener(GUI gui) {
	super(gui);
	//manager = ((UserTableGUI)gui).getUserAccountManager();
    }

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();

	UserAccountManager manager =
	    ((UserTableGUI)gui).getUserAccountManager();

	if (command.equals("AC_ADD")) {
	    getExecutioner().loadUserAdmin(manager);
	}  
	else if (command.equals("AC_EDIT")) {
	    JTable table = ((UserTableGUI)gui).getTable();
	    int row = table.getSelectedRow();
	    if (row >= 0) {
		try {
		    String username = (String)table.getValueAt(row, 0);	
		    getExecutioner().loadUserAdmin(manager, username);
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    else {
		new ErrorMessage("Please Select User to Edit.");
	    }
	}
	else if (command.equals("AC_REMOVE")) {
	    JTable table = ((UserTableGUI)gui).getTable();
	    int row = table.getSelectedRow();
	    if (row >= 0) {
		try {
		    String username = (String)table.getValueAt(row, 0);	
		    manager.removeUser(username);
		    getExecutioner().loadUserTable();
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    else {
		new ErrorMessage("Please Select User to Remove.");
	    }
	}
	else if (command.equals("AC_CANCEL")) {
	    getExecutioner().loadSpecialFunctions();
	    
	}
    }
}

