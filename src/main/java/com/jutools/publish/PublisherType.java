package com.jutools.publish;

import com.jutools.publish.formatter.console.ConsolePublisher;
import com.jutools.publish.formatter.excel.ExcelPublisher;
import com.jutools.publish.formatter.text.TextPublisher;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * publisher 종류
 * 
 * @author jmsohn
 */
public enum PublisherType {

	/** text 및 text file 출력용 publisher */
	TEXT_FILE("text", TextPublisher.class),
	/** console 출력용 publisher */
	CONSOLE("console", ConsolePublisher.class),
	/** excel 파일 출력용 publisher */
	EXCEL_FILE("excel", ExcelPublisher.class);
	
	//--------------------

	/** publisher 이름 */
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private String name;
	
	/** 구체화된 publisher 클래스 */
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private Class<? extends Publisher> concreteType;
	
	/**
	 * 생성자 
	 * 
	 * @param name publisher 이름
	 * @param concreteType 구체화된 publisher 클래스
	 */
	PublisherType(String name, Class<? extends Publisher> concreteType) {
		this.setName(name);
		this.setConcreteType(concreteType);
	}
}
