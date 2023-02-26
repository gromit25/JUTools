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
	public static String escape(String str) {
		
		// �Է°� ����
		if(str == null) {
			return null;
		}
		
		// �̽������� ó���� ���ڿ� ���� ����
		StringBuffer buffer = new StringBuffer("");
		
		// �̽������� ó���� ���� ���� ����
		// 0: ���ڿ�, 1: �̽������� ���� 
		int status = 0;
		
		for(int index = 0; index < str.length(); index++) {
			
			char ch = str.charAt(index);
			
			if(status == 0) {
				
				if(ch == '\\') {
					status = 1;
				} else {
					buffer.append(ch);
				}
				
			} else {
				
				if(ch == 't') {
					buffer.append('\t');
				} else if(ch == 'r') {
					buffer.append('\r');
				} else if(ch == 'n') {
					buffer.append('\n');
				} else {
					buffer.append(ch);
				}
				
				status = 0;
			}
		} // End of for
		
		return buffer.toString();
	}
}
