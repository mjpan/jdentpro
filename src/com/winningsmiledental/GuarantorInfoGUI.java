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

public class GuarantorInfoGUI extends AbstractGUI {

    //public static String SEARCHFIELD_RCN = "RCN";
    public static String SEARCHFIELD_LASTNAME = "Last Name";
    public static String SEARCHFIELD_FIRSTNAME = "First Name";
    public static String SEARCHFIELD_SSN = "SSN";
    public static String SEARCHFIELD_PHONE = "Phone";

    public JPanel panel, buttons;
    public JButton cancel, newGuarantor, editGuarantor, seek; 
    //rcn, last, guarNum, phoneNum, ssn;
    public JTable table;
    public RecordTableModel model;
    public JScrollPane scrollPane;
    public JSplitPane splitPane;

    private RecordManager rManager;
    private int ptRCN;

    public JTextField searchParameterValue;
    public JComboBox searchParameter;

    public GuarantorInfoGUI(ApplicationFrame af, int rcn) {
	super(af);
	setPatientRCN(rcn);
	setSearchParameters();
    }

    protected void setSearchParameters() {
	//searchParmeter.addItem(SEARCHFIELD_RCN);
	searchParameter.addItem(SEARCHFIELD_LASTNAME);
	searchParameter.addItem(SEARCHFIELD_FIRSTNAME);
	searchParameter.addItem(SEARCHFIELD_SSN);
	searchParameter.addItem(SEARCHFIELD_PHONE);
    }

    /**
     * sets the active patient RCN
     */
    protected void setPatientRCN(int rcn) {
	ptRCN = rcn;
    }

    protected String getGUIxml() {
	return "/xml/GuarantorInfo.xml";
    }

    protected RecordTableModel getModel() {
	return model;
    }

    protected void refresh() {
	refreshTable();
    }

    protected void connectActionListeners() {
	actionableItems.add(cancel);
	actionableItems.add(newGuarantor);
	actionableItems.add(editGuarantor);
	actionableItems.add(seek);
	//actionableItems.add(rcn);
	//actionableItems.add(last);
	//actionableItems.add(guarNum);
	//actionableItems.add(phoneNum);
	//actionableItems.add(ssn);
	GuarantorInfoListener listener = new GuarantorInfoListener(this);
    }

    public JTable getTable() {
	return table;
    }

    public RecordManager getRecordManager() {
	return getExecutioner().getGuarantorRecordManager();
    }

    /*
    public void setRecordManager() {
	rManager = new GuarantorRecordManager(getExecutioner().getDatabase());
    }
    */

    public int getPatientRCN() {
	return ptRCN;
    }


    protected void setupGUI() throws Exception {
	super.setupGUI();
	
	//setRecordManager();

	//model = new RecordTableModel(getRecordManager());
	model = new GuarantorRecordTableModel();
	model.setResultSet(getRecordManager().getAllGuarantorRecords());

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
	    resultSet = 
		//getRecordManager().getAllPatientRecords();
		getRecordManager().getAllGuarantorRecords();
	}
	/*
	else if (searchField.equals(SEARCHFIELD_RCN)) {
	    try {
		int rcn = Integer.parseInt(searchValue);
		resultSet = getRecordManager().getPreviewRecordHavingRCN(rcn);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	*/
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



}