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



public class PatientInfoListener extends AbstractListener {

    //private RecordManager manager;

    public PatientInfoListener(GUI gui) {
	super(gui);
	//manager = ((PatientInfoGUI)gui).getRecordManager();
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();

	//RecordManager manager = ((PatientInfoGUI)gui).getRecordManager();

	if (command.equals("AC_CANCEL")) {
	    getExecutioner().loadMainMenu();
        } 
	else if (command.equals("AC_NEW")) {
	    getExecutioner().loadPatientRecord();

	}
	else if (command.equals("AC_EDIT")) {
	    //System.out.println("attempting command >> edit");
	    JTable table = ((PatientInfoGUI)gui).getTable();
	    int row = table.getSelectedRow();
	    //System.out.println("row selected: " + row);
	    if (row >= 0) {
		try {
		    int rcn = ((Integer)table.getValueAt(row, 4)).intValue();
		    System.out.println("rcn: " + rcn);
		    getExecutioner().loadPatientRecord(rcn);
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}	   
	else if (command.equals("AC_SEEK")) {
	    ((PatientInfoGUI)gui).refreshTable();
	}
    }
    
   

}



