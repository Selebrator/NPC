package de.selebrator.npc.metadata;

import org.bukkit.inventory.MainHand;

public interface HumanMetadata extends LivingMetadata {

	float getAbsorption();

	void setAbsorption(float absorption);

	int getScore();

	void setScore(int score);

	boolean getSkinFlag(SkinFlag target);

	void setSkinFlag(SkinFlag target, boolean state);

	default boolean isCapeEnabled() {
		return this.getSkinFlag(SkinFlag.CAPE);
	}

	default void enableCape(boolean state) {
		this.setSkinFlag(SkinFlag.CAPE, state);
	}

	default boolean isJacketEnabled() {
		return this.getSkinFlag(SkinFlag.JACKET);
	}

	default void enableJacket(boolean state) {
		this.setSkinFlag(SkinFlag.JACKET, state);
	}

	default boolean isLeftArmEnabled() {
		return this.getSkinFlag(SkinFlag.LEFT_SLEEVE);
	}

	default void enableLeftArm(boolean state) {
		this.setSkinFlag(SkinFlag.LEFT_SLEEVE, state);
	}

	default boolean isRightArmEnabled() {
		return this.getSkinFlag(SkinFlag.RIGHT_SLEEVE);
	}

	default void enableRightArm(boolean state) {
		this.setSkinFlag(SkinFlag.RIGHT_SLEEVE, state);
	}

	default boolean isLeftLegEnabled() {
		return this.getSkinFlag(SkinFlag.LEFT_PANTS);
	}

	default void enableLeftLeg(boolean state) {
		this.setSkinFlag(SkinFlag.LEFT_PANTS, state);
	}

	default boolean isRightLegEnabled() {
		return this.getSkinFlag(SkinFlag.RIGHT_PANTS);
	}

	default void enableRightLeg(boolean state) {
		this.setSkinFlag(SkinFlag.RIGHT_PANTS, state);
	}

	default boolean isHatEnabled() {
		return this.getSkinFlag(SkinFlag.HAT);
	}

	default void enableHat(boolean state) {
		this.setSkinFlag(SkinFlag.HAT, state);
	}

	void setSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat);

	MainHand getMainHand();

	void setMainHand(MainHand mainHand);

	enum SkinFlag {
		CAPE(0),
		JACKET(1),
		LEFT_SLEEVE(2),
		RIGHT_SLEEVE(3),
		LEFT_PANTS(4),
		RIGHT_PANTS(5),
		HAT(6);

		byte id;

		SkinFlag(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
