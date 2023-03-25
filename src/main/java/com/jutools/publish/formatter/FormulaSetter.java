package com.jutools.publish.formatter;

import java.lang.reflect.Method;
import java.util.Map;

import com.jutools.olexp.OLExp;

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
	private OLExp formula;
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Method setter;
	
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private Method writer;
	
	/*package*/ FormulaSetter(OLExp formula, Method setter, Method writer) {
		this.setFormula(formula);
		this.setSetter(setter);
		this.setWriter(writer);
	}
	
	/*package*/ FormulaSetter(String formulaScript, Method setter, Method writer) throws Exception {
		this(OLExp.compile(formulaScript), setter, writer);
	}
	
	/*package*/ void setData(Formatter formatter, Map<String, Object> values) throws Exception {
		Object retValue = this.getFormula().execute(values).pop(Object.class);
		this.getSetter().invoke(null, formatter, this.getWriter(), retValue.toString());
	}

}
