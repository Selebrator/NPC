package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SlimeMetadata;
import org.bukkit.entity.EntityType;

public interface SlimeNPC extends InsentientNPC, SlimeMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SLIME;
	}
}
