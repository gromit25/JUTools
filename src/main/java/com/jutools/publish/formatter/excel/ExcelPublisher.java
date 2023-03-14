package com.jutools.publish.formatter.excel;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.publish.formatter.FormatterXmlHandler;
import com.jutools.publish.Publisher;

/**
 * excel publisher 클래스
 * 
 * @author jmsohn
 */
public class ExcelPublisher extends Publisher {

	@Override
	protected FormatterXmlHandler createXmlHandler() throws Exception {
		return new ExcelFormatterXmlHandler();
	}

	@Override
	public void publish(OutputStream out, Charset charset, Map<String, Object> values) throws Exception {
		
		if((this.getRootFormatter() instanceof WorkbookFormatter) == false) {
			throw new Exception("root formatter is not WorkbookFormatter:" + this.getRootFormatter().getClass());
		}
		
		// 엑셀 출력 수행
		WorkbookFormatter root = (WorkbookFormatter)this.getRootFormatter();
		root.format(out, charset, values);
	}

}
