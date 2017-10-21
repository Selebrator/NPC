package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.entity.Parrot;

public class FakeParrotMetadata extends FakeTameableAnimalMetadata {
	private MetadataObject<Integer> variant = new MetadataObject<>(this.getDataWatcher(), 0, "EntityParrot", "bG", 0); //15

	public FakeParrotMetadata() {
		super();
	}

	public Parrot.Variant getVariant() {
		switch(this.variant.get()) {
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

	public void setVariant(Parrot.Variant variant) {
		switch(variant) {
			case RED:
				this.variant.set(0);
				break;
			case BLUE:
				this.variant.set(1);
				break;
			case GREEN:
				this.variant.set(2);
				break;
			case CYAN:
				this.variant.set(3);
				break;
			case GRAY:
				this.variant.set(4);
				break;
		}
	}
}
