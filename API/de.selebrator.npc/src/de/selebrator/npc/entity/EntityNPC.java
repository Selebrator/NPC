package de.selebrator.npc.entity;

import de.selebrator.npc.*;
import de.selebrator.npc.entity.metadata.EntityMetadata;
import org.bukkit.*;
import org.bukkit.util.Vector;

import java.util.UUID;

public interface EntityNPC extends NPC, EntityMetadata {

	void spawn(Location location);

	void despawn();

	/**
	 * Look in the given direction
	 *
	 * @param yaw   absolute yaw in degrees
	 * @param pitch absolute pitch in degrees
	 */
	void look(float yaw, float pitch);

	/**
	 * Look at the given ~position
	 *
	 * @param dx relative x
	 * @param dy relative y
	 * @param dz relative z
	 */
	default void look(double dx, double dy, double dz) {
		float yaw = MathHelper.calcYaw(dx, dz);
		float pitch = MathHelper.calcPitch(dx, dy, dz);

		this.look(yaw, pitch);
	}

//	/**
//	 * Look at the given location
//	 *
//	 * @param location absolute target location
//	 */
//	default void look(Location location) {
//		Vector distance = MathHelper.calcDistanceVector(this.getEyeLocation(), location);
//
//		this.look(distance.getX(), distance.getY(), distance.getZ());
//	}

	/**
	 * Move to the given ~position
	 * x, y AND z MUST BE SMALLER THAN 8
	 *
	 * @param dx relative x
	 * @param dy relative y
	 * @param dz relative z
	 */
	void move(double dx, double dy, double dz);

	/**
	 * Move to the given location
	 * CANT BE FARTHER AWAY THAN 8 BLOCKS IN ANY DIRECTION
	 *
	 * @param location absolute target location
	 */
	default void move(Location location) {
		Vector distance = MathHelper.calcDistanceVector(this.getLocation(), location);

		this.move(distance.getX(), distance.getY(), distance.getZ());
	}

	void teleport(Location location);

	void playAnimation(Animation animation);

	void playEffect(EntityEffect effect);

	void updateMetadata();

	int getEntityId();

	UUID getUniqueId();

	int getNoDamageTicks();

	void setNoDamageTicks(int noDamageTicks);

	int getFireTicks();

	void setFireTicks(int fireTicks);

	/** @return whether or not the NPC is invulnerable in general */
	boolean isInvulnerable();

	void setInvulnerable(boolean invulnerable);

	boolean hasLocation();

	Location getLocation();

	boolean isAlive();

	boolean isFrozen();

	void setFrozen(boolean frozen);

	void tick();
}
