package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.GuardianNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityGuardian_bA;
import static de.selebrator.npc.fake.Imports.FIELD_EntityGuardian_bB;

public class FakeGuardian extends FakeMonster implements GuardianNPC {

	MetadataObject<Boolean> retractingSpikes;
	MetadataObject<Integer> targetEID;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.retractingSpikes = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityGuardian_bA, false); //12
		this.targetEID = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityGuardian_bB, 0); //13
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5D);
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(16.0D);
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
	}

	public boolean isRetractingSpikes() {
		return this.retractingSpikes.get();
	}

	public void setRetractingSpikes(boolean retractingSpikes) {
		this.retractingSpikes.set(retractingSpikes);
	}

	public int getTargetEID() {
		return this.targetEID.get();
	}

	public void setTargetEID(int entityId) {
		this.targetEID.set(entityId);
	}
}
