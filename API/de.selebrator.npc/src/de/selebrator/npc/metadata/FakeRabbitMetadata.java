package de.selebrator.npc.metadata;

import org.bukkit.entity.Rabbit;

public class FakeRabbitMetadata extends FakeAnimalMetadata {
	private MetadataObject<Integer> variant = new MetadataObject<>(this.getDataWatcher(), 0, "EntityRabbit", "bx", 0); // 13

	public FakeRabbitMetadata() {
		super();
	}

	public Rabbit.Type getVariant() {
		switch(this.variant.get()) {
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

	public void setVariant(Rabbit.Type variant) {
		switch(variant) {
			case BROWN:
				this.variant.set(0);
				break;
			case WHITE:
				this.variant.set(1);
				break;
			case BLACK:
				this.variant.set(2);
				break;
			case BLACK_AND_WHITE:
				this.variant.set(3);
				break;
			case GOLD:
				this.variant.set(4);
				break;
			case SALT_AND_PEPPER:
				this.variant.set(5);
				break;
			case THE_KILLER_BUNNY:
				this.variant.set(99);
				break;
		}
	}
}
