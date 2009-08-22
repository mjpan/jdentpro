package com.winningsmiledental;

import java.lang.ThreadGroup;

public class JDentProThreadGroup extends ThreadGroup {
    public JDentProThreadGroup(String name) {
	super(name);
    }

    public void uncaughtException(Thread t, Throwable e) {
	e.printStackTrace();
	t.dumpStack();
    }
}
