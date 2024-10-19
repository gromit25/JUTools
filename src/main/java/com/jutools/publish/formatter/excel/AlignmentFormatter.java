package com.jutools.publish.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * alignment formatter
 * cellstyle의 cell내 텍스트 수직 수평 배열 정의하기 위한 formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="alignment")
public class AlignmentFormatter extends AbstractCellStyleComponentFormatter {
	
	/**
	 * 수평 위치(horizontal)
	 * 속성값은 HorizontalAlignment 값의 문자열이어야 함
	 */
	@Getter
	@Setter
	@FormatterAttr(name="horizontal", mandatory=false)
	private HorizontalAlignment horizontal;
	
	/**
	 * 수직 위치(vertical)
	 * 속성값은 VerticalAlignment 값의 문자열이어야 함
	 */
	@Getter
	@Setter
	@FormatterAttr(name="vertical", mandatory=false)
	private VerticalAlignment vertical;
	
	@Override
	protected void formatCellStyle(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		// cell style 획득
		XSSFCellStyle style = this.getParentStyle();
		
		System.out.println("AlignmentFormatter DEBUG 100:" + style.getAlignment() + ", " + style.getVerticalAlignment());
		
		// 수평 위치 값 설정
		if(this.getHorizontal() != null) {
			style.setAlignment(this.getHorizontal());
		}
		
		// 수직 위치 값 설정
		if(this.getVertical() != null) {
			style.setVerticalAlignment(this.getVertical());
		}
		
		System.out.println("AlignmentFormatter DEBUG 200:" + style.getAlignment() + ", " + style.getVerticalAlignment());
	}

}
