package com.jutools.publish.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.olexp.OLExp;
import com.jutools.publish.formatter.Formatter;
import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * print formatter
 * 화면상에 표현식(exp) 수행결과를 출력
 *  
 * ex)
 * <print exp="info.getName()" length="20"/>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="text", tag="print")
public class PrintFormatter extends AbstractTextFormatter {
	
	/** 표현식(exp)의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp", mandatory=true)
	private OLExp exp;
	
	/**
	 * 출력 길이
	 * 출력 길이(옵션) 설정되어 있지 않으면 수행 결과 스트링의 길이 만큼만 출력
	 */
	@Getter
	@Setter
	@FormatterAttr(name="length", mandatory=false)
	private int length = -1;
	
	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter fomatter) throws FormatterException {
		// do nothing
	}

	@Override
	public void formatText(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		// 표현식(exp)을 수행한 후
		// 수행 결과 값을 반환함
		try {
			
			Object result = this.getExp().execute(values).pop(Object.class);
			
			// 수행 결과를 메세지에 설정함
			if(result != null) {
				out.write(
						makeString(result.toString(), this.getLength())
						// 개행만 있고 '|' 이 없을경우,
						// 개행 이후 부분을 최종적으로 잘라내기 때문에
						// '|'으로 변경해 줌
						.replaceAll("\n", "\n|")
						.getBytes(charset));
			} else {
				out.write(makeString("N/A", this.getLength()).getBytes(charset));
			}
			
			out.flush();
			
		} catch(Exception ex) {
			
			// 예외 발생시
			throw new FormatterException(this, ex);
		}
		
	}
	
	/**
	 * print tag에 설정된 형식으로 문자열을 만듦
	 * -> String.format의 %s를 사용하지 않는 이유는
	 *    한글의 경우 2byte이지만 1문자로 계산되어
	 *    실제 원하는 길이가 안맞는 문제 발생
	 *    ex) "한test", "1test" 문자열에 6칸으로 요청하면
	 *        -> "한test ","1test " 이렇게 출력되어 console 화면에 표현되는 길이가 맞지 않음
	 * -> length 가 음수(-)일 경우, 원래 스트링을 반환
	 * 
	 * @param target 길이를 맞출 문자열
	 * @param length 맞출 길이
	 * @return 길이(PrintFormatter.length)가 맞추어진 문자열
	 */
	private static String makeString(String target, int length) {
		
		// taget 문자열의 byte 길이를 가져옴
		int targetLength = 0;
		if(target != null) {
			targetLength = target.getBytes().length;
		}
		
		// 설정해야할 길이가 0 보다 작거나,
		// target 문자열이 더 길 경우,
		// target문자열을 리턴함
		if(length <= 0 || length <= targetLength) {
			return target;
		}
		
		// 추가해야할 space개수를 계산함
		int countOfSpace = length;
		if(target != null) {
			countOfSpace -= targetLength;
			return target + String.format("%" + countOfSpace + "s", "");
		} else {
			return String.format("%" + countOfSpace + "s", "");
		}
		
	}

}
