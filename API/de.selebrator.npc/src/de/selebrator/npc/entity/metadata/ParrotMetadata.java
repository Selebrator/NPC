package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.Parrot;

public interface ParrotMetadata extends TameableMetadata {

	int getVariantId();

	default Parrot.Variant getVariant() {
		switch(this.getVariantId()) {
			case 0:
			default:
				return Parrot.Variant.RED;
			case 1:
				return Parrot.Variant.BLUE;
			case 2:
				return Parrot.Variant.GREEN;
			case 3:
				return Parrot.Variant.CYAN;
			case 4:
				return Parrot.Variant.GRAY;
		}
	}

	void setVariant(int id);

	default void setVariant(Parrot.Variant variant) {
		switch(variant) {
			case RED:
				this.setVariant(0);
				break;
			case BLUE:
				this.setVariant(1);
				break;
			case GREEN:
				this.setVariant(2);
				break;
			case CYAN:
				this.setVariant(3);
				break;
			case GRAY:
				this.setVariant(4);
				break;
		}
	}
}
