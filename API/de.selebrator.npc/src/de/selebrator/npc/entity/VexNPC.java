package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.VexMetadata;
import org.bukkit.entity.EntityType;

public interface VexNPC extends MonsterNPC, VexMetadata {

	@Override
	default EntityType getType() {
		return EntityType.VEX;
	}
}
