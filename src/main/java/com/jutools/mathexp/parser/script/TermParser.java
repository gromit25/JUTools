package com.jutools.mathexp.parser.script;

import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.parser.AbstractParser;

public class TermParser extends AbstractParser<Instruction> {

	public TermParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return null;
	}
	
	@Override
	protected void init() throws Exception {
		
	}
}
