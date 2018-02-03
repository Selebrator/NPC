package de.selebrator.npc.entity.metadata;

import org.bukkit.Nameable;

import static de.selebrator.npc.entity.metadata.MetadataObject.StatusFlag.*;

public interface EntityMetadata extends Nameable {

	byte getStatus();

	default boolean getStatus(int target) {
		return MetadataObject.getBitmaskValue(this.getStatus(), target);
	}

	void setStatus(byte value);

	default void setStatus(int target, boolean state) {
		this.setStatus(MetadataObject.setBitmaskValue(this.getStatus(), target, state));
	}

	default boolean isBurning() {
		return this.getStatus(BURNING);
	}

	default void setBurning(boolean state) {
		this.setStatus(BURNING, state);
	}

	default boolean isSneaking() {
		return this.getStatus(SNEAKING);
	}

	default void setSneaking(boolean state) {
		this.setStatus(SNEAKING, state);
	}

	default boolean isSprinting() {
		return this.getStatus(SPRINTING);
	}

	default void setSprinting(boolean state) {
		this.setStatus(SPRINTING, state);
	}

	default boolean isInvisible() {
		return this.getStatus(INVISIBLE);
	}

	default void setInvisible(boolean state) {
		this.setStatus(INVISIBLE, state);
	}

	default boolean isGlowing() {
		return this.getStatus(GLOWING);
	}

	default void setGlowing(boolean state) {
		this.setStatus(GLOWING, state);
	}

	default boolean isGliding() {
		return this.getStatus(GLIDING);
	}

	default void setGliding(boolean state) {
		this.setStatus(GLIDING, state);
	}

	default void setStatus(boolean burn, boolean sneak, boolean sprint, boolean invisible, boolean glow, boolean gliding) {
		byte value = 0x00;
		if(burn) value |= BURNING;
		if(sneak) value |= SNEAKING;
		if(sprint) value |= SPRINTING;
		if(invisible) value |= INVISIBLE;
		if(glow) value |= GLOWING;
		if(gliding) value |= GLIDING;
		this.setStatus(value);
	}

	int getRemainingAir();

	void setRemainingAir(int air);

	void setCustomNameVisible(boolean var1);

	boolean isCustomNameVisible();

	boolean isSilent();

	void setSilent(boolean silent);

	boolean hasGravity();

	void setGravity(boolean gravity);
}
