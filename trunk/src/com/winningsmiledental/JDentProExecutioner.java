package com.winningsmiledental;

public class JDentProExecutioner implements Executioner {

    private JDentPro jdp;

    public JDentProExecutioner(JDentPro jdp) {
	this.jdp = jdp;
    }

    public ApplicationFrame getAppFrame() {
	return jdp.getAppFrame();
    }

    public void setDatabase(String database) {
	jdp.setDatabase(database);
    }

    public String getDatabase() {
	return jdp.getDatabase();
    }

    public void setCurrentEmployee(int id) {
	jdp.setCurrentEmployee(id);
    }

    public int getCurrentEmployeeID() {
	return jdp.getCurrentEmployeeID();
    }

    public void loadLoginScreen() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI loginScreen = new LoginScreenGUI(af);
	af.loadScreen(loginScreen);
    }
    
    public void loadMainMenu() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI mainMenu = new MainMenuGUI(af);
	af.loadScreen(mainMenu);
    }

    public void loadPatientInfo() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI patientInfo = new PatientInfoGUI(af);
	af.loadScreen(patientInfo);
    }

    public void loadPatientRecord(RecordManager manager) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI patientRecord = new PatientRecordGUI(af, manager);
	af.loadScreen(patientRecord);
    }

    public void loadPatientRecord(RecordManager manager, int rcn) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI patientRecord = new PatientRecordGUI(af, manager, rcn);
	af.loadScreen(patientRecord);
    }

    public void loadGuarantorInfo(int rcn) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI guarantorInfo = new GuarantorInfoGUI(af, rcn);
	af.loadScreen(guarantorInfo);
    }

    public void loadGuarantorRecord(int ptRCN, RecordManager manager) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI guarantorRecord = new GuarantorRecordGUI(af, ptRCN, manager);
	af.loadScreen(guarantorRecord);
    }

    public void loadGuarantorRecord(int ptRCN, RecordManager manager, int gPatNum) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI guarantorRecord = new GuarantorRecordGUI(af, ptRCN, manager, gPatNum);
	af.loadScreen(guarantorRecord);
    }

    public void loadRecall(RecordManager manager, int patnum) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI recall = new RecallGUI(af, manager, patnum);
	af.loadScreen(recall);
    }

    public void loadSpecialFunctions() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI specialFunctions = new SpecialFunctionsGUI(af);
	af.loadScreen(specialFunctions);
    }

    public void loadPrintOptions() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI printOptions = new PrintOptionsGUI(af);
	af.loadScreen(printOptions);
    }

    public void loadPrintBDay() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI printBDay = new PrintBDayGUI(af);
	af.loadScreen(printBDay);
    }

    public void loadPrintRecall() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI printRecall = new PrintRecallGUI(af);
	af.loadScreen(printRecall);
    }

    public void loadUserTable() {
	ApplicationFrame af = jdp.getAppFrame();
	GUI userTable = new UserTableGUI(af);
	af.loadScreen(userTable);
    }

    /**
     * this loads the user admin screen, for adding a new user
     */
    public void loadUserAdmin(UserAccountManager manager) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI userAdmin = new UserAdminGUI(af, manager);
	af.loadScreen(userAdmin);
    }

    /**
     * this loads the user admin screen, for editing an existing user
     */
    public void loadUserAdmin(UserAccountManager manager, String user) {
	ApplicationFrame af = jdp.getAppFrame();
	GUI userAdmin = new UserAdminGUI(af, manager, user);
	af.loadScreen(userAdmin);	
    }
    

    public void exit(int i) {
	jdp.exit(i);
    }

    public void about() {
    }
}