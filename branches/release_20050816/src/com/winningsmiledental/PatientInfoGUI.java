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

public class PatientInfoGUI extends AbstractGUI {
    public static String SEARCHFIELD_RCN = "RCN";
    public static String SEARCHFIELD_LASTNAME = "Last Name";
    public static String SEARCHFIELD_FIRSTNAME = "First Name";
    public static String SEARCHFIELD_SSN = "SSN";
    public static String SEARCHFIELD_PHONE = "PHONE";

    public JPanel panel, buttons;
    public JButton cancel, newPatient, editPatient, seek;
    //rcn, last, guarNum, phoneNum, ssn;

    public JTable table;
    public RecordTableModel model;
    public JScrollPane scrollPane;
    public JSplitPane splitPane;

    public JTextField searchParameterValue;
    public JComboBox searchParameter;

    private RecordManager rManager;

    public PatientInfoGUI(ApplicationFrame af) {
	super(af);
	setSearchParameters();
    }

    protected void setSearchParameters() {
	searchParameter.addItem(SEARCHFIELD_RCN);
	searchParameter.addItem(SEARCHFIELD_LASTNAME);
	searchParameter.addItem(SEARCHFIELD_FIRSTNAME);
	searchParameter.addItem(SEARCHFIELD_SSN);
	searchParameter.addItem(SEARCHFIELD_PHONE);
    }

    protected String getGUIxml() {
	return "/xml/PatientInfo.xml";
    }

    protected RecordTableModel getModel() {
	return model;
    }

    protected void connectActionListeners() {
	actionableItems.add(cancel);
	actionableItems.add(newPatient);
	actionableItems.add(editPatient);
	actionableItems.add(seek);
	//actionableItems.add(rcn);
	//actionableItems.add(last);
	//actionableItems.add(guarNum);
	//actionableItems.add(phoneNum);
	//actionableItems.add(ssn);
	PatientInfoListener listener = new PatientInfoListener(this);
    }

    public JTable getTable() {
	return table;
    }

    public RecordManager getRecordManager() {
	//return rManager;
	return getExecutioner().getPatientRecordManager();
    }

    /*
    public void setRecordManager() {
	rManager = new PatientRecordManager(getExecutioner().getDatabase());
    }
    */


    protected void setupGUI() throws Exception {
	super.setupGUI();
	
	//setRecordManager();

	//model = new RecordTableModel(getRecordManager());
	model = new PatientRecordTableModel();
	getModel().setResultSet(getRecordManager().getAllPatientRecords());
	
	displayTable();

	scrollPane = new JScrollPane(table);
	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttons, scrollPane);
	panel.add(splitPane, BorderLayout.CENTER);
	
    }

    protected void displayTable() {	
	TableSorter ts = new TableSorter(getModel());
	table = new JTable(ts);
	ts.setTableHeader(table.getTableHeader());
    }

    protected void refresh() {
	refreshTable();
    }

    protected void refreshTable() {
	/*
	searchParameter.addItem("RCN");
	searchParameter.addItem("Last Name");
	searchParameter.addItem("Phone Number");
	searchParameter.addItem("SSN");
	 */
	String searchField = (String) searchParameter.getSelectedItem();
	String searchValue = searchParameterValue.getText();
	searchParameterValue.setText("");

	ResultSet resultSet = null;

	if (searchValue.length() == 0) {
	    resultSet = getRecordManager().getAllPatientRecords();
	}
	else if (searchField.equals(SEARCHFIELD_RCN)) {
	    try {
		int rcn = Integer.parseInt(searchValue);
		resultSet = getRecordManager().getPreviewRecordHavingRCN(rcn);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	else if (searchField.equals(SEARCHFIELD_LASTNAME)) {
	    try {
		resultSet = getRecordManager().getRecordsWithLastName(searchValue);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	else if (searchField.equals(SEARCHFIELD_FIRSTNAME)) {
	    try {
		resultSet = getRecordManager().getRecordsWithFirstName(searchValue);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	else if (searchField.equals(SEARCHFIELD_SSN)) {
	    try {
		resultSet = getRecordManager().getRecordWithSSN(searchValue);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	else if (searchField.equals(SEARCHFIELD_PHONE)) {
	    try {
		resultSet = getRecordManager().getRecordsWithPhoneNum(searchValue);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}

	if (resultSet != null) {
	    getModel().setResultSet(resultSet);
	    table.repaint();
	}
    }

    /*
      Returns patient record with given rcn.
    */
    /*
    protected ResultSet getRecordWithRCN(int rcn) {
	ResultSet resultSet = null;
	try {
	    PreparedStatement stmt = 
		getRecordManager().getConnection().prepareStatement("SELECT LName, FName, Address, HmPhone, RCN FROM patient WHERE RCN=?"); 
	    stmt.setInt(1, rcn);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected ResultSet getRecordsWithLastName(String last) {
	ResultSet resultSet = null;
	try {
	    PreparedStatement stmt = 
		getRecordManager().getConnection().prepareStatement("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.RCN FROM patient P WHERE P.LNAME=? ORDER BY P.FNAME"); 
	    stmt.setString(1, last);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

   protected ResultSet getRecordsWithFirstName(String first) {
	ResultSet resultSet = null;
	try {
	    PreparedStatement stmt = 
		getRecordManager().getConnection().prepareStatement("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.RCN FROM patient P WHERE P.FNAME=? ORDER BY P.LNAME"); 
	    stmt.setString(1, first);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected ResultSet getRecordWithSSN(String ssn) {
	ResultSet resultSet = null;
	try {
	    String temp = ssn.replaceAll("\\D", "");

	    PreparedStatement stmt = getRecordManager().getConnection().prepareStatement("SELECT lname, fname, address, hmphone, rcn FROM patient WHERE ssn=?");
	    stmt.setString(1, temp);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected ResultSet getRecordsWithPhoneNum(String phone) {
	ResultSet resultSet = null;
	try {
	    String temp = phone.replaceAll("\\D", "");
	    
	    PreparedStatement stmt = getRecordManager().getConnection().prepareStatement("SELECT lname, fname, address, hmphone, rcn FROM patient WHERE hmphone=?");
	    stmt.setString(1, temp);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
	
    }
    */
}