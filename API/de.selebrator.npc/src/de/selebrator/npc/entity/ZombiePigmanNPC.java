package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ZombiePigmanMetadata;
import org.bukkit.entity.EntityType;

public interface ZombiePigmanNPC extends ZombieNPC, ZombiePigmanMetadata {

	@Override
	default EntityType getType() {
		return EntityType.PIG_ZOMBIE;
	}
}
