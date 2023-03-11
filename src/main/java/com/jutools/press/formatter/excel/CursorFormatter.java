package com.jutools.press.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.press.formatter.FormatterAttr;
import com.jutools.press.formatter.FormatterException;
import com.jutools.press.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * worksheet의 커서의 정보를 설정하는 Formatter
 * 
 * @author jmsohn
 */
@FormatterSpec(group="excel", tag="cursor")
public class CursorFormatter extends AbstractExcelFormatter {
	
	@Getter
	@Setter
	@FormatterAttr(name="position")
	private RowColumnEval positionExpEval;

	@Override
	protected void formatExcel(OutputStream out, Charset charset, Map<String, Object> values)	throws FormatterException {
		
		try {
			
			// row/column 위치를 계산함
			int rowPosition = this.getPositionExpEval().evalRowValue(values);
			int columnPosition = this.getPositionExpEval().evalColumnValue(values);
			
			// workbook 커서 위치를 설정함
			this.getParent(WorksheetFormatter.class)
				.setCursorPosition(rowPosition, columnPosition);
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
