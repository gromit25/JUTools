package com.jutools.publish.formatter.flow;

import java.util.Map;

import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;
import com.jutools.script.olexp.OLExp;

import lombok.Getter;
import lombok.Setter;

/**
 * when formatter<br>
 * choose 내에서 test 조건 만족시 실행
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="when")
public class WhenFormatter extends AbstractFlowComponentFormatter {
	
	/** test 조건 표현식 */
	@Getter
	@Setter
	@FormatterAttr(name="test", mandatory=true)
	private OLExp testExp;
	
	/**
	 * 설정된 test 조건 표현식 실행 후 결과 반환(boolean)
	 * 
	 * @param values value container
	 * @return test 표현식 실행 결과
	 */
	public boolean test(Map<String, Object> values) throws FormatterException {
		try {
			return this.testExp.execute(values).pop(Boolean.class);
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}
}
