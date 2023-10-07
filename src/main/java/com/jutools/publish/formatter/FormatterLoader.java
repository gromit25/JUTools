package com.jutools.publish.formatter;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.jutools.publish.formatter.console.StyleFormatter;
import com.jutools.publish.formatter.excel.AlignmentFormatter;
import com.jutools.publish.formatter.excel.BackgroundFormatter;
import com.jutools.publish.formatter.excel.BorderFormatter;
import com.jutools.publish.formatter.excel.CellFormatter;
import com.jutools.publish.formatter.excel.CellStyleFormatter;
import com.jutools.publish.formatter.excel.CursorFormatter;
import com.jutools.publish.formatter.excel.ExcelAttrSetter;
import com.jutools.publish.formatter.excel.FontFormatter;
import com.jutools.publish.formatter.excel.ImageFormatter;
import com.jutools.publish.formatter.excel.InsertColumn;
import com.jutools.publish.formatter.excel.InsertRow;
import com.jutools.publish.formatter.excel.RowCellsFormatter;
import com.jutools.publish.formatter.excel.WorkbookFormatter;
import com.jutools.publish.formatter.excel.WorksheetFormatter;
import com.jutools.publish.formatter.excel.chart.AxesFormatter;
import com.jutools.publish.formatter.excel.chart.AxisFormatter;
import com.jutools.publish.formatter.excel.chart.BarSeriesFormatter;
import com.jutools.publish.formatter.excel.chart.ChartFormatter;
import com.jutools.publish.formatter.excel.chart.DataSourceFormatter;
import com.jutools.publish.formatter.excel.chart.ExcelChartAttrSetter;
import com.jutools.publish.formatter.excel.chart.LegendFormatter;
import com.jutools.publish.formatter.excel.chart.LineSeriesFormatter;
import com.jutools.publish.formatter.excel.chart.PieSeriesFormatter;
import com.jutools.publish.formatter.flow.AltFlowFormatter;
import com.jutools.publish.formatter.flow.CaseFormatter;
import com.jutools.publish.formatter.flow.CustomFormatter;
import com.jutools.publish.formatter.flow.DefaultFormatter;
import com.jutools.publish.formatter.flow.ForFormatter;
import com.jutools.publish.formatter.flow.ForeachFormatter;
import com.jutools.publish.formatter.flow.IfFormatter;
import com.jutools.publish.formatter.flow.MapToListFormatter;
import com.jutools.publish.formatter.flow.SetFormatter;
import com.jutools.publish.formatter.flow.SwitchFormatter;
import com.jutools.publish.formatter.flow.WhileFormatter;
import com.jutools.publish.formatter.text.PrintFormatter;
import com.jutools.publish.formatter.text.TextFlowFormatter;

/**
 * Formatter 목록 클래스<br>
 * 새로운 Formatter를 만들면 이 클래스에 등록하여야 함
 * 
 * @author jmsohn
 */
class FormatterLoader {
	
	/** Formatter 클래스 목록*/
	private ArrayList<Class<?>> formatterClazzes;
	private ArrayList<Class<?>> attrSetterClazzes;
	
	/**
	 * 생성자
	 */
	FormatterLoader() {
		this.init();
	}
	
	/**
	 * Formatter 목록 설정 메소드<br>
	 * 새로운 Formatter를 이 메소드에 클래스를 등록하여야 함
	 */
	private void init() {
		
		// Formatter 클래스 목록 객체 생성
		this.formatterClazzes = new ArrayList<Class<?>>();
		
		// Flow Formatter
		this.formatterClazzes.add(AltFlowFormatter.class);
		this.formatterClazzes.add(CaseFormatter.class);
		this.formatterClazzes.add(CustomFormatter.class);
		this.formatterClazzes.add(DefaultFormatter.class);
		this.formatterClazzes.add(ForeachFormatter.class);
		this.formatterClazzes.add(ForFormatter.class);
		this.formatterClazzes.add(IfFormatter.class);
		this.formatterClazzes.add(MapToListFormatter.class);
		this.formatterClazzes.add(SetFormatter.class);
		this.formatterClazzes.add(SwitchFormatter.class);
		this.formatterClazzes.add(WhileFormatter.class);
		
		// Text Formatter
		this.formatterClazzes.add(PrintFormatter.class);
		this.formatterClazzes.add(TextFlowFormatter.class);
		
		// Console Formatter
		this.formatterClazzes.add(StyleFormatter.class);
		
		// Excel Formatter
		this.formatterClazzes.add(AlignmentFormatter.class);
		this.formatterClazzes.add(BackgroundFormatter.class);
		this.formatterClazzes.add(BorderFormatter.class);
		this.formatterClazzes.add(CellFormatter.class);
		this.formatterClazzes.add(CellStyleFormatter.class);
		this.formatterClazzes.add(CursorFormatter.class);
		this.formatterClazzes.add(FontFormatter.class);
		this.formatterClazzes.add(ImageFormatter.class);
		this.formatterClazzes.add(InsertColumn.class);
		this.formatterClazzes.add(InsertRow.class);
		this.formatterClazzes.add(RowCellsFormatter.class);
		this.formatterClazzes.add(WorkbookFormatter.class);
		this.formatterClazzes.add(WorksheetFormatter.class);
		
		// Excel Chart Formatter
		this.formatterClazzes.add(AxesFormatter.class);
		this.formatterClazzes.add(AxisFormatter.class);
		this.formatterClazzes.add(BarSeriesFormatter.class);
		this.formatterClazzes.add(ChartFormatter.class);
		this.formatterClazzes.add(DataSourceFormatter.class);
		this.formatterClazzes.add(LegendFormatter.class);
		this.formatterClazzes.add(LineSeriesFormatter.class);
		this.formatterClazzes.add(PieSeriesFormatter.class);
		
		// ---------------------------------------
		
		// Formatter의 속성 설정을 위한 Setter 클래스
		this.attrSetterClazzes = new ArrayList<Class<?>>();
		
		//
		this.attrSetterClazzes.add(CommonAttrSetter.class);
		
		// Excel 관련 Setter 클래스 등록
		this.attrSetterClazzes.add(ExcelAttrSetter.class);
		this.attrSetterClazzes.add(ExcelChartAttrSetter.class);
	}
	
	/**
	 * Formatter Class Stream 반환
	 * 
	 * @return Formatter Class Stream
	 */
	Stream<Class<?>> streamOfFormatter() {
		return this.formatterClazzes.stream();
	}
	
	/**
	 * Attribute Setter Class Stream 반환
	 * 
	 * @return Attribute Setter Class Stream
	 */
	Stream<Class<?>> streamOfAttrSetter() {
		return this.attrSetterClazzes.stream();
	}
}
