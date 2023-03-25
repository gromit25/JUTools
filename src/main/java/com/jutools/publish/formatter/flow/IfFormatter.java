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
 * if formatter
 * if formatter는 판별식(exp)의 참/거짓을 판별하여, 
 * 참일 경우에만 if tag 내부의 명령어를 수행함
 * 
 * exp 속성 : 판별식
 *
 * ex)
 * <if exp="expression"></if>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="if")
public class IfFormatter extends AbstractFlowComponentFormatter {
	
	/** if문 수행 여부를 확인 하기 위한 스크립트 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp", mandatory=true)
	private OLExp exp;

	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		try {
			
			// if 문 설정된 script의 수행결과가
			// TRUE 이면, basic flow를 수행함
			Boolean condition = this.getExp().execute(values)
					.pop(Boolean.class);
			
			if(condition == true) {
				this.getBasicFlowFormatter().format(out, charset, values);
			}

		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
