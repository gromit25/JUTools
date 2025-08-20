package com.jutools.timer;

import lombok.Data;

/**
 * 타임아웃 발생 이벤트 클래스
 * 
 * @author jmsohn
 */
@Data
public class TimeoutEvent {

	/** 타임 아웃 발생 시간 */
	private long timestamp;

	/** 최총 touch 된 시간 */
	private long lastTouched;
}
