package com.jutools.filetracker;

import java.util.function.Consumer;

/**
 * 
 * 
 * @author jmsohn
 */
public interface PauseReader {

	/**
	 * 
	 * 
	 * @param action
	 * @param buffer
	 * @return
	 */
	public void read(Consumer<String> action, byte[] buffer) throws Exception;
}
