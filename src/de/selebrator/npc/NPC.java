package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

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

	void equip(EnumEquipmentSlot slot, ItemStack item);

	void playAnimation(EnumAnimation animation);

	void setEntityStatus(EnumEntityStatus status);

	void updateMetadata();

	void updateGameProfile(GameProfile gameProfile);

	int getEntityId();

	FakePlayerMeta getMeta();

	void setMeta(FakePlayerMeta meta);

	String getName();

	String getDisplayName();

	boolean isAlive();

	float getHealth();

	void damage(int amount);

	void damage(int amount, EntityDamageEvent.DamageCause cause);

	int getNoDamageTicks();

	void setNoDamageTicks(int noDamageTicks);

	int getAir();

	void setAir(int air);

	int getFireTicks();

	void setFireTicks(int fireTicks);

	/**
	 *
	 * @return movespeed in blocks/second
	 */
	double getMoveSpeed();

	/**
	 *
	 * @param speed movespeed in blocks/second
	 */
	void setMoveSpeed(double speed);

	double getEyeHeight(boolean ignoreSneaking);

	boolean hasLocation();

	Location getLocation();

	Location getEyeLocation();

	Location getRespawnLocation();

	void setRespawnLocation(Location location);

	boolean hasTarget();

	LivingEntity getTarget();

	void setTarget(LivingEntity target);

	EnumNature getNature();

	void setNature(EnumNature nature);

	boolean hasEquipment(EnumEquipmentSlot slot);

	ItemStack getEquipment(EnumEquipmentSlot slot);

	void setSneaking(boolean state);

	void setSprinting(boolean state);

	boolean touches(Block block);

	List<Block> getTouchedBlocks();

	List<Block> getSurroundingBlocks();

	void attack(LivingEntity target);

	void tick();
}
