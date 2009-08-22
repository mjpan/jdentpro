package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.util.*;


public class LoginScreenGUI extends AbstractGUI {

    public JPanel panel;
    public XGridBagConstraints gbc1, gbc2;
    public JPasswordField passwordField;
    public JTextField userField;
    public JButton login, quit;
    public JComboBox database;
    public UserAccountManager accountManager;

    public LoginScreenGUI(ApplicationFrame af) {
	super(af);

	setDatabaseNames();
	/*
	database.addItem("Choose Database..");
	database.addItem("Temple City");
	database.addItem("Cerritos");
	*/
    }

    protected void setDatabaseNames() {
	JDentPro application = (JDentPro) getAppFrame().getApplication();
	String databaseNames = 
	    application.getProperty("database.names");
	String[] databaseNameArray = databaseNames.split(";");
	if (databaseNameArray.length>1) {
	    database.addItem("Choose database");
	}
	for (int i=0; i<databaseNameArray.length; i++) {
	    database.addItem(databaseNameArray[i]);
	}
    }

    public UserAccountManager getUserAccountManager() {
	return accountManager;
    }

    public void setUserAccountManager(String database) {
	accountManager = UserAccountManagerFactory.getUserAccountManager
	    (database);
    }

    public String getUserName() {
	return userField.getText();
    }

    public String getPassword() {
	return new String(passwordField.getPassword());
    }

    /*
    public int getDatabase() {
	return database.getSelectedIndex();
    }
    */
    protected String getDatabase() {
	return (String) database.getSelectedItem();
    }

    protected String getGUIxml() {
	return "/xml/LoginScreen.xml";
    }

    protected void connectActionListeners() {
	actionableItems.add(login);
	actionableItems.add(quit);
	LoginScreenListener listener = new LoginScreenListener(this);
    }

}