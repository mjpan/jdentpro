package com.winningsmiledental;

import java.util.*;
import java.io.*;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.math.BigInteger;

public class MessageDigester {
    /**
     * this is the abstract method for message digest methods
     */
    public static String digest(String message) {
	return digest(message.getBytes());
    }
    
    public static String digest(byte[] message) {
	return sha1(message);
    }
    
    public static String md5 (byte[] message) {
	String digest = null;
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    
	    byte[] digestBytes = md.digest(message);
	    digest = toHex(digestBytes);
	}
	catch (Exception e) {
	}
	return digest;
    }
    
    public static String sha1 (byte[] message) {
	String digest = null;
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA");
	    
	    byte[] digestBytes = md.digest(message);
	    digest = toHex(digestBytes);
	}
	catch (Exception e) {
	}
	return digest;
    }
    
    /**
     * converts a byte array to hex string
     * handling the case where Java's Integer.toString method 
     * which does not return leading 0's
     */
    public static String toHex(byte[] input) {
	StringBuffer sbf = new StringBuffer();

	if (input == null) {
	    return "";
	}

	byte[] temp = new byte[input.length];

	for ( int i = 0; i <input.length ; i++) {
	    temp[i] = input[i];
	    //int v = input[i] & 0XFF;
	    int v = temp[i] & 0xFF;
	    
	    //Integer.toString will not return leading 0's
	    String hexVal = Integer.toString(v, 16);
	    while (hexVal.length() < 2) {
		hexVal = "0"+hexVal;
	    }
	    sbf.append(hexVal);
	}
	return sbf.toString();
    }

    /**
     * Accepts as input a hex string
     */
    public static byte[] toByteArray (String message) {
	StringReader sr = new StringReader(message);
	byte[] byteArray = new byte[message.length()/2];
	try {
	    for (int i=0; i<message.length()/2; i++) {

		//read 1st char of hex of byte
		char c1 = (char) sr.read();
		switch (c1) {
		case '0':
		    byteArray[i] = (byte) 0x00; break;
		case '1':
		    byteArray[i] = (byte) 0x10; break;
		case '2':
		    byteArray[i] = (byte) 0x20; break;
		case '3':
		    byteArray[i] = (byte) 0x30; break;
		case '4':
		    byteArray[i] = (byte) 0x40; break;
		case '5':
		    byteArray[i] = (byte) 0x50; break;
		case '6':
		    byteArray[i] = (byte) 0x60; break;
		case '7':
		    byteArray[i] = (byte) 0x70; break;
		case '8':
		    byteArray[i] = (byte) 0x80; break;
		case '9':
		    byteArray[i] = (byte) 0x90; break;
		case 'a':
		    byteArray[i] = (byte) 0xa0; break;
		case 'b':
		    byteArray[i] = (byte) 0xb0; break;
		case 'c':
		    byteArray[i] = (byte) 0xc0; break;
		case 'd':
		    byteArray[i] = (byte) 0xd0; break;
		case 'e':
		    byteArray[i] = (byte) 0xe0; break;
		case 'f':
		    byteArray[i] = (byte) 0xf0; break;
		default:
		    byteArray[i] = (byte) 0x00; break;
		}

		//read 2nd char of hex of byte
		char c2 = (char) sr.read();
		switch (c2) {
		case '0':
		    byteArray[i] |= (byte) 0x00; break;
		case '1':
		    byteArray[i] |= (byte) 0x01; break;
		case '2':
		    byteArray[i] |= (byte) 0x02; break;
		case '3':
		    byteArray[i] |= (byte) 0x03; break;
		case '4':
		    byteArray[i] |= (byte) 0x04; break;
		case '5':
		    byteArray[i] |= (byte) 0x05; break;
		case '6':
		    byteArray[i] |= (byte) 0x06; break;
		case '7':
		    byteArray[i] |= (byte) 0x07; break;
		case '8':
		    byteArray[i] |= (byte) 0x08; break;
		case '9':
		    byteArray[i] |= (byte) 0x09; break;
		case 'a':
		    byteArray[i] |= (byte) 0x0a; break;
		case 'b':
		    byteArray[i] |= (byte) 0x0b; break;
		case 'c':
		    byteArray[i] |= (byte) 0x0c; break;
		case 'd':
		    byteArray[i] |= (byte) 0x0d; break;
		case 'e':
		    byteArray[i] |= (byte) 0x0e; break;
		case 'f':
		    byteArray[i] |= (byte) 0x0f; break;
		default:
		    byteArray[i] |= (byte) 0x0f; break;
		}


		byte[] temp = new byte[1];
		temp[0] = byteArray[i];
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return byteArray;
    }

}
