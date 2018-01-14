package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SheepMetadata;
import org.bukkit.entity.EntityType;

public interface SheepNPC extends AnimalNPC, SheepMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SHEEP;
	}
}
