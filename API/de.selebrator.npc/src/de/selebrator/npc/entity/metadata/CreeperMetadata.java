package de.selebrator.npc.entity.metadata;

public interface CreeperMetadata extends MonsterMetadata {

	int getState();

	void setState(int state);

	boolean isCharged();

	void setCharged(boolean charged);

	boolean idIgnited();

	void setIgnited(boolean ignited);
}
