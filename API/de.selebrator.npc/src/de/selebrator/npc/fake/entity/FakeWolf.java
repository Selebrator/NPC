package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.WolfNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.*;

public class FakeWolf extends FakeTameable implements WolfNPC {

	MetadataObject<Float> tailHealth;
	MetadataObject<Boolean> begging;
	MetadataObject<Integer> color;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.tailHealth = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWolf_DATA_HEALTH, 20.0f); //15
		this.begging = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWolf_bC, false); //16
		this.color = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWolf_bD, 14); //17
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.isTamed() ? 20.0D : 8.0D);
		this.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
	}

	public float getTailHealth() {
		return this.tailHealth.get();
	}

	public void setTailHealth(float health) {
		this.tailHealth.set(health);
	}

	public boolean isBegging() {
		return this.begging.get();
	}

	public void setBegging(boolean begging) {
		this.begging.set(begging);
	}

	public int getColor() {
		return this.color.get();
	}

	public void setColor(int color) {
		this.color.set(color);
	}
}
