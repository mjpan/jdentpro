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

    public JPanel panel, buttons;
    public JButton cancel, newGuarantor, editGuarantor, seek, 
        rcn, last, guarNum, phoneNum, ssn;
    public JTable table;
    public RecordTableModel model;
    public JScrollPane scrollPane;
    public JSplitPane splitPane;

    private RecordManager rManager;
    private int ptRCN;

    public GuarantorInfoGUI(ApplicationFrame af, int rcn) {
	super(af);
	ptRCN = rcn;
    }

    protected String getGUIxml() {
	return "/xml/GuarantorInfo.xml";
    }


    protected void connectActionListeners() {
	actionableItems.add(cancel);
	actionableItems.add(newGuarantor);
	actionableItems.add(editGuarantor);
	actionableItems.add(seek);
	actionableItems.add(rcn);
	actionableItems.add(last);
	actionableItems.add(guarNum);
	actionableItems.add(phoneNum);
	actionableItems.add(ssn);
	GuarantorInfoListener listener = new GuarantorInfoListener(this);
    }

    public JTable getTable() {
	return table;
    }

    public RecordManager getRecordManager() {
	return rManager;
    }

    public void setRecordManager() {
	rManager = new GuarantorRecordManager(getExecutioner().getDatabase());
    }

    public int getPatientRCN() {
	return ptRCN;
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
	public String COLUMN_PATNUM = "PatNum";

	private String[] columnNames = { COLUMN_NAME, COLUMN_ADDRESS, COLUMN_PHONENUM, COLUMN_PATNUM };

	public RecordTableModel(RecordManager rm) {
	    super();
	    for (int i = 0; i < columnNames.length; i++) {
		addColumn(columnNames[i]);
	    }
	    rManager = rm;
	    populate();
	}

	protected void populate() {
	    ResultSet rs = rManager.getAllGuarantorRecords();
	    try {
		while (rs.next()) {
		    String name = rs.getString(1) + ", " + rs.getString(2);
		    String address = rs.getString(3);
		    String phone = rs.getString(4);
		    int patnum = rs.getInt(5);
		    addRow(name, address, phone, patnum);
		}
	    }
	    catch (Exception e) {

	    }
	}
	
	protected void addRow(String name, String address, String phone, int patnum) {
	    Vector rowData = new Vector();

	    rowData.add(name);
	    rowData.add(address);
	    rowData.add(phone);
	    rowData.add(new Integer(patnum));

	    addRow(rowData);
	}
    }
    
}