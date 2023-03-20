package com.jutools.olexp;

import com.jutools.instructions.AbstractExp;
import com.jutools.instructions.Instruction;
import com.jutools.olexp.parser.ComparisonParser;
import com.jutools.parserfw.AbstractParser;

/**
 * 
 * 
 * @author jmsohn
 */
public class OLExp extends AbstractExp {
	
	/**
	 * 
	 */
	protected OLExp(String exp) throws Exception {
		super(exp);
	}

	@Override
	protected AbstractParser<Instruction> getRootParser() throws Exception {
		return new ComparisonParser();
	}
}
