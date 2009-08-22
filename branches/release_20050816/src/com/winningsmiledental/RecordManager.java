package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;


public interface RecordManager {
    public Executioner getExecutioner();

    //public void establishConnection();

    public Connection getConnection();

    public ResultSet getAllPatientRecords();

    public void updateAllPatientRecordsWithGuarantor(int rcn, String addy, String city, String state, String zip, String hmPhone, int employee);

    public int getGuarantorOfPatient(int rcn);

    public ResultSet getAllGuarantorRecords();

    //public int getNewRCN();
    //public int getNewPatNum();

    public String getNameOfPatientWithPatNum(int patnum);

    //public ResultSet getRecordWithRCN(int rcn);
    //public ResultSet getRecordWithPatNum(int patnum);
    public Record getRecordUsingInternalID(int id) throws Exception;

    //public int getRCNOfPatientWithPatNum(int patnum);
    //public int getPatNumOfPatientWithRCN(int rcn);

    public int getPatNumOfRecordWithSSN(String ssn) throws Exception;

    //public ResultSet getRecordsWithLastName(String name);

    /*
    public void addPatient(int rcn, String last, String first, String middle, String salutation, 
			   String nickname, String address, String address2, String city, String state, 
			   String zip, String hmPhone, String mobile, String wkPhone, String ssn, int gender, String bday, int employee);
    */

    /*
    public void editPatient(int rcn, String last, String first, String middle, String salutation, String nickname, 
			    String address, String address2, String city, String state, String zip, String hmPhone, 
			    String mobile, String wkPhone, String ssn, int gender, String bday, int employee);
    */

    /*
    public void addNewGuarantor(String last, String first, String address, String city, 
				String state, String zip, String hmPhone, 
				String ssn, int gender, String bday, int employee);
    */

    /*
    public void editGuarantor(int gPatNum, String last, String first, String address, 
			      String city, String state, String zip, String hmPhone, 
			      String ssn, int gender, String bday, int employee);
    
    public void editGuarantor(String ssn, int rcn, int employee);
    */

    /*
    public void setGuarantor(int gPatnum, int ptRCN, int employee);
    */
    public void editRecord(Record record) throws Exception;
    public void addRecord(Record record) throws Exception;

    /*
    public boolean patientAlreadyExists(String ssn);
    */
    //public boolean verifyRecordExists(String ssn);


    //public boolean recallAlreadyExists(int patnum);
    //public boolean recallAlreadyExists(int patnum, String recallType);

    //public ResultSet getRecallWithPatNum(int patnum);
    //public ResultSet getRecall(int patientNum, String recallType);
    //public boolean hasActiveRecall(int patientNum);


    public void addRecall(int patnum, String recallType, String nextRecall, String lastRecall, int interval, boolean disabled) throws Exception;

    public void editRecall(int patnum, String recallType, String nextRecall, String lastRecall, int interval, boolean disabled) throws Exception;

    //public void calculateRecallDate(int patnum, String lastRecall, int interval);

    //public void activateNewPatientRecall(int patnum) throws Exception;
    //public void activateExistingPatientRecall(int patnum) throws Exception;
    //public void deactivatePatientRecall(int patnum) throws Exception;

    public ResultSet getRecordsWithRecallBetweenDates(String start, String end);

    public ResultSet getRecordsWithBirthdateInMonth(String month);

    /**
     * @returns patient record with given rcn.
     */
    public ResultSet getPreviewRecordHavingRCN(int rcn);
    public ResultSet getRecordsWithLastName(String last);
    public ResultSet getRecordsWithFirstName(String first);
    public ResultSet getRecordWithSSN(String ssn);
    public ResultSet getRecordsWithPhoneNum(String phone);
}