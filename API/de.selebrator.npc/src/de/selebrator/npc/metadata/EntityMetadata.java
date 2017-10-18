package de.selebrator.npc.metadata;

import org.bukkit.Nameable;

public interface EntityMetadata extends Nameable {

	boolean getStatus(Status target);

	void setStatus(Status target, boolean state);

	default boolean isOnFire() {
		return this.getStatus(Status.FIRE);
	}

	default void setOnFire(boolean state) {
		this.setStatus(Status.FIRE, state);
	}

	default boolean isSneaking() {
		return this.getStatus(Status.SNEAK);
	}

	default void setSneaking(boolean state) {
		this.setStatus(Status.SNEAK, state);
	}

	default boolean isSprinting() {
		return this.getStatus(Status.SPRINT);
	}

	default void setSprinting(boolean state) {
		this.setStatus(Status.SPRINT, state);
	}

	default boolean isInvisible() {
		return this.getStatus(Status.INVISIBLE);
	}

	default void setInvisible(boolean state) {
		this.setStatus(Status.INVISIBLE, state);
	}

	default boolean isGlowing() {
		return this.getStatus(Status.GLOW);
	}

	default void setGlowing(boolean state) {
		this.setStatus(Status.GLOW, state);
	}

	default boolean isGliding() {
		return this.getStatus(Status.ELYTRA);
	}

	default void setGliding(boolean state) {
		this.setStatus(Status.ELYTRA, state);
	}

	void setStatus(boolean fire, boolean sneak, boolean sprint, boolean invisible, boolean glow, boolean gliding);

	int getRemainingAir();

	void setRemainingAir(int air);

	void setCustomNameVisible(boolean var1);

	boolean isCustomNameVisible();

	boolean isSilent();

	void setSilent(boolean silent);

	boolean hasGravity();

	void setGravity(boolean gravity);

	enum Status {
		FIRE(0),
		SNEAK(1),
		SPRINT(3),
		INVISIBLE(5),
		GLOW(6),
		ELYTRA(7);

		private byte id;

		Status(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
