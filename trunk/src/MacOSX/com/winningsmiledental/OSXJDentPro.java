package com.winningsmiledental;

public class OSXJDentPro extends JDentPro implements WinningSmileDentalOSXApplication {

    protected OSXApplication osxApplication;
    protected OSXAdapter osxAdapter;

    public OSXJDentPro() {
	super();
	if (runningOnMacOSX()) {
	    registerForMacOSX();
	}
    }

    public void registerForMacOSX() {
	osxApplication = new OSXApplication();
	osxAdapter = new OSXAdapter(this);
	osxApplication.addApplicationListener(osxAdapter);
    }
}