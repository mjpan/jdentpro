
package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.io.*;
import java.util.*;
import java.net.*;
import org.jdom.*;
import java.sql.*;



public class GuarantorRecordListener extends AbstractListener {

    public GuarantorRecordListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();

	GuarantorRecordGUI temp = (GuarantorRecordGUI)gui;
	int ptRCN = temp.getPatientRCN();
	int gPatNum = temp.getGuarantorPatNum();

	//RecordManager manager = temp.getRecordManager();

	int employee = getExecutioner().getCurrentEmployeeID();

	if (command.equals("AC_CANCEL")) {
	    //return to patient record
	    getExecutioner().loadPatientRecord(ptRCN);
	    return;
        }
	if (command.equals("AC_CHANGE")) {
	    //go back to the guarantor records table
	    getExecutioner().loadGuarantorInfo(ptRCN);
	    return;
	}
	if (command.equals("AC_CHANGE_TO_SELF")) {
	    //getExecutioner().loadGuarantorInfo(ptRCN);
	    /* set guarantor to self */
	    //temp.setSelfAsGuarantor();

	    try {
		gPatNum =
		    getExecutioner().getPatientRecordManager().getPatNumOfPatientWithRCN(ptRCN);	    
		getExecutioner().loadGuarantorRecord(ptRCN, gPatNum); 
	    }
	    catch (Exception e) {
		new ErrorMessage("failed on setting self as guarantor >> "+e.getMessage());
	    }
	    return;
	}


	String ssn = temp.getSSN();
	try {
	    getExecutioner().getGuarantorRecordManager().validateSSN(ssn);
	}
	catch (InvalidSSNException e) {
	    //new ErrorMessage ("Invalid SSN");
	    //return;
	    temp.generateSSN();
	}
	catch (DuplicateSSNException e) {
	    new ErrorMessage("Dupicate SSN");
	    return;
	}

	saveAction(temp, gPatNum, ptRCN, employee);
	try {
	    Thread.sleep(1000);
	}
	catch (InterruptedException e) {
	}

	if (command.equals("AC_UPDATE_FAM")) {
	    //update all patients' address & phone number who have this guarantor
	    getExecutioner().getPatientRecordManager().updateAllPatientRecordsWithGuarantor(gPatNum, temp.getAddress(), temp.getCity(), temp.getState(), temp.getZipCode(), temp.getHmPhone(), employee);
	}
	else if (command.equals("AC_RECALL")) {
	    //do nothing
	}
	else if (command.equals("AC_SAVE")) {
	    //save values and return to patient record
	    getExecutioner().loadPatientRecord(ptRCN);
	}
    }


    
    /*
      Sets current patient's guarantor reference to this guarantor.
    */
    public void saveAction(GuarantorRecordGUI temp, int gPatNum, int ptRCN, int employee) {
	try {
	    String last = temp.getLastName();
	    String first = temp.getFirstName();
	    String address = temp.getAddress();
	    String city = temp.getCity();
	    String state = temp.getState();
	    String zip = temp.getZipCode();
	    String hmPhone = temp.getHmPhone();
	    String ssn = temp.getSSN();
	    int gender = temp.getGender();
	    String bday = temp.getBirthdate();

	    GuarantorRecord guarantorRecord = 
		new GuarantorRecord(last, first, address, city, state, zip, 
				    hmPhone, ssn, gender, bday, employee);

	    GuarantorRecordManager guarantorRecordManager =
		getExecutioner().getGuarantorRecordManager();

	    //if not a new guarantor, update values
	    //else, insert guarantor into database
	    if (!temp.isNewGuarantor()) {
		//manager.editGuarantor(gPatNum, last, first, address, city, state, zip, hmPhone, ssn, gender, bday, employee);
		guarantorRecord.setInternalID(gPatNum);
		guarantorRecordManager.editRecord(guarantorRecord);
	    }
	    else {
		//manager.addNewGuarantor();
		guarantorRecordManager.addRecord(guarantorRecord);
	    }

	    PatientRecordManager patientRecordManager =
		getExecutioner().getPatientRecordManager();
	    PatientRecord patientRecord = 
		(PatientRecord) patientRecordManager.getRecordUsingExternalID(ptRCN);
	    //set guarantor reference in patient to gPatNum
	    //i think we have to delay this as if patient is its own guarantor
	    patientRecordManager.setGuarantor(patientRecord, gPatNum, employee);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }


}
