package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.WitherNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.*;

public class FakeWither extends FakeMonster implements WitherNPC {

	MetadataObject<Integer> centerTargetEID;
	MetadataObject<Integer> leftTargetEID;
	MetadataObject<Integer> rightTargetEID;
	MetadataObject<Integer> invulnerableTime;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.centerTargetEID = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWither_a, 0); //12
		this.leftTargetEID = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWither_b, 0); //13
		this.rightTargetEID = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWither_c, 0); //14
		this.invulnerableTime = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWither_by, 0); //15
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(300.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40.0D);
		this.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(4.0D);
	}

	public int getCenterTargetEID() {
		return this.centerTargetEID.get();
	}

	public void setCenterTargetEID(int entityId) {
		this.centerTargetEID.set(entityId);
	}

	public int getLeftTargetEID() {
		return this.leftTargetEID.get();
	}

	public void setLeftTargetEID(int entityId) {
		this.leftTargetEID.set(entityId);
	}

	public int getRightTargetEID() {
		return this.rightTargetEID.get();
	}

	public void setRightTargetEID(int entityId) {
		this.rightTargetEID.set(entityId);
	}

	public int getInvulnerableTime() {
		return this.invulnerableTime.get();
	}

	public void setInvulnerableTime(int time) {
		this.invulnerableTime.set(time);
	}
}
