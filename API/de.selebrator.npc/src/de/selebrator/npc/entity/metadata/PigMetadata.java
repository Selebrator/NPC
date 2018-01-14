package de.selebrator.npc.entity.metadata;

public interface PigMetadata extends AnimalMetadata {

	boolean hasSaddle();

	void setSaddle(boolean saddle);

	int getBoostTime();

	void setBoostTime(int time);
}
