package de.selebrator.npc.entity.metadata;

public interface TameableMetadata extends AnimalMetadata, Ownable {

	boolean getTameableAnimalInfo(TameableAnimalInfo target);

	void setTameableAnimalInfo(TameableAnimalInfo target, boolean state);

	default boolean isSitting() {
		return this.getTameableAnimalInfo(TameableAnimalInfo.SITTING);
	}

	default void setSitting(boolean state) {
		this.setTameableAnimalInfo(TameableAnimalInfo.SITTING, state);
	}

	default boolean isAngry() {
		return this.getTameableAnimalInfo(TameableAnimalInfo.ANGRY);
	}

	default void setAngry(boolean state) {
		this.setTameableAnimalInfo(TameableAnimalInfo.ANGRY, state);
	}

	default boolean isTamed() {
		return this.getTameableAnimalInfo(TameableAnimalInfo.TAMED);
	}

	default void setTamed(boolean state) {
		this.setTameableAnimalInfo(TameableAnimalInfo.TAMED, state);
	}

	enum TameableAnimalInfo {
		SITTING(0),
		ANGRY(1),
		TAMED(2);

		private byte id;

		TameableAnimalInfo(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
