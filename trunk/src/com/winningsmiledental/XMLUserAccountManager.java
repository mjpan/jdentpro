package com.winningsmiledental;

import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;

public class XMLUserAccountManager extends AbstractUserAccountManager {
    private String path = "users.xml";

    public XMLUserAccountManager() {
	this("dental");
    }

    public XMLUserAccountManager(String databaseName) {	
	super(databaseName);
	/*
	String path = "users.xml";
	try {
	    Reader reader = new FileReader(path);
	    load(reader);
	}
	catch (Exception e) {
	}
	*/
    }
    

    private void load(Reader reader) throws JDOMException, IOException {
	
	SAXBuilder builder = new SAXBuilder(false);
	Document doc = builder.build(reader);

	Element usersElement = doc.getRootElement();
	    
	/* create user accounts here */
	Iterator userElementIter = usersElement.getChildren().iterator();
	while (userElementIter.hasNext()) {
	    Element userElement = (Element) userElementIter.next();
	    String login = userElement.getAttributeValue("login");
	    String password = userElement.getAttributeValue("password");
	    if (login != null && password != null) {
		//UserAccount userAccount = new UserAccount(login);
		//userAccount.setPassword(password);

		String accessLevelString = 
		    userElement.getAttributeValue("accessLevel", "");
		int accessLevel = ACCESS_NONE;
		try {
		    accessLevel = Integer.parseInt(accessLevelString);
		}
		catch(Exception e) {
		}
		addUser(login, password, accessLevel);
		//userAccount.setAccessLevel(accessLevel);
		//userAccounts.add(userAccount);
	    }
	}
	
    }

    /**
     */
    public void addUser(String login, String password, int accessLevel) {
	
	super.addUser(login, password, accessLevel);

	//write to file
	writeToFile();
    }

    public void removeUser(String login)  {
	super.removeUser(login);
	writeToFile();
    }

    /**
     * @return whether the operation succeeded
     */
    public void setPassword(String login, String newPassword)  {

	super.setPassword(login, newPassword);

	writeToFile();
    }

    public void setAccessLevel(String login, int accessLevel) {
	super.setAccessLevel(login, accessLevel);
	writeToFile();
    }

    private void writeToFile() {
	/*
	Element usersElement = new Element("Users");

	Iterator userAccountIter = getUserAccounts().iterator();
	while (userAccountIter.hasNext()) {
	    UserAccount userAccount =
		(UserAccount) userAccountIter.next();
	    
	    Element userElement = new Element("User");

	    //set all user info
	    userElement.setAttribute("login", userAccount.getLogin());
	    userElement.setAttribute("password", userAccount.getPassword());
	    userElement.setAttribute("accessLevel", 
				     ""+userAccount.getAccessLevel());
	    usersElement.addContent(userElement);
	}

	try {
	    Writer writer = new FileWriter(new File(path));
	    
	    Format f = 
		org.jdom.output.Format.getPrettyFormat();
	    XMLOutputter xmlOut = new XMLOutputter (f);
	    
	    xmlOut.output(new Document(usersElement), writer);
	}
	catch (Exception e) {
	}
*/
    }

}
