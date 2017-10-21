package de.selebrator.npc.entity.metadata;

import org.bukkit.entity.Villager;

public interface ZombieVillagerMetadata extends ZombieMetadata {
	boolean isConverting();

	void setConverting(boolean converting);

	int getProfessionId();

	default Villager.Profession getProfession() {
		switch(this.getProfessionId()) {
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

	void setProfession(int id);

	default void setProfession(Villager.Profession profession) {
		switch(profession) {
			case NORMAL:
				this.setProfession(0);
				break;
			case FARMER:
				this.setProfession(1);
				break;
			case LIBRARIAN:
				this.setProfession(2);
				break;
			case PRIEST:
				this.setProfession(3);
				break;
			case BLACKSMITH:
				this.setProfession(4);
				break;
			case BUTCHER:
				this.setProfession(5);
				break;
			case NITWIT:
				this.setProfession(0);
				break;
			case HUSK:
				this.setProfession(1);
				break;
		}
	}
}
