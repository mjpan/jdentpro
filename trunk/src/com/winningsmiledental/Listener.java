package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.swixml.SwingEngine;

public interface Listener extends ActionListener {

    public void setGUI(GUI gui);

    public void configure();

    public void actionPerformed (ActionEvent ae);

}