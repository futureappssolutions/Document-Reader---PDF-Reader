package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.thirdpart.achartengine.chart.AbstractChart;

public class WPChartShape extends WPAutoShape
{

	public AbstractChart getAChart() 
	{
		return chart;
	}

	public void setAChart(AbstractChart chart) 
	{
		this.chart = chart;
	}
	
	private AbstractChart chart;
}
