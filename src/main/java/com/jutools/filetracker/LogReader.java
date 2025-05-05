package com.jutools.filetracker;

import java.util.function.Consumer;

/**
 * 
 * 2025-05-05T17:44:50.276+09:00  INFO 84760 --- [app] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
 * 
 * @author jmsohn
 */
public class LogReader implements PauseReader {

	@Override
	public void read(Consumer<String> action, byte[] buffer) throws Exception {
	}
}
