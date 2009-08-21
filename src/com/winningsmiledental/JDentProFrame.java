package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.swixml.SwingEngine;

import java.io.*;
import java.util.*;
import java.net.*;

import org.jdom.*;

/**
 * This is the Main Frame of the Application.
 */
public class JDentProFrame implements ApplicationFrame {
    public SwingEngine swix = null;

    private boolean CONTINUE_RUNNING;

    /*
     *====================
     *BEGIN SwiXML fields
     *====================
     */

    public JMenuBar menubar;

    public JMenu menuFile;

    //application menu items
    public JMenuItem mi_Options;
    public JMenuItem mi_About;
    public JMenuItem mi_Help;
    public JMenuItem mi_Exit;

    private Application application;

    /*
     *====================
     *END SwiXML fields
     *====================
     */

    public JDentProFrame(Application a) {
	super();
	setApplication(a);
	configure();
    }

    public void setApplication(Application a) {
	application = a;
    }

    public Application getApplication() {
	return application;
    }

    public void configure() {

	try {
	    setupGUI();
	}
	catch (Exception e) {
	    e.printStackTrace(System.out);
	    getApplication().exit(1);
	}
    }

    public JFrame getFrame () {
	JFrame frame = null;
	if (swix!=null) {
	    frame = (JFrame) swix.getRootComponent();
	}
	return frame;
    }

    private void setupGUI () throws Exception {
	System.out.println("creating new SwingEngine");

	swix = new SwingEngine( this );

	String urlString = "/xml/AppWindow.xml";

	URL url = 	    
		getClass().getResource(urlString);

	swix.render(url);

	System.out.println("created new SwingEngine");

	connectActionListeners();

	LoginScreenGUI loginScreen = new LoginScreenGUI(this);

	loadScreen(loginScreen);

	getFrame().setVisible(true);
    }

    public void loadScreen(GUI gui) {	
	getFrame().getContentPane().removeAll();
	getFrame().getContentPane().add(gui.getPane(), 0);
	getFrame().getContentPane().validate();
	getFrame().getContentPane().repaint();
    }

    private void connectActionListeners() {
	// File Menu
	mi_Exit.addActionListener(this);
	mi_Options.addActionListener(this);
	mi_Help.addActionListener(this);
	mi_About.addActionListener(this);
    }


    /*
     * ============================
     * BEGIN ACTIONLISTENER IMPLEMENTATION
     * ============================
     */

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();


	// Application Menu

	if (command.equals("AC_Help")) {
	    /** Help menu item action needs a help file to open */
	}
	else if (command.equals("AC_About")) {
	    onActionAbout();
	}
	else if (command.equals("AC_Exit")) {

	    onActionExit();
        }

    }

    /*
     * ============================
     * END ACTIONLISTENER IMPLEMENTATION
     * ============================
     */

    /*
     * ============================
     * BEGIN WINDOWLISTENER IMPLEMENTATION
     * ============================
     */

    public void windowActivated(WindowEvent e) {
	//should redraw everything
    }

    public void windowClosed(WindowEvent e) {
    }

    /**
     * Invoked when a window is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    public void windowClosing( WindowEvent e ) {
	onActionExit();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    /*
     * ============================
     * END WINDOWLISTENER IMPLEMENTATION
     * ============================
     */


    private void exitGUI () {
	CONTINUE_RUNNING = false;
    }



    public void onActionExit() {
	exitGUI();
    }



    /** pops up a dialog box specifying the
     * information about the  application.
     */
    public void onActionAbout () {

	JDialog dialog =
	    new JDialog(getFrame(),
			"About");

	JScrollPane scrollPane =
	    new JScrollPane(new AboutPane(this));

	dialog.getContentPane().add("Center", scrollPane);

	JPanel panel = new JPanel();
	dialog.getContentPane().add("South", panel);

	dialog.setSize(400, 200);
	dialog.setVisible(true);
    }

    public SwingEngine getSwingEngine() {
	return swix;
    }

    private void cleanup() {
	getApplication().exit(0);
    }

    private boolean continueRunning() {
	return CONTINUE_RUNNING;
    }

    /*
     * ============================
     * BEGIN RUNNABLE IMPLEMENTATION
     * ============================
     */

    public void run() {
	CONTINUE_RUNNING = true;

	while (continueRunning()) {
	    try {
		Thread.sleep(1);
	    }
	    catch (Exception e) {
	    }
	}

	cleanup();
    }
    
    /*
     * ============================
     * END RUNNABLE IMPLEMENTATION
     * ============================
     */

}

