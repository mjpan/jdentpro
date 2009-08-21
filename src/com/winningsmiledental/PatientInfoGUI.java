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

    public JPanel panel, buttons;
    public JButton cancel, newPatient, editPatient, seek, 
        rcn, last, guarNum, phoneNum, ssn;
    public JTable table;
    public RecordTableModel model;
    public JScrollPane scrollPane;
    public JSplitPane splitPane;

    private RecordManager rManager;

    public PatientInfoGUI(ApplicationFrame af) {
	super(af);
    }

    protected String getGUIxml() {
	return "/xml/PatientInfo.xml";
    }


    protected void connectActionListeners() {
	actionableItems.add(cancel);
	actionableItems.add(newPatient);
	actionableItems.add(editPatient);
	actionableItems.add(seek);
	actionableItems.add(rcn);
	actionableItems.add(last);
	actionableItems.add(guarNum);
	actionableItems.add(phoneNum);
	actionableItems.add(ssn);
	PatientInfoListener listener = new PatientInfoListener(this);
    }

    public JTable getTable() {
	return table;
    }

    public RecordManager getRecordManager() {
	return rManager;
    }

    public void setRecordManager() {
	rManager = new PatientRecordManager(getExecutioner().getDatabase());
    }



    protected void setupGUI() throws Exception {
	super.setupGUI();
	
	setRecordManager();

	model = new RecordTableModel(getRecordManager());
	
	TableSorter ts = new TableSorter(model);
	table = new JTable(ts);
	ts.setTableHeader(table.getTableHeader());

	scrollPane = new JScrollPane(table);
	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttons, scrollPane);
	panel.add(splitPane, BorderLayout.CENTER);
	
    }

    protected class RecordTableModel extends JDentProTableModel {
	
	private RecordManager rManager;
	
	public String COLUMN_NAME = "Name";
	public String COLUMN_ADDRESS = "Address";
	public String COLUMN_PHONENUM = "Phone Number";
	public String COLUMN_RCN = "RCN";

	private String[] columnNames = { COLUMN_NAME, COLUMN_ADDRESS, COLUMN_PHONENUM, COLUMN_RCN };

	public RecordTableModel(RecordManager rm) {
	    super();
	    for (int i = 0; i < columnNames.length; i++) {
		addColumn(columnNames[i]);
	    }
	    rManager = rm;
	    populate();
	}

	protected void populate() {
	    ResultSet rs = rManager.getAllPatientRecords();
	    try {
		while (rs.next()) {
		    String name = rs.getString(1) + ", " + rs.getString(2);
		    String address = rs.getString(3);
		    String phone = rs.getString(4);
		    int rcn = rs.getInt(5);
		    addRow(name, address, phone, rcn);
		}
	    }
	    catch (Exception e) {

	    }
	}
	
	protected void addRow(String name, String address, String phone, int rcn) {
	    Vector rowData = new Vector();

	    rowData.add(name);
	    rowData.add(address);
	    rowData.add(phone);
	    rowData.add(new Integer(rcn));

	    addRow(rowData);
	}
    }
    
}