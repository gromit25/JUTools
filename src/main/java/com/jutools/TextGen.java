package com.jutools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jutools.instructions.AbstractExp;
import com.jutools.instructions.Instruction;
import com.jutools.olexp.parser.BooleanParser;
import com.jutools.parserfw.AbstractParser;

/**
 * 
 * @author jmsohn
 */
public class TextGen {
	
	/**
	 * 
	 * @author jmsohn
	 */
	private static interface TextElement {
		
		/**
		 * 
		 * @param values
		 * @return
		 */
		public String getText(Map<String, ?> values) throws Exception;
	}
	
	/**
	 * Text 내의 표현식 처리를 위한 클래스
	 * ex) Hello, ${name} 에서 ${name}에 대한 처리
	 * 
	 * @author jmsohn
	 */
	private static class ExpElement extends AbstractExp implements TextElement{
		
		/**
		 * 생성자
		 */
		private ExpElement(String exp) throws Exception {
			super(exp);
		}

		@Override
		protected AbstractParser<Instruction> getRootParser() throws Exception {
			return new BooleanParser();
		}
		
		/**
		 * 생성 메소드
		 * 
		 * @param exp 명령어 문자열
		 * @return 생성된 명령어 처리 클래스
		 */
		public static ExpElement compile(String exp) throws Exception {
			return new ExpElement(exp);
		}
		
		@Override
		public String getText(Map<String, ?> values) throws Exception {
			return this.execute(values).pop(String.class);
		}
	}
	
	/**
	 * 
	 * @author jmsohn
	 */
	private static class StringElement implements TextElement {
		
		/** */
		private String textStr;
		
		/**
		 * 생성자
		 * 
		 * @param textStr
		 */
		StringElement(String textStr) {
			this.textStr = textStr;
		}

		@Override
		public String getText(Map<String, ?> values) throws Exception {
			return this.textStr;
		}
	}
	
	/** */
	private List<TextElement> elements;
	
	/**
	 * 
	 * @param formatText
	 */
	private TextGen(String formatText) throws Exception {
		this.parse(formatText);
	}
	
	/**
	 * 
	 * 
	 * @param formatText
	 * @return
	 */
	public static TextGen compile(String formatText) throws Exception {
		return new TextGen(formatText);
	}
	
	/**
	 * 
	 * 
	 * @param formatText
	 */
	private void parse(String formatText) throws Exception {
		
		// 
		this.elements = new ArrayList<>();
		
		// ${표현식}의 표현식을 담을 임시 변수
		StringBuilder exp = new StringBuilder();

		// 상태 변수, false: 일반 문자열 상태, true: 표현식 상태
		boolean isExp = false;
		
		// 문자열 시작 인덱스 변수
		int startIndex = 0;
		
		// 한 문자씩 파싱 수행
		int index = 0;
		for (; index < formatText.length(); index++) {

			char ch = formatText.charAt(index);
            
			if (isExp == true) {
				
				// 표현식 상태
				if (ch == '}') {
					// 표현식 종료

					// 표현식을 컴파일하여 저장
					this.elements.add(ExpElement.compile(exp.toString()));
                        
					// 표현식 버퍼 초기화
					exp.setLength(0);

					// 일반 문자열 상태로 상태 변경 및 시작 인덱스 설정 
					isExp = false;
					startIndex = index + 1;
                    
				} else {
					
					// 표현식에 추가
					exp.append(ch);
				}

			} else {
            	
				// 일반 문자열 상태
				if (ch == '$' && formatText.charAt(index+1) == '{' && index+1 < formatText.length()) {
					
					// "${" 가 들어온 경우
					// 현재까지 문자열을 StringElement로 만들어 추가
					if(startIndex < index) {
						this.elements.add(new StringElement(formatText.substring(startIndex, index)));
						startIndex = index;
					}
					
					// 표현식 상태로 변환
					isExp = true;
					index++;
				}
			}
		} // End of for
		
		// 모든 문자열을 파싱을 했는데 문자열이 남아 있는 경우 추가함
		if(startIndex < index) {
			this.elements.add(new StringElement(formatText.substring(startIndex, index)));
		}
	}
	
	/**
	 *
	 * 
	 * @param values
	 * @return
	 */
	public String gen(Map<String, ?> values) throws Exception {
		
		StringBuilder text = new StringBuilder("");
		for(TextElement element: this.elements) {
			text.append(element.getText(values));
		}
		
		return text.toString();
	}
}
