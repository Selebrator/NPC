package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.Rabbit;

public interface RabbitMetadata extends AnimalMetadata {

	int getVariantId();

	default Rabbit.Type getVariant() {
		switch(this.getVariantId()) {
			case 0:
			default:
				return Rabbit.Type.BROWN;
			case 1:
				return Rabbit.Type.WHITE;
			case 2:
				return Rabbit.Type.BLACK;
			case 3:
				return Rabbit.Type.BLACK_AND_WHITE;
			case 4:
				return Rabbit.Type.GOLD;
			case 5:
				return Rabbit.Type.SALT_AND_PEPPER;
			case 99:
				return Rabbit.Type.THE_KILLER_BUNNY;
		}
	}

	void setVariant(int id);

	default void setVariant(Rabbit.Type variant) {
		switch(variant) {
			case BROWN:
				this.setVariant(0);
				break;
			case WHITE:
				this.setVariant(1);
				break;
			case BLACK:
				this.setVariant(2);
				break;
			case BLACK_AND_WHITE:
				this.setVariant(3);
				break;
			case GOLD:
				this.setVariant(4);
				break;
			case SALT_AND_PEPPER:
				this.setVariant(5);
				break;
			case THE_KILLER_BUNNY:
				this.setVariant(99);
				break;
		}
	}
}
