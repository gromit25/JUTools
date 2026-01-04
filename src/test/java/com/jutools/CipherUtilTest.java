package com.jutools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;

import com.jutools.CipherUtil.AESUtil;
import com.jutools.CipherUtil.PrivateKeyUtil;
import com.jutools.CipherUtil.PublicKeyUtil;
import com.jutools.CipherUtil.SHAUtil;

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
	
	@Test
	public void testPKIEncryptDecrypt() {
		
		try {
			
			String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgnHKGCyKGNWSBe/3W5D9kArFOoq6cPvqGiHeZ16ewjE7T6d0S00MUrbsL/eerkzaq9WwTiLKElrJAGhglKZ5IQ4pvcf2fp7TZbB4FBQXnvTk4XegvcHLz3m6c9x+YOqSmP5QyCwRgOhpG0mil4gkv6SILHkITtO5Da6OORq1U5wNvGEHfkMGIRI5BCxtZXCXwnpYXBpnukz3gpm0SLdd1B2zYSm+kjdi+9ZciDFOjQdCmYBOe10lCaNG6dwcEZb+r8d1xY+clt+kek1BxJ8YT9slJwVmT62F9DlK9Y1pcqfKk13s1sEv7kG0c8tc93JE/ZCkQv8Xix+a8ZRzsf/XJAgMBAAECggEADw3FiN16UEH+OEoQMhgVU6ESUAsyJTWcnbIkzlKcRmagQinlWA0HzeRgEnEAnzWee3T4EEymZC8LvgkJbPzy96r89WfQcdUc6lQWluSFooGQihajMJnFlyiouCWhhySCp40bWCVtijy9QcNu85K1cbwpk16JVaIxGDilN9dVX7kGroMCwTK1stPhubuaU9viSvGLrqXdsc5n3sevpwHR2nBJpYeU5xqkXtiD6O6t6O4ZuVJNQL6M+ZbsDPoURPXP+PuyoXUPg9Ajloz6jv5+J6lnIrniSmOvTnGaELrKWRy4i5Ou01SbqxWem8OctV2nYUQeieNa6jAn+/pm/GzLUQKBgQDCHqjXri1hwJOefssv39IcayOXd6sUXEC3BJ2QmoFXvk4ZF8K8IsKoHRJOPXp6sKEWi9rp8Iz7o24jhUwIOuNLgcR/XQlfArwzP+HZx39CiwtS2Od3Or34jvg/VtmBvyq1Y2c5fHAnfDxdEBQV0RnRsvRz5J0lrePL+jwVkA4UEwKBgQDTz0f2795V76JMmb247kAK52W2w/e1QyJkFJC72SI19vtkvE+y5Rysiq3ha9MTln8Ns41kAtEYGoKkQ+pt6CbW5y0pTDZC3O1JrcUk0BE1+9oev+/YuUhYVnW985bGufyH3tofweMbyTw1jmq17+m5wk1gA8USceMuM5XfkWjyMwKBgADuGKz/3qPLQmnN/bc7AUy1jrQZFK7CSmUWFpMsylXbSZCBNqOkHv6jNQU+8ilIU11CSkg4qNRD0jFFYDnh3ljsRD6/+V2FK9WrBq/+brOo6uL4uxMtKt/X/4R+LAidG91GnqX93KOzyq4/d3krSU5h5x3vCHm3vZGqn0MTL54VAoGBAKbGuMB8YNHDrUSpbZS2rGCpkRJWva3RHbw14Ty4HNLnFU1dZVfURT6qt30enIX44zPTpEoSWbej8CgNfgJqYGKohovVAYimUoTIOcnhtxuNolLCCMXaJlfARFd7/MWfXnNRiuikjwGq8yisaeO185AbqTcn4L3ERQB1B9aQtVS3AoGAU/Rxq1cl9+O5ofusuD9tzB/p/Y/fyfHucsdV89R8BqOWsHfWNAv4WRmOnkQmIuouEe62ExgkGi2bdKXUXgvMGa9wil4JsX61NoHsU+6LkR4H1R32YMhnkmlXIArfCQNdIag1YgDPwb3UYKOZljlYZJP6SoA+QrrVEcEAhexY8h4=";
			String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoJxyhgsihjVkgXv91uQ/ZAKxTqKunD76hoh3mdensIxO0+ndEtNDFK27C/3nq5M2qvVsE4iyhJayQBoYJSmeSEOKb3H9n6e02WweBQUF5705OF3oL3By895unPcfmDqkpj+UMgsEYDoaRtJopeIJL+kiCx5CE7TuQ2ujjkatVOcDbxhB35DBiESOQQsbWVwl8J6WFwaZ7pM94KZtEi3XdQds2EpvpI3YvvWXIgxTo0HQpmATntdJQmjRuncHBGW/q/HdcWPnJbfpHpNQcSfGE/bJScFZk+thfQ5SvWNaXKnypNd7NbBL+5BtHPLXPdyRP2QpEL/F4sfmvGUc7H/1yQIDAQAB";
			
			PrivateKey privateKey = PrivateKeyUtil.load(privateKeyStr);
			PublicKey publicKey = PublicKeyUtil.load(publicKeyStr); 

			// 암호화 - Java 내부적으로 암호화 결과는 항상 달라지게 설계되어 있음
			String encText = PublicKeyUtil.encrypt("Hello world.", publicKey);
			
			// 복호화
			String plainText = PrivateKeyUtil.decrypt(encText, privateKey);
			
			// 결과 확인
			assertEquals("Hello world.", plainText);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
