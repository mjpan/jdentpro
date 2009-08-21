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

public class ErrorMessage implements ActionListener  {

    public JDialog dialog;
    public JLabel message;
    public JPanel panel;
    public JButton okay;

    public ErrorMessage(String error) {
	dialog = new JDialog();
	message = new JLabel(error);
	panel = new JPanel();
	okay = new JButton("OKAY");
	setUpDialog();
    }

    public void setUpDialog() {
	okay.setActionCommand("AC_OKAY");
	okay.addActionListener(this);
	panel.add("Center", message);
	panel.add(okay);
	dialog.setSize(400, 100);
	dialog.getContentPane().add(panel);
	dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();
	if (command.equals("AC_OKAY")) {
	    dialog.dispose();
        } 
    }





}