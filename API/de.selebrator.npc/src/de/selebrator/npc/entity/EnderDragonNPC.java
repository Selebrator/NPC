package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.EnderDragonMetadata;
import org.bukkit.entity.EntityType;

public interface EnderDragonNPC extends InsentientNPC, EnderDragonMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ENDER_DRAGON;
	}
}
