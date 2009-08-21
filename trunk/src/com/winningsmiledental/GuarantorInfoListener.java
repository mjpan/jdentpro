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



public class GuarantorInfoListener extends AbstractListener {

    //private RecordManager manager;

    public GuarantorInfoListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();

	RecordManager manager = ((GuarantorInfoGUI)gui).getRecordManager();
	int ptRCN = ((GuarantorInfoGUI)gui).getPatientRCN();
	if (command.equals("AC_CANCEL")) {
	    getExecutioner().loadPatientRecord(manager, ptRCN);
        } 
	else if (command.equals("AC_NEW")) {
	    getExecutioner().loadGuarantorRecord(ptRCN, manager);
	}
	else if (command.equals("AC_EDIT")) {
	    System.out.println("attempting command >> edit");
	    JTable table = ((GuarantorInfoGUI)gui).getTable();
	    int row = table.getSelectedRow();
	    if (row >= 0) {
		try {
		    int gPatNum = ((Integer)table.getValueAt(row, 3)).intValue();
		    getExecutioner().loadGuarantorRecord(ptRCN, manager, gPatNum);
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}	   

	
    }
    
   

}