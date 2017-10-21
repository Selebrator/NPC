package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.entity.Villager;

public class FakeZombieVillagerMetadata extends FakeZombieMetadata {
	private MetadataObject<Boolean> converting = new MetadataObject<>(this.getDataWatcher(), false, "EntityZombieVillager", "b", 0); //15
	private MetadataObject<Integer> profession = new MetadataObject<>(this.getDataWatcher(), 0, "EntityZombieVillager", "c", 1); //16

	public FakeZombieVillagerMetadata() {
		super();
	}

	public boolean isConverting() {
		return this.converting.get();
	}

	public void setConverting(boolean converting) {
		this.converting.set(converting);
	}

	public Villager.Profession getProfession() {
		switch(this.profession.get()) {
			case 0:
			default:
				return Villager.Profession.NORMAL;
			case 1:
				return Villager.Profession.FARMER;
			case 2:
				return Villager.Profession.LIBRARIAN;
			case 3:
				return Villager.Profession.PRIEST;
			case 4:
				return Villager.Profession.BLACKSMITH;
			case 5:
				return Villager.Profession.BUTCHER;
		}
	}

	public void setProfession(Villager.Profession profession) {
		switch(profession) {
			case NORMAL:
				this.profession.set(0);
				break;
			case FARMER:
				this.profession.set(1);
				break;
			case LIBRARIAN:
				this.profession.set(2);
				break;
			case PRIEST:
				this.profession.set(3);
				break;
			case BLACKSMITH:
				this.profession.set(4);
				break;
			case BUTCHER:
				this.profession.set(5);
				break;
			case NITWIT:
				this.profession.set(0);
				break;
			case HUSK:
				this.profession.set(1);
				break;
		}
	}
}
