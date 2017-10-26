package de.selebrator.npc.entity.metadata;

public interface WolfMetadata extends TameableMetadata {

	float getTailHealth();

	void setTailHealth(float health);

	boolean isBegging();

	void setBegging(boolean begging);

	int getColor();

	void setColor(int color);
}
