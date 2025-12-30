package com.jutools;

import java.io.File;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;

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
		 * 해시 알고리즘 종류
		 * 
		 * @author jmsohn
		 */
		public static enum HashType {
			 
			SHA_256("SHA-256"),
			SHA_512("SHA-512");

			@Getter
			private String name;

			HashType(String name) {
				this.name = name;
			}
		}

		/**
		 * 문자열을 SHA 단방향 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @param algorithm SHA 암호화 알고리즘
		 * @return 암호화된 문자열
		 */
		public static String encrypt(String text, HashType algorithm) throws Exception {
			
			// 입력값 검증
			if(text == null) {
				throw new Exception("'text' is null.");
			}
	
			// hash 생성 
			MessageDigest digest = MessageDigest.getInstance(algorithm.getName());
			byte[] hashedText = digest.digest(text.getBytes());
	
			// 문자열 변환 후 반환
			return Base64.getEncoder().encodeToString(hashedText);
		}
		
		/**
		 * 문자열을 SHA-256 단방향 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @return
		 */
		public static String encrypt256(String text) throws Exception {
			return encrypt(text, HashType.SHA_256);
		}
	
		/**
		 * 문자열을 SHA-512 단방향 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @return 암호화된 문자열
		 */
		public static String encrypt512(String text) throws Exception {
			return encrypt(text, HashType.SHA_512);
		}
	}
	
	// -----------------------------------

	/**
	 * AES 암복호화 Utility 클래스
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
		public static String gen128Key() throws Exception {
			
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
		public static String gen256Key() throws Exception {
			
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
			byte[] keyBytes = Base64.getDecoder().decode(key.getBytes());
			
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
			
			// 암호화 모듈 생성
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(opmode, secretKey);
			
			return cipher;
		}
		
		/**
		 * AES 암호화된 문자열 반환
		 * 
		 * @param text 암호화할 문자열
		 * @param key 암호화 키(base64 인코딩)
		 * @param charset charset
		 * @return 암호화된 문자열(base64 인코딩)
		 */
		public static String encrypt(String text, String key, Charset charset) throws Exception {
			
			// 입력값 검증
			if(charset == null) {
				throw new Exception("'charset' is null.");
			}
			
			// 암호화 모듈 생성
			Cipher cipher = makeCipher(Cipher.ENCRYPT_MODE, key);
			
			// 암호화 수행
			byte[] encryptedData = cipher.doFinal(text.getBytes(charset));
			
			// 인코딩하여 문자열로 만들어 반환
			return Base64.getEncoder().encodeToString(encryptedData);
		}
		
		/**
		 * AES 암호화된 문자열 반환
		 * 
		 * @param text 암호화할 문자열
		 * @param key 암호화 키(base64 인코딩)
		 * @return 암호화된 문자열(base64 인코딩)
		 */
		public static String encrypt(String text, String key) throws Exception {
			return encrypt(text, key, Charset.defaultCharset());
		}
		
		/**
		 * AES 복호화된 문자열 반환
		 * 
		 * @param text 복호화할 문자열(base64 인코딩)
		 * @param key 복호화 키(base64 인코딩)
		 * @param charset charset
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String text, String key, Charset charset) throws Exception {
			
			// 입력값 검증
			if(StringUtil.isBlank(text) == true) {
				return text;
			}
			
			if(charset == null) {
				throw new Exception("'charset' is null.");
			}
			
			// 암호화 모듈 생성
			Cipher cipher = makeCipher(Cipher.DECRYPT_MODE, key);
			
			// 복호화 수행
			byte[] decryptedText = cipher.doFinal(Base64.getDecoder().decode(text));
			
			// 문자열로 만들어 반환
			return new String(decryptedText, charset);
		}
		
		/**
		 * AES 복호화된 문자열 반환
		 * 
		 * @param text 복호화할 문자열(base64 인코딩)
		 * @param key 복호화 키(base64 인코딩)
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String text, String key) throws Exception {
			return decrypt(text, key, Charset.defaultCharset());
		}
	} // End of AESUtil
	
	// -----------------------------------
	
	/**
	 * 공개키 유틸리티 클래스
	 * 
	 * @author jmsohn
	 */
	public static class PublicKeyUtil { 
	
		/**
		 * 바이트열에서 공개키 객체 생성 
		 * 
		 * @param key 공개키 바이트열
		 * @return 생성된 공개키 객체 
		 */
		public static PublicKey load(byte[] key) throws Exception {
			
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			return keyFactory.generatePublic(keySpec);
		}
		
		/**
		 * 파일에서 공개키를 읽어 객체로 만들어 반환
		 * 
		 * @param keyFile 공개키 파일명
		 * @return 공개키 객체 
		 */
		public static PublicKey loadPEM(File keyFile) throws Exception {

			// 입력값 검증
			if(keyFile == null) {
				throw new IllegalArgumentException("'keyFile' is null.");
			}
			
			if(keyFile.canRead() == false) {
				throw new IllegalArgumentException("can't read key file: " + keyFile.getAbsolutePath());
			}

			// 확장자 검증
			String ext = FileUtil.getExt(keyFile);
			if(ext.equals(".pem") == false) {
				throw new IllegalArgumentException("invalid file extension(.pem): " + keyFile.getAbsolutePath());
			}

			// 파일 읽기
			String read = new String(FileUtil.readAllBytes(keyFile));

			// 헤더, 푸터, 줄바꿈, 공백 제거
			String publicKey = read
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replaceAll("\\s", "")
				.replace("-----END PUBLIC KEY-----", "");

			// pem 파일을 읽어 byte를 가져옴
			return load(publicKey);
		}
	
		/**
		 * base64로 인코딩된 문자열에서 공개키 객체 생성
		 * 
		 * @param keyText 공개키 문자열(base64 인코딩)
		 * @return 생성된 공개키 객체
		 */
		public static PublicKey load(String keyText) throws Exception {
			
			if(StringUtil.isBlank(keyText) == true) {
				throw new IllegalArgumentException("'keyText' is null or blank.");
			}
			
			return load(Base64.getDecoder().decode(keyText));
		}
	
		/**
		 * 공개키로 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @param charset 암호화할 문자열의 캐릭터셋
		 * @param key 공개키 객체
		 * @return 암호화된 문자열
		 */
		public static String encrypt(String text, Charset charset, PublicKey key) throws Exception {
			
			// 입력값 검증
			if(key == null) {
				throw new IllegalArgumentException("'key' is null.");
			}

			if(charset == null) {
				throw new IllegalArgumentException("'charset' is null.");
			}
			
			// 입력 문자열이 null 이거나 blank 이면 text 반환
			if(StringUtil.isBlank(text) == true) {
				return text;
			}
			
			// Cipher 객체 초기화 (알고리즘: RSA, 패딩: PKCS1Padding)
	        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	
	        // 평문 바이트를 암호화
	        return Base64.getEncoder().encodeToString(
	        	cipher.doFinal(text.getBytes(charset))
	        );
		}

		/**
		 * 공개키로 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @param key 공개키 객체
		 * @return 암호화된 문자열
		 */
		public static String encrypt(String text, PublicKey key) throws Exception {
			return encrypt(text, Charset.defaultCharset(), key);
		}
	
		/**
		 * 공개키로 복호화 수행
		 * 
		 * @param text 복호화할 문자열
		 * @param charset 문자열의 캐릭터셋
		 * @param key 공개키 객체
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String text, Charset charset, PublicKey key) throws Exception {

			// 입력값 검증
			if(key == null) {
				throw new IllegalArgumentException("'key' is null.");
			}

			if(charset == null) {
				throw new IllegalArgumentException("'charset' is null.");
			}
			
			// 입력 문자열이 null 이거나 blank 이면 text 반환
			if(StringUtil.isBlank(text) == true) {
				return text;
			}

			// base64 디코딩
			byte[] textBytes = Base64.getDecoder().decode(text);
			
			// Cipher 인스턴스 생성 (RSA 알고리즘 사용)
			Cipher cipher = Cipher.getInstance("RSA");

			// 복호화 모드(DECRYPT_MODE)로 초기화, 공개키 전달
			cipher.init(Cipher.DECRYPT_MODE, key);

			// 복호화 수행
			return new String(cipher.doFinal(textBytes), charset);
		}

		/**
		 * 공개키로 복호화 수행
		 * 
		 * @param text 복호화할 문자열
		 * @param key 공개키 객체
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String text, PublicKey key) throws Exception {
			return decrypt(text, Charset.defaultCharset(), key);
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
		 * 바이트열에서 개인키 객체 생성 
		 * 
		 * @param key 개인키 바이트열
		 * @return 생성된 개인키 객체 
		 */
		public static PrivateKey load(byte[] key) throws Exception {

			// 입력값 검증
			if(key == null) {
				throw new IllegalArgumentException("'key' is null.");
			}
			
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			return keyFactory.generatePrivate(keySpec);
		}
	
		/**
		 * 파일에서 개인키를 읽어 객체로 만들어 반환
		 * 
		 * @param keyFile 개인키 파일명
		 * @return 개인키 객체 
		 */
		public static PrivateKey loadPEM(File keyFile) throws Exception {
			
			// 입력값 검증
			if(keyFile == null) {
				throw new IllegalArgumentException("'keyFile' is null.");
			}
			
			if(keyFile.canRead() == false) {
				throw new IllegalArgumentException("can't read key file: " + keyFile.getAbsolutePath());
			}

			// 확장자 검증
			String ext = FileUtil.getExt(keyFile);
			if(ext.equals(".pem") == false) {
				throw new IllegalArgumentException("invalid file extension(.pem): " + keyFile.getAbsolutePath());
			}

			// 파일 읽기
			String read = new String(FileUtil.readAllBytes(keyFile));

			// 헤더, 푸터, 줄바꿈, 공백 제거
			String privateKey = read
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replaceAll("\\s", "")
				.replace("-----END PRIVATE KEY-----", "");

			// pem 파일을 읽어 byte를 가져옴
			return load(privateKey);
		}


		/**
		 * base64로 인코딩된 문자열에서 개인키 객체 생성
		 * 
		 * @param keyText 개인키 문자열(base64 인코딩)
		 * @return 생성된 개인키 객체
		 */
		public static PrivateKey load(String keyText) throws Exception {
			
			if(StringUtil.isBlank(keyText) == true) {
				throw new IllegalArgumentException("'keyText' is null or blank.");
			}
			
			return load(Base64.getDecoder().decode(keyText));
		}
	

		/**
		 * 개인키로 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @param charset 암호화할 문자열의 캐릭터셋
		 * @param key 개인키 객체
		 * @return 암호화된 문자열
		 */
		public static String encrypt(String text, Charset charset, PrivateKey key) throws Exception {
			
			// Cipher 객체 생성 (RSA 알고리즘 사용)
			Cipher cipher = Cipher.getInstance("RSA");

			// ENCRYPT_MODE로 초기화
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// 암호화 후 Base64 문자열로 변환하여 반환
			return Base64.getEncoder().encodeToString(
				cipher.doFinal(text.getBytes(charset))
			);
		}


		/**
		 * 개인키로 암호화 수행
		 * 
		 * @param text 암호화할 문자열
		 * @param key 개인키 객체
		 * @return 암호화된 문자열
		 */
		public static String encrypt(String text, PrivateKey key) throws Exception {
			return encrypt(text, Charset.defaultCharset(), key);
		}
	
		/**
		 * 개인키로 복호화 수행
		 * 
		 * @param text 복호화할 문자열
		 * @param charset 문자열의 캐릭터셋
		 * @param key 개인키 객체
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String text, PrivateKey key, Charset charset) throws Exception {

			// 입력값 검증
			if(key == null) {
				throw new IllegalArgumentException("'key' is null.");
			}

			if(charset == null) {
				throw new IllegalArgumentException("'charset' is null.");
			}
			
			// 입력 문자열이 null 이거나 blank 이면 text 반환
			if(StringUtil.isBlank(text) == true) {
				return text;
			}

			// base64 디코딩
			byte[] textBytes = Base64.getDecoder().decode(text);
			
			// Cipher 인스턴스 생성 (RSA 알고리즘 사용)
			Cipher cipher = Cipher.getInstance("RSA");

			// 복호화 모드(DECRYPT_MODE)로 초기화, 공개키 전달
			cipher.init(Cipher.DECRYPT_MODE, key);

			// 복호화 수행
			return new String(cipher.doFinal(textBytes), charset);
		}

		/**
		 * 개인키로 복호화 수행
		 * 
		 * @param text 복호화할 문자열
		 * @param key 개인키 객체
		 * @return 복호화된 문자열
		 */
		public static String decrypt(String text, PrivateKey key) throws Exception {
			return decrypt(text, key, Charset.defaultCharset());
		}
	} // End of PrivateKeyUtil 
}
