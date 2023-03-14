package com.jutools.publish.formatter.flow;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.mathexp.MathExp;
import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

/**
 * 개발 중
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="for")
public class ForFormatter extends AbstractFlowComponentFormatter {
	
	/** for문 initExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="init", mandatory=true)
	private MathExp initExp;

	/** for문 conditionExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="cond", mandatory=true)
	private MathExp conditionExp;
	
	/** for문 stepExp속성의 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="step", mandatory=true)
	private MathExp stepExp;

	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		// for문 수행
		try {
			
			for(
				// for 문 인덱스 초기화 수행
				this.getInitExp().execute(values);
				// for 문 조건문 수행, true 이면 for 문 수행
				this.getConditionExp().execute(values)
					.getResult().getValue(Boolean.class) == true;
				// for 문 인덱스 증가문 수행
				this.getStepExp().execute(values)
			) {
				
				// for 문 내부 플로우 반복 수행
				this.getBasicFlowFormatter().format(out, charset, values);
				
			}
		
		} catch(FormatterException fex) {
			throw fex;
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
