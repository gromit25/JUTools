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
 *  메일 발송 객체 클래스
 * 
 * @author jmsohn
 */
public class MailSender {
	
	/** 메일 서버 접속용 설정 프로퍼티 */
	private Properties props;
	
	/** 캐릭터 셋 설정 */
	private String charset = "UTF-8";
	
	/** 발신인 메일 주소 */
	@Getter
	private InternetAddress sender;

	/** 발신인 메일 서버 패스워드 */
	private String password;
	
	/** 수신자 메일 주소 목록 */
	@Getter
	private List<InternetAddress> receiverList = new Vector<>();
	
	/** 제목 생성기 */
	private TextGen subjectGen;
	
	/** 내용 생성기 */
	private TextGen bodyGen;
	
	/** 내용의 MIME Type*/
	private String bodyMimeType = "text/html; charset=UTF-8";
	
	/** 첨무 파일 목록 */
	private List<File> attachFileList = new Vector<>();
	
	/**
	 * 생성자
	 * 
	 * @param props 메일 서버 접속용 설정 프로퍼티
	 * @param sender 메일 송신자
	 * @param password 메일 서버 패스워드
	 * @param charset 캐릭터셋
	 */
	MailSender(Properties props, InternetAddress sender, String password, String charset) throws Exception {

		// 접속 프로퍼티 설정
		if(props == null) {
			throw new IllegalArgumentException("props is null.");
		}

		this.props = props;

		// 발신인 설정
		if(sender == null) {
			throw new IllegalArgumentException("sender is null.");
		}
		this.sender = sender;

		// 패스워드 설정
		if(StringUtil.isBlank(password) == true) {
			throw new IllegalArgumentException("password is null or blank.");
		}
		this.password = password;

		// 캐릭터셋 설정
		if(StringUtil.isBlank(charset) == false) {
			this.charset = charset;
		}
	}

	/**
 	 * 메일 전송 실행
	 *
	 * @param values 변수 컨테이너
	 */
	public MailSender send(Map<String, ?> values) throws Exception {

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

		return this;
	}
	
	/**
	 * 수신자 추가
	 *
	 * @param receiveerAry 수신자 목록
	 * @return 현재 객체
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
	 * 수신자 추가
	 * 
	 * @param receiverList 수신자 목록
	 * @return 현재 객체
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
	 * 제목 템플릿 설정 
	 * 
	 * @param subjectTemplate 제목 템플릿
	 * @return 현재 객체
	 */
	public MailSender setSubjectTemplate(String subjectTemplate) throws Exception {
		this.subjectGen = TextGen.compile(subjectTemplate);
		return this;
	}

	/**
	 * 메시지 내용 템플릿 설정
	 * 
	 * @param bodyTemplate 메시지 내용 템플릿
	 * @return 현재 객체
	 */
	public MailSender setBodyTemplate(String bodyTemplate) throws Exception {
		this.bodyGen = TextGen.compile(bodyTemplate);
		return this;
	}

	/**
	 * 첨부 파일 추가
	 * 
	 * @param attachFileAry 첨부 파일 목록
	 * @return 현재 객체
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
	 * 첨부 파일 추가
	 * 
	 * @param attachFileList 첨부 파일 목록
	 * @return 현재 객체
	 */
	public MailSender addFile(List<File> attachFileList) {
		
		if(attachFileList == null || attachFileList.size() == 0) {
			return this;
		}

		this.attachFileList.addAll(attachFileList);

		return this;
	}
}
