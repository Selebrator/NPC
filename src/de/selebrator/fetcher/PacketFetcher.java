package de.selebrator.fetcher;

import com.mojang.authlib.GameProfile;
import de.selebrator.reflection.Reflection;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.EnumItemSlot;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PacketFetcher {
	public static PacketPlayOutNamedEntitySpawn namedEntitySpawn(int entityId, GameProfile gameProfile, Location location, DataWatcher dataWatcher) {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		Reflection.getField(packet.getClass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass(), "b").set(packet, gameProfile.getId());
		Reflection.getField(packet.getClass(), "c").set(packet, location.getX());
		Reflection.getField(packet.getClass(), "d").set(packet, location.getY());
		Reflection.getField(packet.getClass(), "e").set(packet, location.getZ());
		Reflection.getField(packet.getClass(), "f").set(packet, angle(location.getYaw()));
		Reflection.getField(packet.getClass(), "g").set(packet, angle(location.getPitch()));
		Reflection.getField(packet.getClass(), "h").set(packet, dataWatcher);
		return packet;
	}

	public static PacketPlayOutEntityDestroy entityDestroy(int entityId) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
		Reflection.getField(packet.getClass(), "a").set(packet, new int[] { entityId});
		return packet;
	}

	public static PacketPlayOutPlayerInfo playerInfo(GameProfile gameProfile, PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		Reflection.getField(packet.getClass(), "a").set(packet, action);
		Reflection.getField(packet.getClass(), "b").set(packet, Arrays.asList(
				packet.new PlayerInfoData(gameProfile, 0, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(gameProfile.getName()))));
		return packet;
	}

	public static PacketPlayOutEntity.PacketPlayOutEntityLook entityLook(int entityId, float yaw, float pitch) {
		PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook();
		Reflection.getField(packet.getClass().getSuperclass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass().getSuperclass(), "e").set(packet, angle(yaw));
		Reflection.getField(packet.getClass().getSuperclass(), "f").set(packet, angle(pitch));
		Reflection.getField(packet.getClass().getSuperclass(), "g").set(packet, false);
		Reflection.getField(packet.getClass().getSuperclass(), "h").set(packet, true);
		return packet;
	}

	public static PacketPlayOutEntityHeadRotation headRotation(int entityId, float yaw) {
		PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();
		Reflection.getField(packet.getClass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass(), "b").set(packet, angle(yaw));
		return packet;
	}

	public static PacketPlayOutEntity.PacketPlayOutRelEntityMove relEntityMove(int entityId, double x, double y, double z) {
		PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMove();
		Reflection.getField(packet.getClass().getSuperclass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass().getSuperclass(), "b").set(packet, rel(x));
		Reflection.getField(packet.getClass().getSuperclass(), "c").set(packet, rel(y));
		Reflection.getField(packet.getClass().getSuperclass(), "d").set(packet, rel(z));
		Reflection.getField(packet.getClass().getSuperclass(), "g").set(packet, true); //onGround
		Reflection.getField(packet.getClass().getSuperclass(), "h").set(packet, true);
		return packet;
	}

	public static PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook relEntityMoveLook(int entityId, double x, double y, double z, float yaw, float pitch) {
		PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook();
		Reflection.getField(packet.getClass().getSuperclass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass().getSuperclass(), "b").set(packet, rel(x));
		Reflection.getField(packet.getClass().getSuperclass(), "c").set(packet, rel(y));
		Reflection.getField(packet.getClass().getSuperclass(), "d").set(packet, rel(z));
		Reflection.getField(packet.getClass().getSuperclass(), "e").set(packet, angle(yaw));
		Reflection.getField(packet.getClass().getSuperclass(), "f").set(packet, angle(pitch));
		Reflection.getField(packet.getClass().getSuperclass(), "g").set(packet, true); //onGround
		Reflection.getField(packet.getClass().getSuperclass(), "h").set(packet, true);
		return packet;
	}

	public static PacketPlayOutEntityTeleport entityTeleport(int entityId, Location location) {
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		Reflection.getField(packet.getClass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass(), "b").set(packet, location.getX());
		Reflection.getField(packet.getClass(), "c").set(packet, location.getY());
		Reflection.getField(packet.getClass(), "d").set(packet, location.getZ());
		Reflection.getField(packet.getClass(), "e").set(packet, angle(location.getYaw()));
		Reflection.getField(packet.getClass(), "f").set(packet, angle(location.getPitch()));
		Reflection.getField(packet.getClass(), "g").set(packet, false);		//onGround
		return packet;
	}

	public static PacketPlayOutEntityEquipment entityEquipment(int entityId, EnumItemSlot slot, ItemStack item) {
		PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
		Reflection.getField(packet.getClass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass(), "b").set(packet, slot);
		Reflection.getField(packet.getClass(), "c").set(packet, item); //itemStack
		return packet;
	}

	public static PacketPlayOutAnimation animation(int entityId, int anim) {
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
		Reflection.getField(packet.getClass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass(), "b").set(packet, anim);
		return packet;
	}

	public static PacketPlayOutEntityStatus entityStatus(int entityId, byte status) {
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
		Reflection.getField(packet.getClass(), "a").set(packet, entityId);
		Reflection.getField(packet.getClass(), "b").set(packet, status);
		return packet;
	}


	public static void sendPackets(Player player, Packet<?>... packets) {
		for(Packet<?> packet : packets) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void broadcastPackets(Packet<?>... packets) {
		Bukkit.getOnlinePlayers().forEach(player -> sendPackets(player, packets));
	}

	/**
	 * prepare angle for packet
	 * @param value angle in degrees
	 * @return rotation in 1/265 steps
	 */
	public static byte angle(float value) {
		return (byte) ((int) (value * 256F / 360F));
	}

	/**
	 * prepare relative coordinate for packet
	 */
	public static int rel(double value) {
		return (int) (4096 * value);
	}
}
