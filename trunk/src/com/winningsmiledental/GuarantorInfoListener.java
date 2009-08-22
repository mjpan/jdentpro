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


	int ptRCN = ((GuarantorInfoGUI)gui).getPatientRCN();
	if (command.equals("AC_CANCEL")) {
	    getExecutioner().loadPatientRecord(ptRCN);
        } 
	else if (command.equals("AC_NEW")) {
	    getExecutioner().loadGuarantorRecord(ptRCN);
	}
	else if (command.equals("AC_EDIT")) {
	    //System.out.println("attempting command >> edit");
	    JTable table = ((GuarantorInfoGUI)gui).getTable();
	    int row = table.getSelectedRow();
	    if (row >= 0) {
		int gPatNum = -1;
		try {
		    gPatNum = ((Integer)table.getValueAt(row, 3)).intValue();
		    getExecutioner().loadGuarantorRecord(ptRCN, gPatNum);
		}
		catch (Exception e) {
		    e.printStackTrace();

		    System.out.println("loading guarantor "+gPatNum+
				       " for patient "+ptRCN+" failed");
		}
	    }
	}
	else if (command.equals("AC_SEEK")) {
	    ((GuarantorInfoGUI)gui).refreshTable();
	}	   
    }
    
   

}