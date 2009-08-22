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



public class PatientRecordListener extends AbstractListener implements ItemListener {


    public PatientRecordListener(GUI gui) {
	super(gui);
	((PatientRecordGUI)gui).getRecallActiveCheckbox().addItemListener(this);
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();

	System.out.println("processing action >> " + command);

	PatientRecordGUI temp = (PatientRecordGUI)gui;

	if (command.equals("AC_CANCEL")) {
	    getExecutioner().loadPatientInfo();
	    return;
        }

	try {
	    getExecutioner().getPatientRecordManager().validateSSN(temp.getSSN());
	    //save data and return to patient info table
	    
	}
	catch (InvalidSSNException e) {
	    e.printStackTrace();
	    new ErrorMessage(e.getMessage());
	    return;
	}
	catch (DuplicateSSNException e) {
	    e.printStackTrace();
	    new ErrorMessage(e.getMessage());
	    return;
	}
 
	saveAction(temp);
	int ptRCN = temp.getRCN();

	try {
	    Thread.sleep(1000);
	}
	catch (InterruptedException e) {
	}

	if (command.equals("AC_SET_TO_SELF")) {
	    try {
		int gPatNum =
		    getExecutioner().getPatientRecordManager().getPatNumOfPatientWithRCN(ptRCN);
		getExecutioner().loadGuarantorRecord(ptRCN, gPatNum); 
	    }
	    catch (Exception e) {
		e.printStackTrace();
		new ErrorMessage("failed on setting self to guarantor >> "+e.getMessage());
		return;
	    }
	}
	else if (command.equals("AC_GUARANTOR")) {

	    //if no guarantor is set, load guarantor list table
	    //else, load record of current guarantor
	    if (temp.getGuarantorName().equals("")) {
		getExecutioner().loadGuarantorInfo(ptRCN);
	    }
	    else {
		getExecutioner().loadGuarantorRecord(ptRCN, temp.getGPatNum());
	    }
	}
	else if (command.equals("AC_RECALL")) {
	    //save data and load recall
	    try {
		int patnum = 
		    getExecutioner().getPatientRecordManager().getPatNumOfPatientWithRCN(ptRCN);
		getExecutioner().loadRecall(patnum);
	    }
	    catch (Exception e) {
		e.printStackTrace();
		new ErrorMessage("failed loading recall for patient having rcn "+ptRCN+" >> "+e.getMessage());
		return;
	    }
	}
	else if (command.equals("AC_SAVE")) {
	    getExecutioner().loadPatientInfo();

	}
    }

    /*
      saves values entered into fields and updates database to those values
    */
    public void saveAction(PatientRecordGUI temp) {
	PatientRecordManager manager = getExecutioner().getPatientRecordManager();
	try {
	    /*
	    int rcn = temp.getRCN();
	    int patnum = temp.getPatNum();
	    String last = temp.getLastName();
	    String first = temp.getFirstName();
	    String middle = temp.getMiddleName();
	    String salutation = temp.getTitle();
	    String nickname = temp.getNickname();
	    String address = temp.getAddress();
	    String address2 = temp.getAddress2();
	    String city = temp.getCity();
	    String state = temp.getState();
	    String zip = temp.getZipCode();
	    String hmPhone = temp.getHmPhone();
	    String mobile = temp.getMobile();
	    String wkPhone = temp.getWkPhone();
	    String ssn = temp.getSSN();
	    int gender = temp.getGender();
	    String bday = temp.getBirthdate();
	    int employee = getExecutioner().getCurrentEmployeeID();
	    PreparedStatement stmt = null;
	    */
	    int employee = getExecutioner().getCurrentEmployeeID();
	    PatientRecord record = (PatientRecord) temp.getRecord();

	    //if a new patient and does not exist as a guarantor, insert new patient
	    //if a new patient and exists as guarantor, update guarantor record and add rcn
	    //if not a new patient, update values
	    boolean editPatient = true;
	    if (temp.isNewPatient()) {
		//if "patient" already exists as a guarantor
		if (manager.patientAlreadyExists(record.getSsn())) {
		    //this should use the record object instead
		    manager.editGuarantor(record.getSsn(), temp.getRCN(), employee);
		}
		else {
		    /*
		    manager.addPatient(rcn, last, first, middle, salutation, nickname,
				       address, address2, city, state, zip, hmPhone,
				       mobile, wkPhone, ssn, gender, bday, employee);
		    */
		    manager.addRecord(record);
		    editPatient = false;
		}
	    }
	    if (editPatient) {
		/*
		manager.editPatient(rcn, last, first, middle, salutation,
				    nickname, address, address2, city, state, zip,
				    hmPhone, mobile, wkPhone, ssn, gender, bday, employee);
		*/
		manager.editRecord(record);
	    }

	    //if 'active patient' is unchecked, deactivate & v/v.
	    //activeness refers to whether we are still
	    //actively sending the patient for dental work recalls
	    if (!temp.isActive()) {
		manager.deactivatePatientRecall(record.getInternalID());
	    }
	    else {
		manager.activateExistingPatientRecall(record.getInternalID());
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	    new ErrorMessage("failed on saving patient record >> "+e.getMessage());
	}
    }
    
    public void itemStateChanged(ItemEvent e) {
	Object source = e.getItemSelectable();

	PatientRecordGUI recordGUI = (PatientRecordGUI)gui;
	//RecordManager manager = recordGUI.getRecordManager();
	int patnum = recordGUI.getPatNum();

	/* if recall activated/disabled */
	if (source == recordGUI.getRecallActiveCheckbox()) {
	    if (!recordGUI.isActive()) {
		try {
		    getExecutioner().getPatientRecordManager().deactivatePatientRecall(patnum);
		}
		catch (Exception ex) {
		    ex.printStackTrace();
		    //shouldnt happen
		    new ErrorMessage("error when deactivating patient recall >> "+ex.getMessage());
		}
	    }
	}
    }

}

