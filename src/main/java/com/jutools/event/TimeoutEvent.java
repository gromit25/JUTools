package com.jutools.event;

import lombok.Data;

/**
 * 
 * 
 * @author jmsohn
 */
@Data
public class TimeoutEvent {

	/** */
	private long timestamp;

	/** */
	private long lastTouchedTimestamp;
}
