package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.WolfMetadata;
import org.bukkit.entity.EntityType;

public interface WolfNPC extends TameableNPC, WolfMetadata {

	@Override
	default EntityType getType() {
		return EntityType.WOLF;
	}
}
