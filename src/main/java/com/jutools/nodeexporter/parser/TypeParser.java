package com.jutools.nodeexporter.parser;

import com.jutools.nodeexporter.instructions.MAKE_METRIC;
import com.jutools.nodeexporter.instructions.NOP;
import com.jutools.nodeexporter.instructions.NodeMetricInstruction;
import com.jutools.nodeexporter.instructions.SET_VALUE;
import com.jutools.script.parserfw.AbstractParser;
import com.jutools.script.parserfw.EndStatusType;
import com.jutools.script.parserfw.TransferBuilder;
import com.jutools.script.parserfw.TransferEventHandler;
import com.jutools.script.parserfw.TreeNode;

/**
 * 
 * 
 * @author jmsohn
 */
public class TypeParser extends AbstractParser<NodeMetricInstruction> {
	
	/** */
	private StringBuffer type;
	/** */
	private StringBuffer value;

	/**
	 * 생성자
	 */
	public TypeParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 변수 초기화 및 루트노드 설정
		this.type = new StringBuffer();
		this.value = new StringBuffer();
		
		this.setNodeData(new NOP());
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add("A-Za-z0-9_", "TYPE")
				.add("^A-Za-z0-9_", "ERROR")
				.build());
		
		this.putTransferMap("TYPE", new TransferBuilder()
				.add("A-Za-z0-9_", "TYPE")
				.add("{", "ATTRS", -1)
				.add(" ", "VALUE")
				.add("^A-Za-z0-9_{ ", "ERROR")
				.build());
		
		this.putTransferMap("ATTRS", new TransferBuilder()
				.add(" ", "VALUE")
				.add("^ ", "ERROR")
				.build());
		
		this.putTransferMap("VALUE", new TransferBuilder()
				.add(".", "VALUE")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("VALUE");
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	@TransferEventHandler(
			source={"START", "TYPE"},
			target="TYPE"
	)
	public void handleType(Event event) throws Exception {
		this.type.append(event.getCh());
	}
	
	@TransferEventHandler(
			source="TYPE",
			target={"ATTRS", "VALUE_START"}
	)
	public void handleAttr(Event event) throws Exception {
		
		//
		this.addChildData(new MAKE_METRIC(this.type.toString()));
		
		//
		if(event.getTarget().equals("ATTRS") == true) {
			TreeNode<NodeMetricInstruction> attrsNode = new AttrParser().parse(event.getReader());
			this.addChild(attrsNode);
		}
	}
	
	@TransferEventHandler(
			source="VALUE",
			target="VALUE"
	)
	public void handleValue(Event event) throws Exception {
		this.value.append(event.getCh());
	}
	
	@Override
	public void exit() throws Exception {
		this.addChildData(new SET_VALUE(this.value.toString()));
	}
}
