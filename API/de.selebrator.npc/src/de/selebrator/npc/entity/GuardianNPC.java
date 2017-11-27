package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.GuardianMetadata;
import org.bukkit.entity.EntityType;

public interface GuardianNPC extends MonsterNPC, GuardianMetadata {

	@Override
	default EntityType getType() {
		return EntityType.GUARDIAN;
	}
}
