package de.selebrator.npc.metadata;

import org.bukkit.entity.Villager;

public class FakeVillagerMetadata extends FakeAgeableMetadata {
	private MetadataObject<Integer> profession = new MetadataObject<>(this.getDataWatcher(), 0, "EntityVillager", "bz", 0); //13

	public FakeVillagerMetadata() {
		super();
	}

	public Villager.Profession getProfession() {
		switch(this.profession.get()) {
			case 0:
			default:
				return Villager.Profession.FARMER;
			case 1:
				return Villager.Profession.LIBRARIAN;
			case 2:
				return Villager.Profession.PRIEST;
			case 3:
				return Villager.Profession.BLACKSMITH;
			case 4:
				return Villager.Profession.BUTCHER;
			case 5:
				return Villager.Profession.NITWIT;
		}
	}

	public void setProfession(Villager.Profession profession) {
		switch(profession) {
			case FARMER:
				this.profession.set(0);
				break;
			case LIBRARIAN:
				this.profession.set(1);
				break;
			case PRIEST:
				this.profession.set(2);
				break;
			case BLACKSMITH:
				this.profession.set(3);
				break;
			case BUTCHER:
				this.profession.set(4);
				break;
			case NITWIT:
				this.profession.set(5);
				break;
			case NORMAL:
			case HUSK:
				throw new IllegalArgumentException("Profession is reserved for Zombies: " + profession.name());
			default:
				if(profession.isZombie())
					throw new IllegalArgumentException("Profession is reserved for Zombies: " + profession.name());
				else
					this.profession.set(0);
		}
	}
}
