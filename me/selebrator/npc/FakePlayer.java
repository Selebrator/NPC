package me.selebrator.npc;

import java.util.Arrays;

import me.selebrator.reflection.Reflection;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;

public class FakePlayer {
	
	private int entityID;
	private Plugin plugin;
	private GameProfile gameProfile;
	private NullDataWatcher dataWatcher;

	private Location location;
	private float health = 20;
	private LivingEntity target;

	private ItemStack hand;
	private ItemStack[] armor = new ItemStack[4];




	public FakePlayer(GameProfile gameProfile, Plugin plugin) {
		this.entityID = (int) Reflection.getField(Entity.class, "entityCount").get(null);
		Reflection.getField(Entity.class, "entityCount").set(null, this.entityID + 1);
		
		this.gameProfile = gameProfile;
		this.plugin = plugin;
		
		this.dataWatcher = new NullDataWatcher();
		this.dataWatcher.a(0, (byte) 0);
		this.dataWatcher.a(6, 20F);
		this.dataWatcher.a(10, (byte) 127);
	}
	
	public void updateGameProfile(GameProfile gameProfile) {
		despawn();
		removeFromTabList();
		this.gameProfile = gameProfile;
		addToTabList();
		spawn(location);
	}

	public void spawn(Location location) {
		this.location = location;
		PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn();
		Reflection.getField(namedEntitySpawn.getClass(), "a").set(namedEntitySpawn, this.entityID);
		Reflection.getField(namedEntitySpawn.getClass(), "b").set(namedEntitySpawn, this.gameProfile.getId());
		Reflection.getField(namedEntitySpawn.getClass(), "c").set(namedEntitySpawn, toFixedPointNumber(this.location.getX()));
		Reflection.getField(namedEntitySpawn.getClass(), "d").set(namedEntitySpawn, toFixedPointNumber(this.location.getY()));
		Reflection.getField(namedEntitySpawn.getClass(), "e").set(namedEntitySpawn, toFixedPointNumber(this.location.getZ()));
		Reflection.getField(namedEntitySpawn.getClass(), "f").set(namedEntitySpawn, toAngle(this.location.getYaw()));
		Reflection.getField(namedEntitySpawn.getClass(), "g").set(namedEntitySpawn, toAngle(this.location.getPitch()));
		Reflection.getField(namedEntitySpawn.getClass(), "h").set(namedEntitySpawn, 0);  //itemInHand
		Reflection.getField(namedEntitySpawn.getClass(), "i").set(namedEntitySpawn, this.dataWatcher);
		sendPackets(namedEntitySpawn);
	}
	
	public void despawn() {
		PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(this.entityID);
		sendPackets(entityDestroy);
		removeFromTabList();
	}

	public void addToTabList() {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER);
		Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, EnumGamemode.NOT_SET, new ChatComponentText(this.gameProfile.getName()))));
		sendPackets(playerInfo);
	}

	public void removeFromTabList() {
		PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
		Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, null, null)));
		sendPackets(playerInfo);
	}

	public void look(Location location) {
		double differenceX = location.getX() - this.location.getX();
		double differenceY = location.getY() - (this.location.getY() + 1.6F);
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
		
		this.location.setYaw(yaw);
		this.location.setPitch(pitch);
		sendPackets(entityLook, entityHeadRotation);
	}
	
	public void teleport(Location location) {
		PacketPlayOutEntityTeleport entityTeleport = new PacketPlayOutEntityTeleport();
		Reflection.getField(entityTeleport.getClass(), "a").set(entityTeleport, this.entityID);
		Reflection.getField(entityTeleport.getClass(), "b").set(entityTeleport, toFixedPointNumber(location.getX()));
		Reflection.getField(entityTeleport.getClass(), "c").set(entityTeleport, toFixedPointNumber(location.getY()));
		Reflection.getField(entityTeleport.getClass(), "d").set(entityTeleport, toFixedPointNumber(location.getZ()));
		Reflection.getField(entityTeleport.getClass(), "e").set(entityTeleport, toAngle(location.getYaw()));
		Reflection.getField(entityTeleport.getClass(), "f").set(entityTeleport, toAngle(location.getPitch()));
		Reflection.getField(entityTeleport.getClass(), "g").set(entityTeleport, false);		//onGround
		
		PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
		Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityID);
		Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, toAngle(location.getYaw()));
		
		this.location = location;
		sendPackets(entityTeleport, entityHeadRotation);
	}

	public void walk(Location location) {
		// TODO
	}
	
	private BukkitRunnable tickTask = new BukkitRunnable() {
		
		@Override
		public void run() {
			look(target.getEyeLocation());
			walk(target.getLocation());
		}
	};

	public void equip(EquipmentSlot slot, ItemStack item) {
		PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment();
		Reflection.getField(entityEquipment.getClass(), "a").set(entityEquipment, this.entityID);
		Reflection.getField(entityEquipment.getClass(), "b").set(entityEquipment, slot.getID());
		Reflection.getField(entityEquipment.getClass(), "c").set(entityEquipment, CraftItemStack.asNMSCopy(item));		//itemStack
		sendPackets(entityEquipment);
		
		if(slot == EquipmentSlot.HAND)
			this.hand = item;
		else if(slot == EquipmentSlot.HELMET )
			this.armor[0] = item;
		else if(slot == EquipmentSlot.CHESTPLATE )
			this.armor[1] = item;
		else if(slot == EquipmentSlot.LEGGINGS )
			this.armor[2] = item;
		else if(slot == EquipmentSlot.BOOTS )
			this.armor[3] = item;
		
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
		sendPackets(new PacketPlayOutEntityMetadata(this.entityID, this.dataWatcher, true));
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
		if(slot == EquipmentSlot.HAND && this.hand != null)
			return true;
		if(slot == EquipmentSlot.HELMET && this.armor[0] != null)
			return true;
		if(slot == EquipmentSlot.CHESTPLATE && this.armor[1] != null)
			return true;
		if(slot == EquipmentSlot.LEGGINGS && this.armor[2] != null)
			return true;
		if(slot == EquipmentSlot.BOOTS && this.armor[3] != null)
			return true;
		return false;
	}
	
	public ItemStack getEquipment(EquipmentSlot slot) {
		if(slot == EquipmentSlot.HAND && this.hasEquipment(slot))
			return this.hand;
		else if(slot == EquipmentSlot.HELMET && this.hasEquipment(slot))
			return this.armor[0];
		else if(slot == EquipmentSlot.CHESTPLATE && this.hasEquipment(slot))
			return this.armor[1];
		else if(slot == EquipmentSlot.LEGGINGS && this.hasEquipment(slot))
			return this.armor[2];
		else if(slot == EquipmentSlot.BOOTS && this.hasEquipment(slot))
			return this.armor[3];
		return null;
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
		
		tickTask.runTaskTimer(plugin, 0, 1);
	}
	
	
	
	// ### DATAWATCHER BITMASKS ###
	
	// 0
	public void status(boolean fire, boolean sneak, boolean sprint, boolean use, boolean invisible) {
		byte status = 0;
		status = changeMask(status, 0, fire);
		status = changeMask(status, 1, sneak);
		//status = changeMask(status, 2, true);		//unused
		status = changeMask(status, 3, sprint);
		status = changeMask(status, 4, use);
		status = changeMask(status, 5, invisible);
		this.dataWatcher.update(0, status);
	}
	
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
		this.dataWatcher.update(10, skinFlags);
	}
	

	
	// ### UTIL ###
	
	private byte changeMask(byte bitMask, int bit, boolean state) {
		if(state) {
			return bitMask |= 1 << bit;
		} else {
			return bitMask &= ~(1 << bit);
		}
	}
	
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
