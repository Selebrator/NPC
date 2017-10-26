package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.Ocelot;

public interface OcelotMetadata extends TameableMetadata {

	int getVariantId();

	default Ocelot.Type getVariant() {
		switch(this.getVariantId()) {
			case 0:
			default:
				return Ocelot.Type.WILD_OCELOT;
			case 1:
				return Ocelot.Type.BLACK_CAT;
			case 2:
				return Ocelot.Type.RED_CAT;
			case 3:
				return Ocelot.Type.SIAMESE_CAT;
		}
	}

	void setVariant(int id);

	default void setVariant(Ocelot.Type variant) {
		switch(variant) {
			case WILD_OCELOT:
				this.setVariant(0);
				break;
			case BLACK_CAT:
				this.setVariant(1);
				break;
			case RED_CAT:
				this.setVariant(2);
				break;
			case SIAMESE_CAT:
				this.setVariant(3);
				break;
		}
	}
}
