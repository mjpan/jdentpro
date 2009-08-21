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

public class PrintOptionsListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;

    public PrintOptionsListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();
	if (command.equals("AC_BIRTHDAY")) {
	    //load PrintBDay screen
	    ((JDentProExecutioner)getGUI().getAppFrame().getApplication().getExecutioner()).loadPrintBDay();
	}
	else if (command.equals("AC_RECALL_PRINT")) {
	    //load PrintRecall Screen
	    ((JDentProExecutioner)getGUI().getAppFrame().getApplication().getExecutioner()).loadPrintRecall();
	}
	else if (command.equals("AC_MAIN")) {
	    //load MainMenu screen
	    ((JDentProExecutioner)getGUI().getAppFrame().getApplication().getExecutioner()).loadMainMenu();
	}
    }

}

