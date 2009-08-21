
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
	RecordManager manager = temp.getRecordManager();
	int employee = getExecutioner().getCurrentEmployeeID();

	if (command.equals("AC_CANCEL")) {
	    //return to patient record
	    getExecutioner().loadPatientRecord(manager, ptRCN);
	    return;
        }
	if (command.equals("AC_CHANGE")) {
	    getExecutioner().loadGuarantorInfo(ptRCN);
	    return;
	}
	if (temp.getSSN() == null) {
	    new ErrorMessage ("Invalid SSN.");
	    return;
	}
	else {
	    saveAction(manager, temp, gPatNum, ptRCN, employee);
	    if (command.equals("AC_UPDATE_FAM")) {
		//update all patients' address & phone number who have this guarantor
		manager.updateAllPatientRecordsWithGuarantor(gPatNum, temp.getAddress(), temp.getCity(), temp.getState(), temp.getZipCode(), temp.getHmPhone(), employee);
	    }
	    else if (command.equals("AC_RECALL")) {
		//do nothing
	    }
	    else if (command.equals("AC_SAVE")) {
		//save values and return to patient record
		getExecutioner().loadPatientRecord(manager, ptRCN);
	    }
	}
	
    }
    
    /*
      Sets current patient's guarantor reference to this guarantor.
    */
    public void saveAction(RecordManager manager, GuarantorRecordGUI temp, int gPatNum, int ptRCN, int employee) {
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

	    //if not a new guarantor, update values
	    //else, insert guarantor into database
	    if (!temp.isNewGuarantor()) {
		manager.editGuarantor(gPatNum, last, first, address, city, state, zip, hmPhone, ssn, gender, bday, employee);
	    }
	    else {
		manager.addNewGuarantor(last, first, address, city, state, zip, hmPhone, ssn, gender, bday, employee);
	    }
	    //set guarantor reference in patient to gPatNum
	    manager.setGuarantor(gPatNum, ptRCN, employee);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }


}
