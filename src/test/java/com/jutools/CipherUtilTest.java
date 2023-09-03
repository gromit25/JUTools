package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * CipherUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class CipherUtilTest {

	@Test
	public void testEncryptSHA512_1() {
		try {
			
			String msg = "Hello World";
			String encryptedMsg = CipherUtil.encryptSHA512(msg);
			
			System.out.println(encryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testEncryptDecryptAES1() {
		try {
			
			String key = CipherUtil.genAES128Key();
			String msg = "Hello World";
			
			String encryptedMsg = CipherUtil.encryptAES(key, msg);
			String decryptedMsg = CipherUtil.decryptAES(key, encryptedMsg);
			
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
			String key = CipherUtil.genAES128Key();
			String msg = "테스트 입니다.";
			
			String encryptedMsg = CipherUtil.encryptAES(key, msg);
			String decryptedMsg = CipherUtil.decryptAES(key, encryptedMsg);
			
			assertEquals(msg, decryptedMsg);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGenAES128Key() {
		try {
			
			String key = CipherUtil.genAES128Key();
			System.out.println(key);
			
			assertEquals(32, key.length());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testGenAES256Key() {
		try {
			
			String key = CipherUtil.genAES256Key();
			System.out.println(key);
			
			assertEquals(64, key.length());
			
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}
