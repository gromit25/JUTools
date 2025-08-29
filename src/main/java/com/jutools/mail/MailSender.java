package com.jutools.mail;

import java.io.File;
import java.util.List;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author jmsohn
 */
public class MailSender {
	
	/** */
	private Properties props;
	
	/** */
	@Getter
	private String sender;
	
	/** */
	private List<String> receiverList = new Vector<>();
	
	/** */
	private TextGen subjectGen;
	
	/** */
	private TextGen bodyGen;
	
	/** */
	private List<File> attachFileList = new Vector<>();
	
	/**
	 * 
	 * 
	 * @param props
	 */
	MailSender(Properties props) throws Exception {
		
		if(props == null) {
			throw new IllegalArgumentException("props is null.");
		}

		this.props = props;
	}

	/**
 	 *
	 *
	 * @param values
	 */
	public void send(Map<String, ?> values) throws Exception {

		// 입력값 검증 및 객체 설정 상태 검증
		if(values == null) {
			throw new IllegalArgumentException("values is null.");
		}

		if(this.props == null) {
			throw new Exception("properties is null.");
		}

		if(StirngUtil.isBlank(this.sender) == true) {
			throw new Exception("sender is null or blank.");
		}

		if(this.receiverList == null || this.receiverList.size() == 0) {
			throw new Exception("receiver list is null or none.");
		}

		if(this.subjectGen == null) {
			throw new Exception("subject template is not set.");
		}

		if(this.bodyGen == null) {
			throw new Exception("body template is not set.");
		}

		// subject/body 생성
		String subject = this.subjectGen.gen(values);
		String body = this.bodyGen.gen(values);
	}
	
	/**
	 * 
	 * 
	 * @param sender
	 * @return
	 */
	public MailSender setSender(String sender) throws Exception {
		this.sender = sender;
		return this;
	}

	/**
	 *
	 *
	 * @param receiveerAry
	 * @return
	 */
	public MailSender addReceiver(String... receiverAry) {

		if(receiverAry == null || receiverAry.length == 0) {
			return this;
		}

		for(String receiver: receiverAry) {
			this.receiverList.add(receiver);
		}
		
		return this;
	}

	public MailSender addReceiver(List<String> receiverList) {
		
		if(receiverList == null || receiverList.size() == 0) {
			return this;
		}

		this.receiverList.addAll(receiverList);

		return this;
	}

	public MailSender setSubjectTemplate(String subjectTemplate) throws Exception {
		this.subjectGen = TextGen.compile(subjectTemplate);
		return this;
	}

	public MailSender setBodyTemplate(String bodyTemplate) throws Exception {
		this.bodyGen = TextGen.compile(bodyTemplate);
		return this;
	}

	public MailSender addFile(File... attachFileAry) {

		if(attachFileAry == null || attachFileAry.length == 0) {
			return this;
		}

		for(File attachFile: attachFileAry) {
			this.attach.add(attachFile);
		}
		
		return this;
	}

	public MailSender addFile(List<String> attachFileList) {
		
		if(attachFileList == null || attachFileList.size() == 0) {
			return this;
		}

		this.attachFileList.addAll(attachFileList);

		return this;
	}
}
