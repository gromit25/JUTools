package com.jutools.event;

import lombok.Data;

@Data
public class TimeoutEvent {

	/** */
	private long timestamp;

	/** */
	private long lastTouchedTimestamp;
}
