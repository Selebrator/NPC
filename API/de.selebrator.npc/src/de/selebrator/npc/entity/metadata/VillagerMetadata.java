package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.Villager;

public interface VillagerMetadata extends AgeableMetadata {

	int getProfessionId();

	default Villager.Profession getProfession() {
		switch(this.getProfessionId()) {
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

	void setProfession(int id);

	default void setProfession(Villager.Profession profession) {
		switch(profession) {
			case FARMER:
				this.setProfession(0);
				break;
			case LIBRARIAN:
				this.setProfession(1);
				break;
			case PRIEST:
				this.setProfession(2);
				break;
			case BLACKSMITH:
				this.setProfession(3);
				break;
			case BUTCHER:
				this.setProfession(4);
				break;
			case NITWIT:
				this.setProfession(5);
				break;
			case NORMAL:
			case HUSK:
				throw new IllegalArgumentException("Profession is reserved for Zombies: " + profession.name());
			default:
				if(profession.isZombie())
					throw new IllegalArgumentException("Profession is reserved for Zombies: " + profession.name());
				else
					this.setProfession(0);
		}
	}
}
