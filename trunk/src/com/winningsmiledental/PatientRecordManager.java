package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;

public class PatientRecordManager extends AbstractRecordManager {

    public PatientRecordManager(Executioner executioner) {
	super(executioner);
    }

    public PatientRecordManager(Executioner executioner, String databaseName) {
	super(executioner,databaseName);
    }

    /**
     * Returns ResultSet of data about patient with given RCN.
     *
     * @param int        RCN of patient being queried. 
     * @return ResultSet containing data on patient.
     */
    /*
    public ResultSet getRecordWithRCN(int rcn) {
	try {
	    PreparedStatement stmt = 
		getConnection().prepareStatement("SELECT P.PatNum, P.LName, P.FName, P.MiddleI, P.Salutation, P.Preferred, P.Address, P.Address2, P.City, P.State, P.Zip, P.HmPhone, P.WkPhone, P.WirelessPhone, P.SSN, P.Gender, P.Birthdate, P.Guarantor FROM patient P WHERE P.RCN=?");
	    stmt.setInt(1, rcn);
	    ResultSet rs = stmt.executeQuery(); 
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
    */
    public Record getRecordUsingExternalID(int id) throws Exception {
	try {
	    PreparedStatement stmt = 
		getConnection().prepareStatement("SELECT P.LName, P.FName, P.MiddleI, P.Salutation, P.Preferred, P.Address, P.Address2, P.City, P.State, P.Zip, P.HmPhone, P.WirelessPhone, P.WkPhone,  P.SSN, P.Gender, P.Birthdate, P.Guarantor, P.PatNum, P.RCN FROM patient P WHERE P.RCN=?");
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
	    rs.next();
	    return createRecordFromResultSet(rs);
					      
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("error retrieving patient with rcn="+id,e);
	}
    }

    /**
     * Returns int RCN of patient with given patnum.
     *
     * @param int  PatNum of patient.
     * @return int RCN of patient.
     */
    public int getRCNOfPatientWithPatNum(int patnum) throws Exception {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select RCN FROM patient P WHERE P.PatNum = " + patnum);
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on getting rcn from internal id "+patnum, e);
	}
    }

    /**
     * Returns int PatNum of patient with given RCN.
     *
     * @param int  RCN of patient.
     * @return int PatNum of patient.
     */
    public int getPatNumOfPatientWithRCN(int rcn) throws Exception{
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("Select PatNum FROM patient WHERE RCN = " + rcn);
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on getting patnum from rcn "+rcn, e);
	}
    }


    public Record getRecordUsingInternalID(int id) throws Exception {
	try {
	    PreparedStatement stmt = 
		getConnection().prepareStatement("SELECT P.LName, P.FName, P.MiddleI, P.Salutation, P.Preferred, P.Address, P.Address2, P.City, P.State, P.Zip, P.HmPhone, P.WirelessPhone, P.WkPhone,  P.SSN, P.Gender, P.Birthdate, P.Guarantor, P.PatNum, P.RCN FROM patient P WHERE P.Patnum=?");
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
	    rs.next();
	    return createRecordFromResultSet(rs);
					      
	}
	catch (Exception e) {
	    throw new Exception("error retrieving patient with rcn="+id,e);
	}
    }


    /*
      Returns records of patients who have recall within given dates.
    */
    /*
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
    */

    
    /*
      Returns records of patients who have a birthday in the given month
    */
    /*
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
    */



    private PatientRecord createRecordFromResultSet(ResultSet rs) throws SQLException {
	int employeeID = getExecutioner().getCurrentEmployeeID();

	    PatientRecord record = new PatientRecord(rs.getString(1), //last name
					      rs.getString(2), //first name
					      rs.getString(3), //middle
					      rs.getString(4), //salutation
					      rs.getString(5), //nickname
					      rs.getString(6), //address
					      rs.getString(7), //address2
					      rs.getString(8), //city
					      rs.getString(9), //state
					      rs.getString(10), //zip
					      rs.getString(11), //home phone
					      rs.getString(12), //mobile phone
					      rs.getString(13), //work phone
					      rs.getString(14), //ssn
					      rs.getInt(15), //gender
					      rs.getString(16), //birthdate
					      employeeID); //employee
	    record.setGuarantorID(rs.getInt(17));
	    record.setInternalID(rs.getInt(18));
	    record.setExternalID(rs.getInt(19));

	    return record;
    }

    protected  String getQueryForRecordsHavingLastName() {
	return "SELECT LName, FName, Address, HmPhone, rcn FROM patient WHERE LName=? ORDER BY FNAME"; 
    }
    
    protected  String getQueryForRecordsHavingFirstName() {
	return "SELECT LName, FName, Address, HmPhone, rcn FROM patient WHERE FName=? ORDER BY LNAME";
    }

    protected  String getQueryForRecordsHavingSSN() {
	return "SELECT lname, fname, address, hmphone, rcn FROM patient WHERE ssn=?";
    }

    protected  String getQueryForRecordsHavingPhoneNumber() {
	return "SELECT lname, fname, address, hmphone, rcn FROM patient WHERE hmphone=?";
    }

    /**
     * Adds a new patient to the database.
     *
     * @param String LName, FName, MiddleI, Salutation, Preferred
     * @param String Address, Address2, City, State, Zip
     * @param String HmPhone, Wireless, WkPhone,
     * @param String SSN, Gender, Birthdate
     * @param int    rcn, employeenum of currently logged in user.
     */
    /*
    public void addPatient(int rcn, String last, String first, String middle, String salutation, 
			   String nickname, String address, String address2, String city, String state, 
			   String zip, String hmPhone, String mobile, String wkPhone, String ssn, int gender, String bday, int employee) throws Exception {
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
	    Exception newE = new Exception("failed on creating patient with rcn "+rcn, e);
	    throw newE;
	}
    }
    */
    public void addRecord(Record record) throws Exception {
	PatientRecord patientRecord = (PatientRecord)record;

	//cannot add if already exists
	try {
	    patientRecord.getInternalID();
	    editRecord(record);
	    return;
	}
	catch (Exception e) {
	    //no internal id yet
	}

	try {
	    PreparedStatement stmt = null;


	    //if the order of the "?"  (or the number of them) in the statement changes
	    // may need to change fillStatementValues (if corresponding changes are applied to addPatient)
	    // or create new method (if addPatient method does not have corresponding changes)
	    stmt = getConnection().prepareStatement("INSERT INTO patient (LName, FName, MiddleI, Salutation, Preferred, Address, Address2, City, State, Zip, HmPhone, WirelessPhone, WkPhone, SSN, Gender, Birthdate, LastUpdatedBy, RCN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	    int rcn = getNewRCN();
	    patientRecord.setExternalID(rcn);
	    fillStatementValues(stmt, patientRecord);
	    stmt.executeUpdate();

	    //update and set patient record's new internal id
	    int internalID = 
		getPatNumOfPatientWithRCN(rcn);
	    patientRecord.setInternalID(internalID);

	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on creating new patient", e);
	    throw newE;
	}
    }


    /**
     * Edits a patient in the database.
     *
     * @param String LName, FName, MiddleI, Salutation, Preferred
     * @param String Address, Address2, City, State, Zip
     * @param String HmPhone, Wireless, WkPhone,
     * @param String SSN, Gender, Birthdate
     * @param int    rcn, employeenum of currently logged in user.
     */
    /*
    public void editPatient(int rcn, String last, String first, String middle, String salutation, String nickname, 
			    String address, String address2, String city, String state, String zip, String hmPhone, 
			    String mobile, String wkPhone, String ssn, int gender, String bday, int employee) throws Exception {
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
	    Exception newE = new Exception("failed on editing patient with rcn "+rcn, e);
	    throw newE;
	}
    }
    */
    public void editRecord(Record record) throws Exception {
	PatientRecord patientRecord = (PatientRecord) record;
	//cannot add if already exists
	try {
	    patientRecord.getInternalID();
	}
	catch (Exception e) {
	    //no internal id yet, cannot edit
	    throw new Exception("patient with rcn "+patientRecord.getExternalID()+" does not exist", e);
	}

	try {
	    //if the order of the "?"  (or the number of them) in the statement changes
	    // may need to change fillStatementValues (if corresponding changes are applied to addPatient)
	    // or create new method (if addPatient method does not have corresponding changes)
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET LName=?, FName=?, MiddleI=?, Salutation=?, Preferred=?, Address=?, Address2=?, City=?, State=?, Zip=?, HmPhone=?, WirelessPhone=?, WkPhone=?, SSN=?, Gender=?, Birthdate=?, LastUpdatedBy=? WHERE RCN=?;");
	    fillStatementValues(stmt, patientRecord);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = 
		new Exception("failed on editing patient with rcn "+patientRecord.getExternalID(), e);
	    throw newE;
	}
    }


    /**
     * Sets guarantor of patient with rcn ptRCN to gPatNum.
     *
     * @param int patnum of guarantor
     * @param int rcn of patient being updated
     * @param int employeenum of currently logged in user.
     */
    // this should be combined with add/update
    public void setGuarantor(PatientRecord record, int gPatNum, int employee) throws Exception {
	try {
	    record.getInternalID();
	}
	catch (Exception e) {
	    //no record exists yet
	    addRecord(record);
	}

	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE patient SET guarantor=?, lastupdatedby=? WHERE RCN=?;");
	    stmt.setInt(1, gPatNum);
	    stmt.setInt(2, employee);
	    stmt.setInt(3, record.getExternalID());
	    stmt.executeUpdate();
	    //System.out.println("Patient with RCN:" + ptRCN + " now has guarantor with patnum:" + gPatNum);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on setting patient with rcn "+record.getInternalID()+
					   " to guarantor "+gPatNum);
	    throw newE;
	}
    }



    protected void fillStatementValues(PreparedStatement stmt, PatientRecord record) throws Exception {
	    stmt.setString(1, record.getLastName());
	    stmt.setString(2, record.getFirstName());
	    stmt.setString(3, record.getMiddleName());
	    stmt.setString(4, record.getSalutation());
	    stmt.setString(5, record.getNickname());
	    stmt.setString(6, record.getAddressStreet());
	    stmt.setString(7, record.getAddressStreet2());
	    stmt.setString(8, record.getAddressCity());
	    stmt.setString(9, record.getAddressState());
	    stmt.setString(10, record.getAddressZip());
	    stmt.setString(11, record.getHomePhone());
	    stmt.setString(12, record.getMobilePhone());
	    stmt.setString(13, record.getWorkPhone());
	    stmt.setString(14, record.getSsn());
	    stmt.setInt(15, record.getGender());
	    stmt.setString(16, record.getBirthday());
	    stmt.setInt(17, record.getEmployee());
	    stmt.setInt(18, record.getExternalID());
    }

    /**
     * Returns lowest unassigned RCN.
     *
     * @return int Lowest unassigned RCN.
     */
    public int getNewRCN() throws Exception {
	Statement stmt = getConnection().createStatement();
	ResultSet rs = stmt.executeQuery("SELECT MAX(RCN) + 1 from patient");
	rs.first();
	return rs.getInt(1);
    }


    /*
      Returns true if a Recall already exists with patnum.
    */
    public boolean recallAlreadyExists(int patnum) throws Exception {
	try {
	    PreparedStatement stmt= getConnection().prepareStatement("SELECT RecallNum FROM recall WHERE PatNum=?");
	    stmt.setInt(1, patnum);
	    ResultSet rs = stmt.executeQuery();
	    return rs.next();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on calling recallAlreadyExists("+patnum+")", e);
	}
    }

    /**
     * @returns true if a Recall already exists with patnum and type recallType
     */
    public boolean recallAlreadyExists(int patnum, String recallType) throws Exception {
	try {
	    PreparedStatement stmt= getConnection().prepareStatement("SELECT RecallNum FROM recall WHERE PatNum=? AND RecallType=?");
	    stmt.setInt(1, patnum);
	    stmt.setString(2, recallType);
	    ResultSet rs = stmt.executeQuery();
	    return rs.next();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on calling recallAlreadyExists("+patnum+","+recallType+")", e);
	}
    }
    
    /**
     * @returns recall with patnum.
     */
    //public ResultSet getRecallWithPatNum(int patnum) {
    //public ResultSet getRecallsForPatient(int patientNum) {
    /*
    public ResultSet getRecall(int patientNum, String recallType) throws Exception {
	ResultSet results = null;
	try {
	    //stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	    //         ResultSet.CONCUR_UPDATABLE);
	    //need to add recall type and last visit.
	    PreparedStatement stmt = 
		getConnection().prepareStatement
		("SELECT RecallNum, DatePrevious, DateDueCalc, RecallInterval, IsDisabled FROM recall WHERE PatNum=? and RecallType=?",
		 ResultSet.TYPE_FORWARD_ONLY,
		  ResultSet.CONCUR_UPDATABLE);

	    //stmt.setInt(1, patnum);
	    stmt.setInt(1, patientNum);

	    stmt.setString(2, recallType);

	    results = stmt.executeQuery();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on retrieving recall", e);
	}
	return results;
    }
    */
    public PatientRecall getRecall(int patientNum, String recallType) throws Exception {

	try {
	    //stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	    //         ResultSet.CONCUR_UPDATABLE);
	    //need to add recall type and last visit.
	    PreparedStatement stmt = 
		getConnection().prepareStatement
		("SELECT RecallNum, DatePrevious, DateDueCalc, RecallInterval, IsDisabled FROM recall WHERE PatNum=? and RecallType=?",
		 ResultSet.TYPE_FORWARD_ONLY,
		  ResultSet.CONCUR_UPDATABLE);

	    stmt.setInt(1, patientNum);

	    stmt.setString(2, recallType);

	    ResultSet rs = stmt.executeQuery();

	    PatientRecall recall = null;
	    while (rs.next()) {
		if (recall != null) {
		    rs.deleteRow();
		    continue;
		}

		int recallNum = rs.getInt(1);
		String previousDate = rs.getString(2);
		String dueDate = rs.getString(3);
		int interval = rs.getInt(4);
		boolean disabled = (rs.getInt(5) == 1);

		recall = new PatientRecall(recallNum,
					   patientNum,
					   dueDate,
					   previousDate,
					   interval,
					   recallType,
					   disabled);

	    }

	    if (recall == null) {
		throw new Exception("no "+recallType+" recall found for patient with internalID "+patientNum);
	    }
	    return recall;
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on retrieving recall", e);
	}
    }

    /**
     * @returns whether a patient has an active recall
     */
    public boolean hasActiveRecall(int patientNum) throws Exception {
	boolean b = false;
	try {
	    //need to add recall type and last visit.
	    PreparedStatement stmt = 
		getConnection().prepareStatement
		("SELECT count(*) FROM recall WHERE PatNum=? and isDisabled=?");

	    stmt.setInt(1, patientNum);

	    stmt.setInt(2, 0);

	    ResultSet results = stmt.executeQuery();

	    if (results.next()) {
		if (results.getInt(1) > 0) {
		    b = true;
		}
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new Exception("failed on hasActiveRecall", e);
	}
	return b;
    }

    /*
      Activates a new patient recall.
    */
    public void activateNewPatientRecall(int patnum) throws Exception {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO recall (PatNum, IsDisabled) VALUES (?, ?)");
	    stmt.setInt(1, patnum);
	    stmt.setInt(2, 0);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on editing recall", e);
	    throw newE;
	}
    }

    /**
     * Activates all recalls for an existing patient
     */
    public void activateExistingPatientRecall(int patnum) throws Exception {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET IsDisabled=? WHERE PatNum=?");
	    stmt.setInt(1, 0);
	    stmt.setInt(2, patnum);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on editing recall", e);
	    throw newE;
	}
    }

    /**
     *  Deactivates all recalls for a patient.
     */
    public void deactivatePatientRecall(int patnum) throws Exception {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE recall SET IsDisabled=? WHERE PatNum=?");
	    stmt.setInt(1, 1);
	    stmt.setInt(2, patnum);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	    Exception newE = new Exception("failed on editing recall", e);
	    throw newE;
	}
    }


}