package de.selebrator.npc.entity.metadata;

public interface ShulkerMetadata extends GolemMetadata {

	//TODO add facing direction
	//TODO add attachment position

	byte getShieldHeight();

	void setShieldHeight(byte height);

	byte getColorId();

	void setColorId(byte color);
}
