package de.selebrator.npc.metadata;

public interface ZombieMetadata extends MonsterMetadata {

	boolean isBaby();

	void setBaby(boolean baby);

	boolean areHandsUp();

	void setHandsUp(boolean handsUp);
}
