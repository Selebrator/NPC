package de.selebrator.npc.entity.metadata;

public interface ZombieMetadata extends MonsterMetadata {

	boolean isBaby();

	void setBaby(boolean baby);

	boolean areHandsUp();

	void setHandsUp(boolean handsUp);
}
