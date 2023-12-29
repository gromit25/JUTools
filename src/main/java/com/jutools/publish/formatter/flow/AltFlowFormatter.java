package com.jutools.publish.formatter.flow;

import org.xml.sax.Attributes;

import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

/**
 * 대체(alternative) flow formatter<br>
 * foreach 등 대체 flow(List의 0개인 경우 다른 메시지 표시 등)가 있는 경우 사용되며,<br>
 * alt 테그의  내용을 처리함<br>
 * FlowFormatter와 기능 상 완전 동일함  
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="alt")
public class AltFlowFormatter extends AbstractFlowComponentFormatter {

	@Override
	public void setAttributes(Attributes attributes) throws FormatterException {
		//do nothing
	}
}
