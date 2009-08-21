
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

	RecallGUI temp = (RecallGUI)gui;
	RecordManager manager = temp.getRecordManager();
	int patnum = temp.getPatNum();
	int ptRCN = manager.getRCNOfPatientWithPatNum(patnum);


	if (command.equals("AC_CANCEL")) {
	    ((JDentProExecutioner)getExecutioner()).loadPatientRecord(manager, ptRCN);
        } 

	else if (command.equals("AC_SAVE")) {
	    //save data and return to patient record
	    String recallType = temp.getRecallType();
	    String lastRecall = temp.getLastRecallDate();
	    String nextRecall = temp.getNextRecallDate();
	    int recallInterval = temp.getRecallInterval();
	    try {
		if (recallInterval < 0 || lastRecall.equals("0001-01-01") || nextRecall.equals("0001-01-01")) {
		    new ErrorMessage("Invalid input. Please fix and try again.");
		}
		else {
		    if (manager.recallAlreadyExists(patnum)) {
			manager.editRecall(patnum, recallType, nextRecall, lastRecall, recallInterval);
		    }
		    else {
			manager.addRecall(patnum, recallType, nextRecall, lastRecall, recallInterval);
		    }
		    ((JDentProExecutioner)getExecutioner()).loadPatientRecord(manager, ptRCN);
		}
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
    }

}

