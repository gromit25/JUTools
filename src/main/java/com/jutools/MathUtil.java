package com.jutools;

import java.text.DecimalFormat;
import java.util.HashMap;

import com.jutools.mathexp.MathExp;

public class MathUtil {
	
	private static HashMap<String, Double> unitPrefixMap;
	
	static {
		
		unitPrefixMap = new HashMap<String, Double>();
		
		// https://en.wikipedia.org/wiki/Metric_prefix
		unitPrefixMap.put("Q", 1000000000000000000000000000000.0);
		unitPrefixMap.put("R", 1000000000000000000000000000.0);
		unitPrefixMap.put("Y", 1000000000000000000000000.0);
		unitPrefixMap.put("Z", 1000000000000000000000.0);
		unitPrefixMap.put("E", 1000000000000000000.0);
		unitPrefixMap.put("P", 1000000000000000.0);
		unitPrefixMap.put("T", 1000000000000.0);
		unitPrefixMap.put("G", 1000000000.0);
		unitPrefixMap.put("M", 1000000.0);
		unitPrefixMap.put("k", 1000.0);
		unitPrefixMap.put("K", 1000.0);	// 표준은 소문자 k 이나, 대문자도 일반적으로 사용하기 때문에 추가
		unitPrefixMap.put("h", 100.0);
		unitPrefixMap.put("da", 10.0);
		unitPrefixMap.put("d", 0.1);
		unitPrefixMap.put("c", 0.01);
		unitPrefixMap.put("m", 0.001);
		unitPrefixMap.put("μ", 0.000001);
		unitPrefixMap.put("n", 0.000000001);
		unitPrefixMap.put("p", 0.000000000001);
		unitPrefixMap.put("f", 0.000000000000001);
		unitPrefixMap.put("a", 0.000000000000000001);
		unitPrefixMap.put("z", 0.000000000000000000001);
		unitPrefixMap.put("y", 0.000000000000000000000001);
		unitPrefixMap.put("r", 0.000000000000000000000000001);
		unitPrefixMap.put("q", 0.000000000000000000000000000001);
		
		// https://en.wikipedia.org/wiki/Binary_prefix
		unitPrefixMap.put("Ki", 1024.0);
		unitPrefixMap.put("Mi", 1048576.0);
		unitPrefixMap.put("Gi", 1073741824.0);
		unitPrefixMap.put("Ti", 1099511627776.0);
		unitPrefixMap.put("Pi", 1125899906842624.0);
		unitPrefixMap.put("Ei", 1152921504606846976.0);
		unitPrefixMap.put("Zi", 1180591620717411303424.0);
		unitPrefixMap.put("Yi", 1208925819614629174706176.0);
		
	}
	
	/**
	 * 
	 * 참조)<br>
	 * https://en.wikipedia.org/wiki/Metric_prefix<br>
	 * https://en.wikipedia.org/wiki/Binary_prefix
	 * 
	 * @param unitPrefix
	 * @return
	 */
	public static double unitPrefixToFactor(String unitPrefix) throws Exception {
		
		if(unitPrefixMap != null && unitPrefixMap.containsKey(unitPrefix) == true) {
			return unitPrefixMap.get(unitPrefix);
		} else if(unitPrefix != null && unitPrefix.trim().equals("") == true) {
			return 1;
		} else {
			throw new Exception("requested unit prefix(" + unitPrefix + ") is not found");
		}
	}
	
	/**
	 * 
	 * 
	 * @param value
	 * @param unitPrefix
	 * @return
	 */
	public static double toFullValue(double value, String unitPrefix) throws Exception {
		return value * unitPrefixToFactor(unitPrefix); 
	}
	
	
	/**
	 * 
	 * 
	 * @param value
	 * @param unitPrefix
	 * @return
	 */
	public static double toUnitValue(double value, String unitPrefix) throws Exception {
		return value / unitPrefixToFactor(unitPrefix); 
	}
	
	/**
	 * 
	 * 
	 * @param exp
	 * @return
	 */
	public static double calculate(String exp) throws Exception {
		return MathExp.calculate(exp);
	}
	
	/**
	 * 천자리 콤마 추가
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(int value) {
		DecimalFormat df = new DecimalFormat("###,###");
		return df.format(value);
	}
	
	/**
	 * 천자리 콤마 추가
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(long value) {
		DecimalFormat df = new DecimalFormat("###,###");
		return df.format(value);
	}

	/**
	 * 천자리 콤마 추가
	 * 소수점 3자리까지 표시
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(float value) {
		DecimalFormat df = new DecimalFormat("###,###.###");
		return df.format(value);
	}
	
	/**
	 * 천자리 콤마 추가
	 * 소수점 3자리까지 표시
	 * 
	 * @param value 콤마 추가할 대상
	 * @return 천자리 콤마 추가된 문자열
	 */
	public static String toThousandCommaStr(double value) {
		DecimalFormat df = new DecimalFormat("###,###.###");
		return df.format(value);
	}

}
