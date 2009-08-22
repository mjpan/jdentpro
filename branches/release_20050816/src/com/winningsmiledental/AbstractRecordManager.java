package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;

public abstract class AbstractRecordManager implements RecordManager {

    protected static Connection connection;

    protected String databaseName;

    private Executioner executioner;

    public AbstractRecordManager(Executioner executioner) {
	this(executioner, "dental");
    }

    public AbstractRecordManager(Executioner executioner, String databaseName) {
	this.databaseName = databaseName;
	establishConnection();
	setExecutioner(executioner);
    }

    public Executioner getExecutioner() {
	return executioner;
    }

    private void setExecutioner(Executioner e) {
	executioner = e;
    }

    /**
     * Establishes connection to database.
     */
    protected void establishConnection() {
	connection = JDentPro.getConnection(databaseName);
    }

    /**
     * Returns connection to database.
     *
     * @return Connection
     */
    public Connection getConnection() {
	return connection;
    }

    /**
     * Queries and returns a ResultSet with name, address, hmphone and rcn of all patients.
     * Ordered by RCN.
     *
     * @return ResultSet Name, Address, HmPhone and RCN of all patients.
     */
    public ResultSet getAllPatientRecords() {
	try {
	    Statement stmt = connection.createStatement();
	    ResultSet rs = 
		stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.RCN FROM patient P WHERE P.RCN is not null ORDER BY P.RCN;");
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Queries and returns a ResultSet with name, address, hmphone and patnum of all guarantors.
     * Ordered by patnum.
     *
     * @return ResultSet Name, Address, HmPhone and PatNum of all guarantors.
     */
    public ResultSet getAllGuarantorRecords() {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = 
		stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.PatNum FROM patient P ORDER BY P.PatNum");
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Updates address and home phone number of all patients who share a guarantor.
     *
     * @param int    patnum of guarantor
     * @param String address of guarantor
     * @param String city of guarantor
     * @param String state of guarantor
     * @param String zip cod of guarantor
     * @param String home phone of guarantor
     * @param int    employeenum of employee currently logged in
     */
    public void updateAllPatientRecordsWithGuarantor(int patnum, String address, String city, String state, String zip, String hmPhone, int employee) {
	try {
	    PreparedStatement stmt = 
		connection.prepareStatement("UPDATE patient SET Address=?, City=?, State=?, Zip=?, HmPhone=?, LastUpdatedBy=? WHERE Guarantor=?");
	    stmt.setString(1, address);
	    stmt.setString(2, city);
	    stmt.setString(3, state);
	    stmt.setString(4, zip);
	    stmt.setString(5, hmPhone);
	    stmt.setInt(6, employee);
	    stmt.setInt(7, patnum);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Returns a patient's guarantor's patnum.
     *
     * @param int  RCN of patient being queried.
     * @return int patnum of patient's gurantor.
     */
    public int getGuarantorOfPatient(int rcn) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("Select guarantor FROM patient WHERE RCN=?");
	    stmt.setInt(1, rcn);
	    ResultSet rs = stmt.executeQuery();
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }



    /**
     * Returns String of patient with given patnum in the form "LName, FName".
     *�
     * @param int     PatNum of patient being queried.
     * @return String Name of patient in form "LName, Fname"
     */
    public String getNameOfPatientWithPatNum(int patnum) {
	String name = "";
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select P.LName, P.FName FROM patient P WHERE P.PatNum = " + patnum);
	    rs.first();
	    //return rs.getString(1) + ", " + rs.getString(2);
	    name = rs.getString(1) + ", " + rs.getString(2);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return name;
    }



    /**
     * Returns lowest unassigned PatNum.
     *
     * @return int lowest unassigned PatNum.
     */
    /*
    public int getNewPatNum() {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select MAX(PatNum) + 1 from patient");
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }
    */

    /**
     * Returns int PatNum of patient with given SSN.
     *
     * @param String SSN of patient being queried.
     * @return int   Patnum of patient with given SSN.
     */
    public int getPatNumOfRecordWithSSN(String ssn) throws Exception{
	try {
	    //Statement stmt = getConnection().createStatement();
	    //ResultSet rs = stmt.executeQuery("SELECT PatNum FROM patient WHERE SSN=?");

	    PreparedStatement stmt = 
		getConnection().prepareStatement("SELECT PatNum FROM patient WHERE SSN=?");
	    stmt.setString(1, ssn);
	    ResultSet rs = stmt.executeQuery();


	    if (rs.next()) {
		return rs.getInt(1);
	    }
	    else {
		throw new UnknownSSNException("no person with ssn "+ssn+" found");
	    }
	}
	catch (SQLException e) {
	    throw new Exception("failed on getting patnum with ssn", e);
	}
    }

    protected void validateSSN(String ssn) throws InvalidSSNException, DuplicateSSNException {
	if (ssn == null) {
	    throw new InvalidSSNException("ssn cannot be null");
	}
	if (!ssn.matches("\\d{9}") && //actual ssn
	    !ssn.matches(JDentProExecutioner.AUTOGEN_SSN_PATTERN)) { //auto generated unique
	    throw new InvalidSSNException("ssn is invalid");
	}
	try {
	    //make sure ssn does not already exist
	    int internalID = getPatNumOfRecordWithSSN(ssn);
	    throw new DuplicateSSNException("person with ssn already exists in database");
	}
	catch (UnknownSSNException e) {
	    //do nothing
	}
	catch (Exception e) {
	    //do nothing
	}
    }




    /*
      Updates a guarantor's RCN, so that he/she is a patient.
    */
    public void editGuarantor(String ssn, int rcn, int employee) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET RCN=?, LastUpdatedBy=? WHERE SSN=?;");
	    stmt.setInt(1, rcn);
	    stmt.setInt(2, employee);
	    stmt.setString(3, ssn);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Returns true if patient with given SSN already exists in the database.
    */
    public boolean patientAlreadyExists(String ssn) {
	/*
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT PatNum FROM patient WHERE SSN=?");
	    stmt.setString(1, ssn);
	    ResultSet rs = stmt.executeQuery();
	    return rs.next();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
	*/
	try {
	    getPatNumOfRecordWithSSN(ssn);
	    return true;
	}
	catch (Exception e) {
	    return false;
	}
    }


    /*
      Adds a recall to patient with patnum.
    */
    public void addRecall(int patnum, String recallType, String nextRecall, String lastRecall, int interval, boolean disabled) throws Exception {
	//note: add recall type and last visit later.
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO recall (PatNum, RecallType, DateDueCalc, DatePrevious, RecallInterval, IsDisabled) VALUES (?, ?, ?, ?, ?, ?)");
	    stmt.setInt(1, patnum);
	    stmt.setString(2, recallType);
	    stmt.setString(3, nextRecall);
	    stmt.setString(4, lastRecall);
	    stmt.setInt(5, interval);

	    int disabledInt = 0;
	    if (disabled) {
		disabledInt = 1;
	    }
	    stmt.setInt(6, disabledInt);

	    stmt.executeUpdate();
	    //calculateRecallDate(patnum, lastRecall, interval);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on adding new recall to patient "+patnum+" with recallType "+recallType, e);
	    throw newE;
	}
    }

    /*
      Edits recall of patient with patnum.
    */
    public void editRecall(int patnum, String recallType, String nextRecall, String lastRecall, int interval, boolean disabled) throws Exception {
	//add recall type and last visit later.

	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET DateDueCalc=?, DatePrevious=?, RecallInterval=?, IsDisabled=? WHERE PatNum=? AND RecallType=?");
	    stmt.setString(1, nextRecall);
	    stmt.setString(2, lastRecall);
	    stmt.setInt(3, interval);
	    stmt.setInt(5, patnum);
	    stmt.setString(6, recallType);

	    int disabledInt = 0;
	    if (disabled) {
		disabledInt = 1;
	    }
	    stmt.setInt(4, disabledInt);

	    stmt.executeUpdate();
	    //calculateRecallDate(patnum, lastRecall, interval);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on editing recall", e);
	    throw newE;
	}
    }


    /*
      Returns records of patients who have recall within given dates.
    */
    public ResultSet getRecordsWithRecallBetweenDates(String start, String end) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT p.rcn, p.lname, p.fname, p.address, p.address2, p.city, p.state, p.zip, p.hmphone, p.wkphone, p.wirelessphone, r.dateduecalc, r.recalltype, YEAR(CURRENT_DATE())-YEAR(p.birthdate) AS age FROM patient p, recall r WHERE r.isdisabled=0 AND r.dateduecalc>=? AND r.dateduecalc<=? AND p.patnum=r.patnum ORDER BY p.lname");
	    stmt.setString(1, start);
	    stmt.setString(2, end);
	    ResultSet rs = stmt.executeQuery();
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    /*
      Returns records of patients who have a birthday in the given month
    */
    public ResultSet getRecordsWithBirthdateInMonth(String month) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT p.rcn, p.lname, p.fname, p.address, p.address2, p.city, p.state, p.zip, p.hmphone, p.wkphone, p.wirelessphone, p.birthdate, YEAR(CURRENT_DATE())-YEAR(p.birthdate) AS age FROM patient p, recall r WHERE r.isdisabled=0 AND p.patnum=r.patnum AND p.birthdate LIKE ? ORDER BY p.lname");
	    String date = "____-" + month + "-__";
	    stmt.setString(1, date);
	    ResultSet rs = stmt.executeQuery();
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }





    /**
     * @returns patient record with given rcn.
     */
    public ResultSet getPreviewRecordHavingRCN(int rcn) {
	ResultSet resultSet = null;
	try {
	    PreparedStatement stmt = 
		getConnection().prepareStatement("SELECT LName, FName, Address, HmPhone, rcn FROM patient WHERE rcn=?");
	    stmt.setInt(1, rcn);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected abstract String getQueryForRecordsHavingLastName();

    public ResultSet getRecordsWithLastName(String last) {
	ResultSet resultSet = null;
	try {
	    PreparedStatement stmt = 
		//getConnection().prepareStatement("SELECT LName, FName, Address, HmPhone, rcn FROM patient WHERE LName=? ORDER BY FNAME"); 
		getConnection().prepareStatement(getQueryForRecordsHavingLastName());
	    stmt.setString(1, last);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected abstract String getQueryForRecordsHavingFirstName();

   public ResultSet getRecordsWithFirstName(String first) {
	ResultSet resultSet = null;
	try {
	    PreparedStatement stmt = 
		//getConnection().prepareStatement("SELECT LName, FName, Address, HmPhone, rcn FROM patient WHERE FName=? ORDER BY LNAME");
		getConnection().prepareStatement(getQueryForRecordsHavingFirstName());
	    stmt.setString(1, first);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected abstract String getQueryForRecordsHavingSSN();

    public ResultSet getRecordWithSSN(String ssn) {
	ResultSet resultSet = null;
	try {
	    //String temp = ssn.replaceAll("\\D", "");

	    PreparedStatement stmt = 
		//getConnection().prepareStatement("SELECT lname, fname, address, hmphone, rcn FROM patient WHERE ssn=?");
		getConnection().prepareStatement(getQueryForRecordsHavingSSN());
	    stmt.setString(1, ssn);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }

    protected abstract String getQueryForRecordsHavingPhoneNumber();

    public ResultSet getRecordsWithPhoneNum(String phone) {
	ResultSet resultSet = null;
	try {
	    String temp = phone.replaceAll("\\D", "");
	    
	    PreparedStatement stmt = 
		//getConnection().prepareStatement("SELECT lname, fname, address, hmphone, rcn FROM patient WHERE hmphone=?");
		getConnection().prepareStatement(getQueryForRecordsHavingPhoneNumber());
	    stmt.setString(1, temp);
	    resultSet = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return resultSet;
    }
   
}