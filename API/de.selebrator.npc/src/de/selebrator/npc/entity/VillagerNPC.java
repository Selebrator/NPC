package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.VillagerMetadata;
import org.bukkit.entity.EntityType;

public interface VillagerNPC extends AgeableNPC, VillagerMetadata {

	@Override
	default EntityType getType() {
		return EntityType.VILLAGER;
	}
}
