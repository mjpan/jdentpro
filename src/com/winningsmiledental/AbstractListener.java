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

public abstract class AbstractListener implements Listener {

    public SwingEngine swix = null;
    protected boolean CONTINUE_RUNNING;
    public JMenuBar menubar;
    public JMenu menuFile;
    public JMenuItem mi_Options;
    public JMenuItem mi_About;
    public JMenuItem mi_Help;
    public JMenuItem mi_Exit;

    protected GUI gui;


    public AbstractListener(GUI gui) {
	setGUI(gui);
	configure();
    }

    public void setGUI(GUI g) {
	gui = g;
	Iterator actionableItemIter = g.getActionableItems().iterator();
	while (actionableItemIter.hasNext()) {
	    AbstractButton actionableItem = (AbstractButton)actionableItemIter.next();
	    actionableItem.addActionListener(this);
	}
    }

    public void configure() {

    }

    protected GUI getGUI() {
	return gui;
    }

    public abstract void actionPerformed (ActionEvent ae);

    public Executioner getExecutioner() {
	return getGUI().getAppFrame().getApplication().getExecutioner();
    }

}