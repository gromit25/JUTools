package com.jutools.publish.formatter;

import java.lang.reflect.Method;
import java.util.Map;

import com.jutools.mathexp.MathExp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author jmsohn
 */
/*package*/ class FormulaSetter {
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private MathExp formula;
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Method setter;
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Method writer;
	
	/*package*/ FormulaSetter(MathExp formula, Method setter, Method writer) {
		this.setFormula(formula);
		this.setSetter(setter);
		this.setWriter(writer);
	}
	
	/*package*/ FormulaSetter(String formulaScript, Method setter, Method writer) throws Exception {
		this(MathExp.compile(formulaScript), setter, writer);
	}
	
	/*package*/ void setData(Formatter formatter, Map<String, Object> values) throws Exception {
		Object retValue = this.getFormula().execute(values).getResult();
		this.getSetter().invoke(null, formatter, this.getWriter(), retValue.toString());
	}

}
