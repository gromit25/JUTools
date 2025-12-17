package com.jutools;

import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

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
	 * SHA Utility 클래스
	 * 
	 * @author jmsohn
	 */
	public static class SHAUtil {

		/**
		 * 문자열을 SHA 단방향 암호화 수행
		 * 
		 * @param str 암호화할 문자열
		 * @param algorithm SHA 암호화 알고리즘 명
		 * @return 암호화된 문자열
		 */
		public static String encryptSHA(String str, String algorithm) throws Exception {
			
			// 입력값 검증
			if(str == null) {
				throw new Exception("'str' is null.");
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
			return encryptSHA(str, "SHA-256");
		}
	
		/**
		 * 문자열을 SHA-512 단방향 암호화 수행
		 * 
		 * @param str 암호화할 문자열
		 * @return 암호화된 문자열
		 */
		public static String encryptSHA512(String str) throws Exception {
			return encryptSHA(str, "SHA-512");
		}
	}
	
	// -----------------------------------

	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	public static class AESUtil {
	
		/**
		 * AES-128 키 생성 반환<br>
		 * Base64로 인코딩됨
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
			return Base64.getEncoder().encodeToString(keyBytes);
		}
		
		/**
		 * AES-256 키 생성 반환<br>
		 * Base64로 인코딩됨
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
			return Base64.getEncoder().encodeToString(keyBytes);
		}
		
		/**
		 * 암호화 모듈 생성 및 반환
		 * 
		 * @param opmode 생성 모드
		 * @param key 암호화 키(base64 인코딩)
		 * @return 암호화 모듈
		 */
		private static Cipher makeCipher(int opmode, String key) throws Exception {
			
			// 입력값 검증
			if(StringUtil.isEmpty(key) == true) {
				throw new Exception("key is empty");
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
		 * AES 암호화된 문자열 반환
		 * 
		 * @param str 암호화할 문자열
		 * @param key 암호화 키(base64 인코딩)
		 * @param cs charset
		 * @return 암호화된 문자열(base64 인코딩)
		 */
		public static String encrypt(String str, String key, Charset cs) throws Exception {
			
			// 입력값 검증
			if(cs == null) {
				throw new Exception("charset is null");
			}
			
			// 암호화 모듈 생성
			Cipher cipher = makeCipher(Cipher.ENCRYPT_MODE, key);
			
			// 암호화 수행
			byte[] encryptedData = cipher.doFinal(str.getBytes(cs));
			
			// 인코딩하여 문자열로 만들어 반환
			return Base64.getEncoder().encodeToString(encryptedData);
		}
		
		/**
		 * AES 암호화된 문자열 반환
		 * 
		 * @param str 암호화할 문자열
		 * @param key 암호화 키(base64 인코딩)
		 * @return 암호화된 문자열(base64 인코딩)
		 */
		public static String encrypt(String str, String key) throws Exception {
			return encrypt(str, key, Charset.defaultCharset());
		}
		
		/**
		 * AES 복호화된 문자열 반환
		 * 
		 * @param str 복호화할 문자열(base64 인코딩)
		 * @param key 복호화 키(base64 인코딩)
		 * @param cs charset
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String str, String key, Charset cs) throws Exception {
			
			// 입력값 검증
			if(cs == null) {
				throw new Exception("charset is null");
			}
			
			// 암호화 모듈 생성
			Cipher cipher = makeCipher(Cipher.DECRYPT_MODE, key);
			
			// 복호화 수행
			byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(str));
			
			// 문자열로 만들어 반환
			return new String(decryptedData, cs);
		}
		
		/**
		 * AES 복호화된 문자열 반환
		 * 
		 * @param str 복호화할 문자열(base64 인코딩)
		 * @param key 복호화 키(base64 인코딩)
		 * @return 복호화된 문자열
		 */
		public static String decryptAES(String str, String key) throws Exception {
			return decrypt(str, key, Charset.defaultCharset());
		}
	} // End of AESUtil
	
	// -----------------------------------
	
	/**
	 * Public Key 유틸리티 클래스
	 * 
	 * @author jmsohn
	 */
	public static class PublicKeyUtil { 
	
		/**
		 * 
		 * 
		 * @param key
		 * @return
		 */
		public static PublicKey load(byte[] key) throws Exception {
			
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			return keyFactory.generatePublic(keySpec);
		}
		
		/**
		 * 
		 * 
		 * @param keyFile
		 * @return
		 */
		public static PublicKey load(File keyFile) throws Exception {
			
			if(keyFile == null) {
				throw new IllegalArgumentException("'keyFile' is null.");
			}
			
			if(keyFile.canRead() == false) {
				throw new IllegalArgumentException("can't read key file: " + keyFile.getAbsolutePath());
			}
			
			return load(FileUtil.readAllBytes(keyFile));
		}
	
		/**
		 * 
		 * 
		 * @param base64Key
		 * @return
		 */
		public static PublicKey load(String base64Key) throws Exception {
			
			if(StringUtil.isBlank(base64Key) == true) {
				throw new IllegalArgumentException("'base64Key' is null or blank.");
			}
			
			return load(Base64.getDecoder().decode(base64Key));
		}
	
		/**
		 * 
		 * 
		 * @param str 
		 * @param key
		 * @return
		 */
		public static String encrypt(String str, PublicKey key) throws Exception {
			
			// 입력값 검증
			if(key == null) {
				throw new IllegalArgumentException("'key' is null.");
			}
			
			// 입력 문자열이 null 이거나 blank 이면 빈 문자열 반환
			if(StringUtil.isBlank(str) == true) {
				return "";
			}
			
			// Cipher 객체 초기화 (알고리즘: RSA, 패딩: PKCS1Padding)
	        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	
	        // 평문 바이트를 암호화
	        return Base64.getEncoder().encodeToString(
	        	cipher.doFinal(str.getBytes("UTF-8"))
	        );
		}
	
		/**
		 * 
		 * 
		 * @param str
		 * @param key
		 * @return
		 */
		public static String decrypt(String str, PublicKey key) throws Exception {
			
		}
		
	} // End of PublicKeyUtil
	
	// -----------------------------------
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	public static class PrivateKeyUtil {

		/**
		 * 
		 * 
		 * @param key
		 * @return
		 */
		public static PrivateKey load(byte[] key) throws Exception {
			
		}
	
		/**
		 * 
		 * 
		 * @param keyFile
		 * @return
		 */
		public static PrivateKey load(File keyFile) throws Exception {
			
			if(keyFile == null) {
				throw new IllegalArgumentException("'keyFile' is null.");
			}
			
			if(keyFile.canRead() == false) {
				throw new IllegalArgumentException("can't read key file: " + keyFile.getAbsolutePath());
			}
			
			return load(FileUtil.readAllBytes(keyFile));
		}
	
		/**
		 * 
		 * 
		 * @param base64Key
		 * @return
		 */
		public static PrivateKey load(String base64Key) throws Exception {
			
			if(StringUtil.isBlank(base64Key) == true) {
				throw new IllegalArgumentException("'base64Key' is null or blank.");
			}
			
			return load(Base64.getDecoder().decode(base64Key));
		}
	
		/**
		 * 
		 * 
		 * @param str
		 * @param key
		 * @return
		 */
		public static String encrypt(String str, PrivateKey key) throws Exception {
			
		}
	
		/**
		 * 
		 * 
		 * @param str
		 * @param key
		 * @return
		 */
		public static String decrypt(String str, PrivateKey key) throws Exception {
			
		}
		
	} // End of PrivateKeyUtil 
}
