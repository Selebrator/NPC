package de.selebrator.npc;

public enum EnumSkinFlag {
	CAPE(0),
	JACKET(1),
	LEFT_SLEEVE(2),
	RIGHT_SLEEVE(3),
	LEFT_PANTS(4),
	RIGHT_PANTS(5),
	HAT(6);

	int id;

	EnumSkinFlag(int id) {
		this. id = id;
	}

	public int getId() {
		return id;
	}
}
