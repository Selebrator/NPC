package de.selebrator.npc;

public enum EnumStatus {
	FIRE(0),
	SNEAK(1),
	SPRINT(3),
	USE(4),
	INVISIBLE(5),
	GLOW(6),
	ELYTRA(7);

	int id;

	EnumStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
