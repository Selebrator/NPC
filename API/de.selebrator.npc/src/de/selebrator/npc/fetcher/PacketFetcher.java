package de.selebrator.npc.fetcher;

import com.mojang.authlib.GameProfile;
import de.selebrator.reflection.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.*;

import static de.selebrator.reflection.Reflection.*;

public class PacketFetcher {
	//Minecraft classes
	private static final Class CLASS_Packet = getMinecraftClass("Packet");
	private static final Class CLASS_PacketPlayOutNamedEntitySpawn = getMinecraftClass("PacketPlayOutNamedEntitySpawn");
	private static final Class<?> CLASS_PacketPlayOutSpawnEntityLiving = getMinecraftClass("PacketPlayOutSpawnEntityLiving");
	private static final Class CLASS_PacketPlayOutEntityDestroy = getMinecraftClass("PacketPlayOutEntityDestroy");
	private static final Class CLASS_PacketPlayOutPlayerInfo = getMinecraftClass("PacketPlayOutPlayerInfo");
	private static final Class CLASS_PacketPlayOutEntityLook = getMinecraftClass("PacketPlayOutEntity$PacketPlayOutEntityLook");
	private static final Class CLASS_PacketPlayOutEntityHeadRotation = getMinecraftClass("PacketPlayOutEntityHeadRotation");
	private static final Class CLASS_PacketPlayOutRelEntityMove = getMinecraftClass("PacketPlayOutEntity$PacketPlayOutRelEntityMove");
	private static final Class CLASS_PacketPlayOutRelEntityMoveLook = getMinecraftClass("PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook");
	private static final Class CLASS_PacketPlayOutEntityTeleport = getMinecraftClass("PacketPlayOutEntityTeleport");
	private static final Class CLASS_PacketPlayOutEntityEquipment = getMinecraftClass("PacketPlayOutEntityEquipment");
	private static final Class CLASS_PacketPlayOutAnimation = getMinecraftClass("PacketPlayOutAnimation");
	private static final Class CLASS_PacketPlayOutEntityStatus = getMinecraftClass("PacketPlayOutEntityStatus");
	private static final Class CLASS_PacketPlayOutEntityMetadata = getMinecraftClass("PacketPlayOutEntityMetadata");

	private static final Class<?> CLASS_EntityPlayer = getMinecraftClass("EntityPlayer");
	private static final Class<?> CLASS_PlayerConnection = getMinecraftClass("PlayerConnection");
	private static final Class CLASS_DataWatcher = getMinecraftClass("DataWatcher");
	private static final Class CLASS_PlayerInfoData = getMinecraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
	private static final Class CLASS_IChatBaseComponent = getMinecraftClass("IChatBaseComponent");
	private static final Class CLASS_ChatComponentText = getMinecraftClass("ChatComponentText");
	private static final Class<?> CLASS_ItemStack = getMinecraftClass("ItemStack");

	//Minecraft Enums
	private static final Class<? extends Enum> CLASS_EnumPlayerInfoAction = getMinecraftEnum("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
	private static final Class<? extends Enum> CLASS_EnumGamemode = getMinecraftEnum("EnumGamemode");
	private static final Class<? extends Enum> CLASS_EnumItemSlot = getMinecraftEnum("EnumItemSlot");

	//CraftBukkit classes
	private static final Class CLASS_CraftEquipmentSlot = getCraftBukkitClass("CraftEquipmentSlot");
	private static final Class CLASS_CraftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
	private static final Class<?> CLASS_CraftPlayer = getCraftBukkitClass("entity.CraftPlayer");

	//Constructors
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutNamedEntitySpawn = getConstructor(CLASS_PacketPlayOutNamedEntitySpawn);
	private static final ConstructorAccessor<Object> CONSTRUCTOR_PacketPlayOutSpawnEntityLiving = getConstructor(CLASS_PacketPlayOutSpawnEntityLiving);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityDestroy = getConstructor(CLASS_PacketPlayOutEntityDestroy);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutPlayerInfo = getConstructor(CLASS_PacketPlayOutPlayerInfo);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityLook = getConstructor(CLASS_PacketPlayOutEntityLook);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityHeadRotation = getConstructor(CLASS_PacketPlayOutEntityHeadRotation);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutRelEntityMove = getConstructor(CLASS_PacketPlayOutRelEntityMove);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutRelEntityMoveLook = getConstructor(CLASS_PacketPlayOutRelEntityMoveLook);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityTeleport = getConstructor(CLASS_PacketPlayOutEntityTeleport);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityEquipment = getConstructor(CLASS_PacketPlayOutEntityEquipment);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutAnimation = getConstructor(CLASS_PacketPlayOutAnimation);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityStatus = getConstructor(CLASS_PacketPlayOutEntityStatus);
	private static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityMetadata = getConstructor(CLASS_PacketPlayOutEntityMetadata);

	private static final ConstructorAccessor CONSTRUCTOR_PlayerInfoData = getConstructor(CLASS_PlayerInfoData, CLASS_PacketPlayOutPlayerInfo, GameProfile.class, int.class, CLASS_EnumGamemode, CLASS_IChatBaseComponent);
	private static final ConstructorAccessor CONSTRUCTOR_ChatComponentText = getConstructor(CLASS_ChatComponentText, String.class);

	//Methods
	private static final MethodAccessor METHOD_CraftEquipmentSlot_getNMS = getMethod(CLASS_CraftEquipmentSlot, CLASS_EnumItemSlot, "getNMS", EquipmentSlot.class);
	private static final MethodAccessor METHOD_CraftItemStack_asNMSCopy = getMethod(CLASS_CraftItemStack, CLASS_ItemStack, "asNMSCopy", ItemStack.class);
	private static final MethodAccessor METHOD_DataWatcher_c = getMethod(CLASS_DataWatcher, List.class, "c");
	private static final MethodAccessor METHOD_CraftPlayer_getHandle = getMethod(CLASS_CraftPlayer, CLASS_EntityPlayer, "getHandle");
	private static final MethodAccessor METHOD_PlayerConnection_sendPacket = getMethod(CLASS_PlayerConnection, null, "sendPacket", CLASS_Packet);

	//Fields
	private static final FieldAccessor FIELD_EntityPlayer_playerConnection = getField(CLASS_EntityPlayer, CLASS_PlayerConnection, "playerConnection");

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

	public static Object spawnEntityLiving(int entityId, EntityType type, UUID uuid, Location location, Object dataWatcher) {
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
		values.forEach((fieldName, value) -> getField(packet.getClass(), fieldName).set(packet, value));
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
