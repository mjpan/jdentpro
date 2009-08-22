package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;

public class GuarantorRecordManager extends AbstractRecordManager {

    public GuarantorRecordManager (Executioner executioner) {
	super(executioner);
    }

    public GuarantorRecordManager (Executioner executioner, String databaseName) {
	super(executioner, databaseName);
    }

   /**
     * Returns ResultSet of data about guarantor with given PatNum.
     *
     * @param int        PatNum of guarantor being queried. 
     * @return ResultSet containing data on guarantor.
     */
    /*
    public ResultSet getRecordWithPatNum(int patnum) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.City, P.State, P.Zip, P.HmPhone, P.SSN, P.Birthdate, P.Gender FROM patient P WHERE P.PatNum=" + patnum);
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
    */
    public Record getRecordUsingInternalID(int id) throws Exception {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.City, P.State, P.Zip, P.HmPhone, P.SSN,P.Gender, P.Birthdate FROM patient P WHERE P.PatNum=" + id);

	    int employeeID = getExecutioner().getCurrentEmployeeID();
	    
	    rs.next();
	    //create the guarantor record here
	    GuarantorRecord record = new GuarantorRecord(rs.getString(1), //last
						rs.getString(2), //first
						rs.getString(3), //street
						rs.getString(4), //city
						rs.getString(5), //state
						rs.getString(6), //zip
						rs.getString(7), //home phone
						rs.getString(8), //ssn
						rs.getInt(9), //gender
						rs.getString(10), //birthday
						employeeID);
	    record.setInternalID(id);
						
	    return record;
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("error retrieving guarantor with internal id "+id, e);
	}
    }


    protected String getQueryForRecordsHavingLastName() {
	return "SELECT LName, FName, Address, HmPhone, PatNum FROM patient WHERE LName=? ORDER BY FNAME"; 
    }

    protected  String getQueryForRecordsHavingFirstName() {
	return "SELECT LName, FName, Address, HmPhone, PatNum FROM patient WHERE FName=? ORDER BY LNAME";
    }

    protected  String getQueryForRecordsHavingSSN() {
	return "SELECT lname, fname, address, hmphone, PatNum FROM patient WHERE ssn=?";
    }

    protected  String getQueryForRecordsHavingPhoneNumber() {
	return "SELECT lname, fname, address, hmphone, PatNum FROM patient WHERE hmphone=?";
   }


    /**
     * If patient with given SSN already exists, edits guarantor with ssn.
     * Else, adds new guarantor to the database.
     *
     * @param String LName, FName
     * @param String Address, Address2, City, State, Zip
     * @param String HmPhone
     * @param String SSN, Gender, Birthdate
     * @param int    employeenum of currently logged in user.
     */
    /*
    public void addNewGuarantor(String last, String first, String address, String city, 
				String state, String zip, String hmPhone, 
				String ssn, int gender, String bday, int employee) throws Exception {
	if (patientAlreadyExists(ssn)) {
	    int gPatNum = getPatNumOfRecordWithSSN(ssn);
	    editGuarantor(gPatNum, last, first, address, city, state, zip, hmPhone, ssn, gender, bday, employee);
	}
	else {
	    try {
		PreparedStatement stmt = null;
		//if a new patient, insert new patient
		stmt = getConnection().prepareStatement("INSERT INTO patient (LName, FName, Address, City, State, Zip, HmPhone, SSN, Gender, Birthdate, LastUpdatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		stmt.setString(1, last);
		stmt.setString(2, first);
		stmt.setString(3, address);
		stmt.setString(4, city);
		stmt.setString(5, state);
		stmt.setString(6, zip);
		stmt.setString(7, hmPhone);
		stmt.setString(8, ssn);
		stmt.setInt(9, gender);
		stmt.setString(10, bday);
		stmt.setInt(11, employee);
		stmt.executeUpdate();
	    }
	    catch (Exception e) {
		e.printStackTrace();
		Exception newE = new Exception("failed on creating new guarantor record", e);
		throw newE;
	    }
	}
    }
    */
    public void addRecord(Record record) throws Exception {
	GuarantorRecord guarantorRecord = (GuarantorRecord) record;
	try {
	    record.getInternalID();
	    //internal id already exists
	    editRecord(record);
	    return;
	}
	catch (Exception e) {
	    // no internal id yet
	}

	if (patientAlreadyExists(guarantorRecord.getSsn())) {
	    //this assumes that ssn is unique

	    int gPatNum = getPatNumOfRecordWithSSN(guarantorRecord.getSsn());
	    guarantorRecord.setInternalID(gPatNum);
	    editRecord(record);
	    return;
	}
	try {
	    PreparedStatement stmt = null;
	    
	    stmt = getConnection().prepareStatement("INSERT INTO patient (LName, FName, Address, City, State, Zip, HmPhone, SSN, Gender, Birthdate, LastUpdatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

	    fillStatementValues(stmt, guarantorRecord);

	    stmt.executeUpdate();

	    //update guarantor record's internal id
	    int internalID = getPatNumOfRecordWithSSN(guarantorRecord.getSsn());
	    guarantorRecord.setInternalID(internalID);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on creating new guarantor record", e);
	    throw newE;
	}
    }

    /*
      Updates a guarantors information.
    */
    /*
    public void editGuarantor(int gPatNum, String last, String first, String address, 
			      String city, String state, String zip, String hmPhone, 
			      String ssn, int gender, String bday, int employee) throws Exception {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET LName=?, FName=?, Address=?, City=?, State=?, Zip=?, HmPhone=?, SSN=?, Gender=?, Birthdate=?, LastUpdatedBy=? WHERE PatNum=?;");
	    stmt.setString(1, last);
	    stmt.setString(2, first);
	    stmt.setString(3, address);
	    stmt.setString(4, city);
	    stmt.setString(5, state);
	    stmt.setString(6, zip);
	    stmt.setString(7, hmPhone);
	    stmt.setString(8, ssn);
	    stmt.setInt(9, gender);
	    stmt.setString(10, bday);
	    stmt.setInt(11, employee);
	    stmt.setInt(12, gPatNum);
	    stmt.executeUpdate();
	    System.out.println("");
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on editing guarantor "+gPatNum, e);
	    throw newE;
	}
    }    
    */
    public void editRecord(Record record) throws Exception {	

	try {
	    record.getInternalID();
	}
	catch (Exception e) {
	    throw new Exception("guarantor record does not currently exist", e);	    
	}

	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET LName=?, FName=?, Address=?, City=?, State=?, Zip=?, HmPhone=?, SSN=?, Gender=?, Birthdate=?, LastUpdatedBy=? WHERE PatNum=?;");
	    fillStatementValues(stmt, (GuarantorRecord)record, record.getInternalID());
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on editing guarantor "+record.getInternalID(), e);
	    throw newE;
	}
    }



    protected void fillStatementValues(PreparedStatement stmt, GuarantorRecord record) throws SQLException {
	    stmt.setString(1, record.getLastName());
	    stmt.setString(2, record.getFirstName());
	    stmt.setString(3, record.getAddressStreet());
	    stmt.setString(4, record.getAddressCity());
	    stmt.setString(5, record.getAddressState());
	    stmt.setString(6, record.getAddressZip());
	    stmt.setString(7, record.getHomePhone());
	    stmt.setString(8, record.getSsn());
	    stmt.setInt(9, record.getGender());
	    stmt.setString(10, record.getBirthday());
	    stmt.setInt(11, record.getEmployee());
    }

    protected void fillStatementValues(PreparedStatement stmt, GuarantorRecord record, int id) throws SQLException {
	fillStatementValues(stmt,record);
	stmt.setInt(12, id);
    }

}