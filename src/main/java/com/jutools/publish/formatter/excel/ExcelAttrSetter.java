package com.jutools.publish.formatter.excel;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jutools.FileUtil;
import com.jutools.publish.formatter.Formatter;
import com.jutools.publish.formatter.FormatterAttrSetter;
import com.jutools.publish.formatter.FormatterAttrSetterClass;

/**
 * 엑셀 객체에 대한 XML 속성값(스트링)을 파싱하여 설정하는 클래스
 * 
 * @author jmsohn
 */
@FormatterAttrSetterClass
public class ExcelAttrSetter {
	
	/**
	 * 
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(XSSFWorkbook.class)
	public static void setWorkbook(Formatter formatter, Method setMethod, String attrValue) throws Exception {

		// template 파일을 읽어옴
		// 주의!
		// 만일 XSSFWorkbook을 만들때, new XSSFWorkbook(File file)로
		// 만들게 되면, 변경사항이 원본 파일에도 영향을 주게됨
		try(InputStream templateIS = FileUtil.getInputStream(attrValue)) {
			XSSFWorkbook template = new XSSFWorkbook(templateIS);
			setMethod.invoke(formatter, template);
		}
	}
	
	/**
	 * RowColumnEval type의 속성 설정 메서드
	 *   FormatterAttrSetter 어노테이션 주석 참조
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(RowColumnEval.class)
	public static void setRowColumnEval(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, RowColumnEval.compile(attrValue, "0", "0"));
	}
	
	/**
	 * 
	 * @param formatter 속성을 지정할 formatter 객체
	 * @param setMethod formatter의 속성값 setMethod
	 * @param attrValue formatter에 설정할 속성의 문자열값
	 */
	@FormatterAttrSetter(RangeEval.class)
	public static void setRangeEval(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		RangeEval range = RangeEval.create(attrValue);
		setMethod.invoke(formatter, range);
	}
	
	@FormatterAttrSetter(CellType.class)
	public static void setCellType(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, CellType.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(HorizontalAlignment.class)
	public static void setHorizontalAlignment(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, HorizontalAlignment.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(VerticalAlignment.class)
	public static void setVerticalAlignment(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, VerticalAlignment.valueOf(attrValue));
	}
	
	@FormatterAttrSetter(XSSFColor.class)
	public static void setXSSFColor(Formatter formatter, Method setMethod, String attrValue) throws Exception {
		setMethod.invoke(formatter, ExcelUtil.getColor(attrValue));
	}
}
