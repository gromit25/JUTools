package com.jutools.spring.workflow;

import lombok.Builder;
import lombok.Data;

/**
 * 워크플로우 메시지 클래스
 * 
 * @author jmsohn
 */
@Data
@Builder
public class Message<T> {
	
	/** 제목 */
	private String topic;
	
	/** 메시지 바디 */
	private T body;
	
	
	/**
	 * 디폴트 생성자
	 */
	public Message() {
	}
	
	/**
	 * 생성자
	 * 
	 * @param topic 제목
	 */
	public Message(String topic) {
		this.setTopic(topic);
	}
	
	/**
	 * 생성자
	 * 
	 * @param topic 제목
	 * @param body 내용
	 */
	public Message(String topic, T body) {
		this.setTopic(topic);
		this.setBody(body);
	}
	
	/**
	 * 바디 데이터를 특정 타입으로 변환하여 반환 
	 * 
	 * @param <R> 반환 받을 타입
	 * @param returnType 반환 받을 타입의 클래스
	 * @return 바디 데이터
	 */
	public <R> R getBody(Class<R> returnType) {
		return returnType.cast(this.body);
	}
}
