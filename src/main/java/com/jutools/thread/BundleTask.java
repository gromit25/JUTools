package com.jutools.thread;

import java.util.List;

/**
 * 
 * 
 * @author jmsohn
 */
@FunctionalInterface
public interface BundleTask {
	
	/**
	 * 
	 * @param list
	 * @param bundleId
	 * @param start
	 * @param end
	 */
	public void consume(List<?> list, int bundleId, int start, int end);
}
