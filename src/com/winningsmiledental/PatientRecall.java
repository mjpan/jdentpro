package com.winningsmiledental;

public class PatientRecall {
    public static String[] RECALL_TYPE =
	new String[] {  "Prophylaxis",
			"OfficeReline",
			"RootPlanning"
    };

    /*
    public static int[] RECALL_INTERVAL =
	new int[] { 0, // this is used as a 1-based array
	            92,  //3 months
		    184, //6 months
		    365, //1 year
		    730, //2 years
		    1095 //3 years
    };
    */
    public static int getInterval(int index) throws Exception {
	int value = 0;
	switch (index) {
	case 1:
	    value = 91; break;
	case 2:
	    value =  182; break;
	case 3:
	    value = 365; break;
	case 4:
	    value = 730; break;
	case 5:
	    value = 1095; break;
	default:
	    throw new Exception("invalid index");
	}
	return value;
    }

    public static int DATEINDEX_YEAR = 0;
    public static int DATEINDEX_MONTH = 1;
    public static int DATEINDEX_DAY = 2;

    int recallNum;
    int patientNum;

    Integer[] dueDate;
    Integer[] previousDate;

    int interval;

    boolean isDisabled;

    String type;

    int guiIndex;

    public PatientRecall(int recallNum,
			 int patientNum,
			 String dueDate,
			 String previousDate,
			 int interval,
			 String type,
			 boolean disabled) {
	this (patientNum, dueDate, previousDate, interval, type, disabled);
	setID(recallNum);
	/*
	setPatient(patientNum);
	setDueDate(dueDate);
	setPreviousDate(previousDate);
	setInterval(interval);
	setType(type);
	setDisabled(disabled);
	*/
    }

    /**
     * no recall num because not yet stored to database
     */
    public PatientRecall(int patientNum,
			 String dueDate,
			 String previousDate,
			 int interval,
			 String type,
			 boolean disabled) {
	setPatient(patientNum);
	setDueDate(dueDate);
	setPreviousDate(previousDate);
	setInterval(interval);
	setType(type);
	setDisabled(disabled);
    }

    public int getGUIIndex() {
	return guiIndex;
    }

    protected void setGUIIndex(int i) {
	guiIndex = i;
    }

    protected void setID(int id) {
	recallNum = id;
    }

    public int getRecallNum() {
	return recallNum;
    }

    protected void setPatient(int id) {
	patientNum = id;
    }

    public int getPatientNum() {
	return patientNum;
    }

    protected void setDueDate(String dateString) {
	//setDate(dueDate, dateString);
	dueDate = parseDate(dateString);
    }

    public Integer getDueDate(int index) {
	return dueDate[index];
    }

    public String getDueDate() {
	return getDateString(dueDate);
    }

    protected void setPreviousDate(String dateString) {
	//setDate(previousDate, dateString);
	previousDate = parseDate(dateString);
    }


    public Integer getPreviousDate(int index) {
	return previousDate[index];
    }

    public String getPreviousDate() {
	return getDateString(previousDate);
    }

    public String getDateString(Integer[] date) {
	return date[0]+"-"+date[1]+"-"+date[2];
    }

    protected Integer[] parseDate(String dateString) {
	Integer[] date = new Integer[3];
	String[]parsedDateString = dateString.split("-", 3);
	for (int i=0; i<parsedDateString.length; i++) {
	    date[i] = new Integer(parsedDateString[i]);
	}
	return date;
    }

    protected void setInterval(int i) {
	interval = i;
    }

    public int getInterval() {
	return interval;
    }

    protected void setType(String s) {
	type = s;
    }

    public String getType() {
	return type;
    }

    protected void setDisabled(boolean b) {
	isDisabled = b;
    }

    public boolean isDisabled() {
	return isDisabled;
    }
}