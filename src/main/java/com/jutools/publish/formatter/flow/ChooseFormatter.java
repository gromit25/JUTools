package com.jutools.publish.formatter.flow;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jutools.publish.formatter.Formatter;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * choose formatter<br>
 * choose 분기 수행,<br>
 * when formatter와 else formatter를 같이 사용함<br>
 * <pre>
 * ex)
 * &lt;choose&gt;
 *   &lt;when test="severity=='info'"&gt;|green&lt;/when&gt;
 *   &lt;when test="severity=='warn'"&gt;|yellow&lt;/when&gt;
 *   &lt;when test="severity=='fatal'"&gt;|red&lt;/when&gt;
 *   &lt;else&gt;|black&lt;/default&gt;
 * &lt;/choose&gt;
 * </pre>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="choose")
public class ChooseFormatter extends AbstractFlowFormatter {
	
	/** when formatter 목록 */
	@Getter(AccessLevel.PRIVATE)
	private List<WhenFormatter> whenFormatterList = new ArrayList<>();
	
	/** else formatter */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ElseFormatter elseFormatter;

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		
		// When Formatter와 Else Formatter 설정
		if((formatter instanceof WhenFormatter) == true ) {
			
			WhenFormatter whenFormatter = (WhenFormatter)formatter;
			this.getWhenFormatterList().add(whenFormatter);
			
		} else if((formatter instanceof ElseFormatter) == true) {
			
			this.setElseFormatter((ElseFormatter)formatter);
		} else {
			throw new FormatterException(this, "unexpected formatter type(not WhenFormatter or ElseFormatter):" + formatter.getClass().getName());
		}
	}

	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		// when formatter 중 test에 설정된 표현식이 true 인 경우
		// 해당 when formatter를 수행하고 반환
		for(WhenFormatter whenFormatter: this.getWhenFormatterList()) {
			if(whenFormatter.test(values) == true) {
				whenFormatter.format(out, charset, values);
				return;
			}
		}
		
		// 매치되는 when formatter가 없는 경우,
		// else formatter를 실행하고 종료
		if(this.getElseFormatter() != null) {
			this.getElseFormatter().format(out, charset, values);
		}
	}
}
