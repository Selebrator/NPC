package de.selebrator.npc.fetcher;

import com.mojang.authlib.GameProfile;
import de.selebrator.reflection.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.*;

import static de.selebrator.npc.Imports.*;

public class PacketFetcher {
	public static Object namedEntitySpawn(int entityId, GameProfile gameProfile, Location location, Object dataWatcher) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", gameProfile.getId());
		fields.put("c", location.getX());
		fields.put("d", location.getY());
		fields.put("e", location.getZ());
		fields.put("f", angle(location.getYaw()));
		fields.put("g", angle(location.getPitch()));
		fields.put("h", dataWatcher);
		return packet(CONSTRUCTOR_PacketPlayOutNamedEntitySpawn, fields);
	}

	public static Object spawnEntityLiving(int entityId, UUID uuid, EntityType type, Location location, Object dataWatcher) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", uuid);
		fields.put("c", type.getTypeId()); //RegistryID of entityType
		fields.put("d", location.getX());
		fields.put("e", location.getY());
		fields.put("f", location.getZ());
		fields.put("g", 0); //motX
		fields.put("h", 0); //motY
		fields.put("i", 0); //motZ
		fields.put("j", angle(location.getYaw()));
		fields.put("k", angle(location.getPitch()));
		fields.put("l", (byte) 0); //???
		fields.put("m", dataWatcher);
		return packet(CONSTRUCTOR_PacketPlayOutSpawnEntityLiving, fields);
	}

	public static Object entityDestroy(int entityId) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", new int[] { entityId });
		return packet(CONSTRUCTOR_PacketPlayOutEntityDestroy, fields);
	}

	@SuppressWarnings("unchecked")
	public static Object playerInfo(GameProfile gameProfile, String action) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", Enum.valueOf(CLASS_EnumPlayerInfoAction, action.toUpperCase()));
		fields.put("b", Collections.singletonList(CONSTRUCTOR_PlayerInfoData.newInstance(CONSTRUCTOR_PacketPlayOutPlayerInfo.newInstance(), gameProfile, 0, Enum.valueOf(CLASS_EnumGamemode, "NOT_SET"), CONSTRUCTOR_ChatComponentText.newInstance(gameProfile.getName()))));
		return packet(CONSTRUCTOR_PacketPlayOutPlayerInfo, fields);
	}

	public static Object entityLook(int entityId, float yaw, float pitch) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("e", angle(yaw));
		fields.put("f", angle(pitch));
		fields.put("g", false);
		fields.put("h", true);
		return packet(CONSTRUCTOR_PacketPlayOutEntityLook, fields);
	}

	public static Object headRotation(int entityId, float yaw) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", angle(yaw));
		return packet(CONSTRUCTOR_PacketPlayOutEntityHeadRotation, fields);
	}

	public static Object relEntityMove(int entityId, double x, double y, double z) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", rel(x));
		fields.put("c", rel(y));
		fields.put("d", rel(z));
		fields.put("g", true); //onGround
		fields.put("h", true);
		return packet(CONSTRUCTOR_PacketPlayOutRelEntityMove, fields);
	}

	public static Object relEntityMoveLook(int entityId, double x, double y, double z, float yaw, float pitch) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", rel(x));
		fields.put("c", rel(y));
		fields.put("d", rel(z));
		fields.put("e", angle(yaw));
		fields.put("f", angle(pitch));
		fields.put("g", true); //onGround
		fields.put("h", true);
		return packet(CONSTRUCTOR_PacketPlayOutRelEntityMoveLook, fields);
	}

	public static Object entityTeleport(int entityId, Location location) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", location.getX());
		fields.put("c", location.getY());
		fields.put("d", location.getZ());
		fields.put("e", angle(location.getYaw()));
		fields.put("f", angle(location.getPitch()));
		fields.put("g", true); //onGround
		return packet(CONSTRUCTOR_PacketPlayOutEntityTeleport, fields);
	}

	public static Object entityEquipment(int entityId, EquipmentSlot slot, ItemStack item) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", METHOD_CraftEquipmentSlot_getNMS.invoke(null, slot));
		fields.put("c", METHOD_CraftItemStack_asNMSCopy.invoke(null, item));
		return packet(CONSTRUCTOR_PacketPlayOutEntityEquipment, fields);
	}

	public static Object animation(int entityId, byte animation) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", animation);
		return packet(CONSTRUCTOR_PacketPlayOutAnimation, fields);
	}

	public static Object entityStatus(int entityId, byte status) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", status);
		return packet(CONSTRUCTOR_PacketPlayOutEntityStatus, fields);
	}

	public static Object entityMetadata(int entityId, Object dataWatcher) {
		Map<String, Object> fields = new HashMap<>();
		fields.put("a", entityId);
		fields.put("b", METHOD_DataWatcher_c.invoke(dataWatcher));
		return packet(CONSTRUCTOR_PacketPlayOutEntityMetadata, fields);
	}

	@SuppressWarnings("unchecked")
	private static Object packet(ConstructorAccessor constructorAccessor, Map<String, Object> values) {
		Object packet = constructorAccessor.newInstance();
		values.forEach((fieldName, value) -> Reflection.getField(packet.getClass(), fieldName).set(packet, value));
		return packet;
	}

	public static void sendPackets(Player player, Object... packets) {
		for(Object packet : packets) {
			METHOD_PlayerConnection_sendPacket.invoke(FIELD_EntityPlayer_playerConnection.get(METHOD_CraftPlayer_getHandle.invoke(player)), packet);
		}
	}

	public static void broadcastPackets(Object... packets) {
		Bukkit.getOnlinePlayers().forEach(player -> sendPackets(player, packets));
	}

	/**
	 * prepare angle for packet
	 *
	 * @param value angle in degrees
	 * @return rotation in 1/265 steps
	 */
	private static byte angle(float value) {
		return (byte) ((int) (value * 256F / 360F));
	}

	/**
	 * prepare relative coordinate for packet
	 */
	private static short rel(double value) {
		return (short) (4096 * value);
	}
}
