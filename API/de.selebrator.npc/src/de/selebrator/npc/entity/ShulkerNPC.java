package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ShulkerMetadata;
import org.bukkit.entity.EntityType;

public interface ShulkerNPC extends GolemNPC, ShulkerMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SHULKER;
	}
}
