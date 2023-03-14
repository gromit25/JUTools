package com.jutools.publish.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.press.formatter.FormatterException;
import com.jutools.publish.formatter.flow.BasicFlowFormatter;

/**
 * Workbook의 하위 컴포넌트 Formatter의 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractWorkbookComponentFormatter extends BasicFlowFormatter {
	
	/**
	 * excel의 workbook에 출력작업 수행
	 * @param out 출력 스트림
	 * @param charset 출력시 사용할 character set
	 * @param values value container
	 */
	protected abstract void formatExcel(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException;
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {

		try {
			this.formatExcel(out, charset, values);
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
		
	}
}
