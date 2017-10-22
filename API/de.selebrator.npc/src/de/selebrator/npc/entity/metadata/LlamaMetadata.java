package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.Llama;

public interface LlamaMetadata extends ChestedHorseMetadata {

	int getStrength();

	void setStrength(int strength);

	int getCarpetColor();

	void setCarpetColor(int color);

	//TODO add getter and setter using Bukkit Color

	int getVariantId();

	default Llama.Color getVariant() {
		switch(this.getVariantId()) {
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

	void setVariant(int id);

	default void setVariant(Llama.Color variant) {
		switch(variant) {
			case CREAMY:
				this.setVariant(0);
				break;
			case WHITE:
				this.setVariant(1);
				break;
			case BROWN:
				this.setVariant(2);
				break;
			case GRAY:
				this.setVariant(3);
				break;
		}
	}
}
