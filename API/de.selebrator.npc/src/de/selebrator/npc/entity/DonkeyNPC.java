package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.DonkeyMetadata;
import org.bukkit.entity.EntityType;

public interface DonkeyNPC extends ChestedHorseNPC, DonkeyMetadata {

	@Override
	default EntityType getType() {
		return EntityType.DONKEY;
	}
}
