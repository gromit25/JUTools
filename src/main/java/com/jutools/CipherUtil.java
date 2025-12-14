package com.jutools;

import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.jutools.common.OrderType;

/**
 * 암복호화 Utility 클래스
 * 
 * @author jmsohn
 */
public class CipherUtil {
	
	/**
	 * 문자열을 SHA 단방향 암호화 수행
	 * 
	 * @param algorithm SHA 암호화 알고리즘 명
	 * @param str 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	public static String encryptSHA(String algorithm, String str) throws Exception {
		
		// 입력값 검증
		if(str == null) {
			throw new Exception("str is null");
		}

		// hash 생성 
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		byte[] hash = digest.digest(str.getBytes());

		// 문자열 변환 후 반환
		return BytesUtil.bytesToStr(hash, OrderType.ASCEND);
	}
	
	/**
	 * 문자열을 SHA-256 단방향 암호화 수행
	 * 
	 * @param str 암호화할 문자열
	 * @return
	 */
	public static String encryptSHA256(String str) throws Exception {
		return encryptSHA("SHA-256", str);
	}

	/**
	 * 문자열을 SHA-512 단방향 암호화 수행
	 * 
	 * @param str 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	public static String encryptSHA512(String str) throws Exception {
		return encryptSHA("SHA-512", str);
	}
	
	// -----------------------------------

	/**
	 * AES-128 키 생성 반환 메소드
	 * 
	 * @return 생성된 AES 키
	 */
	public static String genAES128Key() throws Exception {
		
		// 키 생성
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128); // 128비트 AES 키 생성
		SecretKey key = keyGen.generateKey();

		// 키를 문자열로 변환하여 반환
		byte[] keyBytes = key.getEncoded();
		return BytesUtil.bytesToStr(keyBytes, OrderType.ASCEND);
	}
	
	/**
	 * AES-256 키 생성 반환 메소드
	 * 
	 * @return 생성된 AES 키
	 */
	public static String genAES256Key() throws Exception {
		
		// 키 생성
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256); // 256비트 AES 키 생성
		SecretKey key = keyGen.generateKey();

		// 키를 문자열로 변환하여 반환
		byte[] keyBytes = key.getEncoded();
		return BytesUtil.bytesToStr(keyBytes, OrderType.ASCEND);
	}
	
	/**
	 * 암호화 모듈 생성 메소드
	 * 
	 * @param opmode 생성 모드
	 * @param key 암호화 키(32자리 문자열 - genAESKey 메소드를 통해 생성할 수 있음)
	 * @return 암호화 모듈
	 */
	private static Cipher makeCipher(int opmode, String key) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isEmpty(key) == true) {
			throw new Exception("key is empty");
		}
		
		if(key.length() != 32 && key.length() != 64) {
			throw new Exception("key str length must be 32 or 64");
		}
		
		// 암호화 키 생성
		byte[] keyBytes = new byte[key.length()/2];
		
		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
		
		// 암호화 모듈 생성
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(opmode, secretKey);
		
		return cipher;
	}
	
	/**
	 * AES 암호화 메소드
	 * 
	 * @param key 암호화 키(32자리 문자열 - genAESKey 메소드를 통해 생성할 수 있음)
	 * @param cs charset
	 * @param str 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	public static String encryptAES(String key, Charset cs, String str) throws Exception {
		
		// 입력값 검증
		if(cs == null) {
			throw new Exception("charset is null");
		}
		
		// 암호화 모듈 생성
		Cipher cipher = makeCipher(Cipher.ENCRYPT_MODE, key);
		
		// 암호화 수행
		byte[] encryptedData = cipher.doFinal(str.getBytes(cs));
		
		// 인코딩하여 문자열로 만들어 반환
		return BytesUtil.bytesToStr(encryptedData, OrderType.ASCEND);
	}
	
	/**
	 * AES 암호화 메소드
	 * 
	 * @param key 암호화 키(32자리 문자열 - genAESKey 메소드를 통해 생성할 수 있음)
	 * @param str 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	public static String encryptAES(String key, String str) throws Exception {
		return encryptAES(key, Charset.defaultCharset(), str);
	}
	
	/**
	 * AES 복호화 메소드
	 * 
	 * @param key 암호화 키(32자리 문자열 - genAESKey 메소드를 통해 생성할 수 있음)
	 * @param cs charset
	 * @param str 암호화된 문자열
	 * @return 복호화된 문자열
	 */
	public static String decryptAES(String key, Charset cs, String str) throws Exception {
		
		// 입력값 검증
		if(cs == null) {
			throw new Exception("charset is null");
		}
		
		// 암호화 모듈 생성
		Cipher cipher = makeCipher(Cipher.DECRYPT_MODE, key);
		
		// 복호화 수행
		byte[] decryptedData = cipher.doFinal(BytesUtil.strToBytes(str, OrderType.ASCEND));
		
		// 문자열로 만들어 반환
		return new String(decryptedData, cs);
	}
	
	/**
	 * AES 복호화 메소드
	 * 
	 * @param key 암호화 키(32자리 문자열 - genAESKey 메소드를 통해 생성할 수 있음)
	 * @param str 암호화된 문자열
	 * @return 복호화된 문자열
	 */
	public static String decryptAES(String key, String str) throws Exception {
		return decryptAES(key, Charset.defaultCharset(), str);
	}
	
	// -----------------------------------
	
	/**
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public static PublicKey loadPublicKey(byte[] key) throws Exception {
		
	}
	
	/**
	 * 
	 * 
	 * @param keyFile
	 * @return
	 */
	public static PublicKey loadPublicKey(File keyFile) throws Exception {
		
	}

	/**
	 * 
	 * 
	 * @param keyFileName
	 * @return
	 */
	public static PublicKey loadPublicKey(String keyFileName) throws Exception {
		
	}

	/**
	 * 
	 * 
	 * @param key
	 * @param str
	 * @return
	 */
	public static String encryptPublicKey(PublicKey key, String str) throws Exception {
		
		// Cipher 객체 초기화 (알고리즘: RSA, 패딩: PKCS1Padding)
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // 평문 바이트를 암호화
        return cipher.doFinal(str.getBytes("UTF-8"));
	}

	/**
	 * 
	 * 
	 * @param key
	 * @param str
	 * @return
	 */
	public static String decryptPublicKey(PublicKey key, String str) throws Exception {
		
	}
	
	// -----------------------------------

	/**
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public static PrivateKey loadPrivateKey(byte[] key) throws Exception {
		
	}

	/**
	 * 
	 * 
	 * @param keyFile
	 * @return
	 */
	public static PrivateKey loadPrivateKey(File keyFile) throws Exception {
		
	}

	/**
	 * 
	 * 
	 * @param keyFileName
	 * @return
	 */
	public static PrivateKey loadPrivateKey(String keyFileName) throws Exception {
		
	}

	/**
	 * 
	 * 
	 * @param key
	 * @param str
	 * @return
	 */
	public static String encryptPrivateKey(PrivateKey key, String str) throws Exception {
		
	}

	/**
	 * 
	 * 
	 * @param key
	 * @param str
	 * @return
	 */
	public static String decryptPrivateKey(PrivateKey key, String str) throws Exception {
		
	}
}
