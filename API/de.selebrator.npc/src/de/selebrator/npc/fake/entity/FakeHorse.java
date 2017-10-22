package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.HorseNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityHorse_bI;
import static de.selebrator.npc.fake.Imports.FIELD_EntityHorse_bJ;

public class FakeHorse extends FakeAbstractHorse implements HorseNPC {

	MetadataObject<Integer> variant;
	MetadataObject<Integer> armor;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.variant = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHorse_bI, 0); //15 TODO what is this?
		this.armor = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHorse_bJ, HorseArmorType.NONE.getId()); //16
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) this.getModifiedMaxHealth());
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
		this.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
	}

	public int getVariant() {
		return this.variant.get();
	}

	public void setVariant(int variant) {
		this.variant.set(variant);
	}

	public int getArmorTypeId() {
		return this.armor.get();
	}

	public void setArmorType(int id) {
		this.armor.set(id);
	}
}
