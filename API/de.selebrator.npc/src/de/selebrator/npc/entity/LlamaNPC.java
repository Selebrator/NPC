package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.LlamaMetadata;
import org.bukkit.entity.EntityType;

public interface LlamaNPC extends ChestedHorseNPC, LlamaMetadata {

	@Override
	default EntityType getType() {
		return EntityType.LLAMA;
	}
}
