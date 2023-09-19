package com.jutools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.jutools.publish.Publisher;
import com.jutools.publish.PublisherFactory;
import com.jutools.publish.PublisherType;

/**
 * Under Construction
 * 
 * @author jmsohn
 */
public class PublishUtil {
	
	public static void toConsole(File formatFile) throws Exception {
		toConsole(formatFile, Charset.defaultCharset(), new HashMap<String, Object>());
	}
	
	public static void toConsole(File formatFile, Map<String, Object> values) throws Exception {
		toConsole(formatFile, Charset.defaultCharset(), values);
	}
	
	public static void toConsole(File formatFile, Charset cs) throws Exception {
		toConsole(formatFile, cs, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param formatFile
	 * @param cs
	 * @param values
	 */
	public static void toConsole(File formatFile, Charset cs, Map<String, Object> values) throws Exception {
		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatFile);
		publisher.publish(null, cs, values);
	}
	
	// ----------------------------------------------

	public static void toTxt(File formatFile, File out) throws Exception {
		toTxt(formatFile, out, Charset.defaultCharset(), new HashMap<String, Object>());
	}
	
	public static void toTxt(File formatFile, File out, Map<String, Object> values) throws Exception {
		toTxt(formatFile, out, Charset.defaultCharset(), values);
	}
	
	public static void toTxt(File formatFile, File out, Charset cs) throws Exception {
		toTxt(formatFile, out, cs, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param formatFile
	 * @param out
	 * @param cs
	 * @param values
	 */
	public static void toTxt(File formatFile, File out, Charset cs, Map<String, Object> values) throws Exception {
		
		try(OutputStream outTxt = new FileOutputStream(out)) {
			Publisher publisher = PublisherFactory.create(PublisherType.TEXT_FILE, formatFile);
			publisher.publish(outTxt, cs, values);
		}
	}
	
	// ----------------------------------------------

	public static void toExcel(File formatFile, File out) throws Exception {
		toExcel(formatFile, out, Charset.defaultCharset(), new HashMap<String, Object>());
	}

	public static void toExcel(File formatFile, File out, Map<String, Object> values) throws Exception {
		toExcel(formatFile, out, Charset.defaultCharset(), values);
	}
	
	public static void toExcel(File formatFile, File out, Charset cs) throws Exception {
		toExcel(formatFile, out, cs, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param formatFile
	 * @param out
	 * @param cs
	 * @param values
	 */
	public static void toExcel(File formatFile, File out, Charset cs,  Map<String, Object> values) throws Exception {

		try(OutputStream outExcel = new FileOutputStream(out)) {
			Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
			publisher.publish(outExcel, cs, values);
		}
	}
}
