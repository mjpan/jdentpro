package com.winningsmiledental;

import java.io.*;
import java.util.*;

public class PatientRecord extends GuarantorRecord {
    /* inherited from GuarantorRecord 
    int internalID; //this is the "patnum" in the db
    int externalID; //this is the "rcn" in the db
    String lastName;
    String firstName;
    String addressStreet;
    String addressCity;
    String addressState;
    String addressZip;
    String homePhone;
    String ssn;
    int gender;
    String birthday;
    int employee;
    */
    String middleName;
    String salutation;
    String nickname;
    String addressStreet2;
    String mobilePhone;
    String workPhone;
    int guarantorID;
    int externalID; //this is the "rcn" in the db

    public PatientRecord(String last, String first, String middle, String salutation, 
			   String nickname, String address, String address2, String city, String state, 
			   String zip, String hmPhone, String mobile, String wkPhone, 
			 String ssn, int gender, String bday, int employee) {
	super(last, first, address, city, state, zip, hmPhone, ssn, gender, bday, employee);
	setMiddleName(middle);
	setNickname(nickname);
	setSalutation(salutation);
	setAddressStreet2(address2);
	setMobilePhone(mobile);
	setWorkPhone(wkPhone);
	setExternalID(-1);
	setGuarantorID(0);
    }

    public int getExternalID() throws Exception {
	if (externalID < 0) {
	    throw new Exception("no external id has been set");
	}
	return externalID;
    }
    public void setExternalID(int id) {
	externalID = id;
    }
    public int getGuarantorID() {
	return guarantorID;
    }
    public void setGuarantorID(int id) {
	guarantorID = id;
    }
    public String getMiddleName() {
	return middleName;
    }
    public void setMiddleName(String name) {
	middleName = name;
    }
    public String getSalutation() {
	return salutation;
    }
    public void setSalutation(String name) {
	salutation = name;
    } 
    public String getNickname() {
	return nickname;
    }
    public void setNickname(String name) {
	nickname = name;
    } 
    public String getAddressStreet2() {
	return addressStreet2;
    }
    public void setAddressStreet2(String s) {
	addressStreet2 = s;
    }   
    public String getMobilePhone() {
	return mobilePhone;
    }
    public void setMobilePhone(String s) {
	mobilePhone = s;
    }   
    public String getWorkPhone() {
	return workPhone;
    }
    public void setWorkPhone(String s) {
	workPhone = s;
    }   
}