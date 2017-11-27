package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.AbstractSkeletonNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntitySkeletonAbstract_a;

public abstract class FakeAbstractSkeleton extends FakeMonster implements AbstractSkeletonNPC {

	MetadataObject<Boolean> swingingArms;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.swingingArms = new MetadataObject<>(this.getDataWatcher(), FIELD_EntitySkeletonAbstract_a, false); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	public boolean isSwingingArms() {
		return this.swingingArms.get();
	}

	public void setSwingingArms(boolean swingingArms) {
		this.swingingArms.set(swingingArms);
	}
}
