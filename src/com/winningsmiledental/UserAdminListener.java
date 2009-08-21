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

public class UserAdminListener extends AbstractListener {

    public UserAdminListener(GUI gui) {
	super(gui);

    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();


	System.out.println("processing action >> "+command);

	UserAdminGUI temp = (UserAdminGUI)gui;

	UserAccountManager manager = temp.getUserAccountManager();

	if (command.equals("AC_CANCEL")) {
	    System.out.println("cancelled");

	    //do nothing, return to user table screen
	    ((JDentProExecutioner)getExecutioner()).loadUserTable();
        } 
	else if (command.equals("AC_SAVE")) {
	    String user = temp.getUser();
	    String login = temp.getLogin();
	    String password = temp.getPassword();
	    String confirm = temp.getConfirmPassword();
	    int accessLevel = temp.getAccessLevel();
	    //if user is null, add new user
	    if (user == null) {
		if (manager.userAlreadyExists(login)) {
		    new ErrorMessage("User Already Exixts. Please Choose Another Username.");
		    return;
		}
		else if (!password.equals("") && 
		    password.equals(confirm) && 
		    !login.equals("")) {
		    try {
			manager.addUser(login, password, accessLevel);
		    }
		    catch (Exception e) { 
			e.printStackTrace(); 
		    }
		}
	    }
	    //if user is not null, edit user
	    else {
		System.out.println("attempting to edit existing user");

		try {
		    if (!password.equals("") && 
			password.equals(confirm)) {
			manager.setPassword(login, password);
		    }
		    manager.setAccessLevel(login, accessLevel);
		}
		catch (Exception e) { 
		    e.printStackTrace(); 
		}
	    }
	    
	    //return to user table screen
	    ((JDentProExecutioner)getExecutioner()).loadUserTable();
	}
	
    }
    
   

}



