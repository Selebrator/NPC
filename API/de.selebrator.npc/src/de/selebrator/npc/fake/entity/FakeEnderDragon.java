package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.EnderDragonNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityEnderDragon_PHASE;

public class FakeEnderDragon extends FakeInsentient implements EnderDragonNPC {

	MetadataObject<Integer> phase;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.phase = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityEnderDragon_PHASE, 10); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200.0D);
	}

	public int getPhaseId() {
		return this.phase.get();
	}

	public void setPhase(int phaseId) {
		this.phase.set(phaseId);
	}
}
