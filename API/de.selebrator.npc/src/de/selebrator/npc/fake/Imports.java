package de.selebrator.npc.fake;

import com.mojang.authlib.GameProfile;
import de.selebrator.reflection.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffect;

import java.util.List;

import static de.selebrator.reflection.Reflection.*;

public class Imports {
	//Minecraft classes
	public static final Class CLASS_Packet = getMinecraftClass("Packet");
	public static final Class CLASS_PacketPlayOutNamedEntitySpawn = getMinecraftClass("PacketPlayOutNamedEntitySpawn");
	public static final Class CLASS_PacketPlayOutSpawnEntityLiving = getMinecraftClass("PacketPlayOutSpawnEntityLiving");
	public static final Class CLASS_PacketPlayOutEntityDestroy = getMinecraftClass("PacketPlayOutEntityDestroy");
	public static final Class CLASS_PacketPlayOutPlayerInfo = getMinecraftClass("PacketPlayOutPlayerInfo");
	public static final Class CLASS_PacketPlayOutEntityLook = getMinecraftClass("PacketPlayOutEntity$PacketPlayOutEntityLook");
	public static final Class CLASS_PacketPlayOutEntityHeadRotation = getMinecraftClass("PacketPlayOutEntityHeadRotation");
	public static final Class CLASS_PacketPlayOutRelEntityMove = getMinecraftClass("PacketPlayOutEntity$PacketPlayOutRelEntityMove");
	public static final Class CLASS_PacketPlayOutRelEntityMoveLook = getMinecraftClass("PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook");
	public static final Class CLASS_PacketPlayOutEntityTeleport = getMinecraftClass("PacketPlayOutEntityTeleport");
	public static final Class CLASS_PacketPlayOutEntityEquipment = getMinecraftClass("PacketPlayOutEntityEquipment");
	public static final Class CLASS_PacketPlayOutAnimation = getMinecraftClass("PacketPlayOutAnimation");
	public static final Class CLASS_PacketPlayOutEntityStatus = getMinecraftClass("PacketPlayOutEntityStatus");
	public static final Class CLASS_PacketPlayOutEntityMetadata = getMinecraftClass("PacketPlayOutEntityMetadata");

	public static final Class CLASS_Entity = getMinecraftClass("Entity");
	public static final Class CLASS_EntityLiving = getMinecraftClass("EntityLiving");
	public static final Class CLASS_EntityInsentient = getMinecraftClass("EntityInsentient");
	public static final Class CLASS_EntityHuman = getMinecraftClass("EntityHuman");
	public static final Class<?> CLASS_EntityPlayer = getMinecraftClass("EntityPlayer");
	public static final Class CLASS_EntityEnderman = Reflection.getMinecraftClass("EntityEnderman");
	public static final Class CLASS_EntityZombie = Reflection.getMinecraftClass("EntityZombie");
	public static final Class CLASS_EntityZombieVillager = Reflection.getMinecraftClass("EntityZombieVillager");
	public static final Class CLASS_EntityHorseAbstract = Reflection.getMinecraftClass("EntityHorseAbstract");
	public static final Class CLASS_EntityAgeable = Reflection.getMinecraftClass("EntityAgeable");
	public static final Class CLASS_EntityHorseChestedAbstract = Reflection.getMinecraftClass("EntityHorseChestedAbstract");
	public static final Class CLASS_EntityHorse = Reflection.getMinecraftClass("EntityHorse");
	public static final Class CLASS_EntityLlama = Reflection.getMinecraftClass("EntityLlama");
	public static final Class CLASS_EntityTameableAnimal = Reflection.getMinecraftClass("EntityTameableAnimal");
	public static final Class CLASS_EntityOcelot = Reflection.getMinecraftClass("EntityOcelot");
	public static final Class CLASS_EntityParrot = Reflection.getMinecraftClass("EntityParrot");
	public static final Class CLASS_EntityWolf = Reflection.getMinecraftClass("EntityWolf");
	public static final Class CLASS_EntityGuardian = getMinecraftClass("EntityGuardian");
	public static final Class CLASS_EntitySkeletonAbstract = getMinecraftClass("EntitySkeletonAbstract");
	public static final Class<?> CLASS_PlayerConnection = getMinecraftClass("PlayerConnection");
	public static final Class CLASS_DataWatcher = getMinecraftClass("DataWatcher");
	public static final Class<?> CLASS_DataWatcherObject = getMinecraftClass("DataWatcherObject");
	public static final Class CLASS_PlayerInfoData = getMinecraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
	public static final Class CLASS_IChatBaseComponent = getMinecraftClass("IChatBaseComponent");
	public static final Class CLASS_ChatComponentText = getMinecraftClass("ChatComponentText");
	public static final Class<?> CLASS_ItemStack = getMinecraftClass("ItemStack");
	public static final Class CLASS_NBTTagCompound = getMinecraftClass("NBTTagCompound");
	public static final Class<?> CLASS_IBlockData = Reflection.getMinecraftClass("IBlockData");
	public static final Class CLASS_Block = Reflection.getMinecraftClass("Block");

	//Minecraft Enums
	public static final Class<? extends Enum> CLASS_EnumPlayerInfoAction = getMinecraftEnum("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
	public static final Class<? extends Enum> CLASS_EnumGamemode = getMinecraftEnum("EnumGamemode");
	public static final Class<? extends Enum> CLASS_EnumItemSlot = getMinecraftEnum("EnumItemSlot");

	//CraftBukkit classes
	public static final Class CLASS_CraftEquipmentSlot = getCraftBukkitClass("CraftEquipmentSlot");
	public static final Class CLASS_CraftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
	public static final Class CLASS_CraftPlayer = getCraftBukkitClass("entity.CraftPlayer");

	//Constructors
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutNamedEntitySpawn = getConstructor(CLASS_PacketPlayOutNamedEntitySpawn);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutSpawnEntityLiving = getConstructor(CLASS_PacketPlayOutSpawnEntityLiving);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityDestroy = getConstructor(CLASS_PacketPlayOutEntityDestroy);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutPlayerInfo = getConstructor(CLASS_PacketPlayOutPlayerInfo);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityLook = getConstructor(CLASS_PacketPlayOutEntityLook);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityHeadRotation = getConstructor(CLASS_PacketPlayOutEntityHeadRotation);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutRelEntityMove = getConstructor(CLASS_PacketPlayOutRelEntityMove);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutRelEntityMoveLook = getConstructor(CLASS_PacketPlayOutRelEntityMoveLook);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityTeleport = getConstructor(CLASS_PacketPlayOutEntityTeleport);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityEquipment = getConstructor(CLASS_PacketPlayOutEntityEquipment);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutAnimation = getConstructor(CLASS_PacketPlayOutAnimation);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityStatus = getConstructor(CLASS_PacketPlayOutEntityStatus);
	public static final ConstructorAccessor CONSTRUCTOR_PacketPlayOutEntityMetadata = getConstructor(CLASS_PacketPlayOutEntityMetadata);

	public static final ConstructorAccessor CONSTRUCTOR_PlayerInfoData = getConstructor(CLASS_PlayerInfoData, CLASS_PacketPlayOutPlayerInfo, GameProfile.class, int.class, CLASS_EnumGamemode, CLASS_IChatBaseComponent);
	public static final ConstructorAccessor CONSTRUCTOR_ChatComponentText = getConstructor(CLASS_ChatComponentText, String.class);
	public static final ConstructorAccessor CONSTRUCTOR_DataWatcher = getConstructor(CLASS_DataWatcher, CLASS_Entity);
	public static final ConstructorAccessor CONSTRUCTOR_NBTTagCompound = getConstructor(CLASS_NBTTagCompound);

	//Methods
	public static final MethodAccessor METHOD_CraftEquipmentSlot_getNMS = getMethod(CLASS_CraftEquipmentSlot, CLASS_EnumItemSlot, "getNMS", EquipmentSlot.class);
	public static final MethodAccessor METHOD_CraftItemStack_asNMSCopy = getMethod(CLASS_CraftItemStack, CLASS_ItemStack, "asNMSCopy", ItemStack.class);
	public static final MethodAccessor<List> METHOD_DataWatcher_c = getMethod(CLASS_DataWatcher, List.class, "c");
	public static final MethodAccessor METHOD_DataWatcher_registerObject = getMethod(CLASS_DataWatcher, null, "registerObject", CLASS_DataWatcherObject, Object.class);
	public static final MethodAccessor METHOD_CraftPlayer_getHandle = getMethod(CLASS_CraftPlayer, CLASS_EntityPlayer, "getHandle");
	public static final MethodAccessor METHOD_PlayerConnection_sendPacket = getMethod(CLASS_PlayerConnection, null, "sendPacket", CLASS_Packet);
	public static final MethodAccessor METHOD_Block_getByCombinedId = Reflection.getMethod(CLASS_Block, CLASS_IBlockData, "getByCombinedId", int.class);
	public static final MethodAccessor<Integer> METHOD_Block_getCombinedId = Reflection.getMethod(CLASS_Block, int.class, "getCombinedId", CLASS_IBlockData);

	//Fields
	public static final FieldAccessor FIELD_EntityPlayer_playerConnection = getField(CLASS_EntityPlayer, CLASS_PlayerConnection, "playerConnection");
	public static final FieldAccessor<Integer> FIELD_Entity_entityCount = getField(CLASS_Entity, int.class, "entityCount");
	public static final FieldAccessor<Integer> FIELD_PotionEffect_duration = getField(PotionEffect.class, int.class, "duration");

	public static final FieldAccessor FIELD_Entity_Z = getField(CLASS_Entity, CLASS_DataWatcherObject, "Z"); //0
	public static final FieldAccessor FIELD_Entity_aA = getField(CLASS_Entity, CLASS_DataWatcherObject, "aA"); //1
	public static final FieldAccessor FIELD_Entity_aB = getField(CLASS_Entity, CLASS_DataWatcherObject, "aB"); //2
	public static final FieldAccessor FIELD_Entity_aC = getField(CLASS_Entity, CLASS_DataWatcherObject, "aC"); //3
	public static final FieldAccessor FIELD_Entity_aD = getField(CLASS_Entity, CLASS_DataWatcherObject, "aD"); //4
	public static final FieldAccessor FIELD_Entity_aE = getField(CLASS_Entity, CLASS_DataWatcherObject, "aE"); //5
	public static final FieldAccessor FIELD_EntityLiving_at = getField(CLASS_EntityLiving, CLASS_DataWatcherObject, "at"); //6
	public static final FieldAccessor FIELD_EntityLiving_HEALTH = getField(CLASS_EntityLiving, CLASS_DataWatcherObject, "HEALTH"); //7
	public static final FieldAccessor FIELD_EntityLiving_g = getField(CLASS_EntityLiving, CLASS_DataWatcherObject, "g"); //8
	public static final FieldAccessor FIELD_EntityLiving_h = getField(CLASS_EntityLiving, CLASS_DataWatcherObject, "h"); //9
	public static final FieldAccessor FIELD_EntityLiving_br = getField(CLASS_EntityLiving, CLASS_DataWatcherObject, "br"); //10
	public static final FieldAccessor FIELD_EntityHuman_a = getField(CLASS_EntityHuman, CLASS_DataWatcherObject, "a"); //11
	public static final FieldAccessor FIELD_EntityInsentient_a = getField(CLASS_EntityInsentient, CLASS_DataWatcherObject, "a"); //11
	public static final FieldAccessor FIELD_EntityHuman_b = getField(CLASS_EntityHuman, CLASS_DataWatcherObject, "b"); //12
	public static final FieldAccessor FIELD_EntityEnderman_bx = getField(CLASS_EntityEnderman, CLASS_DataWatcherObject, "bx"); //12
	public static final FieldAccessor FIELD_EntityZombie_bx = getField(CLASS_EntityZombie, CLASS_DataWatcherObject, "bx"); //12
	public static final FieldAccessor FIELD_EntityAgeable_bx = getField(CLASS_EntityAgeable, CLASS_DataWatcherObject, "bx"); //12
	public static final FieldAccessor FIELD_EntityGuardian_bA = getField(CLASS_EntityGuardian, CLASS_DataWatcherObject, "bA"); //12
	public static final FieldAccessor FIELD_EntitySkeletonAbstract_a = getField(CLASS_EntitySkeletonAbstract, CLASS_DataWatcherObject, "a"); //12
	public static final FieldAccessor FIELD_EntityHuman_br = getField(CLASS_EntityHuman, CLASS_DataWatcherObject, "br"); //13
	public static final FieldAccessor FIELD_EntityHorseAbstract_bI = getField(CLASS_EntityHorseAbstract, CLASS_DataWatcherObject, "bI"); //13
	public static final FieldAccessor FIELD_EntityEnderman_by = getField(CLASS_EntityEnderman, CLASS_DataWatcherObject, "by"); //13
	public static final FieldAccessor FIELD_EntityTameableAnimal_bx = getField(CLASS_EntityTameableAnimal, CLASS_DataWatcherObject, "bx"); //13
	public static final FieldAccessor FIELD_EntityGuardian_bB = getField(CLASS_EntityGuardian, CLASS_DataWatcherObject, "bB"); //13
	public static final FieldAccessor FIELD_EntityHuman_bs = getField(CLASS_EntityHuman, CLASS_DataWatcherObject, "bs"); //14
	public static final FieldAccessor FIELD_EntityZombie_bz = getField(CLASS_EntityZombie, CLASS_DataWatcherObject, "bz"); //14
	public static final FieldAccessor FIELD_EntityHorseAbstract_bJ = getField(CLASS_EntityHorseAbstract, CLASS_DataWatcherObject, "bJ"); //14
	public static final FieldAccessor FIELD_EntityTameableAnimal_by = getField(CLASS_EntityTameableAnimal, CLASS_DataWatcherObject, "by"); //14
	public static final FieldAccessor FIELD_EntityHuman_bt = getField(CLASS_EntityHuman, CLASS_DataWatcherObject, "bt"); //15
	public static final FieldAccessor FIELD_EntityZombieVillager_b = getField(CLASS_EntityZombieVillager, CLASS_DataWatcherObject, "b"); //15
	public static final FieldAccessor FIELD_EntityHorse_bI = getField(CLASS_EntityHorse, CLASS_DataWatcherObject, "bI"); //15
	public static final FieldAccessor FIELD_EntityHorseChestedAbstract_bH = getField(CLASS_EntityHorseChestedAbstract, CLASS_DataWatcherObject, "bH"); //15
	public static final FieldAccessor FIELD_EntityOcelot_bB = getField(CLASS_EntityOcelot, CLASS_DataWatcherObject, "bB"); //15
	public static final FieldAccessor FIELD_EntityParrot_bG = getField(CLASS_EntityParrot, CLASS_DataWatcherObject, "bG"); //15
	public static final FieldAccessor FIELD_EntityWolf_DATA_HEALTH = getField(CLASS_EntityWolf, CLASS_DataWatcherObject, "DATA_HEALTH"); //15
	public static final FieldAccessor FIELD_EntityHuman_bu = getField(CLASS_EntityHuman, CLASS_DataWatcherObject, "bu"); //16
	public static final FieldAccessor FIELD_EntityZombieVillager_c = getField(CLASS_EntityZombieVillager, CLASS_DataWatcherObject, "c"); //16
	public static final FieldAccessor FIELD_EntityHorse_bJ = getField(CLASS_EntityHorse, CLASS_DataWatcherObject, "bJ"); //16
	public static final FieldAccessor FIELD_EntityLlama_bH = getField(CLASS_EntityLlama, CLASS_DataWatcherObject, "bH"); //16
	public static final FieldAccessor FIELD_EntityWolf_bC = getField(CLASS_EntityWolf, CLASS_DataWatcherObject, "bC"); //16
	public static final FieldAccessor FIELD_EntityLlama_bI = getField(CLASS_EntityLlama, CLASS_DataWatcherObject, "bI"); //17
	public static final FieldAccessor FIELD_EntityWolf_bD = getField(CLASS_EntityWolf, CLASS_DataWatcherObject, "bD"); //17
	public static final FieldAccessor FIELD_EntityLlama_bJ = getField(CLASS_EntityLlama, CLASS_DataWatcherObject, "bJ"); //18
}
