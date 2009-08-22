package com.winningsmiledental;

import java.io.*;
import java.util.*;

public class GuarantorRecord implements Record {
    int internalID; //this is the "patnum" in the db
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
    int employee;  //the employee who last updated this record

    public GuarantorRecord(String last, String first, String address, String city, 
			   String state, String zip, String hmPhone, 
			   String ssn, int gender, String bday, int employee) {
	setLastName(last);
	setFirstName(first);
	setAddressStreet(address);
	setAddressCity(city);
	setAddressState(state);
	setAddressZip(zip);
	setHomePhone(hmPhone);
	setSsn(ssn);
	setGender(gender);
	setBirthday(bday);
	setEmployee(employee);

	//this is to show that has not been saved to db
	setInternalID(-1);
    }

    public int getInternalID() throws Exception {
	if (internalID < 0) {
	    throw new Exception("no internal id has been set");
	}
	return internalID;
    }
    public void setInternalID(int id) {
	internalID = id;
    }
    public int getGender() {
	return gender;
    }
    public void setGender(int id) {
	gender = id;
    }
    public int getEmployee() {
	return employee;
    }
    public void setEmployee(int id) {
	employee = id;
    }
    public String getLastName() {
	return lastName;
    }
    public void setLastName(String name) {
	lastName = name;
    }
    public String getFirstName() {
	return firstName;
    }
    public void setFirstName(String name) {
	firstName = name;
    } 
    public String getAddressStreet() {
	return addressStreet;
    }
    public void setAddressStreet(String s) {
	addressStreet = s;
    }   
    public String getAddressCity() {
	return addressCity;
    }
    public void setAddressCity(String s) {
	addressCity = s;
    }   
    public String getAddressState() {
	return addressState;
    }
    public void setAddressState(String s) {
	addressState = s;
    }   
    public String getAddressZip() {
	return addressZip;
    }
    public void setAddressZip(String s) {
	addressZip = s;
    }   
    public String getHomePhone() {
	return homePhone;
    }
    public void setHomePhone(String s) {
	homePhone = s;
    }   
    public String getSsn() {
	return ssn;
    }
    public void setSsn(String s) {
	ssn = s;
    }   
    public String getBirthday() {
	return birthday;
    }
    public void setBirthday(String s) {
	birthday = s;
    }   

}