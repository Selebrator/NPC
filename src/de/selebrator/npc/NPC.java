package de.selebrator.npc;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface NPC {

	FakePlayer spawn(Location location);

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

	int getEntityId();

	FakePlayerMeta getMeta();

	boolean isAlive();
}
