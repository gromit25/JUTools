package com.jutools.filetracker.trimmer;

import com.jutools.script.parser.AbstractParser;
import com.jutools.script.parser.EndStatusType;
import com.jutools.script.parser.TransferBuilder;

/**
 * 
 * 
 * @author jmsohn
 */
class LogfmtParser extends AbstractParser<String> {
	
	/** */
	private StringBuffer buffer = new StringBuffer("");
	
	
	/**
	 * 생성자
	 */
	LogfmtParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t\r", "START")
				.add("\n", "END")
				.add("^ \t\r\n", "KEY")
				.build());
		
		this.putTransferMap("KEY", new TransferBuilder()
				.add("\\=", "SET")
				.add("\n", "ERROR")
				.add("^\\=\n", "KEY")
				.build());
		
		this.putTransferMap("SET", new TransferBuilder()
				.add("\\\"", "STRING")
				.add("0-9", "INTEGER")
				.add("\n", "END")
				.add("^0-9\\\"\n", "VALUE")
				.build());

		this.putTransferMap("STRING", new TransferBuilder()
				.add("^\\\"", "STRING")
				.add("\\\"", "START")
				.build());
		
		this.putTransferMap("INTEGER", new TransferBuilder()
				.add("0-9", "INTEGER")
				.add("\\.", "DOUBLE")
				.add(" \t\r", "START")
				.add("\n", "END")
				.add("^0-9\\. \t\r\n", "VALUE")
				.build());
		
		this.putTransferMap("DOUBLE", new TransferBuilder()
				.add("0-9", "DOUBLE")
				.add(" \t\r", "START")
				.add("\n", "END")
				.add("^0-9 \t\r\n", "VALUE")
				.build());
		
		this.putTransferMap("VALUE", new TransferBuilder()
				.add(" \t\r", "START")
				.add("\n", "END")
				.add("^ \t\r\n", "VALUE")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("START");
		this.putEndStatus("SET");
		this.putEndStatus("INTEGER");
		this.putEndStatus("DOUBLE");
		this.putEndStatus("VALUE");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
}
