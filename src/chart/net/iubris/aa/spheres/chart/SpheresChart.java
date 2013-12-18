package net.iubris.aa.spheres.chart;

import java.awt.Color;
import java.awt.Dimension;

import net.iubris.aa.spheres.model.Volume;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SpheresChart {
	
//	private SpheresChartDataSet[] spheresChartDataSets;
	private final String title;
	
	private final XYSeriesCollection dataset;

	private double maxRange;

	public SpheresChart(/*SpheresChartDataSet[] spheresChartDataSets,*/ String title) {
//		this.spheresChartDataSets = spheresChartDataSets;
		this.title = title;
		dataset = new XYSeriesCollection();		
	}
	
	public ChartPanel getPanel() {
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart,true,true,true,true,true);
		chartPanel.setPreferredSize(new Dimension(600, 400));
		return chartPanel;
	}
	
	public void addSeries(SpheresChartDataSet[] spheresChartDataSets) {
		final XYSeries series = new XYSeries(title);
		
		for (SpheresChartDataSet scds: spheresChartDataSets) {
			Volume volume = scds.getVolume();
			series.add( volume.getRandomPoints(), volume.getVolumeMontecarlo() );
		}
		
		dataset.addSeries(series);
		this.maxRange = spheresChartDataSets[0].getVolume().getVolumeTotal();
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
		
		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,	// chart title
				"random points",			    // x axis label
				"volume",			    // y axis label
				dataset,			// data
				PlotOrientation.VERTICAL,
				true,			   // include legend
				true,			   // tooltips
				false			   // urls
		);
	
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.lightGray);
	
//		final StandardLegend legend = (StandardLegend) chart.getLegend();
//		legend.setDisplaySeriesShapes(true);
		
		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
//		plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.darkGray);
		plot.setRangeGridlinePaint(Color.darkGray);
		plot.setDomainAxis(0, new LogarithmicAxis("random points"));
//		plot.setDomainAxis(1, new NumberAxis("volume"));
		
		
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//		final XYLine3DRenderer renderer = new XYLine3DRenderer();
		renderer.setSeriesLinesVisible(1, false);
		renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);		
	
		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(0);		
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//		rangeAxis.setAutoRange(true);
		rangeAxis.setRange( new Range(maxRange*1/5, maxRange*3/2));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;	
	}

}
