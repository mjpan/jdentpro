package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;


import java.io.*;
import java.util.*;
import java.net.*;


/**
 * This is the Main Frame of the Application.
 */
public interface ApplicationFrame
    extends  Runnable,
	     WindowListener,
	     ActionListener {

    public void setApplication(Application a);

    public Application getApplication();

    public JFrame getFrame ();

    public void loadScreen(GUI gui);
}

