package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

import java.util.*;

public class FakeHorseMetadata extends FakeAbstractHorseMetadata {
	private MetadataObject<Integer> variant = new MetadataObject<>(this.getDataWatcher(), 0, "EntityHorse", "bI", 0); //15 TODO what is this?
	private MetadataObject<Integer> armor = new MetadataObject<>(this.getDataWatcher(), HorseArmorType.NONE.getId(), "EntityHorse", "bJ", 1); //16

	public FakeHorseMetadata() {
		super();
	}

	public int getVariant() {
		return this.variant.get();
	}

	public void setVariant(int variant) {
		this.variant.set(variant);
	}

	public HorseArmorType getArmorType() {
		return HorseArmorType.fromId(this.armor.get());
	}

	public enum HorseArmorType {
		NONE(0),
		IRON(1),
		GOLD(2),
		DIAMOND(3);

		private static final Map<Integer, HorseArmorType> BY_ID = new HashMap<>();

		static {
			for(HorseArmorType type : HorseArmorType.values())
				BY_ID.put(type.getId(), type);
		}

		private int id;

		HorseArmorType(int id) {
			this.id = id;
		}

		public static HorseArmorType fromId(int id) {
			return BY_ID.getOrDefault(id, NONE);
		}

		public int getId() {
			return this.id;
		}
	}
}
