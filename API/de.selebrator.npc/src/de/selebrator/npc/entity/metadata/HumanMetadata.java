package de.selebrator.npc.entity.metadata;

import org.bukkit.inventory.MainHand;

import static de.selebrator.npc.entity.metadata.MetadataObject.SkinFlag.*;

public interface HumanMetadata extends LivingMetadata {

	float getAbsorption();

	void setAbsorption(float absorption);

	int getScore();

	void setScore(int score);

	byte getSkinFlag();

	default boolean getSkinFlag(int target) {
		return MetadataObject.getBitmaskValue(this.getSkinFlag(), target);
	}

	void setSkinFlag(byte value);

	default void setSkinFlag(int target, boolean state) {
		this.setSkinFlag(MetadataObject.setBitmaskValue(this.getSkinFlag(), target, state));
	}

	default boolean isCapeEnabled() {
		return this.getSkinFlag(CAPE);
	}

	default void enableCape(boolean state) {
		this.setSkinFlag(CAPE, state);
	}

	default boolean isJacketEnabled() {
		return this.getSkinFlag(JACKET);
	}

	default void enableJacket(boolean state) {
		this.setSkinFlag(JACKET, state);
	}

	default boolean isLeftArmEnabled() {
		return this.getSkinFlag(LEFT_SLEEVE);
	}

	default void enableLeftArm(boolean state) {
		this.setSkinFlag(LEFT_SLEEVE, state);
	}

	default boolean isRightArmEnabled() {
		return this.getSkinFlag(RIGHT_SLEEVE);
	}

	default void enableRightArm(boolean state) {
		this.setSkinFlag(RIGHT_SLEEVE, state);
	}

	default boolean isLeftLegEnabled() {
		return this.getSkinFlag(LEFT_PANTS);
	}

	default void enableLeftLeg(boolean state) {
		this.setSkinFlag(LEFT_PANTS, state);
	}

	default boolean isRightLegEnabled() {
		return this.getSkinFlag(RIGHT_PANTS);
	}

	default void enableRightLeg(boolean state) {
		this.setSkinFlag(RIGHT_PANTS, state);
	}

	default boolean isHatEnabled() {
		return this.getSkinFlag(HAT);
	}

	default void enableHat(boolean state) {
		this.setSkinFlag(HAT, state);
	}

	default void setSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
		byte value = 0x00;
		if(cape) value |= CAPE;
		if(jacket) value |= JACKET;
		if(leftArm) value |= LEFT_SLEEVE;
		if(rightArm) value |= RIGHT_SLEEVE;
		if(leftLeg) value |= LEFT_PANTS;
		if(rightLeg) value |= RIGHT_PANTS;
		if(hat) value |= HAT;
		this.setSkinFlag(value);
	}

	MainHand getMainHand();

	void setMainHand(MainHand mainHand);
}
