package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ZombieHorseMetadata;
import org.bukkit.entity.EntityType;

public interface ZombieHorseNPC extends HorseNPC, ZombieHorseMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ZOMBIE_HORSE;
	}
}
