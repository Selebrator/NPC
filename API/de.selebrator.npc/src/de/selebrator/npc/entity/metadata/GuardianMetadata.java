package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.LivingEntity;

public interface GuardianMetadata extends MonsterMetadata {

	boolean isRetractingSpikes();

	void setRetractingSpikes(boolean retractingSpikes);

	default boolean hasTarget() {
		return !(this.getTargetEID() == 0);
	}

	int getTargetEID();

	void setTargetEID(int entityId);

	default void setTarget(LivingEntity target) {
		this.setTargetEID(target.getEntityId());
	}
}
