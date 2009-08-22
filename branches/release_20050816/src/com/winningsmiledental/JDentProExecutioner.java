package com.winningsmiledental;

public class JDentProExecutioner implements Executioner {

    private JDentPro jdp;
    private GUI loginScreen;
    private GUI mainMenu;
    private GUI patientInfo;

    private GUI patientRecordGUI;
    private GUI guarantorInfo;
    private GUI guarantorRecord;
    private GUI recall;
    private GUI specialFunctions;
    private GUI printOptions;
    private GUI printBDay;
    private GUI printRecall;
    private GUI userTable;
    private GUI userAdmin;

    private GuarantorRecordManager guarantorRecordManager;
    private PatientRecordManager patientRecordManager;

    public JDentProExecutioner(JDentPro jdp) {
	this.jdp = jdp;
	setRecordManagers();
    }

    private void setRecordManagers() {
	guarantorRecordManager = new GuarantorRecordManager(this);
	patientRecordManager = new PatientRecordManager(this);
    }

    public GuarantorRecordManager getGuarantorRecordManager() {
	return guarantorRecordManager;
    }
    
    public PatientRecordManager getPatientRecordManager() {
	return patientRecordManager;
    }

    /**
     * Returns the ApplicationFrame.
     *
     * @return ApplicationFrame The program's ApplicationFrame.
     */
    public ApplicationFrame getAppFrame() {
	return jdp.getAppFrame();
    }

    /**
     * Sets the program's database.
     *
     * @param String Takes the name of the database as a String
     */
    public void setDatabase(String database) {
	jdp.setDatabase(database);
    }

    /**
     * Returns the name of the program's database.
     *
     * @return String The name of the database.
     */
    public String getDatabase() {
	return jdp.getDatabase();
    }

    public static String AUTOGEN_SSN_PREFIX = "wsd:";
    public static String AUTOGEN_SSN_PATTERN = AUTOGEN_SSN_PREFIX+"\\d+";
    public String generateSSN() {
	return AUTOGEN_SSN_PREFIX+(System.currentTimeMillis());
    }

    /**
     * Sets the current user to the employeenum of the user that is logged in.
     *
     * @param int The current user's employeenum.
     */
    public void setCurrentEmployee(int id) {
	jdp.setCurrentEmployee(id);
    }

    /**
     * Returns the employeenum of the current user.
     *
     * @return int The current user's employeenum.
     */
    public int getCurrentEmployeeID() {
	return jdp.getCurrentEmployeeID();
    }

    /**
     * Loads the loginscreen gui. 
     * If loginScreen is null, this creates a new instance.
     */
    public void loadLoginScreen() {
	ApplicationFrame af = jdp.getAppFrame();
	
	if (loginScreen == null) {
	    loginScreen = new LoginScreenGUI(af);
	}

	af.loadScreen(loginScreen);
    }
    
    /**
     *  Loads the mainMenu gui.
     * If mainMenu is null, this creates a new instance.
     */
    public void loadMainMenu() {
	ApplicationFrame af = jdp.getAppFrame();
	
	if (mainMenu == null) {
	    mainMenu = new MainMenuGUI(af);
	}

	af.loadScreen(mainMenu);
    }

    /**
     * Loads the patientInfo gui.
     * If patientInfo is null, this creates a new instance.
     */
    public void loadPatientInfo() {
	ApplicationFrame af = jdp.getAppFrame();

	if (patientInfo == null) {
	    patientInfo = new PatientInfoGUI(af);
	}

	af.loadScreen(patientInfo);
    }

    /**
     * Loads the patientRecord gui for a new patient.
     * If patientRecord is null, this creates a new instance.
     *
     * @param RecordManager
     */
    public void loadPatientRecord() {
	ApplicationFrame af = jdp.getAppFrame();
	patientRecordGUI = new PatientRecordGUI(af);
	af.loadScreen(patientRecordGUI);	
    }


    /**
     * Loads the patientRecord gui for an existing patient with RCN rcn.
     * If patientRecord is null, this creates a new instance.
     *
     * @param RecordManager
     * @param int RCN of patient.
     */
    public void loadPatientRecord(int patientRCN) {
	ApplicationFrame af = jdp.getAppFrame();
	patientRecordGUI = new PatientRecordGUI(af, patientRCN);
	af.loadScreen(patientRecordGUI);
    }

    /**
     * Loads the guarantorInfo gui.
     *
     * @param int The rcn for the patient to whom a guarantor is going to be assigned.
     */
    // should be called guarantor records gui instead
    // as it is a table that lists all the guarantors
    public void loadGuarantorInfo(int rcn) {
	ApplicationFrame af = jdp.getAppFrame();
	guarantorInfo = new GuarantorInfoGUI(af, rcn);
	af.loadScreen(guarantorInfo);
    }

    /**
     * Loads the guarantorRecord gui for a new guarantor to be assigned to patient with RCN ptRCN.
     * If guarantorRecord is null, this creates a new instance.
     *
     * @param int           RCN of patient.
     * @param RecordManager
     */
    public void loadGuarantorRecord(int ptRCN) {
	ApplicationFrame af = jdp.getAppFrame();
	guarantorRecord = new GuarantorRecordGUI(af, ptRCN);
	af.loadScreen(guarantorRecord);
    }

    /**
     * Loads the guarantorRecord gui for an existing guarantor assigned to patient with RCN ptRCN.
     * If guarantorRecord is null, this creates a new instance.
     *
     * @param int           RCN of the patient
     * @param RecordManager
     * @param int           PatNum of the patient's guarantor.
     */
    public void loadGuarantorRecord(int ptRCN, int gPatNum) {
	ApplicationFrame af = jdp.getAppFrame();
	
	if (guarantorRecord == null) {
	    guarantorRecord = new GuarantorRecordGUI(af, ptRCN, gPatNum);
	}
	else {
	    //should not be calling config directly
	    ((GuarantorRecordGUI)guarantorRecord).config(ptRCN, gPatNum);
	}
	af.loadScreen(guarantorRecord);
    }

    /**
     * Loads the recall gui for patient with PatNum patnum.
     * If recall is null, this creates a new instance.
     *
     * @param RecordManager
     * @param int           PatNum of the patient.
     */
    public void loadRecall(int patnum) {
	ApplicationFrame af = jdp.getAppFrame();

	if (recall == null) {
	    recall = new RecallGUI(af, patnum);
	}
	else {
	    ((RecallGUI)recall).setRecall(patnum);
	}
	af.loadScreen(recall);
    }

    /**
     * Loads the specialFunctions gui. 
     * If specialFunctions is null, this creates a new instance.
     */
    public void loadSpecialFunctions() {
	ApplicationFrame af = jdp.getAppFrame();

	if (specialFunctions == null) {
	    specialFunctions = new SpecialFunctionsGUI(af);
	}

	af.loadScreen(specialFunctions);
    }

    /**
     * Loads the printOptions gui.
     * If printOptions is null, this creates a new instance.
     */
    public void loadPrintOptions() {
	ApplicationFrame af = jdp.getAppFrame();

	if (printOptions == null) {
	    printOptions = new PrintOptionsGUI(af);
	}

	af.loadScreen(printOptions);
    }

    /**
     * Loads the printBDay gui.
     * If printBDay is null, this creates a new instance.
     */
    public void loadPrintBDay() {
	ApplicationFrame af = jdp.getAppFrame();

	if (printBDay == null) {
	    printBDay = new PrintBDayGUI(af);
	}

	af.loadScreen(printBDay);
    }

    /**
     * Loads the printRecall gui.
     * If printRecall is null, this creates a new instance.
     */
    public void loadPrintRecall() {
	ApplicationFrame af = jdp.getAppFrame();

	if (printRecall == null) {
	    printRecall = new PrintRecallGUI(af);
	}

	af.loadScreen(printRecall);
    }

    /**
     * Loads the userTable gui.
     */
    public void loadUserTable() {
	ApplicationFrame af = jdp.getAppFrame();
	userTable = new UserTableGUI(af);
	af.loadScreen(userTable);
    }

    /**
     * Loads the userAdmin gui for a new user.
     * If userAdmin is null, this creates a new instance.
     *
     * @param RecordManager
     */
    public void loadUserAdmin(UserAccountManager manager) {
	ApplicationFrame af = jdp.getAppFrame();

	if (userAdmin == null) {
	    userAdmin = new UserAdminGUI(af, manager);
	}
	else {
	    ((UserAdminGUI)userAdmin).setUser();
	}
	af.loadScreen(userAdmin);
    }

    /**
     * Loads the userAdmin gui to edit an existing user.
     * If userAdmin is null, this creates a new instance.
     *
     * @param RecordManager
     * @param String        UserName of user to be edited.
     */
    public void loadUserAdmin(UserAccountManager manager, String user) {
	ApplicationFrame af = jdp.getAppFrame();
	
	if (userAdmin == null) {
	    userAdmin = new UserAdminGUI(af, manager, user);
	}
	else {
	    ((UserAdminGUI)userAdmin).setUser(user);
	}
	af.loadScreen(userAdmin);	
    }
    
    /**
     * Exits the program.
     *
     * @param int
     */
    public void exit(int i) {
	jdp.exit(i);
    }

    /**
     *
     */
    public void about() {
    }

}