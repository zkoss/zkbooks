/* LabZatsTestCase.java

	Reusable ZATS base class: boots an in-process ZK server once per test class.

	ZATS Mimic is free and part of ZK CE. It emulates the server side only - it never
	renders HTML and never fetches JavaScript, so it can verify the Java half of a
	component very fast, but it cannot verify a mold. That is the WebDriver suite's job.
*/
package com.foo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import org.zkoss.zats.mimic.DefaultZatsEnvironment;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.ZatsEnvironment;

public abstract class LabZatsTestCase {

	protected static ZatsEnvironment env;

	@BeforeAll
	static void initEnv() {
		// arg 1 = the folder holding WEB-INF's contents; arg 2 = the servlet context path
		env = new DefaultZatsEnvironment("./src/test/webapp/WEB-INF", "/componentdev");
		// arg = the web content root, i.e. where the .zul files live.
		// Relative paths resolve against ${basedir}, surefire's working directory.
		env.init("./src/test/webapp");
	}

	@AfterAll
	static void destroyEnv() {
		if (env != null)
			env.destroy();
	}

	@AfterEach
	void cleanupClients() {
		env.cleanup();
	}

	/** Opens a ZUL and returns the server-side desktop. Path is relative to the web root. */
	protected DesktopAgent connect(String zulPath) {
		return env.newClient().connect(zulPath);
	}
}
