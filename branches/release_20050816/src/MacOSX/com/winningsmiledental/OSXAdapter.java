package com.winningsmiledental;

import com.apple.eawt.*;

public class OSXAdapter extends ApplicationAdapter {
    protected com.winningsmiledental.Application application;

    public OSXAdapter(com.winningsmiledental.Application application) {
	setApplication(application);
    }

    protected void setApplication(com.winningsmiledental.Application a) {
	application = a;
    }

    protected com.winningsmiledental.Application getApplication() {
	return application;
    }

    public void handleQuit(ApplicationEvent ae) {
	ae.setHandled(true);
	application.exit(0);
    }

    public void handleAbout(ApplicationEvent ae) {
	ae.setHandled(true);
	application.about();
    }
}