package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.MushroomCowMetadata;
import org.bukkit.entity.EntityType;

public interface MushroomCowNPC extends CowNPC, MushroomCowMetadata {

	@Override
	default EntityType getType() {
		return EntityType.MUSHROOM_COW;
	}
}
