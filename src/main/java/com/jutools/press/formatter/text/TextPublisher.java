package com.jutools.press.formatter.text;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.press.Publisher;
import com.jutools.press.formatter.FormatterXmlHandler;

/**
 * 텍스트 publisher 클래스
 * 
 * @author jmsohn
 */
public class TextPublisher extends Publisher {

	@Override
	protected FormatterXmlHandler createXmlHandler() throws Exception {
		return new TextFormatterXmlHandler();
	}

	@Override
	public void publish(OutputStream out, Charset charset, Map<String, Object> values) throws Exception {
		//
		try (TextFormatOutputStream textFormatOut = new TextFormatOutputStream(out)) {
			this.getRootFormatter().format(textFormatOut, charset, values);
		}
	}

}
