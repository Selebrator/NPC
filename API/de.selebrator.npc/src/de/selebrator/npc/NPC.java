package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.metadata.FakeMetadata;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public interface NPC extends Attributable {

	void spawn(Location location);

	void respawn(Location location);

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

	/**
	 * Look at the given location
	 *
	 * @param location absolute target location
	 */
	default void look(Location location) {
		Vector distance = MathHelper.calcDistanceVector(this.getEyeLocation(), location);

		this.look(distance.getX(), distance.getY(), distance.getZ());
	}

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

	void teleport(Location location);

	void playAnimation(NPCAnimationEvent.Animation animation);

	void playEntityStatus(EnumEntityStatus status);

	/**
	 * alternative to playEntityStatus(EnumEntityStatus) for similarity to Bukkit
	 */
	default void playEffect(EntityEffect effect) {
		this.playEntityStatus(EnumEntityStatus.fromBukkit(effect));
	}

	void updateMetadata();

	void updateGameProfile(GameProfile gameProfile);

	int getEntityId();

	FakeMetadata getMeta();

	void setMeta(FakeMetadata meta);

	String getName();

	String getDisplayName();

	boolean isAlive();

	boolean isFrozen();

	void freeze(boolean freeze);

	float getHealth();

	void setHealth(float health);

	default void damage(float amount) {
		this.damage(amount, EntityDamageEvent.DamageCause.CUSTOM);
	}

	void damage(float amount, EntityDamageEvent.DamageCause cause);

	int getNoDamageTicks();

	void setNoDamageTicks(int noDamageTicks);

	int getRemainingAir();

	void setRemainingAir(int air);

	int getFireTicks();

	void setFireTicks(int fireTicks);

	/**
	 * @return movespeed in blocks/second
	 */
	double getMoveSpeed();

	default double getEyeHeight() {
		return this.getEyeHeight(false);
	}

	double getEyeHeight(boolean ignoreSneaking);

	boolean hasLocation();

	Location getLocation();

	default Location getEyeLocation() {
		return this.hasLocation() ? this.getLocation().clone().add(0, this.getEyeHeight(false), 0) : null;
	}

	Location getRespawnLocation();

	void setRespawnLocation(Location location);

	boolean hasTarget();

	LivingEntity getTarget();

	void setTarget(LivingEntity target);

	boolean isInvulnerable();

	void setInvulnerable(boolean invulnerable);

	EnumNature getNature();

	void setNature(EnumNature nature);

	FakeEquipment getEquipment();

	boolean hasPotionEffect(PotionEffectType potionEffectType);

	PotionEffect getPotionEffect(PotionEffectType potionEffectType);

	Collection<PotionEffect> getActivePotionEffects();

	void addPotionEffect(PotionEffect potionEffect);

	void addPotionEffects(Collection<PotionEffect> potionEffects);

	void removePotionEffect(PotionEffectType potionEffectType);

	void setSneaking(boolean state);

	void setSprinting(boolean state);

	boolean touches(Block block);

	List<Block> getTouchedBlocks();

	List<Block> getSurroundingBlocks();

	void attack(LivingEntity target);

	void tick();
}
