package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.BatMetadata;
import org.bukkit.entity.EntityType;

public interface BatNPC extends AmbientNPC, BatMetadata {

	@Override
	default EntityType getType() {
		return EntityType.BAT;
	}
}
