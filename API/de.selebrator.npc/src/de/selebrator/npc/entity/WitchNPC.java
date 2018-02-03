package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.WitchMetadata;
import org.bukkit.entity.EntityType;

public interface WitchNPC extends MonsterNPC, WitchMetadata {

	@Override
	default EntityType getType() {
		return EntityType.WITCH;
	}
}
