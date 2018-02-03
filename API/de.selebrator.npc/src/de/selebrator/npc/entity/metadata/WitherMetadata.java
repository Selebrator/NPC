package de.selebrator.npc.entity.metadata;

public interface WitherMetadata extends MonsterMetadata {

	int getCenterTargetEID();

	void setCenterTargetEID(int entityId);

	int getLeftTargetEID();

	void setLeftTargetEID(int entityId);

	int getRightTargetEID();

	void setRightTargetEID(int entityId);

	int getInvulnerableTime();

	void setInvulnerableTime(int time);
}
