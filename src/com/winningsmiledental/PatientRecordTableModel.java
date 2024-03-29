package com.winningsmiledental;

import java.sql.*;
import java.util.*;

public class PatientRecordTableModel extends RecordTableModel {
    public static String COLUMN_FIRSTNAME = "FirstName";
    public static String COLUMN_LASTNAME = "LastName";
    public static String COLUMN_ADDRESS = "Address";
    public static String COLUMN_PHONENUM = "Phone Number";
    public static String COLUMN_RCN = "RCN";

    private static String[] columnNames = 
    { COLUMN_LASTNAME, COLUMN_FIRSTNAME, COLUMN_ADDRESS, COLUMN_PHONENUM, COLUMN_RCN };


    public PatientRecordTableModel() {
	super();
    }

    protected String[] getColumnNames() {
	return columnNames;
    }

    protected void addRow(ResultSet rs) throws SQLException {
	String lname = rs.getString(1);
	String fname =rs.getString(2);
	String address = rs.getString(3);
	String phone = rs.getString(4);
	int rcn = rs.getInt(5);

	Vector rowData = new Vector();
	rowData.add(lname);
	rowData.add(fname);
	rowData.add(address);
	rowData.add(phone);
	rowData.add(new Integer(rcn));

	addRow(rowData);
    }

}