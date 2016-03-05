package me.selebrator.npc;

import java.util.Arrays;

import me.selebrator.reflection.Reflection;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R1.WorldSettings.EnumGamemode;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

public class FakePlayer {
	
	private int entityID;
	@SuppressWarnings("unused")
	private Plugin plugin;
	private GameProfile gameProfile;
	private NullDataWatcher dataWatcher;

	private Location location;
	private float health = 20;
	private LivingEntity target;

	private ItemStack[] equip = new ItemStack[6];


	public FakePlayer(GameProfile gameProfile, Plugin plugin) {
		this.entityID = (int) Reflection.getField(Entity.class, "entityCount").get(null);
		Reflection.getField(Entity.class, "entityCount").set(null, this.entityID + 1);
		
		this.gameProfile = gameProfile;
		this.plugin = plugin;
		
		this.dataWatcher = new NullDataWatcher();
		status(false, false, false, false, false, false, false);
		this.dataWatcher.set(EnumDataWatcherObject.LIVING_HELATH_06, 20F);
		skinFlags(true, true, true, true, true, true, true);
	}
	
	public void updateGameProfile(GameProfile gameProfile) {
		despawn();
		this.gameProfile = gameProfile;
		spawn(this.location);
	}

	public void spawn(Location location) {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER);
		Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, EnumGamemode.NOT_SET, new ChatComponentText(this.gameProfile.getName()))));
		
		this.location = location;
		PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn();
		Reflection.getField(namedEntitySpawn.getClass(), "a").set(namedEntitySpawn, this.entityID);
		Reflection.getField(namedEntitySpawn.getClass(), "b").set(namedEntitySpawn, this.gameProfile.getId());
		Reflection.getField(namedEntitySpawn.getClass(), "c").set(namedEntitySpawn, this.location.getX());
		Reflection.getField(namedEntitySpawn.getClass(), "d").set(namedEntitySpawn, this.location.getY());
		Reflection.getField(namedEntitySpawn.getClass(), "e").set(namedEntitySpawn, this.location.getZ());
		Reflection.getField(namedEntitySpawn.getClass(), "f").set(namedEntitySpawn, toAngle(this.location.getYaw()));
		Reflection.getField(namedEntitySpawn.getClass(), "g").set(namedEntitySpawn, toAngle(this.location.getPitch()));
		Reflection.getField(namedEntitySpawn.getClass(), "h").set(namedEntitySpawn, this.dataWatcher.toNMS());
		
		sendPackets(playerInfo, namedEntitySpawn);
		
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			this.equip(slot, this.equip[slot.getID()]);
		}
	}
	
	public void despawn() {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
		Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, null, null)));

		PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(this.entityID);
		
		sendPackets(playerInfo, entityDestroy);
	}

	public void look(Location location) {
		double differenceX = location.getX() - this.location.getX();
		double differenceY = location.getY() - (this.location.getY() + 1.6);
		double differenceZ = location.getZ() - this.location.getZ();
		double hypotenuseXZ = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ);
		float yaw = (float) (Math.atan2(differenceZ, differenceX) * 180D / Math.PI) - 90F;
		float pitch = (float) -(Math.atan2(differenceY, hypotenuseXZ) * 180D / Math.PI);
		
		PacketPlayOutEntityLook entityLook = new PacketPlayOutEntityLook();
		Reflection.getField(entityLook.getClass().getSuperclass(), "a").set(entityLook, this.entityID);
		Reflection.getField(entityLook.getClass().getSuperclass(), "e").set(entityLook, toAngle(yaw));
		Reflection.getField(entityLook.getClass().getSuperclass(), "f").set(entityLook, toAngle(pitch));
		Reflection.getField(entityLook.getClass().getSuperclass(), "g").set(entityLook, false);				
		Reflection.getField(entityLook.getClass().getSuperclass(), "h").set(entityLook, true);
		
		PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
		Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityID);
		Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, toAngle(yaw));
		
		sendPackets(entityLook, entityHeadRotation);
		
		this.location.setYaw(yaw);
		this.location.setPitch(pitch);
	}
	
	public void teleport(Location location) {
		PacketPlayOutEntityTeleport entityTeleport = new PacketPlayOutEntityTeleport();
		Reflection.getField(entityTeleport.getClass(), "a").set(entityTeleport, this.entityID);
		Reflection.getField(entityTeleport.getClass(), "b").set(entityTeleport, location.getX());
		Reflection.getField(entityTeleport.getClass(), "c").set(entityTeleport, location.getY());
		Reflection.getField(entityTeleport.getClass(), "d").set(entityTeleport, location.getZ());
		Reflection.getField(entityTeleport.getClass(), "e").set(entityTeleport, toAngle(location.getYaw()));
		Reflection.getField(entityTeleport.getClass(), "f").set(entityTeleport, toAngle(location.getPitch()));
		Reflection.getField(entityTeleport.getClass(), "g").set(entityTeleport, false);		//onGround
		
		PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
		Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityID);
		Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, toAngle(location.getYaw()));
		
		sendPackets(entityTeleport, entityHeadRotation);

		this.location = location;
	}

	public void walk(Location location) {
		// TODO	
	}

	public void equip(EquipmentSlot slot, ItemStack item) {
		PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment();
		Reflection.getField(entityEquipment.getClass(), "a").set(entityEquipment, this.entityID);
		Reflection.getField(entityEquipment.getClass(), "b").set(entityEquipment, slot.getSlot());
		Reflection.getField(entityEquipment.getClass(), "c").set(entityEquipment, CraftItemStack.asNMSCopy(item));		//itemStack
		sendPackets(entityEquipment);
		
		this.equip[slot.getID()] = item;
	}
	
	public void playAnimation(Animation anim) {
		PacketPlayOutAnimation animation = new PacketPlayOutAnimation();
		Reflection.getField(animation.getClass(), "a").set(animation, this.entityID);
		Reflection.getField(animation.getClass(), "b").set(animation, anim.getId());
		sendPackets(animation);
	}

	public void setStatus(Status status) {
		PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus();
		Reflection.getField(entityStatus.getClass(), "a").set(entityStatus, this.entityID);
		Reflection.getField(entityStatus.getClass(), "b").set(entityStatus, (byte) status.getID());
		sendPackets(entityStatus);
	}
	
	public void updateMetadata() {
		sendPackets(new PacketPlayOutEntityMetadata(this.entityID, (DataWatcher) this.dataWatcher.toNMS(), true));
	}
	
	
	
	// ### GETTER ###
	
	public int getEntityID() {
		return this.entityID;
	}
	
	public String getName() {
		return this.gameProfile.getName();
	}
	
	public float getHealth() {
		return this.health;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public boolean hasTarget() {
		if(this.target != null)
			return true;
		return false;
	}
	
	public LivingEntity getTarget() {
		if(this.hasTarget())
			return this.target;
		return null;
	}
	
	
	public boolean hasEquipment(EquipmentSlot slot) {
		return this.equip[slot.getID()] != null;
	}
	
	public ItemStack getEquipment(EquipmentSlot slot) {
		return equip[slot.getID()];
	}

	// ### SETTER ###
	
	public void setHealth(float health) {
		if(health == 0) {
			this.setStatus(Status.DEAD);
		} else if(this.health > health) {
			this.setStatus(Status.HURT);
		} else if(this.health == 0 && health > 0) {
			this.spawn(this.location);
		}
		this.health = health;
	}
	
	public void setTarget(LivingEntity target) {
		this.target = target;
		
		look(this.target.getEyeLocation());
	}
	
	
	
	// ### DATAWATCHER BITMASKS ###
	
	// 0
	public void status(boolean fire, boolean sneak, boolean sprint, boolean use, boolean invisible, boolean glowing, boolean elytra) {
		byte status = 0;
		status = changeMask(status, 0, fire);
		status = changeMask(status, 1, sneak);
		//status = changeMask(status, 2, true);		//unused
		status = changeMask(status, 3, sprint);
		status = changeMask(status, 4, use);
		status = changeMask(status, 5, invisible);
		status = changeMask(status, 6, glowing);
		status = changeMask(status, 7, elytra);
		this.dataWatcher.set(EnumDataWatcherObject.ENTITY_STATUS_BITMASK_00, status);
	}
	
	//
	
	//10
	public void skinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
		byte skinFlags = 0;
		skinFlags = changeMask(skinFlags, 0, cape);
		skinFlags = changeMask(skinFlags, 1, jacket);
		skinFlags = changeMask(skinFlags, 2, leftArm);
		skinFlags = changeMask(skinFlags, 3, rightArm);
		skinFlags = changeMask(skinFlags, 4, leftLeg);
		skinFlags = changeMask(skinFlags, 5, rightLeg);
		skinFlags = changeMask(skinFlags, 6, hat);
		//skinFlags = changeMask(skinFlags, 7, true);			//unused
		this.dataWatcher.set(EnumDataWatcherObject.HUMAN_SKIN_BITBASK_12, skinFlags);
	}
	

	
	// ### UTIL ###
	
	private byte changeMask(byte bitMask, int bit, boolean state) {
		if(state) {
			return bitMask |= 1 << bit;
		} else {
			return bitMask &= ~(1 << bit);
		}
	}
	
	@SuppressWarnings("unused")
	private int toFixedPointNumber(double value) {
		return (int) (Math.floor(value * 32.0D));
	}
	
	private byte toAngle(float value) {
		return (byte) ((int) (value * 256F / 360F));
	}
	
	private void sendPackets(Packet<?>... packets) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			for(Packet<?> packet : packets) {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		});
	}
}
