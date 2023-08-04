package com.jutools;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.jutools.xml.XMLArray;
import com.jutools.xml.XMLNode;

public class XMLUtilTest {

	@Test
	public void test() throws Exception {
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<bookstore>\r\n"
				+ "  <book category=\"역사\">\r\n"
				+ "    <title lang=\"en\">Historiae</title>\r\n"
				+ "    <author>Author 1</author>\r\n"
				+ "    <year>2001</year>\r\n"
				+ "    <price>29.99</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"역사\">\r\n"
				+ "    <title lang=\"kr\">삼국사기</title>\r\n"
				+ "    <author>김부식</author>\r\n"
				+ "    <year>1145</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"수학\">\r\n"
				+ "    <title lang=\"kr\">공업수학-1</title>\r\n"
				+ "    <author>장길산</author>\r\n"
				+ "    <year>2006</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"수학\">\r\n"
				+ "    <title lang=\"kr\">공업수학-2</title>\r\n"
				+ "    <author>장길산</author>\r\n"
				+ "    <year>2018</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"수학\">\r\n"
				+ "    <title lang=\"kr\">위상 수학</title>\r\n"
				+ "    <author>홍길동</author>\r\n"
				+ "    <year>2010</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"컴퓨터공학\">\r\n"
				+ "    <title lang=\"kr\">오토마타</title>\r\n"
				+ "    <author>손시연</author>\r\n"
				+ "    <year>2023</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "  <book category=\"역사\">\r\n"
				+ "    <title lang=\"kr\">삼국유사</title>\r\n"
				+ "    <author>일'연</author>\r\n"
				+ "    <year>1310</year>\r\n"
				+ "    <price>39.95</price>\r\n"
				+ "  </book>\r\n"
				+ "</bookstore>";
		
		XMLArray books = XMLUtil
				.getRootNode(new ByteArrayInputStream(xml.getBytes()))
				.select("book > author(#text=w'일\\'연')")
				.getParents();
		
		for(XMLNode book: books) {
			
			System.out.println("------------------------------");
			System.out.println(book.selectFirst("title").getText());
			System.out.println(book.selectFirst("author").getText());
			System.out.println(book.selectFirst("year").getText());
		}

	}

}
