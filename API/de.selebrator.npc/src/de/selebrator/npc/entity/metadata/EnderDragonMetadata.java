package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.EnderDragon;

public interface EnderDragonMetadata extends InsentientMetadata {

	int getPhaseId();

	default EnderDragon.Phase getPhase() {

		int phaseId = this.getPhaseId();
		if(0 <= phaseId && phaseId < EnderDragon.Phase.values().length)
			return EnderDragon.Phase.values()[phaseId];
		return EnderDragon.Phase.HOVER;
	}

	void setPhase(int phaseId);

	default void setPhase(EnderDragon.Phase phase) {
		this.setPhase(phase.ordinal());
	}
}
