package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ZombieVillagerMetadata;
import org.bukkit.entity.EntityType;

public interface ZombieVillagerNPC extends ZombieNPC, ZombieVillagerMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ZOMBIE_VILLAGER;
	}
}
