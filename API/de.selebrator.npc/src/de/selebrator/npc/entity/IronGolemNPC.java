package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.IronGolemMetadata;
import org.bukkit.entity.EntityType;

public interface IronGolemNPC extends GolemNPC, IronGolemMetadata {

	@Override
	default EntityType getType() {
		return EntityType.IRON_GOLEM;
	}
}
