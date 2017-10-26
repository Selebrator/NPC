package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ParrotMetadata;
import org.bukkit.entity.EntityType;

public interface ParrotNPC extends TameableNPC, ParrotMetadata {

	@Override
	default EntityType getType() {
		return EntityType.PARROT;
	}
}
