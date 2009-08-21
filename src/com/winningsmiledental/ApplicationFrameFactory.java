package com.winningsmiledental;


/**
 * This is the Main Frame of the Application.
 */
public class ApplicationFrameFactory {
    public static ApplicationFrame createFrame(Application a) {
	return new JDentProFrame(a);
    }
}

