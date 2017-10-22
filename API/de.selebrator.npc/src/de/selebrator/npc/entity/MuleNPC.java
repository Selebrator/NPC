package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.MuleMetadata;
import org.bukkit.entity.EntityType;

public interface MuleNPC extends ChestedHorseNPC, MuleMetadata {

	@Override
	default EntityType getType() {
		return EntityType.MULE;
	}
}
