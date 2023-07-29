package com.jutools;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jutools.xml.XMLArray;
import com.jutools.xml.XMLNode;

public class XMLUtilTest {

	@Test
	public void test() throws Exception {
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<bookstore>\r\n"
				+ "  <book category=\"종류1\">\r\n"
				+ "    <title lang=\"en\">Book 1</title>\r\n"
				+ "    <author>Author 1</author>\r\n"
				+ "    <year>2010</year>\r\n"
				+ "    <price>29.99</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"종류1\">\r\n"
				+ "    <title lang=\"kr\">Book 2</title>\r\n"
				+ "    <author>Author 2</author>\r\n"
				+ "    <year>2015</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"종류2\">\r\n"
				+ "    <title lang=\"en\">Book 3</title>\r\n"
				+ "    <author>Author 2</author>\r\n"
				+ "    <year>2015</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "</bookstore>";
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
		Element root = doc.getDocumentElement();
		
		XMLNode rootNode = new XMLNode(root);
		XMLArray books = rootNode.select("book(category='종류1')");
		
		for(XMLNode book: books) {
			
			XMLNode title = book.select("title(lang='kr')").getFirst();
			if(title == null) {
				continue;
			}
			
			System.out.println(title.getTagName());
			System.out.println(title.getAttribute("lang"));
			System.out.println(title.getText());
			
			System.out.println("----------");
			for(String attrName: title.getAttributeNames()) {
				System.out.println(attrName);
			}
		}

	}

}
