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

public class MainMenuListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;

    public MainMenuListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();
	if (command.equals("AC_PATIENT")) {
	    //load patient info list
	    getExecutioner().loadPatientInfo();
	}
	else if (command.equals("AC_PRINTING")) {
	    //load print options screen
	    getExecutioner().loadPrintOptions();
	}
	else if (command.equals("AC_SPECIAL")) {
	    //load special functions screen
	    getExecutioner().loadSpecialFunctions();
	}
	else if (command.equals("AC_LOGOUT")) {
	    //load login screen
	    getExecutioner().loadLoginScreen();
	}
	else if (command.equals("AC_QUIT")) {
	    //quit jdentpro
	    getExecutioner().exit(0);
	}
    }

}

