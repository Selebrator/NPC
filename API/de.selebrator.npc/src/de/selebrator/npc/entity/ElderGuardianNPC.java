package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ElderGuardianMetadata;
import org.bukkit.entity.EntityType;

public interface ElderGuardianNPC extends GuardianNPC, ElderGuardianMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ELDER_GUARDIAN;
	}
}
