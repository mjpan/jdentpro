
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



public class PatientRecordListener extends AbstractListener {


    public PatientRecordListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();

	System.out.println("processing action >> " + command);

	PatientRecordGUI temp = (PatientRecordGUI)gui;

	RecordManager manager = temp.getRecordManager();

	if (command.equals("AC_CANCEL")) {
	    getExecutioner().loadPatientInfo();
        } 
	else if (command.equals("AC_GUARANTOR")) {
	    saveAction(temp, manager);
	    int ptRCN = temp.getRCN();
	    //if no guarantor is set, load guarantor list table
	    //else, load record of current guarantor
	    if (temp.getGuarantorName().equals("")) {
		getExecutioner().loadGuarantorInfo(ptRCN);
	    }
	    else {
		getExecutioner().loadGuarantorRecord(ptRCN, manager, temp.getGPatNum());
	    }
	}
	else if (command.equals("AC_RECALL")) {
	    //save data and load recall
	    saveAction(temp, manager);
	    int patnum = temp.getPatNum();
	    getExecutioner().loadRecall(manager, patnum);
	}
	else if (command.equals("AC_SAVE")) {
	    //make sure ssn has 9 digits.
	    if (!temp.getSSN().matches("\\d{9}")) {
		new ErrorMessage("Invalid SSN");
		return;
	    }

	    //save data and return to patient info table
	    saveAction(temp, manager);
	    getExecutioner().loadPatientInfo();
	}
	
    }

    /*
      saves values entered into fields and updates database to those values
    */
    public void saveAction(PatientRecordGUI temp, RecordManager manager) {
	try {
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
	    //if a new patient and does not exist as a guarantor, insert new patient
	    //if a new patient and exists as guarantor, update guarantor record and add rcn
	    //if not a new patient, update values
	    boolean editPatient = true;
	    if (temp.isNewPatient()) {
		if (manager.patientAlreadyExists(ssn)) {
		    manager.editGuarantor(ssn, rcn, employee);
		}
		else {
		    manager.addPatient(rcn, last, first, middle, salutation, nickname,
				       address, address2, city, state, zip, hmPhone,
				       mobile, wkPhone, ssn, gender, bday, employee);
		    editPatient = false;
		}
	    }
	    if (editPatient) {
		manager.editPatient(rcn, last, first, middle, salutation,
				    nickname, address, address2, city, state, zip,
				    hmPhone, mobile, wkPhone, ssn, gender, bday, employee);
	    }
	    //if 'active patient' is unchecked, deactivate & v/v.
	    if (!temp.isActive()) {
		manager.deactivatePatientRecall(patnum);
	    }
	    else {
		manager.activateExistingPatientRecall(patnum);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
   

}

