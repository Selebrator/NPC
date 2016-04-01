package de.selebrator.npc;

public enum EnumEntityStatus {
	HURT(2),
	DEAD(3);

	private byte id;

	EnumEntityStatus(int id) {
		this.id = (byte) id;
	}

	public byte getId() {
		return this.id;
	}
}
