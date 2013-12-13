package net.iubris.aa.spheres;

import java.io.IOException;

import net.iubris.aa.spheres.chart.SpheresChart;
import net.iubris.aa.spheres.chart.SpheresChartDataSet;
import net.iubris.aa.spheres.render.stddraw3d.SpheresRender;

import org.jfree.chart.ChartPanel;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class SVMain extends ApplicationFrame {
	
	public SVMain(String title) {
		super(title);
	}

	private static final long serialVersionUID = 3967769338738843508L;

	public static void main(String[] args) {
//		run("sfere1.in", "1000 sfere NON intersecanti NÉ tangenti",true);
//		run("sfere2.in", "1000 sfere concentriche", false);
		run("sfere3.in", "1000 sfere casuali", false);
//		run("sfere4.in","altre sfere", true);		
	}
	
	
	
	
	
	private static void run(String fileName, String title, boolean draw3d) {
		SpheresChart spheresChartAltre = new SpheresChart(title);
		for (int r=1;r<8;r++) {
			spheresChartAltre.addSeries(
					new SpheresChartDataSet[] {
	//						new SpheresChartDataSet( calculateVolume("ds/sfere4.in", "altre sfere", 1E0, false) ),
							new SpheresChartDataSet( calculateSpheresVolume("ds/"+fileName, title, 1E1).getVolume() ),
							new SpheresChartDataSet( calculateSpheresVolume("ds/"+fileName, title, 1E2).getVolume() ),
							new SpheresChartDataSet( calculateSpheresVolume("ds/"+fileName, title, 1E3).getVolume() ),
							new SpheresChartDataSet( calculateSpheresVolume("ds/"+fileName, title, 1E4).getVolume() ),
							new SpheresChartDataSet( calculateSpheresVolume("ds/"+fileName, title, 1E5).getVolume() ),
							new SpheresChartDataSet( calculateSpheresVolume("ds/"+fileName, title, 1E6).getVolume() )}
					);
		}
		
		
		SVMain svMain = new SVMain(title);
		ChartPanel panel = spheresChartAltre.getPanel();
//		svMain.drawChart( panel );
		svMain.setContentPane(panel);
		svMain.pack();
		RefineryUtilities.centerFrameOnScreen(svMain);
		svMain.setVisible(true);		
		
	// rendering section
		if (draw3d) {
			new SpheresRender( calculateSpheresVolume("ds/"+fileName, title, 1E5) ).draw();
		}
	}
	
	private static SpheresVolume calculateSpheresVolume(String inFileName, String title, double howRandoms) {
		System.out.println(""+title+" con "+howRandoms+" punti");
		SpheresVolume sv = new SpheresVolume(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.initSpheresCollection();
		sv.initBoundingBox();
		sv.calculateVolume();
		
		return sv;
	}
	
}
