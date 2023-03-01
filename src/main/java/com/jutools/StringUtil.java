package com.jutools;

/**
 * ���ڿ� ó�� ���� Utility Ŭ����
 * 
 * @author jmsohn
 */
public class StringUtil {
	
	/**
	 * �־��� ���ڿ��� ���� �̽������� ó��
	 * 
	 * @param str �־��� ���ڿ�
	 * @return �̽������� ó���� ���ڿ�
	 */
	public static String escape(String str) throws Exception {
		
		// �Է°� ����
		if(str == null) {
			return null;
		}
		
		// �̽������� ó���� ���ڿ� ����
		StringBuilder escapedStr = new StringBuilder("");
		
		// �����ڵ� �ӽ� ���� ����
		StringBuilder unicodeStr = new StringBuilder(""); 
		
		// �̽������� ó���� ���� ���� ����
		// 0: ���ڿ�, 1: �̽������� ����,
		// 11:�����ڵ� 1��° ����, 12:�����ڵ� 2��° ����, 13:�����ڵ� 3��° ����, 14:�����ڵ� 4��° ���� 
		int status = 0;
		
		for(int index = 0; index < str.length(); index++) {
			
			char ch = str.charAt(index);
			
			if(status == 0) {
				
				if(ch == '\\') {
					status = 1;
				} else {
					escapedStr.append(ch);
				}
				
			} else if(status == 1) {
				
				// ���¸� �Ϲ� ���ڿ� ���·� ����
				// ���� ���¸� �����ϴ� ������ Unicode ���۽� ���°� 10���� �����ϱ� ������
				// �������� ���¸� �����ϸ� �ȵ�
				status = 0;
				
				if(ch == '0') {
					escapedStr.append('\0'); // ASCII 0 �߰�
				} else if(ch == 'b') {
					escapedStr.append('\b');
				} else if(ch == 'f') {
					escapedStr.append('\f');
				} else if(ch == 'n') {
					escapedStr.append('\n');
				} else if(ch == 'r') {
					escapedStr.append('\r');
				} else if(ch == 't') {
					escapedStr.append('\t');
				} else if(ch == 'u') {
					// Unicode ����
					status = 11;
				} else {
					// ���� ��� �ش� ���ڸ� �׳� �߰���
					// ex) \' �ΰ�� '�� �߰�
					escapedStr.append(ch);
				}
				
			} else if(status >= 10 && status <= 14) {
				
				// ch�� 16���� ��(0-9, A-F, a-f) ���� Ȯ��
				if(isHex(ch) == false) {
					throw new Exception("unicode value is invalid:" + ch);
				}
				
				// unicode ���ۿ� ch�߰�
				unicodeStr.append(ch);

				// ���°��� �ϳ� �ø�
				// ex) 10:�����ڵ� ���� -> 11:�����ڵ� 1��° ����
				status++;
				
				// Unicode escape�� ����(status�� 15 �̻�)�Ǹ�
				// Unicode�� �߰��ϰ�, ���¸� �Ϲݹ��ڿ� ���·� ������
				if(status >= 15) {
					
					char unicodeCh = (char)Integer.parseInt(unicodeStr.toString(), 16);
					escapedStr.append(unicodeCh);
					
					unicodeStr.delete(0, unicodeStr.length());
					status = 0;
				}
				
			} else {
				throw new Exception("Unexpected status: " + status);
			}
		} // End of for
		
		return escapedStr.toString();
	}
	
	/**
	 * �־��� ����(ch)�� 16���� ��(0-9, A-F, a-f) ���� Ȯ��
	 * 
	 * @param ch �˻��� ����
	 * @return 16���� �� ����(16���� ���� ��� true, �ƴ� ��� false)
	 */
	public static boolean isHex(char ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}
}
