package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.EndermiteMetadata;
import org.bukkit.entity.EntityType;

public interface EndermiteNPC extends MonsterNPC, EndermiteMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ENDERMITE;
	}
}
