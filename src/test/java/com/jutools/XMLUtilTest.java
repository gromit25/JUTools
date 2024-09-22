package com.jutools;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.jutools.xml.XMLArray;
import com.jutools.xml.XMLNode;

/**
 * XMLUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class XMLUtilTest {
	
	private static final String XML_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<bookstore>\r\n"
			+ "  <book category=\"역사\">\r\n"
			+ "    <isbn>0001</isbn>\r\n"
			+ "    <title lang=\"en\">Historiae</title>\r\n"
			+ "    <author>Author 1</author>\r\n"
			+ "    <year>2001</year>\r\n"
			+ "    <price>29.99</price>\r\n"
			+ "  </book>\r\n"
			+ "  <book category=\"역사\">\r\n"
			+ "    <isbn>0002</isbn>\r\n"
			+ "    <title lang=\"kr\">삼국사기</title>\r\n"
			+ "    <author>김부식</author>\r\n"
			+ "    <year>1145</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </book>\r\n"
			+ "  <book category=\"수학\">\r\n"
			+ "    <isbn>0003</isbn>\r\n"
			+ "    <title lang=\"kr\">공업수학-1</title>\r\n"
			+ "    <author>장길산</author>\r\n"
			+ "    <year>2006</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </book>\r\n"
			+ "  <book category=\"수학\">\r\n"
			+ "    <isbn>0004</isbn>\r\n"
			+ "    <title lang=\"kr\">공업수학-2</title>\r\n"
			+ "    <author>장길산</author>\r\n"
			+ "    <year>2018</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </book>\r\n"
			+ "  <book category=\"수학\">\r\n"
			+ "    <isbn>0005</isbn>\r\n"
			+ "    <title lang=\"kr\">위상 수학</title>\r\n"
			+ "    <author>홍길동</author>\r\n"
			+ "    <year>2010</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </book>\r\n"
			+ "  <book category=\"컴퓨터공학\">\r\n"
			+ "    <isbn>0006</isbn>\r\n"
			+ "    <title lang=\"kr\">오토마타</title>\r\n"
			+ "    <author>손시연</author>\r\n"
			+ "    <year>2023</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </book>\r\n"
			+ "  <book category=\"역사\">\r\n"
			+ "    <isbn>0007</isbn>\r\n"
			+ "    <title lang=\"kr\">삼국유사</title>\r\n"
			+ "    <author>일'연</author>\r\n"
			+ "    <year>1310</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </book>\r\n"
			+ "</bookstore>";
	
	private static final String XML_NAMESPACE_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<bookstore>\r\n"
			+ "  <ns:book category=\"역사\">\r\n"
			+ "    <isbn>0001</isbn>\r\n"
			+ "    <title lang=\"en\">Historiae</title>\r\n"
			+ "    <author>Author 1</author>\r\n"
			+ "    <year>2001</year>\r\n"
			+ "    <price>29.99</price>\r\n"
			+ "  </ns:book>\r\n"
			+ "  <ns:book category=\"역사\">\r\n"
			+ "    <isbn>0002</isbn>\r\n"
			+ "    <title lang=\"kr\">삼국사기</title>\r\n"
			+ "    <author>김부식</author>\r\n"
			+ "    <year>1145</year>\r\n"
			+ "    <price>39.95</price>\r\n"
			+ "  </ns:book>\r\n"
			+ "</bookstore>";
	
	private static final String XML_TAIL_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<text>\n"
			+ "hello <p>name</p>!\n"
			+ "today: <p>year</p>-<p>month</p>-<p>day</p>\n"
			+ "this is test.\n"
			+ "</text>";
	
	/**
	 *
	 * 
	 * @param books
	 */
	private static String makeISBNs(XMLArray books) throws Exception {
		
		StringBuilder isbns = new StringBuilder("");
		
		for(XMLNode isbnNode: books.select("book>isbn")) {
			isbns.append(isbnNode.getText()).append(",");
		}
		
		return isbns.toString();
	}
	
	@Test
	public void testSelect1() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book");
		
		assertEquals("0001,0002,0003,0004,0005,0006,0007,", makeISBNs(books));
	}

	@Test
	public void testSelect2() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book(category='역사')");
		
		assertEquals("0001,0002,0007,", makeISBNs(books));
	}
	
	@Test
	public void testSelect3() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book(category=w'역?')");

		assertEquals("0001,0002,0007,", makeISBNs(books));
	}
	
	@Test
	public void testSelect4() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book(category=p'컴.{4}')");

		assertEquals("0006,", makeISBNs(books));
	}
	
	@Test
	public void testSelectArray1() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book[0-2]");

		assertEquals("0001,0002,0003,", makeISBNs(books));
	}
	
	@Test
	public void testSelectArray2() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book[2-5]");

		assertEquals("0003,0004,0005,0006,", makeISBNs(books));
	}
	
	@Test
	public void testSelectArray3() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book[6-30]");

		assertEquals("0007,", makeISBNs(books));
	}
	
	@Test
	public void testSelectArray4() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book[0-2](category='역사')");
		
		assertEquals("0001,0002,", makeISBNs(books));
	}
	
	@Test
	public void testSelectArray5() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book[1](category='역사')");

		assertEquals("0002,", makeISBNs(books));
	}
	
	@Test
	public void testToMap1() throws Exception {
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap("isbn");
		assertEquals("0001", map.get("isbn.#text"));
	}
	
	@Test
	public void testToMap2() throws Exception {
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap("isbn->isbn_num");
		assertEquals("0001", map.get("isbn_num"));
	}
	
	@Test
	public void testToMap3() throws Exception {
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap("isbn@Double");
		assertEquals(1.0, map.get("isbn.#text"));
	}

	@Test
	public void testToMap4() throws Exception {
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap("isbn_t=0003");
		assertEquals("0003", map.get("isbn_t.#text"));
	}
	
	@Test
	public void testToMap5() throws Exception {
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap(
			"isbn->isbn@Int=-1"
			, "title->title" 
		);
		
		assertEquals(1, map.get("isbn"));
		assertEquals("Historiae", map.get("title"));
	}

	@Test
	public void testToMap6() throws Exception {
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap("isbn_t->isbn@Int=-1");
		assertEquals(-1, map.get("isbn"));
	}
	
	@Test
	public void testToMap7() throws Exception {
		
		XMLUtil.registTypeShift("IsRecent", (map, name, value) -> {
			int year = Integer.parseInt(value);
			if(year > 2000) {
				map.put(name, "Y");
			} else {
				map.put(name, "N");
			}
		});
		
		XMLNode book = XMLUtil
				.fromString(XML_TEXT)
				.selectFirst("book");

		Map<String, Object> map = book.toMap("year->recent_yn@IsRecent=N");
		assertEquals("Y", map.get("recent_yn"));
	}
	
	@Test
	public void testTailText1() throws Exception {
		
		XMLNode text = XMLUtil.fromString(XML_TAIL_TEXT);
		
		assertEquals("\nhello ", text.getText());
		
		XMLArray params = text.select("p");
		
		assertEquals("name", params.get(0).getText());
		assertEquals("!\ntoday: ", params.get(0).getTail());
		
		assertEquals("year", params.get(1).getText());
		assertEquals("-", params.get(1).getTail());
		
		assertEquals("month", params.get(2).getText());
		assertEquals("-", params.get(2).getTail());

		assertEquals("day", params.get(3).getText());
		assertEquals("\nthis is test.\n", params.get(3).getTail());
	}

	@Test
	public void testToString1() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book(category=p'컴.{4}')");
		
		System.out.println(books.getFirst());
	}

	@Test
	public void testSelect999() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_TEXT)
				.select("book > author(#text='일\\'연')")
				.getParents();

		assertEquals("0007,", makeISBNs(books));
	}
	
	@Test
	public void test() throws Exception {
		
		XMLArray printNodes = XMLUtil
					.fromFile("resources/publisher/testformat.xml")
					.select("foreach>style>print");
		
		for(XMLNode node: printNodes) {
			System.out.println(node.getAttributeValue("exp"));
		}
	}
	
	@Test
	public void test1() throws Exception {
		
		XMLNode rootNode = XMLUtil
					.fromFile("resources/publisher/testformat.xml");

		System.out.println(rootNode.getText());
	}
	
	@Test
	public void testNamespace1() throws Exception {
		
		XMLArray books = XMLUtil
				.fromString(XML_NAMESPACE_TEXT)
				.select("ns:book");

		System.out.println(books.getFirst());
	}
}
