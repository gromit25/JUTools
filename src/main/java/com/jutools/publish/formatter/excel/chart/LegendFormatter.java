package com.jutools.publish.formatter.excel.chart;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;

import com.jutools.publish.formatter.FormatterAttr;
import com.jutools.publish.formatter.FormatterException;
import com.jutools.publish.formatter.FormatterSpec;

import lombok.Getter;
import lombok.Setter;

@FormatterSpec(group="excel", tag="legend")
public class LegendFormatter extends AbstractChartComponent {
	
	@Getter
	@Setter
	@FormatterAttr(name="position", mandatory=false)
	private LegendPosition position;

	@Override
	protected void execFormat(OutputStream out, Charset charset, Map<String, Object> values) throws FormatterException {
		// 입력값 검사
		XDDFChartLegend legend = this.getParent(ChartFormatter.class)
									 .getChart().getOrAddLegend();
		
		if(null != this.getPosition()) {
			legend.setPosition(this.getPosition());
		}
		
	}

}
