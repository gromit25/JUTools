package com.jutools.publish.formatter.flow;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.jutools.publish.formatter.Formatter;
import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;
import com.jutools.script.olexp.OLExp;

import lombok.Getter;
import lombok.Setter;

/**
 * set formatter<br>
 * exp의 표현식을 수행한 값을 value container에 name값을 키로 하여 넣음<br>
 * name 속성 : value container에 설정할 이름<br>
 * exp 속성 : value container에 넣을 값의 표현식<br>
 * <br>
 * 주 용도:<br>
 * value container에 값 설정<br>
 * <br>
 * node.getOs().getCpu().getPercent() 를<br> 
 * &lt;set name="oscpu" exp="node.getOs().getCpu()"/&gt;<br>
 * oscpu.getPercent() 로 사용할 수 있음<br>
 * <br>
 * 사용 형식:<br>
 * &lt;set name="infoName" exp="info.getName()"/&gt;<br>
 * 
 * @author jmsohn
 */
@FormatterSpec(group="flow", tag="set")
public class SetFormatter extends AbstractFlowFormatter {
	
	/** value container에 설정할 이름 */
	@Getter
	@Setter
	@FormatterAttr(name="name", mandatory=true)
	private String name;

	/** value container에 추가할 값을 연산하기 위한 스크립트 Evaluator */
	@Getter
	@Setter
	@FormatterAttr(name="exp", mandatory=true)
	private OLExp exp;

	@Override
	public void addText(String text) throws FormatterException {
		// do nothing
	}

	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		// do nothing
	}

	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		// exp에 설정된 operation을 수행 후 결과를
		// value container에 설정할 이름(SetFormatter.name)으로 넣음
		try {
			values.put(this.getName(), this.getExp().execute(values).pop(Object.class));
		} catch(Exception ex) {
			throw new FormatterException(this, ex);
		}
	}

}
