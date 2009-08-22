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

public class SpecialFunctionsListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;

    public SpecialFunctionsListener(GUI gui) {
	super(gui);
    }

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();
	if (command.equals("AC_MANAGE")) {
	    //load user table screen
	    getExecutioner().loadUserTable();
	}
	else if (command.equals("AC_MAIN")) {
	    //load print options screen
	    getExecutioner().loadMainMenu();
	}
    }

}

