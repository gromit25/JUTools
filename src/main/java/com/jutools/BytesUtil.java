package com.jutools;

import java.util.ArrayList;

/**
 * byte array ó�� ���� utility Ŭ����
 * 
 * @author jmsohn
 */
public class BytesUtil {
	
	/**
	 * target Byte�� �� �κа� ������ ���̻� ��ġ ���� ��ȯ
	 * ��ġ�� ��� : true, ��ġ���� ���� ��� : false
	 *
	 * @param target Ȯ�� ��� byte array
	 * @param suffix ���̻� byte array
	 * @return target Byte�� �� �κа� ������ ���̻� ��ġ ����
	 */
	public static boolean endsWith(byte[] target, byte[] suffix) throws Exception {

		// parameter null üũ
		if(target == null) {
			throw new NullPointerException("target array is null.");
		}

		if(suffix == null) {
			throw new NullPointerException("suffix array is null.");
		}

		// target�� ���̻纸�� ���� ��쿡�� �׻� false 
		if(suffix.length > target.length) {
			return false;
		}

		// �񱳸� ������ target�� �� �κ� ��ġ
		int start = target.length - suffix.length;

		// �� Byte array�� index��°�� �ִ� byte�� ������ Ȯ��
		for(int index = 0; index < suffix.length; index++) {
			if(target[start + index] != suffix[index]) {
				return false;
			}
		}

		return true;

	}
    
	/**
	 * �־��� target byte array�� split �ϴ� �޼ҵ�
	 * target byte�� �������� byte�� ���Ͽ�
	 * �����ڰ� ���� ���� ��� �и� ��
	 *
	 * @param target target byte array
	 * @param split ������ byte array
	 * @return �����ڿ� ���� �и��� ��� ���
	 */
	public static ArrayList<byte[]> split(byte[] target, byte[] split) throws Exception {

		// parameter null üũ
		if(target == null) {
			throw new NullPointerException("target array is null.");
		}

		if(split == null) {
			throw new NullPointerException("split array is null.");
		}

		// �����ڿ� ���� �и��� ���
		ArrayList<byte[]> splitedTarget = new ArrayList<>();

		// �����ڿ� �������� �ʾ� �и��� byte
		byte[] pieceBuffer = new byte[target.length];
		int pieceBufferPos = 0;

		// �����ڿ� ������ byte�� �����ص�
		// -> �������� ���ڰ� �Ϻ� ���ԵǾ� ���� ��츦 ����
		byte[] splitBuffer = new byte[split.length];
		int splitBufferPos = 0;

		// �������� ���¸� üũ�� ����.
		// �����ڿ� ������ ��� 1, �ƴ� ��� 0
		int status = 0;
		// ������ ��ġ
		int splitPos = 0;

		for(int index = 0; index < target.length; index++) {

			byte cur = target[index];

			// target�� �������� byte�� ������ ���
			// - splitBuffer�� ����
			// - ���� �񱳸� ���� target�� �������� ��ġ�� ��ĭ �̵�
			// - ���� �����ڰ� ��� ���ԵǾ� ���� ��� splitTarget�� �߰�

			if(cur == split[splitPos]) {

				status = 1; // ���¸� 1�� �ٲ�

				// splitBuffer�� ���� byte ���� �� ��ĭ �̵�
				splitBuffer[splitBufferPos++] = cur;

				// ������ ��ĭ �̵�
				splitPos++;

				// �����ڰ� ��� ���ԵǾ� ���� ���
				// -> ���ݱ��� �и��� byte(pieceBuffer)�� splitTarget�� �߰�
				if(splitPos >= split.length) {

					// pieceBuffer ���� �� �߰�
					byte[] piece = new byte[pieceBufferPos];
					System.arraycopy(pieceBuffer, 0, piece, 0, pieceBufferPos);

					if(pieceBufferPos != 0) {
						splitedTarget.add(piece);
					}

					// �и��� �ʿ��� ���� �ʱ�ȭ
					splitBufferPos = 0;
					pieceBufferPos = 0;
					splitPos = 0;
					status = 0;
				}
	
			} else {

				// target�� �������� byte�� �������� ���� ���
				// - pieceBuffer�� byte�� �߰�
				// - ���°� 1�� ��� �����ڿ� �Ϻ� ���Ե� byte�� �־��� ���̹Ƿ�
				//   splitBuffer�� pieceBuffer�� �߰�

				if(status == 1) {
					// splitBuffer�� ������ pieceBuffer�� �߰���
					System.arraycopy(splitBuffer, 0, pieceBuffer, pieceBufferPos, splitBufferPos);

					// pieceBuffer�� ��ġ�� splitBuffer ��ŭ �̵�
					pieceBufferPos += splitBufferPos;
					splitBufferPos = 0;
				}

				// ���´� �״�� 0
				status = 0;
				splitPos = 0;

				// pieceBuffer�� ���� byte�� �߰��ϰ�
				// pieceBufferPos ��ġ ��ĭ �̵�
				pieceBuffer[pieceBufferPos++] = cur;
			}

		}
    	
		// ���� ó��
		if(pieceBufferPos != 0 || splitBufferPos != 0) {

			// ������� �и��� Byte�� ����
			byte[] piece = new byte[pieceBufferPos + splitBufferPos];
			System.arraycopy(pieceBuffer, 0, piece, 0, pieceBufferPos);

			if(splitBufferPos != 0) {
				System.arraycopy(splitBuffer, 0, piece, pieceBufferPos, splitBufferPos);
			}

			//splitedTarget�� �߰�
			splitedTarget.add(piece);
		}
    	
		// ���� ��� ��ȯ
		return splitedTarget;

	}

}
