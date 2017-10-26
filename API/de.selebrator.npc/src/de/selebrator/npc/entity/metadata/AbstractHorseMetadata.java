package de.selebrator.npc.entity.metadata;

public interface AbstractHorseMetadata extends AnimalMetadata, Ownable {

	boolean getAbstractHorseInfo(AbstractHorseInfo target);

	void setAbstractHorseInfo(AbstractHorseInfo target, boolean state);

	default boolean isTamed() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.TAMED);
	}

	default void setTamed(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.TAMED, state);
	}

	default boolean isSaddled() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.SADDLED);
	}

	default void setSaddled(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.SADDLED, state);
	}

	default boolean isBred() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.BRED);
	}

	default void setBred(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.BRED, state);
	}

	default boolean isEating() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.EATING);
	}

	default void setEating(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.EATING, state);
	}

	default boolean isRearing() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.REARING);
	}

	default void setRearing(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.REARING, state);
	}

	default boolean isMouthOpen() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.MOUTH_OPEN);
	}

	default void setMouthOpen(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.MOUTH_OPEN, state);
	}

	enum AbstractHorseInfo {
		TAMED(1),
		SADDLED(2),
		BRED(3),
		EATING(4),
		REARING(5),
		MOUTH_OPEN(6);

		private byte id;

		AbstractHorseInfo(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
