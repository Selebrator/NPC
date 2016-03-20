package de.selebrator.npc;

public enum EnumMoveSpeed {
	SNEAKING(1.3D, 0.25D),
	WALKING(4.32D, 0.86D),
	SPRINTING(5.61D, 1.125D);

	private double speed;
	private double step;

	EnumMoveSpeed(double speed, double step) {
		this.speed = speed;
		this.step = step;
	}

	public double getSpeed() {
		return this.speed;
	}

	public double getStepSize() {
		return this.step;
	}
}
