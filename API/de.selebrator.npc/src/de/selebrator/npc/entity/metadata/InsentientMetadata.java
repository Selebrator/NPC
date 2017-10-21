package de.selebrator.npc.entity.metadata;

public interface InsentientMetadata extends LivingMetadata {

	boolean hasAI();

	void setAI(boolean ai);

	boolean isLeftHanded();

	void setLeftHanded(boolean leftHanded);
}
