package com.jutools.xml;

import java.util.Iterator;
import java.util.Vector;

public class XMLArray implements Iterable<XMLNode> {
	
	private Vector<XMLNode> nodes;
	
	/**
	 * 
	 * @param nodes
	 */
	XMLArray(XMLNode[] nodes) throws Exception {
		
		if(nodes == null) {
			throw new NullPointerException("nodes is null.");
		}
		
		this.nodes = new Vector<>(nodes.length);
		for(XMLNode node: nodes) {
			
			this.nodes.add(node);
		}
	}
	
	/**
	 * 
	 */
	XMLArray() throws Exception {
		this(new XMLNode[] {});
	}

	@Override
	public Iterator<XMLNode> iterator() {
		return this.nodes.iterator();
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public XMLArray select(String query) throws Exception {
		
		XMLArray nodes = new XMLArray();
		
		for(XMLNode node: this.nodes) {
			nodes.addAll(node.select(query));
		}
		
		return nodes;
	}
	
	/**
	 * 
	 * 
	 * @param node
	 */
	void add(XMLNode node) throws Exception {
		this.nodes.add(node);
	}
	
	/**
	 * 
	 * 
	 * @param nodes
	 */
	void addAll(XMLArray nodes) throws Exception {
		this.nodes.addAll(nodes.nodes);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public XMLNode get(int index) throws Exception {
		return this.nodes.get(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public XMLNode getFirst() {
		if(this.nodes == null || this.nodes.size() < 1) {
			return null;
		} else {
			return this.nodes.elementAt(0);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		if(this.nodes == null) {
			return 0;
		} else {
			return this.nodes.size();
		}
	}

}
