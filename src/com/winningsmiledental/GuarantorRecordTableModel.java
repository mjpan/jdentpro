package com.winningsmiledental;

import java.sql.*;
import java.util.*;

public class GuarantorRecordTableModel extends RecordTableModel {
	
    public static String COLUMN_NAME = "Name";
    public static String COLUMN_ADDRESS = "Address";
    public static String COLUMN_PHONENUM = "Phone Number";
    public static String COLUMN_PATNUM = "PatNum";

    private static String[] columnNames = 
    { COLUMN_NAME, COLUMN_ADDRESS, COLUMN_PHONENUM, COLUMN_PATNUM };

    public GuarantorRecordTableModel() {
	super();
    }


    protected String[] getColumnNames() {
	return columnNames;
    }

    protected void addRow(ResultSet rs)  throws SQLException {
	String name = rs.getString(1) + ", " + rs.getString(2);
	String address = rs.getString(3);
	String phone = rs.getString(4);
	int patnum = rs.getInt(5);

	Vector rowData = new Vector();
	
	rowData.add(name);
	rowData.add(address);
	rowData.add(phone);
	rowData.add(new Integer(patnum));
	
	addRow(rowData);
    }
}
