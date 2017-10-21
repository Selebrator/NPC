package de.selebrator.npc;

public enum Animation {
	SWING_MAINHAND(0),
	TAKE_DAMAGE(1),
	LEAVE_BED(2),
	SWING_OFFHAND(3),
	CRITICAL_EFFECT(4),
	MAGIC_CRITICAL_EFFECT(5),
	CROUCH(104),
	UNCROUCH(105);

	private byte id;

	Animation(int id) {
		this.id = (byte) id;
	}

	public byte getId() {
		return this.id;
	}
}