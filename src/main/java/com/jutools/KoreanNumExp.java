package com.jutools;

import java.util.List;
import java.util.Stack;

import com.jutools.parserfw.ExpReader;
import com.jutools.parserfw.TreeNode;

import lombok.Getter;

/**
 * 숫자 한글 표기 클래스
 * 
 * @author jmsohn
 */
public class KoreanNumExp {
	
	/** 숫자 0의 한글 표현 */
	private static String ZERO_KOREAN_EXP = "영";
	/** 음수 의 한글 표현 */
	private static String MINUS_KOREAN_EXP = "마이너스 ";
	
	/**
	 * 한글 숫자 1~9
	 * 
	 * @author jmsohn
	 */
	private enum KoreanNum {
		
		일(1L),
		이(2L),
		삼(3L),
		사(4L),  
		오(5L),
		육(6L),
		칠(7L),
		팔(8L),
		구(9L);
		
		//--------------
		
		/** 한글 숫자에 대응되는 실제 숫자 */
		@Getter
		private long n;
		
		/**
		 * 생성자
		 * 
		 * @param n 숫자
		 */
		KoreanNum(long n) {
			this.n = n;
		}
		
		/**
		 * 
		 * @param keyCh
		 * @return
		 */
		static KoreanNum get(char keyCh) throws Exception {
			
			//
			String key = new String(new char[] {keyCh});
			for(KoreanNum value: KoreanNum.values()) {
				
				if(key.equals(value.name()) == true) {
					return value;
				}
			}
			
			return null;
		}
	}
	
	/**
	 * 천단위
	 * 
	 * @author jmsohn
	 */
	private enum KoreanThousandUnit {
		
		십(10L),
		백(100L),
		천(1000L);
		
		//--------------
		
		@Getter
		long factor;
		
		/**
		 * 생성자 
		 * 
		 * @param factor
		 */
		KoreanThousandUnit(long factor) {
			this.factor = factor;
		}
		
		/**
		 * 
		 * @param keyCh
		 * @return
		 */
		static KoreanThousandUnit get(char keyCh) throws Exception {
			
			//
			String key = new String(new char[] {keyCh});
			for(KoreanThousandUnit value: KoreanThousandUnit.values()) {
				
				if(key.equals(value.name()) == true) {
					return value;
				}
			}
			
			return null;
		}
	}
	
	/**
	 * 만단위
	 * 
	 * @author jmsohn
	 */
	private enum KoreanTenThousandUnit {
		
		만(10000L),
		억(10000L*10000L),
		조(10000L*10000L*10000L),
		경(10000L*10000L*10000L*10000L); // long 값의 최대 범위까지
		
		//--------------
		
		@Getter
		long factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanTenThousandUnit(long factor) {
			this.factor = factor;
		}
		
		/**
		 * 
		 * @param keyCh
		 * @return
		 */
		static KoreanTenThousandUnit get(char keyCh) throws Exception {
			
			//
			String key = new String(new char[] {keyCh});
			for(KoreanTenThousandUnit value: KoreanTenThousandUnit.values()) {
				
				if(key.equals(value.name()) == true) {
					return value;
				}
			}
			
			return null;
		}
	}
	
	/**
	 * 숫자 위치(pos)의 숫자(n)의 천단위 한글 표현을 반환<br>
	 * 숫자 위치는 0부터 시작임<br>
	 * 예를 들어) 123의 1의 pos는 2, 3의 pos 는 0임
	 * 
	 * @param pos 숫자 자리 위치
	 * @param ch 특정 위치의 숫자의 문자
	 * @return 변환된 한글 표현
	 */
	private static String makeKoreanExpByThousand(int pos, char ch) throws Exception {
		
		// 입력값 검증
		if(pos < 0 || pos > 20) {
			throw new Exception("pos must be between 0 to 20:" + pos);
		}
		
		// 만일 숫자가 '0' 이면, 빈문자열을 반환
		if(ch == '0') {
			return ""; 
		}
		
		// 특정 위치의 숫자의 문자를 숫자로 변환
		int n = ch - '0';
		if(n < 0 || n > 9) {
			throw new Exception("ch must be between '0' to '9':" + ch);
		}
		
		// 한글 표현 변수
		StringBuilder koreanExp = new StringBuilder("");
		
		// 만 이하 단위의 위치
		int unitPos = pos % 4;
		
		// 숫자의 한글 표현을 추가
		// 단, 천,백,십에는 일(1)을 추가하지 않음
		// -> 일천(X), 천(O)
		if(n != 1 || unitPos == 0) {
			koreanExp.append((KoreanNum.values()[n-1]).name());
		}
		
		// 만 이하 단위 표현을 가져옴
		if(unitPos != 0) {
			koreanExp.append((KoreanThousandUnit.values()[unitPos - 1]).name());
		}
		
		return koreanExp.toString();
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"<br>
	 *     n: 1500010, space: " " -> "백오십만 십"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @param space 만단위 띄어쓸 문자열
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(long n, String space) throws Exception {
		
		// 값이 0 일 경우,
		if(n == 0) {
			return ZERO_KOREAN_EXP;
		}
		
		// 만단위 띄어쓰기가 null 일 경우, 스페이스("")로 치환
		if(space == null) {
			space = "";
		}
		
		// 한글 표현식을 담을 변수
		StringBuilder koreanExp = new StringBuilder("");
		
		// 음수 부호 읽기 추가
		if(n < 0) {
			koreanExp.append(MINUS_KOREAN_EXP);
		}
		
		// 양수로 변환(abs), 음수라면 sign이 -1 이 되기 때문에 음수 * -1 이 되어 양수가 됨
		// 한글로 변환하기 위함
		n = MathUtil.sign(n) * n;
		
		// 문자열로 변환하여 오른쪽부터 숫자 위치에 따라 변환함
		// ex) "123" 일 경우, 첫번째 '1'과 위치 2을 "일백" 으로 변환 
		String nStr = Long.toString(n);
		int length = nStr.length();
		
		for(int pos = 0; pos < length; pos++) {
			
			// 숫자 자리 위치, ex) 123 의 1은 2번째 자리임
			int digitPos = length - 1 - pos;
			
			// 천단위로 변환 추가
			koreanExp.append(makeKoreanExpByThousand(digitPos , nStr.charAt(pos)));
			
			// 만단위 표현 추가
			if(digitPos % 4 == 0) {
				
				int tenThousandUnitPos = digitPos / 4;
				if(tenThousandUnitPos != 0) {
					koreanExp
						.append((KoreanTenThousandUnit.values()[tenThousandUnitPos - 1]).name())
						.append(space);
				}
			}
		} // End of for pos
		
		return koreanExp.toString();
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(long n) throws Exception {
		return toKorean(n, "");
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"<br>
	 *     n: 1500010, space: " " -> "백오십만 십"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @param space 만단위 띄어쓸 문자열
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(int n, String space) throws Exception {
		return toKorean((long)n, space);
	}
	
	/**
	 * 숫자를 한글 표현으로 변환하여 출력<br>
	 * ex) n: 56789, space: "" -> "오만육천칠백팔십구"
	 * 
	 * @param n 한글 표현으로 변환할 숫자 
	 * @return 주어진 숫자의 한글 표현
	 */
	public static String toKorean(int n) throws Exception {
		return toKorean(n, "");
	}
	
	//---------------------------
	
	/**
	 * 주어진 문자열을 숫자로 변환
	 * ex) n: "오만육천칠백팔십구", space: "" -> 56789<br>
	 *     n: "백오십만 십", space: " " -> 1500010 
	 * 
	 * @param koreanExp 변환할 숫자
	 * @param space 만단위 띄어쓴 문자열 
	 * @return 변환된 숫자
	 */
	public static long toLong(String koreanExp, String space) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isBlank(koreanExp) == true) {
			throw new Exception("koreanExp is blank.");
		}
		
		// 문자열 양쪽의 공백 제거
		koreanExp = koreanExp.trim();
		
		// space가 null일 경우, 빈문자열("") 로 치환
		if(space == null) {
			space = "";
		}
		
		// 영 이면 0을 반환
		if(koreanExp.equals(ZERO_KOREAN_EXP) == true) {
			return 0L;
		}
		
		//
		TreeNode<AbstractNode> root = parseKoreanExp(koreanExp);
		
		//
		List<AbstractNode> nodes =  root.travelPostOrder();
		Stack<Long> stack = new Stack<>();
		
		for(AbstractNode node: nodes) {
			node.execute(stack);
		}
		
		return stack.pop();
	}
	
	//---------------------------
	/* 문자열을 숫자로 변환하기 위해 파싱 트리를 사용 */
	/* 파싱 트리 관련 클래스 선언               */
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	static abstract class AbstractNode {
		abstract void execute(Stack<Long> stack);
	}

	/**
	 * 
	 * @author jmsohn
	 */
	static class LOAD extends AbstractNode {
		
		/** */
		private long value;
		
		/**
		 * 생성자
		 * 
		 * @param value
		 */
		LOAD(long value) {
			this.value = value;
		}
		
		@Override
		void execute(Stack<Long> stack) {
			stack.push(this.value);
		}
		
		@Override
		public String toString() {
			return "LOAD " + this.value;
		}
	}
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	static class ADD extends AbstractNode {

		@Override
		void execute(Stack<Long> stack) {
			long p1 = stack.pop();
			long p2 = stack.pop();
			
			stack.push(p1 + p2);
		}
		
		@Override
		public String toString() {
			return "ADD";
		}
	}
	
	/**
	 * 
	 * @author jmsohn
	 */
	static class MUL extends AbstractNode {

		@Override
		void execute(Stack<Long> stack) {
			long p1 = stack.pop();
			long p2 = stack.pop();
			
			stack.push(p1 * p2);
		}
		
		@Override
		public String toString() {
			return "MUL";
		}
	}
	
	/**
	 *
	 * 
	 * @param koreanExp
	 * @return
	 */
	private static TreeNode<AbstractNode> parseKoreanExp(String koreanExp) throws Exception {
		
		//
		ExpReader reader = new ExpReader(koreanExp);
		
		//
		TreeNode<AbstractNode> root = parseTenThousand(reader);
		
		int read = -1;
		while((read = reader.read()) != -1) {
			
			reader.unread(read);
			
			//
			TreeNode<AbstractNode> add = new TreeNode<>(new ADD());
			
			add.addChild(root);
			add.addChild(parseTenThousand(reader));
			
			root = add;
		}
		
		return root;
	}
	
	/**
	 * 
	 * @param reader
	 * @return
	 */
	private static TreeNode<AbstractNode> parseTenThousand(ExpReader reader) throws Exception {
		
		//
		TreeNode<AbstractNode> root = parseThousand(reader);
		
		int read = -1;
		while((read = reader.read()) != -1) {
			
			reader.unread(read);
			
			//
			TreeNode<AbstractNode> node = parseThousand(reader);
			//
			if(node == null) {
				break;
			}
			
			//
			TreeNode<AbstractNode> add = new TreeNode<>(new ADD());
			
			add.addChild(root);
			add.addChild(node);
			
			root = add;
		}
		
		//
		read = reader.read();
		if(read != -1) {
			
			char ch = (char)read;
			KoreanTenThousandUnit tenThousandUnit = KoreanTenThousandUnit.get(ch);
			
			if(tenThousandUnit != null) {
				
				TreeNode<AbstractNode> mul = new TreeNode<>(new MUL());
				
				mul.addChild(root);
				mul.addChild(new TreeNode<>(new LOAD(tenThousandUnit.getFactor())));
				
				root = mul;
			}
		}
		
		return root;
	}
	
	/**
	 * 
	 * @param reader
	 * @return
	 */
	private static TreeNode<AbstractNode> parseThousand(ExpReader reader) throws Exception {
		
		//
		TreeNode<AbstractNode> root = null;
		
		//
		TreeNode<AbstractNode> nNode = null;
		{
			
			int read = reader.read();
			if(read == -1) {
				return null;
			}
			
			//
			char ch = (char)read;
			
			//
			KoreanNum n = KoreanNum.get(ch);
			
			if(n != null) {
				
				nNode = new TreeNode<>(new LOAD(n.getN()));
				root = nNode;
				
			} else {
				reader.unread(read);
			}
		}
		
		//
		TreeNode<AbstractNode> thousandUnitNode = null;
		{
			int read = reader.read();
			if(read == -1) {
				// 만일 이전 숫자 노드 읽은 것이 있으면 숫자 노드를 반환
				// 없을 경우 null 이 반환됨
				return root;
			}

			char ch = (char)read;
			
			//
			KoreanThousandUnit thousandUnit = KoreanThousandUnit.get(ch);
			
			if(thousandUnit != null) {
				
				thousandUnitNode = new TreeNode<>(new LOAD(thousandUnit.getFactor()));
				
				//
				if(nNode == null) {
					nNode = new TreeNode<>();
					nNode.setData(new LOAD(1));
				}
				
				//
				TreeNode<AbstractNode> mul = new TreeNode<>(new MUL());
				
				mul.addChild(nNode);
				mul.addChild(thousandUnitNode);
				
				root = mul;
				
			} else {
				reader.unread(read);
			}
		}
		
		return root;
	}
}
