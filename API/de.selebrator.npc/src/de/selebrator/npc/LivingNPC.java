package de.selebrator.npc;

import de.selebrator.npc.entity.*;
import de.selebrator.npc.metadata.FakeLivingMetadata;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.*;
import org.bukkit.util.Vector;

import java.util.Collection;

public interface LivingNPC extends EntityNPC, Attributable {

	@Override
	FakeLivingMetadata getMeta();

	/**
	 * Make a step in the given direction
	 *
	 * @param yaw   absolute yaw in degrees
	 * @param pitch absolute yaw in degrees
	 */
	default void step(float yaw, float pitch) {
		Vector direction = MathHelper.calcDirectionVector(this.getMoveSpeed() / 20, yaw, pitch);

		this.move(direction.getX(), direction.getY(), direction.getZ());
	}

	/**
	 * Make a step towards the given ~position
	 *
	 * @param dx relative x
	 * @param dy relative y
	 * @param dz relative z
	 */
	default void step(double dx, double dy, double dz) {
		float yaw = MathHelper.calcYaw(dx, dz);
		float pitch = MathHelper.calcPitch(dx, dy, dz);

		this.step(yaw, pitch);
	}

	/**
	 * Make a step towards the given location
	 *
	 * @param location absolute target location
	 */
	default void step(Location location) {
		Vector distance = MathHelper.calcDistanceVector(this.getLocation(), location);

		this.step(distance.getX(), distance.getY(), distance.getZ());
	}

	/**
	 * @return movespeed in blocks/second
	 */
	double getMoveSpeed();

	boolean hasPotionEffect(PotionEffectType potionEffectType);

	PotionEffect getPotionEffect(PotionEffectType potionEffectType);

	Collection<PotionEffect> getActivePotionEffects();

	void addPotionEffect(PotionEffect potionEffect);

	void addPotionEffects(Collection<PotionEffect> potionEffects);

	void removePotionEffect(PotionEffectType potionEffectType);

	/**
	 * Checks if the {@link FakeLiving} entity is invulnerable to that {@link EntityDamageEvent.DamageCause}
	 * Does NOT check if the {@link FakeEntity} {@link #isInvulnerable()} in general
	 *
	 * @param cause the checked {@link EntityDamageEvent.DamageCause}
	 * @return true if the {@link FakeLiving} is immune to that {@link EntityDamageEvent.DamageCause}
	 */
	boolean isInvulnerable(EntityDamageEvent.DamageCause cause);

	float getHealth();

	void setHealth(float health);

	default void damage(float amount) {
		this.damage(amount, EntityDamageEvent.DamageCause.CUSTOM);
	}

	void damage(float amount, EntityDamageEvent.DamageCause cause);
}
