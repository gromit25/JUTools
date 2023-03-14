package com.jutools.publish.formatter.excel.chart;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.poi.xddf.usermodel.chart.XDDFChartAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;

import com.jutools.press.formatter.Formatter;
import com.jutools.press.formatter.FormatterException;
import com.jutools.press.formatter.FormatterSpec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="axes")
public class AxesFormatter extends AbstractChartComponent {
	
	/**
	 * chart 객체
	 * AxisFormatter 객체에서 Axis 객체를 생성할 때 사용함
	 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private XSSFChart chart;
	
	/**
	 * category axis 객체
	 * 하위 컴포넌트인 AxisFormatter(tag: category-axis) 객체에서 생성하여 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFChartAxis categoryAxis;
	
	/**
	 * value axis 객체
	 * 하위 컴포넌트인 AxisFormatter(tag: value-axis) 객체에서 생성하여 설정함
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private XDDFValueAxis valueAxis;
	
	
	@Override
	public void addChildFormatter(Formatter formatter) throws FormatterException {
		if(formatter instanceof AxisFormatter) {
			super.addChildFormatter(formatter);
		} else {
			throw new FormatterException(formatter, "Invalid Formatter(AxisFormatter expected).");
		}
	}
	
	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		
		// 입력값 검사
		
		// 1. chartformatter를 가져옴
		ChartFormatter copy = this.getParent(ChartFormatter.class);
		this.setChart(copy.getChart());
		
		// 2. 하위 컴포넌트(AxisFormatter)에서
		//    member 변수인 categoryAxis와 valueAxis를 설정함
		this.execChildFormatters(out, charset, values);
		
		// 3. chart formatter에
		//    categoryAxis와 valueAxis를 설정함
		copy.setCategoryAxis(this.getCategoryAxis());
		copy.setValueAxis(this.getValueAxis());
	}

}
