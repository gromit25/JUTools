package com.jutools.publish.formatter.flow;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.olexp.OLExp;
import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * while formatter
 * while formatter는 판별식(exp)의 참이면, List를 반복수행 함
 * 하나의 FlowFormatter를 여러번 실행시키는 개념임
 * 
 * exp 속성 : 판별식
 *
 * ex)
 * <while exp="expression">...</while>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="while")
public class WhileFormatter extends AbstractFlowComponentFormatter {
	
	/** while문 수행 여부를 확인 하기 위한 스크립트 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="test", mandatory=true)
	private OLExp exp;
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		try {
			
			// 설정된 조건이 TRUE 이면,
			// while문을 계속 수행함
			while(this.getExp().execute(values).pop(Boolean.class) == true) {
				this.getBasicFlowFormatter().format(out, charset, values);
			}
			
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}
}
