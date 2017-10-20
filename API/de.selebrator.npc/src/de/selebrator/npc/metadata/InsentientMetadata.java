package de.selebrator.npc.metadata;

public interface InsentientMetadata extends LivingMetadata {

	boolean hasAI();

	void setAI(boolean ai);

	boolean isLeftHanded();

	void setLeftHanded(boolean leftHanded);
}
