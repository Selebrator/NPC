package de.selebrator.npc;

import java.util.Arrays;

import de.selebrator.reflection.Reflection;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

public class FakePlayer {

    private int entityID;
    private GameProfile gameProfile;
    private NullDataWatcher dataWatcher;

    private Location location;
    private LivingEntity target;

    private float health = 20F;
    private double moveSpeed = 4.3D / 20;

    private ItemStack[] equip = new ItemStack[6];


    public FakePlayer(GameProfile gameProfile) {
        this.entityID = (int) Reflection.getField(Entity.class, "entityCount").get(null);
        Reflection.getField(Entity.class, "entityCount").set(null, this.entityID + 1);

        this.gameProfile = gameProfile;

        this.dataWatcher = new NullDataWatcher();
        status(false, false, false, false, false, false, false);
        skinFlags(true, true, true, true, true, true, true);
        this.dataWatcher.set(EnumDataWatcherObject.LIVING_HELATH_06, this.health);
    }

    public void spawn(Location location) {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, EnumGamemode.NOT_SET, new ChatComponentText(this.gameProfile.getName()))));

        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn();
        Reflection.getField(namedEntitySpawn.getClass(), "a").set(namedEntitySpawn, this.entityID);
        Reflection.getField(namedEntitySpawn.getClass(), "b").set(namedEntitySpawn, this.gameProfile.getId());
        Reflection.getField(namedEntitySpawn.getClass(), "c").set(namedEntitySpawn, location.getX());
        Reflection.getField(namedEntitySpawn.getClass(), "d").set(namedEntitySpawn, location.getY());
        Reflection.getField(namedEntitySpawn.getClass(), "e").set(namedEntitySpawn, location.getZ());
        Reflection.getField(namedEntitySpawn.getClass(), "f").set(namedEntitySpawn, angle(location.getYaw()));
        Reflection.getField(namedEntitySpawn.getClass(), "g").set(namedEntitySpawn, angle(location.getPitch()));
        Reflection.getField(namedEntitySpawn.getClass(), "h").set(namedEntitySpawn, this.dataWatcher.toNMS());

        sendPackets(playerInfo, namedEntitySpawn);

        this.location = location;

        for(EnumEquipmentSlot slot : EnumEquipmentSlot.values()) {
            this.equip(slot, this.equip[slot.getID()]);
        }
    }

    public void despawn() {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, null, null)));

        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy();
        Reflection.getField(entityDestroy.getClass(), "a").set(entityDestroy, this.entityID);

        sendPackets(playerInfo, entityDestroy);
    }

    public void look(Location location) {
        // calculate yaw and pitch
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - (this.location.getY() + 1.6);
        double differenceZ = location.getZ() - this.location.getZ();
        double hypotenuseXZ = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ);
        float yaw = (float) (Math.atan2(differenceZ, differenceX) * 180D / Math.PI) - 90F;
        float pitch = (float) -(Math.atan2(differenceY, hypotenuseXZ) * 180D / Math.PI);

        PacketPlayOutEntityLook entityLook = new PacketPlayOutEntityLook();
        Reflection.getField(entityLook.getClass().getSuperclass(), "a").set(entityLook, this.entityID);
        Reflection.getField(entityLook.getClass().getSuperclass(), "e").set(entityLook, angle(yaw));
        Reflection.getField(entityLook.getClass().getSuperclass(), "f").set(entityLook, angle(pitch));
        Reflection.getField(entityLook.getClass().getSuperclass(), "g").set(entityLook, false);
        Reflection.getField(entityLook.getClass().getSuperclass(), "h").set(entityLook, true);

        PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
        Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityID);
        Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, angle(yaw));

        sendPackets(entityLook, entityHeadRotation);

        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
    }

    public void walk(Location location) {
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - this.location.getY();
        double differenceZ = location.getZ() - this.location.getZ();

        walk(differenceX, differenceY, differenceZ);
    }

    //Coordinates as delta
    public void walk(double x, double y, double z) {
        if(Math.abs(x) < 8 && Math.abs(y) < 8 && Math.abs(z) < 8) {
            long changeX = (long) ((((this.location.getX() + x) * 32) - ((this.location.getX()) * 32)) * 128);
            long changeY = (long) ((((this.location.getY() + y) * 32) - ((this.location.getY()) * 32)) * 128);
            long changeZ = (long) ((((this.location.getZ() + z) * 32) - ((this.location.getZ()) * 32)) * 128);
            byte yaw = angle(this.getLocation().getYaw());
            byte pitch = angle(this.location.getPitch());

            PacketPlayOutRelEntityMoveLook relEntityMoveLook = new PacketPlayOutRelEntityMoveLook();
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "a").set(relEntityMoveLook, this.entityID);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "b").set(relEntityMoveLook, (int) changeX);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "c").set(relEntityMoveLook, (int) changeY);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "d").set(relEntityMoveLook, (int) changeZ);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "e").set(relEntityMoveLook, (byte) yaw);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "f").set(relEntityMoveLook, (byte) pitch);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "g").set(relEntityMoveLook, true); // onGround
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "h").set(relEntityMoveLook, true);

            sendPackets(relEntityMoveLook);

            this.location.add(x, y, z);
        } else
            System.err.println("[NPC] Error in walk input: difference cant be > 8");
    }

    public void step(Location location) {
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - this.location.getY();
        double differenceZ = location.getZ() - this.location.getZ();

        step(differenceX, differenceY, differenceZ);
    }

    //Coordinates as delta
    public void step(double x, double y, double z) {
        float yaw = (float) Math.toRadians((Math.atan2(z, x) * 180D / Math.PI) - 90F);
        float pitch = (float) Math.toRadians(-(Math.atan2(y, Math.sqrt(x * x + z * z)) * 180D / Math.PI));
        double changeX = -Math.sin(yaw);
        double changeY = -Math.sin(pitch);
        double changeZ = Math.cos(yaw);

        walk(changeX * this.moveSpeed, changeY * this.moveSpeed, changeZ * this.moveSpeed);
    }

    public void teleport(Location location) {
        PacketPlayOutEntityTeleport entityTeleport = new PacketPlayOutEntityTeleport();
        Reflection.getField(entityTeleport.getClass(), "a").set(entityTeleport, this.entityID);
        Reflection.getField(entityTeleport.getClass(), "b").set(entityTeleport, location.getX());
        Reflection.getField(entityTeleport.getClass(), "c").set(entityTeleport, location.getY());
        Reflection.getField(entityTeleport.getClass(), "d").set(entityTeleport, location.getZ());
        Reflection.getField(entityTeleport.getClass(), "e").set(entityTeleport, angle(location.getYaw()));
        Reflection.getField(entityTeleport.getClass(), "f").set(entityTeleport, angle(location.getPitch()));
        Reflection.getField(entityTeleport.getClass(), "g").set(entityTeleport, false);		//onGround

        PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
        Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityID);
        Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, angle(location.getYaw()));

        sendPackets(entityTeleport, entityHeadRotation);

        this.location = location;
    }

    public void equip(EnumEquipmentSlot slot, ItemStack item) {
        PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment();
        Reflection.getField(entityEquipment.getClass(), "a").set(entityEquipment, this.entityID);
        Reflection.getField(entityEquipment.getClass(), "b").set(entityEquipment, slot.getNMS());
        Reflection.getField(entityEquipment.getClass(), "c").set(entityEquipment, CraftItemStack.asNMSCopy(item));		//itemStack
        sendPackets(entityEquipment);

        this.equip[slot.getID()] = item;
    }

    public void playAnimation(EnumAnimation anim) {
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation();
        Reflection.getField(animation.getClass(), "a").set(animation, this.entityID);
        Reflection.getField(animation.getClass(), "b").set(animation, anim.getId());
        sendPackets(animation);
    }

    public void setStatus(EnumStatus status) {
        PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus();
        Reflection.getField(entityStatus.getClass(), "a").set(entityStatus, this.entityID);
        Reflection.getField(entityStatus.getClass(), "b").set(entityStatus, (byte) status.getID());
        sendPackets(entityStatus);
    }

    public void updateGameProfile(GameProfile gameProfile) {
        despawn();
        this.gameProfile = gameProfile;
        spawn(this.location);
    }

    public void updateMetadata() {
        sendPackets(new PacketPlayOutEntityMetadata(this.entityID, this.dataWatcher.toNMS(), true));
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

    public double getMoveSpeed() {
        return this.moveSpeed;
    }

    public Location getLocation() {
        if(this.location != null)
            return this.location;
        return null;
    }

    public boolean hasTarget() {
        return this.target != null;
    }

    public LivingEntity getTarget() {
        if(this.hasTarget())
            return this.target;
        return null;
    }


    public boolean hasEquipment(EnumEquipmentSlot slot) {
        return this.equip[slot.getID()] != null;
    }

    public ItemStack getEquipment(EnumEquipmentSlot slot) {
        return equip[slot.getID()];
    }

    // ### SETTER ###

    public void setHealth(float health) {
        if(health == 0) {
            this.setStatus(EnumStatus.DEAD);
        } else if(this.health > health) {
            this.setStatus(EnumStatus.HURT);
        } else if(this.health == 0 && health > 0) {
            this.spawn(this.location);
        }
        this.health = health;
    }

    public void setMoveSpeed(double speed) {
        this.moveSpeed = speed / 20;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
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

    private byte angle(float value) {
        return (byte) ((int) (value * 256F / 360F));
    }

    private void sendPrivatePackets(Player player, Packet<?>... packets) {
        for(Packet<?> packet : packets) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    private void sendPackets(Packet<?>... packets) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPrivatePackets(player, packets));
    }
}
