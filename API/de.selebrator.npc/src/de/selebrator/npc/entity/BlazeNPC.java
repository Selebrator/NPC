package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.BlazeMetadata;
import org.bukkit.entity.EntityType;

public interface BlazeNPC extends MonsterNPC, BlazeMetadata {

	@Override
	default EntityType getType() {
		return EntityType.BLAZE;
	}
}
