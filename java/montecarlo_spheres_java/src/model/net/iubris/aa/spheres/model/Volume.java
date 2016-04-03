package net.iubris.aa.spheres.model;

public class Volume {

	private double volumeTotal;
	private double volumeTotalMontecarlo;
	private double randomPoints;

	public Volume(double volumeTotal, double volumeTotalMontecarlo, double randomPoints) {
		this.volumeTotal = volumeTotal;
		this.volumeTotalMontecarlo = volumeTotalMontecarlo;
		this.randomPoints = randomPoints;
	}

	public double getVolumeTotal() {
		return volumeTotal;
	}
	public double getVolumeMontecarlo() {
		return volumeTotalMontecarlo;
	}
	public double getRandomPoints() {
		return randomPoints;
	}
}
