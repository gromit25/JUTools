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
	
	public static void publishToConsole(File formatFile) throws Exception {
		publishToConsole(formatFile, Charset.defaultCharset(), new HashMap<String, Object>());
	}
	
	public static void publishToConsole(File formatFile, Map<String, Object> values) throws Exception {
		publishToConsole(formatFile, Charset.defaultCharset(), values);
	}
	
	public static void publishToConsole(File formatFile, Charset cs) throws Exception {
		publishToConsole(formatFile, cs, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param formatFile
	 * @param cs
	 * @param values
	 */
	public static void publishToConsole(File formatFile, Charset cs, Map<String, Object> values) throws Exception {
		Publisher publisher = PublisherFactory.create(PublisherType.CONSOLE, formatFile);
		publisher.publish(null, cs, values);
	}
	
	// ----------------------------------------------

	public static void publishToTxt(File formatFile, File out) throws Exception {
		publishToTxt(formatFile, out, Charset.defaultCharset(), new HashMap<String, Object>());
	}
	
	public static void publishToTxt(File formatFile, File out, Map<String, Object> values) throws Exception {
		publishToTxt(formatFile, out, Charset.defaultCharset(), values);
	}
	
	public static void publishToTxt(File formatFile, File out, Charset cs) throws Exception {
		publishToTxt(formatFile, out, cs, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param formatFile
	 * @param out
	 * @param cs
	 * @param values
	 */
	public static void publishToTxt(File formatFile, File out, Charset cs, Map<String, Object> values) throws Exception {
		
		try(OutputStream outTxt = new FileOutputStream(out)) {
			Publisher publisher = PublisherFactory.create(PublisherType.TEXT_FILE, formatFile);
			publisher.publish(outTxt, cs, values);
		}
	}
	
	// ----------------------------------------------

	public static void publishToExcel(File formatFile, File out) throws Exception {
		publishToExcel(formatFile, out, Charset.defaultCharset(), new HashMap<String, Object>());
	}

	public static void publishToExcel(File formatFile, File out, Map<String, Object> values) throws Exception {
		publishToExcel(formatFile, out, Charset.defaultCharset(), values);
	}
	
	public static void publishToExcel(File formatFile, File out, Charset cs) throws Exception {
		publishToExcel(formatFile, out, cs, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param formatFile
	 * @param out
	 * @param cs
	 * @param values
	 */
	public static void publishToExcel(File formatFile, File out, Charset cs,  Map<String, Object> values) throws Exception {

		try(OutputStream outExcel = new FileOutputStream(out)) {
			Publisher publisher = PublisherFactory.create(PublisherType.EXCEL_FILE, formatFile);
			publisher.publish(outExcel, cs, values);
		}
	}
}
