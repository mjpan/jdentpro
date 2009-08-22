package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JComboBox.*;
import javax.swing.JPasswordField.*;
import javax.swing.JTextField.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.sql.*;

/**
 * Administers a user account for a server
 *
 * this class now creates the GUI by loading it from XML
 */
public class UserAdminGUI extends AbstractGUI {
    public JLabel titleLabel;
    public JPanel accountPanel;
    public JTextField userField;
    public JPasswordField passwordField;
    public JPasswordField confirmPasswordField;
    public JComboBox accessLevel;
    public JButton saveButton, cancelButton;
    private String user;
    private UserAccountManager manager;

    public SwingEngine swix = null;
    private ApplicationFrame applicationFrame;

    /**
     * constructor for creating the screen for adding a new user
     */
    public UserAdminGUI(ApplicationFrame af, UserAccountManager manager) {
	super(af);
	loadAccessLevels();
	this.manager = manager;
	setUser();
    }

    /**
     * constructor for creating the screen for editing an existing user
     */
    public UserAdminGUI(ApplicationFrame af, UserAccountManager manager, String user) {
	super(af);
	loadAccessLevels();
	this.manager = manager;
	setUser(user);
    }

    public void setUser() {
	titleLabel.setText("Add User");
	userField.setEditable(true);
	this.user = null;
    }
    
    public void setUser(String user) {
	this.user = user;
	loadFields();
    }

    public UserAccountManager getUserAccountManager() {
	return manager;
    }

    public String getUser() {
	return user;
    }

    public String getLogin() {
	return userField.getText();
    }

    public String getPassword() {
	return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
	return new String(confirmPasswordField.getPassword());
    }

    public int getAccessLevel() {
	return accessLevel.getSelectedIndex();
    }

    protected String getGUIxml() {
	return "/xml/UserAdmin.xml";
    }
    
    protected void loadAccessLevels() {
	accessLevel.addItem("");
	accessLevel.addItem("1 - Employee");
	accessLevel.addItem("2 - Provider");
    }

    protected void loadFields() {
	if (user != null) {
	    userField.setText(user);
	    accessLevel.setSelectedIndex(getUserAccountManager().getAccessLevel(user));
	}
    }

    protected void connectActionListeners() {
	actionableItems.add(saveButton);
	actionableItems.add(cancelButton);
	UserAdminListener listener = new UserAdminListener(this);
    }
}



