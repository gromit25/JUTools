package com.jutools;

import static com.jutools.CipherUtil.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * CipherUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class CipherUtilTest {
	
	@Test
	public void testEncryptSHA256_1() {
		
		try {
			
			String msg = "Hello World";
			String encryptedMsg = SHAUtil.encrypt256(msg);
			
			assertEquals(
				"pZGm1Av0IEBKARczz7exkNYsZb8LzaMrV7J32a2fFG4=",
				encryptedMsg
			);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	public void testEncryptSHA512_1() {
		
		try {
			
			String msg = "Hello World";
			String encryptedMsg = SHAUtil.encrypt512(msg);
			
			assertEquals(
				"LHT9F+2v2A6ER7DUZ0HuJDt+t03SFJoKsbkkb7MDgvJ+hT2FhXGeDmfL2g2qj1FnEGRhXWRa4nrLFb+xRH9Fmw==",
				encryptedMsg
			);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testEncryptDecryptAES1() {
		
		try {
			
			String key = AESUtil.gen128Key();
			String msg = "Hello World";
			
			String encryptedMsg = AESUtil.encrypt(msg, key);
			String decryptedMsg = AESUtil.decrypt(encryptedMsg, key);
			
			assertEquals(msg, decryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testEncryptDecryptAES2() {
		
		try {
			
			// key는 16 byte 이어야 함
			String key = AESUtil.gen128Key();
			String msg = "테스트 입니다.";
			
			String encryptedMsg = AESUtil.encrypt(msg, key);
			String decryptedMsg = AESUtil.decrypt(encryptedMsg, key);
			
			assertEquals(msg, decryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
}
