package com.jutools.mail;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.jutools.StringUtil;
import com.jutools.TextGen;
import com.jutools.TypeUtil;

import jakarta.mail.Authenticator;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
public class MailSender {
	
	/** */
	private Properties props;
	
	/** */
	private String charset = "UTF-8";
	
	/** */
	@Getter
	private InternetAddress sender;
	
	private String password;
	
	/** */
	@Getter
	private List<InternetAddress> receiverList = new Vector<>();
	
	/** */
	private TextGen subjectGen;
	
	/** */
	private TextGen bodyGen;
	
	/** */
	private String bodyMimeType = "text/html; charset=UTF-8";
	
	/** */
	private List<File> attachFileList = new Vector<>();
	
	/**
	 * 
	 * 
	 * @param props
	 * @param password
	 * @param charset
	 */
	MailSender(Properties props, String password, String charset) throws Exception {
		
		if(props == null) {
			throw new IllegalArgumentException("props is null.");
		}

		this.props = props;
		
		if(StringUtil.isBlank(password) == true) {
			throw new IllegalArgumentException("password is null or blank.");
		}
		
		if(StringUtil.isBlank(charset) == false) {
			this.charset = charset;
		}
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

		if(this.sender == null) {
			throw new Exception("sender is null.");
		}
		
		if(StringUtil.isBlank(this.password) == true) {
			throw new Exception("password is blank or null.");
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

		// 메일 발송 ----------
		// 메일 발송 세션 생성
		Session session = Session.getDefaultInstance(
			this.props,
			new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sender.getAddress(), password);
				}
			}
		);

		// 메시지 생성
		MimeMessage message = new MimeMessage(session);

		// 메시지 발신자 설정
		message.setFrom(this.sender);

		// 메시지 수신자 설정
		message.setRecipients(
			RecipientType.TO,
			TypeUtil.toArray(this.receiverList, InternetAddress.class)
		);

		// 메시지 제목 설정
		message.setSubject(
			MimeUtility.encodeText(subject, this.charset, "B")
		);

		// 메시지 내용 설정
		Multipart content = new MimeMultipart();
		
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(body, bodyMimeType);
		
		content.addBodyPart(bodyPart);
		
		message.setContent(content);

		// 메시지 전송
		Transport.send(message);
	}
	
	/**
	 * 
	 * 
	 * @param sender
	 * @return
	 */
	public MailSender setSender(String sender) throws Exception {
		this.sender = new InternetAddress(sender);
		return this;
	}

	/**
	 *
	 *
	 * @param receiveerAry
	 * @return
	 */
	public MailSender addReceiver(String... receiverAry) throws Exception {

		if(receiverAry == null || receiverAry.length == 0) {
			return this;
		}

		for(String receiver: receiverAry) {
			this.receiverList.add(new InternetAddress(receiver));
		}
		
		return this;
	}

	/**
	 * 
	 * 
	 * @param receiverList
	 * @return
	 */
	public MailSender addReceiver(List<String> receiverList) throws Exception {
		
		if(receiverList == null || receiverList.size() == 0) {
			return this;
		}

		for(String receiver: receiverList) {
			this.receiverList.add(new InternetAddress(receiver));
		}

		return this;
	}

	/**
	 * 
	 * 
	 * @param subjectTemplate
	 * @return
	 */
	public MailSender setSubjectTemplate(String subjectTemplate) throws Exception {
		this.subjectGen = TextGen.compile(subjectTemplate);
		return this;
	}

	/**
	 * 
	 * 
	 * @param bodyTemplate
	 * @return
	 */
	public MailSender setBodyTemplate(String bodyTemplate) throws Exception {
		this.bodyGen = TextGen.compile(bodyTemplate);
		return this;
	}

	/**
	 * 
	 * 
	 * @param attachFileAry
	 * @return
	 */
	public MailSender addFile(File... attachFileAry) {

		if(attachFileAry == null || attachFileAry.length == 0) {
			return this;
		}

		for(File attachFile: attachFileAry) {
			this.attachFileList.add(attachFile);
		}
		
		return this;
	}

	/**
	 * 
	 * 
	 * @param attachFileList
	 * @return
	 */
	public MailSender addFile(List<File> attachFileList) {
		
		if(attachFileList == null || attachFileList.size() == 0) {
			return this;
		}

		this.attachFileList.addAll(attachFileList);

		return this;
	}
}
