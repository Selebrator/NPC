package de.selebrator.npc.entity.metadata;

public interface SheepMetadata extends AnimalMetadata {

	int getColorId();

	void setColor(int id);

	boolean isSheared();

	void setSheared(boolean sheared);
}
