package com.winningsmiledental;

import java.sql.*;

public abstract class RecordTableModel extends JDentProTableModel {
    protected ResultSet resultSet;

    //public RecordTableModel(RecordManager rm) {
    public RecordTableModel() {
	super();

	setColumns();

	//rManager = rm;
	//populate();
    }

    protected void setColumns() {
	String[] columnNames = getColumnNames();
	for (int i = 0; i < columnNames.length; i++) {
	    addColumn(columnNames[i]);
	}
    }

    protected abstract String[] getColumnNames();

    public void setResultSet(ResultSet rs) {
	resultSet = rs;
	populate();
    }

    protected ResultSet getResultSet() {
	return resultSet;
    }

    protected void populate() {
	//ResultSet rs = rManager.getAllPatientRecords();

	/* remove all current rows */
	try {
	    while (true) {
		removeRow(0);
	    }
	}
	catch (ArrayIndexOutOfBoundsException e) {
	}

	try {
	    while (getResultSet().next()) {
		/*
		String name = resultSet.getString(1) + ", " + 
		    resultSet.getString(2);
		String address = resultSet.getString(3);
		String phone = resultSet.getString(4);
		int rcn = resultSet.getInt(5);
		addRow(name, address, phone, rcn);
		*/
		addRow(resultSet);
	    }
	}
	catch (SQLException e) {

	}
    }

    /*
    protected void addRow(String name, String address, String phone, int rcn) {
	Vector rowData = new Vector();

	rowData.add(name);
	rowData.add(address);
	rowData.add(phone);
	rowData.add(new Integer(rcn));

	addRow(rowData);
    }
    */
    protected abstract void addRow(ResultSet rs) throws SQLException;

}