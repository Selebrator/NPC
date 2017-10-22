package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.LlamaNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.*;

public class FakeLlama extends FakeChestedHorse implements LlamaNPC {

	MetadataObject<Integer> strength;
	MetadataObject<Integer> carpetColor;
	MetadataObject<Integer> variant;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.strength = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLlama_bH, 0); //16
		this.carpetColor = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLlama_bI, -1); //17
		this.variant = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLlama_bJ, 0); //18
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40.0D);
	}

	public int getStrength() {
		return this.strength.get();
	}

	public void setStrength(int strength) {
		this.strength.set(strength);
	}

	public int getCarpetColor() {
		return this.carpetColor.get();
	}

	public void setCarpetColor(int color) {
		this.carpetColor.set(color);
	}

	public int getVariantId() {
		return this.variant.get();
	}

	public void setVariant(int id) {
		this.variant.set(id);
	}
}
