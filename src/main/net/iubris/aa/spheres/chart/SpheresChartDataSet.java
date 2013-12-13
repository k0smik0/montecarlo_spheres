package net.iubris.aa.spheres.chart;

import net.iubris.aa.spheres.model.Volume;

public class SpheresChartDataSet {

//	private double howPoints;
	private Volume volume;

	public SpheresChartDataSet(Volume volume) {
//		this.howPoints = howPoints;
		this.volume = volume;
	}
	
//	public double getHowPoints() {
//		return howPoints;
//	}
	public Volume getVolume() {
		return volume;
	}
}
