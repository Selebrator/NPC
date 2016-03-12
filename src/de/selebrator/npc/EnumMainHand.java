package de.selebrator.npc;

public enum EnumMainHand {
	LEFT(0),
	RIGHT(1);

	private int id;

	EnumMainHand(int id) {
		this.id = id;
	}

	public byte getId() {
		return (byte) this.id;
	}
}
