package de.selebrator.npc.metadata;

import org.bukkit.entity.Ocelot;

public class FakeOcelotMetadata extends FakeTameableAnimalMetadata {
	private MetadataObject<Integer> variant = new MetadataObject<>(this.getDataWatcher(), 0, "EntityOcelot", "bB", 0); //15

	public FakeOcelotMetadata() {
		super();
	}

	public Ocelot.Type getVariant() {
		switch(this.variant.get()) {
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

	public void setVariant(Ocelot.Type variant) {
		switch(variant) {
			case WILD_OCELOT:
				this.variant.set(0);
				break;
			case BLACK_CAT:
				this.variant.set(1);
				break;
			case RED_CAT:
				this.variant.set(2);
				break;
			case SIAMESE_CAT:
				this.variant.set(3);
				break;
		}
	}
}
