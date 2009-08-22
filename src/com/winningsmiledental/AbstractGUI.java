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


public abstract class AbstractGUI implements GUI {

    public SwingEngine swix = null;
    protected boolean CONTINUE_RUNNING;
    protected ApplicationFrame applicationFrame;
    protected Collection actionableItems;

    public AbstractGUI(ApplicationFrame appFrame) {
	actionableItems = new LinkedList();
	setAppFrame(appFrame);
	configure();
    }
    /**
     * Sets the application frame.
     *
     * @param ApplicationFrame
     */
    public void setAppFrame(ApplicationFrame appFrame) {
	applicationFrame = appFrame;
    }

    /**
     * Returns the application frame.
     *
     * @return ApplicationFrame
     */
    public ApplicationFrame getAppFrame() {
	return applicationFrame;
    }

    /**
     * Calls the setupGUI() method.
     */
    public void configure() {
	try {
	    setupGUI();
	}
	catch (Exception e) {
	    e.printStackTrace(System.out);
	    //getAppFrame().exit(1);
	}
    }

    /**
     * Returns the Collection of acttionableItems.
     *
     * @return Collection actionableItems.
     */
    public Collection getActionableItems() {
	return actionableItems;
    }

    /**
     * Sets up the GUI. Calls method connectActionListeners().
     */
    protected void setupGUI () throws Exception {
	//System.out.println("creating new SwingEngine");
	swix = new SwingEngine( this );
	String urlString = getGUIxml();
	URL url = getClass().getResource(urlString);
	swix.render(url);
	//System.out.println("created new SwingEngine");
	connectActionListeners();
    }

    protected void refresh() {
    }

    protected abstract String getGUIxml();
    
    /**
     * Connects action listeners.
     */
    protected abstract void connectActionListeners();

    /**
     * Returns the main jPanel
     *
     * @return JPanel
     */
    public JPanel getPane() {
	return ((JPanel)swix.getRootComponent());
    }

    /**
     * Returns the executioner.
     *
     * @return Executioner
     */
    public Executioner getExecutioner() {
	return getAppFrame().getApplication().getExecutioner();
    }

}