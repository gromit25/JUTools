package com.jutools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.jutools.xml.XMLArray;
import com.jutools.xml.XMLNode;

public class XMLUtil {
	
	public static XMLArray select(String fileName, String query) throws Exception {
		return select(new File(fileName), query);  
	}
	
	public static XMLArray select(File file, String query) throws Exception {
		return select(new FileInputStream(file), query);
	}
	
	public static XMLArray select(InputStream is, String query) throws Exception {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		
		return select(doc, query);
	}
	
	public static XMLArray select(Document doc, String query) throws Exception {
		
		Element root = doc.getDocumentElement();
		return select(root, query);
	}
	
	public static XMLArray select(Node node, String query) throws Exception {
		
		return new XMLNode(node).select(query);
	}
	
	public static XMLNode getRootNode(String fileName) throws Exception {
		return getRootNode(new File(fileName));
	}
	
	public static XMLNode getRootNode(File file) throws Exception {
		return getRootNode(new FileInputStream(file));
	}
	
	public static XMLNode getRootNode(InputStream is) throws Exception {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		
		return getRootNode(doc);
	}
	
	public static XMLNode getRootNode(Document doc) throws Exception {
		
		Element root = doc.getDocumentElement();
		return new XMLNode(root);
	}

}
