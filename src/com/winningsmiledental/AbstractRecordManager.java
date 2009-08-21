package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;

public abstract class AbstractRecordManager implements RecordManager {

    protected static Connection connection;

    protected String databaseName;

    public AbstractRecordManager() {
	this("dental");
    }

    public AbstractRecordManager(String databaseName) {
	this.databaseName = databaseName;
	establishConnection();
    }

    protected void establishConnection() {
	connection = JDentPro.getConnection(databaseName);
    }

    protected Connection getConnection() {
	return connection;
    }

    public ResultSet getAllPatientRecords() {
	try {
	    Statement stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.RCN FROM patient P WHERE P.RCN is not null ORDER BY P.RCN;");// P.RCN > 1000 GROUP BY P.RCN;");
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
      Returns all patients, shows PatNum.
    */
    public ResultSet getAllGuarantorRecords() {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.PatNum FROM patient P ORDER BY P.PatNum");// GROUP BY P.PatNum");
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
      Updates address & phone number of patiets who share current guarantor
    */
    public void updateAllPatientRecordsWithGuarantor(int patnum, String address, String city, String state, String zip, String hmPhone, int employee) {
	try {
	    PreparedStatement stmt = connection.prepareStatement("UPDATE patient SET Address=?, City=?, State=?, Zip=?, HmPhone=?, LastUpdatedBy=? WHERE Guarantor=?");
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

    /*
      Returns a patients guarantor number
    */
    public int getGuarantorOfPatient(int rcn) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select guarantor FROM patient WHERE RCN=" + rcn);
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /*
      Returns patient record with given rcn.
    */
    public ResultSet getRecordWithRCN(int rcn) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT P.PatNum, P.LName, P.FName, P.MiddleI, P.Salutation, P.Preferred, P.Address, P.Address2, P.City, P.State, P.Zip, P.HmPhone, P.WkPhone, P.WirelessPhone, P.SSN, P.Gender, P.Birthdate, P.Guarantor FROM patient P WHERE P.RCN=" + rcn); 
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
      Returns patient record with given patnum.
    */
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

    /*
      Returns name of patient with given patnum in form "last name, first name".
    */
    public String getNameOfPatientWithPatNum(int patnum) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select P.LName, P.FName FROM patient P WHERE P.PatNum = " + patnum);
	    rs.first();
	    return rs.getString(1) + ", " + rs.getString(2);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
      Returns rcn of patient with given patnum.
    */
    public int getRCNOfPatientWithPatNum(int patnum) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select RCN FROM patient P WHERE P.PatNum = " + patnum);
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /*
      Returns rcn of patient with given patnum.
    */
    public int getPatNumOfPatientWithRCN(int rcn) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select PatNum FROM patient WHERE RCN = " + rcn);
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /*
      Returns next available/empty RCN
    */
    public int getNewRCN() {
	try{
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT MAX(RCN) + 1 from patient");
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /*
      Returns next available/empty PatNum.
    */
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

    /*
      Returns the PatNum of patient with SSN ssn.
    */
    public int getPatNumOfRecordWithSSN(String ssn) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT PatNum FROM patient WHERE SSN=" + ssn);
	    rs.next();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    
	}
	return -1;
    }

    /*
      Returns patients with given last name
    */
    public ResultSet getRecordsWithLastName(String name) {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT P.LName, P.FName, P.Address, P.HmPhone, P.PatNum FROM patient P WHERE P.LName = " + name);
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
      Adds new patient.
    */
    public void addPatient(int rcn, String last, String first, String middle, String salutation, 
			   String nickname, String address, String address2, String city, String state, 
			   String zip, String hmPhone, String mobile, String wkPhone, String ssn, int gender, String bday, int employee) {
	try {
	    PreparedStatement stmt = null;
	    //if a new patient, insert new patient
	    stmt = getConnection().prepareStatement("INSERT INTO patient (RCN, LName, FName, MiddleI, Salutation, Preferred, Address, Address2, City, State, Zip, HmPhone, WirelessPhone, WkPhone, SSN, Gender, Birthdate, LastUpdatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	    stmt.setInt(1, rcn);
	    stmt.setString(2, last);
	    stmt.setString(3, first);
	    stmt.setString(4, middle);
	    stmt.setString(5, salutation);
	    stmt.setString(6, nickname);
	    stmt.setString(7, address);
	    stmt.setString(8, address2);
	    stmt.setString(9, city);
	    stmt.setString(10, state);
	    stmt.setString(11, zip);
	    stmt.setString(12, hmPhone);
	    stmt.setString(13, mobile);
	    stmt.setString(14, wkPhone);
	    stmt.setString(15, ssn);
	    stmt.setInt(16, gender);
	    stmt.setString(17, bday);
	    stmt.setInt(18, employee);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Updates an already existing patient.
    */
    public void editPatient(int rcn, String last, String first, String middle, String salutation, String nickname, 
			    String address, String address2, String city, String state, String zip, String hmPhone, 
			    String mobile, String wkPhone, String ssn, int gender, String bday, int employee) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET LName=?, FName=?, MiddleI=?, Salutation=?, Preferred=?, Address=?, Address2=?, City=?, State=?, Zip=?, HmPhone=?, WirelessPhone=?, WkPhone=?, SSN=?, Gender=?, Birthdate=?, LastUpdatedBy=? WHERE RCN=?;");
	    
	    stmt.setString(1, last);
	    stmt.setString(2, first);
	    stmt.setString(3, middle);
	    stmt.setString(4, salutation);
	    stmt.setString(5, nickname);
	    stmt.setString(6, address);
	    stmt.setString(7, address2);
	    stmt.setString(8, city);
	    stmt.setString(9, state);
	    stmt.setString(10, zip);
	    stmt.setString(11, hmPhone);
	    stmt.setString(12, mobile);
	    stmt.setString(13, wkPhone);
	    stmt.setString(14, ssn);
	    stmt.setInt(15, gender);
	    stmt.setString(16, bday);
	    stmt.setInt(17, employee);
	    stmt.setInt(18, rcn);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Adds new guarantor who is not already a patient.
    */
    public void addNewGuarantor(String last, String first, String address, String city, 
				String state, String zip, String hmPhone, 
				String ssn, int gender, String bday, int employee) {
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
	    }
	}
    }
    
    public void setGuarantor(int gPatNum, int ptRCN, int employee) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET guarantor=?, lastupdatedby=? WHERE RCN=?;");
	    stmt.setInt(1, gPatNum);
	    stmt.setInt(2, employee);
	    stmt.setInt(3, ptRCN);
	    stmt.executeUpdate();
	    System.out.println("Patient with RCN:" + ptRCN + " now has guarantor with patnum:" + gPatNum);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Updates a guarantors information.
    */
    public void editGuarantor(int gPatNum, String last, String first, String address, 
			      String city, String state, String zip, String hmPhone, 
			      String ssn, int gender, String bday, int employee) {
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
    }

    /*
      Returns true if a Recall already exists with patnum.
    */
    public boolean recallAlreadyExists(int patnum) {
	try {
	    PreparedStatement stmt= getConnection().prepareStatement("SELECT RecallNum FROM recall WHERE PatNum=?");
	    stmt.setInt(1, patnum);
	    ResultSet rs = stmt.executeQuery();
	    return rs.next();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }
    
    /*
      Returns recall with patnum.
    */
    public ResultSet getRecallWithPatNum(int patnum) {
	try {
	    //need to add recall type and last visit.
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT DatePrevious, DateDueCalc, RecallInterval, RecallType, IsDisabled FROM recall WHERE PatNum=?");
	    stmt.setInt(1, patnum);
	    return stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
      Adds a recall to patient with patnum.
    */
    public void addRecall(int patnum, String recallType, String nextRecall, String lastRecall, int interval) {
	//note: add recall type and last visit later.
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO recall (PatNum, RecallType, DateDue, DatePrevious, RecallInterval) VALUES (?, ?, ?, ?, ?)");
	    stmt.setInt(1, patnum);
	    stmt.setString(2, recallType);
	    stmt.setString(3, nextRecall);
	    stmt.setString(4, lastRecall);
	    stmt.setInt(5, interval);
	    stmt.executeUpdate();
	    calculateRecallDate(patnum, lastRecall, interval);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Edits recall of patient with patnum.
    */
    public void editRecall(int patnum, String recallType, String nextRecall, String lastRecall, int interval) {
	//add recall type and last visit later.
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET RecallType=?, DateDue=?, DatePrevious=?, RecallInterval=? WHERE PatNum=?");
	    stmt.setString(1, recallType);
	    stmt.setString(2, nextRecall);
	    stmt.setString(3, lastRecall);
	    stmt.setInt(4, interval);
	    stmt.setInt(5, patnum);
	    stmt.executeUpdate();
	    calculateRecallDate(patnum, lastRecall, interval);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Calculates next recall date.
    */
    public void calculateRecallDate(int patnum, String lastRecall, int interval) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET dateduecalc=(SELECT ADDDATE(?, ?)) WHERE patnum=?");
	    stmt.setString(1, lastRecall);
	    stmt.setInt(2, interval);
	    stmt.setInt(3, patnum);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Activates a new patient recall.
    */
    public void activateNewPatientRecall(int patnum) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO recall (PatNum, IsDisabled) VALUES (?, ?)");
	    stmt.setInt(1, patnum);
	    stmt.setInt(2, 0);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
      Activates an existing patient recall.
    */
    public void activateExistingPatientRecall(int patnum) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET IsDisabled=? WHERE PatNum=?");
	    stmt.setInt(1, 0);
	    stmt.setInt(2, patnum);
	    stmt.executeUpdate();
	}
	catch (Exception e) {

	}
    }

    /*
      Deactivates a patient.
    */
    public void deactivatePatientRecall(int patnum) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET IsDisabled=? WHERE PatNum=?");
	    stmt.setInt(1, 1);
	    stmt.setInt(2, patnum);
	    stmt.executeUpdate();
	    System.out.println("deactivate patient recalled in recordmanager..");
	}
	catch (Exception e) {
	    e.printStackTrace();
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

}