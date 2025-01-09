package com.jutools.nodeexporter.parser;

import com.jutools.nodeexporter.instructions.ADD_ATTR;
import com.jutools.nodeexporter.instructions.NOP;
import com.jutools.nodeexporter.instructions.NodeMetricInstruction;
import com.jutools.script.parserfw.AbstractParser;
import com.jutools.script.parserfw.EndStatusType;
import com.jutools.script.parserfw.TransferBuilder;
import com.jutools.script.parserfw.TransferEventHandler;

/**
 * 
 * 
 * @author jmsohn
 */
public class AttrParser extends AbstractParser<NodeMetricInstruction> {
	
	/** */
	private StringBuffer attrName;
	/** */
	private StringBuffer attrValue;

	public AttrParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 변수 초기화 및 루트노드 설정
		this.attrName = new StringBuffer();
		this.attrValue = new StringBuffer();
		
		this.setNodeData(new NOP());
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add("{", "ATTR_NAME")
				.add("^{", "ERROR")
				.build());
		
		this.putTransferMap("ATTR_NAME", new TransferBuilder()
				.add("A-Za-z0-9_", "ATTR_NAME")
				.add("=", "ATTR_VALUE_START")
				.add("^A-Za-z0-9_=", "ERROR")
				.build());
		
		this.putTransferMap("ATTR_VALUE_START", new TransferBuilder()
				.add("\"", "ATTR_VALUE")
				.add("^\"", "ERROR")
				.build());

		this.putTransferMap("ATTR_VALUE", new TransferBuilder()
				.add("^\"", "ATTR_VALUE")
				.add("\"", "ATTR_VALUE_END")
				.build());
		
		this.putTransferMap("ATTR_VALUE_END", new TransferBuilder()
				.add(",", "ATTR_NAME")
				.add("}", "ATTR_END")
				.add("^,}", "ERROR")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("ATTR_END", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	@TransferEventHandler(
			source="ATTR_NAME",
			target="ATTR_NAME"
	)
	public void handleAttrName(Event event) throws Exception {
		this.attrName.append(event.getCh());
	}
	
	@TransferEventHandler(
			source="ATTR_VALUE",
			target="ATTR_VALUE"
	)
	public void handleAttrValue(Event event) throws Exception {
		this.attrValue.append(event.getCh());
	}
	
	@TransferEventHandler(
			source="ATTR_VALUE",
			target="ATTR_VALUE_END"
	)
	public void handleAttrValueEnd(Event event) throws Exception {
		
		this.addChildData(
				new ADD_ATTR(
					this.attrName.toString()
					, this.attrValue.toString()
				));
		
		this.attrName.setLength(0);
		this.attrValue.setLength(0);
	}
}
