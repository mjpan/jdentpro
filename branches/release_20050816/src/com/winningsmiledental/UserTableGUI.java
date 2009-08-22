package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.util.*;
import java.sql.*;

public class UserTableGUI extends AbstractGUI {

    public JPanel panel, buttons;
    public JButton addUser, editUser, removeUser, cancel;
    public JTable table;
    public UserTableModel model;
    public JScrollPane scrollPane;
    public JSplitPane splitPane;

    private UserAccountManager accountManager;

    public UserTableGUI(ApplicationFrame af) {
	super(af);
    }

    protected String getGUIxml() {
	return "/xml/UserTable.xml";
    }


    protected void connectActionListeners() {
	actionableItems.add(addUser);
	actionableItems.add(editUser);
	actionableItems.add(removeUser);
	actionableItems.add(cancel);
	UserTableListener listener = new UserTableListener(this);
    }

    public JTable getTable() {
	return table;
    }

    public UserTableModel getModel() {
	return model;
    }

    public UserAccountManager getUserAccountManager() {
	return accountManager;
    }

    public void setUserAccountManager(String database) {
	accountManager = UserAccountManagerFactory.getUserAccountManager(database);
    }


    protected void setupGUI() throws Exception {
	super.setupGUI();

	setUserAccountManager(getExecutioner().getDatabase());

	//create JTable
	//add JTable to panel
	model = new UserTableModel(getUserAccountManager());
	
	TableSorter ts = new TableSorter(model);
	table = new JTable(ts);
	ts.setTableHeader(table.getTableHeader());

	scrollPane = new JScrollPane(table);
	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				   scrollPane, buttons);
	panel.add(splitPane, BorderLayout.CENTER);
	
    }

    protected class UserTableModel extends JDentProTableModel {
	
	private UserAccountManager uaManager;
	
	public String COLUMN_USERNAME = "User Name";
	public String COLUMN_ACCESSLEVEL = "Access Level";

	private String[] columnNames = { COLUMN_USERNAME, COLUMN_ACCESSLEVEL };

	public UserTableModel(UserAccountManager uam) {
	    super();
	    for (int i = 0; i < columnNames.length; i++) {
		addColumn(columnNames[i]);
	    }
	    uaManager = uam;
	    populate();
	}

	protected void populate() {
	    try {
		ResultSet rs = uaManager.getAllUsers();
		while (rs.next()) {
		    String username = rs.getString(1);
		    int accessLevel = rs.getInt(2);
		    if (accessLevel == 14) {
			accessLevel = 3;
		    }
		    addRow(username, new Integer(accessLevel));
		}
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	protected void addRow(String username, Integer accessLevel) {
	    Vector rowData = new Vector();

	    rowData.add(username);
	    rowData.add(accessLevel);
	    addRow(rowData);
	}

	public void removeRow(int row) {
	    super.removeRow(row);
	}


    }
    
}