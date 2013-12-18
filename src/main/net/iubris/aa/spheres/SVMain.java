package net.iubris.aa.spheres;

import java.io.IOException;

import net.iubris.aa.spheres.chart.SpheresChart;
import net.iubris.aa.spheres.chart.SpheresChartDataSet;
import net.iubris.aa.spheres.render.stddraw3d.SpheresByPointsRender;
import net.iubris.aa.spheres.volume.sequential.SpheresVolumeSequential;

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
//		run("sfere2.in", "1000 sfere concentriche", true);
//		run("sfere3.in", "1000 sfere casuali", 1E5, true);
//		run("sfere4.in","altre sfere", true);
//		run("sfere_noel.in","altre sfere", true);
		
		System.out.println( calculateSpheresVolume("ds/sfere1.in", "1000 sfere NON intersecanti NÉ tangenti", 1E6).getResult() );
	}
	
	
	private static void run(String fileName, String title, boolean draw3d) {
		
//		drawChart(fileName, title);
		
	// rendering section
		if (draw3d) {
			SpheresVolumeSequential calculatedSpheresVolume = calculateSpheresVolume("ds/"+fileName, title, 1E5);
//			new SpheresRender( calculatedSpheresVolume ).draw();
			new SpheresByPointsRender( calculatedSpheresVolume ).draw();
		}
	}
	
	
	private static void drawChart(String fileName, String title) {
		SpheresChart spheresChart = new SpheresChart(title);
		for (int r=1;r<8;r++) {
			spheresChart.addSeries(
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
		ChartPanel panel = spheresChart.getPanel();
//		svMain.drawChart( panel );
		svMain.setContentPane(panel);
		svMain.pack();
		RefineryUtilities.centerFrameOnScreen(svMain);
		svMain.setVisible(true);
	}
	
	private static SpheresVolumeSequential calculateSpheresVolume(String inFileName, String title, double howRandoms) {
		System.out.println(""+title+" con "+howRandoms+" punti");
		SpheresVolumeSequential sv = new SpheresVolumeSequential(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.init();
		sv.calculate();
		
		return sv;
	}	
}
