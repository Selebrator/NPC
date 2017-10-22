package de.selebrator.npc.entity.metadata;

import org.bukkit.Material;

import java.util.*;

public interface HorseMetadata extends AbstractHorseMetadata {

	int getVariant();

	void setVariant(int variant);

	int getArmorTypeId();

	default HorseArmorType getArmorType() {
		return HorseArmorType.fromId(this.getArmorTypeId());
	}

	void setArmorType(int id);

	default void setArmorType(HorseArmorType type) {
		this.setArmorType(type.getId());
	}

	enum HorseArmorType {
		NONE(0, null),
		IRON(1, Material.IRON_BARDING),
		GOLD(2, Material.GOLD_BARDING),
		DIAMOND(3, Material.DIAMOND_BARDING);

		private static final Map<Integer, HorseArmorType> BY_ID = new HashMap<>();

		static {
			for(HorseArmorType type : HorseArmorType.values())
				BY_ID.put(type.getId(), type);
		}

		private int id;
		private Material material;

		HorseArmorType(int id, Material material) {
			this.id = id;
		}

		public static HorseArmorType fromId(int id) {
			return BY_ID.getOrDefault(id, NONE);
		}

		public int getId() {
			return this.id;
		}

		public Material getMaterial() {
			return this.material;
		}
	}
}
