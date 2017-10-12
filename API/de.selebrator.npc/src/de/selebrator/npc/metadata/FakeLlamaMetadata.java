package de.selebrator.npc.metadata;

import org.bukkit.entity.Llama;

public class FakeLlamaMetadata extends FakeChestedHorseMetadata {
	private MetadataObject<Integer> strength = new MetadataObject<>(this.getDataWatcher(), 0, "EntityLlama", "bH", 0); //16
	private MetadataObject<Integer> carpetColor = new MetadataObject<>(this.getDataWatcher(), 0, "EntityLlama", "bI", 0); //17
	private MetadataObject<Integer> variant = new MetadataObject<>(this.getDataWatcher(), 0, "EntityLlama", "bJ", 0); //18

	public FakeLlamaMetadata() {
		super();
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

	public Llama.Color getVariant() {
		switch(this.carpetColor.get()) {
			case 0:
			default:
				return Llama.Color.CREAMY;
			case 1:
				return Llama.Color.WHITE;
			case 2:
				return Llama.Color.BROWN;
			case 3:
				return Llama.Color.GRAY;
		}
	}

	public void setVariant(Llama.Color variant) {
		switch(variant) {
			case CREAMY:
				this.variant.set(0);
				break;
			case WHITE:
				this.variant.set(1);
				break;
			case BROWN:
				this.variant.set(2);
				break;
			case GRAY:
				this.variant.set(3);
				break;
		}
	}
}
