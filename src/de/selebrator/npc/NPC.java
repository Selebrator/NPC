package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.metadata.FakeMetadata;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public interface NPC {

	void spawn(Location location);

	void respawn(Location location);

	void despawn();

	/**
	 * Look in the given direction
	 * @param yaw absolute yaw in degrees
	 * @param pitch absolute pitch in degrees
	 */
	void look(float yaw, float pitch);

	/**
	 * Look at the given ~position
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 */
	void look(double x, double y, double z);

	/**
	 * Look at the given location
	 * @param location absolute target location
	 */
	void look(Location location);

	/**
	 * Move to the given ~position
	 * x, y AND z MUST BE SMALLER THAN 8
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 */
	void move(double x, double y, double z);

	/**
	 * Move to the given location
	 * CANT BE FARTHER AWAY THAN 8 BLOCKS IN ANY DIRECTION
	 * @param location absolute target location
	 */
	void move(Location location);

	/**
	 * Make a step in the given direction
	 * @param yaw absolute yaw in degrees
	 * @param pitch absolute yaw in degrees
	 */
	void step(float yaw, float pitch);

	/**
	 * Make a step towards the given ~position
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 */
	void step(double x, double y, double z);

	/**
	 * Make a step towards the given location
	 * @param location absolute target location
	 */
	void step(Location location);

	void teleport(Location location);

	void playAnimation(NPCAnimationEvent.Animation animation);

	void playEntityStatus(EnumEntityStatus status);

	/** alternative to playEntityStatus(EnumEntityStatus) for similarity to Bukkit */
	void playEffect(EntityEffect effect);

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

	void damage(float amount);

	void damage(float amount, EntityDamageEvent.DamageCause cause);

	int getNoDamageTicks();

	void setNoDamageTicks(int noDamageTicks);

	int getRemainingAir();

	void setRemainingAir(int air);

	int getFireTicks();

	void setFireTicks(int fireTicks);

	/**
	 *
	 * @return movespeed in blocks/second
	 */
	double getMoveSpeed();

	double getEyeHeight();

	double getEyeHeight(boolean ignoreSneaking);

	boolean hasLocation();

	Location getLocation();

	Location getEyeLocation();

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

	AttributeInstance getAttribute(Attribute attribute);

	void setSneaking(boolean state);

	void setSprinting(boolean state);

	boolean touches(Block block);

	List<Block> getTouchedBlocks();

	List<Block> getSurroundingBlocks();

	void attack(LivingEntity target);

	void tick();
}
