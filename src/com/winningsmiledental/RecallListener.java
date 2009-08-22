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



public class RecallListener extends AbstractListener {

    public RecallListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();

	RecallGUI recallGUI = (RecallGUI)gui;

	//RecordManager manager = recallGUI.getRecordManager();
	int patnum = recallGUI.getPatNum();

	int ptRCN = -1;
	try {
	    ptRCN = getExecutioner().getPatientRecordManager().getRCNOfPatientWithPatNum(patnum);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    //shouldnt happen
	    new ErrorMessage("could not find patient with internal id "+ptRCN+" >> "+e.getMessage());
	    return;
	}

	if (command.equals("AC_CANCEL")) {
	    ((JDentProExecutioner)getExecutioner()).loadPatientRecord(ptRCN);
        } 

	else if (command.equals("AC_SAVE")) {
	    //save data and return to patient record
	    String recallType, lastRecall, nextRecall = null;
	    int recallInterval = -1;

	    /* save all recalls */
	    Collection recalls = recallGUI.getRecalls();
	    Iterator recallIter = recalls.iterator();
	    while (recallIter.hasNext()) {
		PatientRecall patientRecall =
		    (PatientRecall) recallIter.next();

		try {
		    /* update values from gui into patient recall*/
		    int guiIndex = patientRecall.getGUIIndex();
		    lastRecall = recallGUI.getLastRecallDate(guiIndex);
		    nextRecall = recallGUI.getNextRecallDate(guiIndex);
		    recallInterval = recallGUI.getRecallInterval(guiIndex);
		    boolean recallActive =
			recallGUI.recallActive(guiIndex);
		    
		    if (recallActive) {
			/*
			  if (invalidValues(recallInterval, lastRecall, nextRecall)) {
			  new ErrorMessage("Invalid input. Please fix and try again.");
			  return;
			  }
			*/
			validateRecallInterval(recallInterval);
			validateRecallDate(lastRecall);
			validateRecallDate(nextRecall);
			patientRecall.setDisabled(false);
		    }
		    else {
			patientRecall.setDisabled(true);
		    }
		    patientRecall.setDueDate(nextRecall);
		    patientRecall.setPreviousDate(lastRecall);
		    patientRecall.setInterval(recallInterval);
		    saveRecall(patientRecall);
		}
		catch (Exception e) {
		    e.printStackTrace();
		    new ErrorMessage("error on saving recalls for patient having rcn "+ptRCN+" >> "+ e.getMessage());
		    return;
		}
	    }


	    /*
	    int numOfRecalls = recallGUI.getNumOfRecalls();
	    
	    if (numOfRecalls > 0) {
		recallType = recallGUI.getRecallType1();
		lastRecall = recallGUI.getLastRecallDate1();
		nextRecall = recallGUI.getNextRecallDate1();
		recallInterval = recallGUI.getRecallInterval1();
		if (invalidValues(recallInterval, lastRecall, nextRecall)) {
		    new ErrorMessage("Invalid input. Please fix and try again.");
		    return;
		}
		updateRecall(manager, patnum, recallType, nextRecall, lastRecall, recallInterval);
	    }
	    if (numOfRecalls > 1) {
		recallType = recallGUI.getRecallType2();
		lastRecall = recallGUI.getLastRecallDate2();
		nextRecall = recallGUI.getNextRecallDate2();
		recallInterval = recallGUI.getRecallInterval2();
		if (invalidValues(recallInterval, lastRecall, nextRecall)) {
		    new ErrorMessage("Invalid input. Please fix and try again.");
		    return;
		}
		updateRecall(manager, patnum, recallType, nextRecall, lastRecall, recallInterval);
	    }
	    if (numOfRecalls > 2) {
		recallType = recallGUI.getRecallType3();
		lastRecall = recallGUI.getLastRecallDate3();
		nextRecall = recallGUI.getNextRecallDate3();
		recallInterval = recallGUI.getRecallInterval3();
		if (invalidValues(recallInterval, lastRecall, nextRecall)) {
		    new ErrorMessage("Invalid input. Please fix and try again.");
		    return;
		}
		updateRecall(manager, patnum, recallType, nextRecall, lastRecall, recallInterval);
	    }
	    */

	    ((JDentProExecutioner)getExecutioner()).loadPatientRecord(ptRCN);
	}
	
    }

    /*
    public boolean invalidValues(int recallInterval, String lastRecall, String nextRecall) {
	if (recallInterval < 0 || lastRecall.equals("0001-01-01") || nextRecall.equals("0001-01-01")) {
	    return true;
	}
	return false;
    }
    */
    protected void validateRecallInterval(int recallInterval) throws Exception {
	if (recallInterval < 0) {
	    throw new Exception("recall interval cannot be less than 0");
	}
    }

    protected void validateRecallDate(String recallDate) throws Exception {
	if (recallDate.equals("0001-01-01")) {
	    throw new Exception("recall date cannot be 0001-01-01");
	}
    }


    protected void saveRecall(PatientRecall recall) throws Exception {
	saveRecall(recall.getPatientNum(),
		   recall.getType(),
		   recall.getDueDate(),
		   recall.getPreviousDate(),
		   recall.getInterval(),
		   recall.isDisabled());
    }

    //each recall needs to become a record
    //need to create a recall record manager
    protected void saveRecall(int patnum, 
			      String recallType, String nextRecall, 
			      String lastRecall, int recallInterval,
			      boolean disabled) throws Exception {
	PatientRecordManager manager = getExecutioner().getPatientRecordManager();

	if (manager.recallAlreadyExists(patnum, recallType)) {
	    manager.editRecall(patnum, recallType, nextRecall, lastRecall, recallInterval, disabled);
	}
	else {
	    manager.addRecall(patnum, recallType, nextRecall, lastRecall, recallInterval, disabled);
	}
    }
    

}

