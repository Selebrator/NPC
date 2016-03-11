package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
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

import java.util.Arrays;

public class FakePlayer {

    private int entityID;
    private GameProfile gameProfile;
    private NullDataWatcher dataWatcher;

    private boolean living;
    private Location location;
    private LivingEntity target;

    private boolean noClip = true;
    private boolean gravity = false;

    private boolean[] status = new boolean[8];

    private float health = 20F;
    private double moveSpeed = 4.3D / 20;
    private static final double WALK_SPEED = 4.3D / 20;
    private static final double SNEAK_SPEED = 1.3D / 20;
    private static final double SPRINT_SPEED = 5.6D / 20;
    private static final double EYE_HEIGHT = 1.6D;

    private ItemStack[] equip = new ItemStack[6];


    public FakePlayer(GameProfile gameProfile) {
        this.entityID = (int) Reflection.getField(Entity.class, "entityCount").get(null);
        Reflection.getField(Entity.class, "entityCount").set(null, this.entityID + 1);

        this.gameProfile = gameProfile;

        this.dataWatcher = new NullDataWatcher();
        this.updateStatus(false, false, false, false, false, false, false);
        this.updateSkinFlags(true, true, true, true, true, true, true);
        this.dataWatcher.set(EnumDataWatcherObject.LIVING_HELATH_06, this.health);
    }

    public void spawn(Location location) {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();
        Reflection.getField(playerInfo.getClass(), "a").set(playerInfo, EnumPlayerInfoAction.ADD_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(
                playerInfo.new PlayerInfoData(this.gameProfile, 0, EnumGamemode.NOT_SET, new ChatComponentText(this.gameProfile.getName()))));

        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn();
        Reflection.getField(namedEntitySpawn.getClass(), "a").set(namedEntitySpawn, this.entityID);
        Reflection.getField(namedEntitySpawn.getClass(), "b").set(namedEntitySpawn, this.gameProfile.getId());
        Reflection.getField(namedEntitySpawn.getClass(), "c").set(namedEntitySpawn, location.getX());
        Reflection.getField(namedEntitySpawn.getClass(), "d").set(namedEntitySpawn, location.getY());
        Reflection.getField(namedEntitySpawn.getClass(), "e").set(namedEntitySpawn, location.getZ());
        Reflection.getField(namedEntitySpawn.getClass(), "f").set(namedEntitySpawn, angle(location.getYaw()));
        Reflection.getField(namedEntitySpawn.getClass(), "g").set(namedEntitySpawn, angle(location.getPitch()));
        Reflection.getField(namedEntitySpawn.getClass(), "h").set(namedEntitySpawn, this.dataWatcher.toNMS());

        broadcastPackets(playerInfo, namedEntitySpawn);

        this.location = location;
        this.living = true;

        for(EnumEquipmentSlot slot : EnumEquipmentSlot.values()) {
            this.equip(slot, this.equip[slot.getId()]);
        }
    }

    public void despawn() {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, null, null)));

        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy();
        Reflection.getField(entityDestroy.getClass(), "a").set(entityDestroy, new int[] { this.entityID });

        broadcastPackets(playerInfo, entityDestroy);

        this.living = false;
    }

    public void look(float yaw, float pitch) {
        if(this.living) {
            //rotate body
            PacketPlayOutEntityLook entityLook = new PacketPlayOutEntityLook();
            Reflection.getField(entityLook.getClass().getSuperclass(), "a").set(entityLook, this.entityID);
            Reflection.getField(entityLook.getClass().getSuperclass(), "e").set(entityLook, angle(yaw));
            Reflection.getField(entityLook.getClass().getSuperclass(), "f").set(entityLook, angle(pitch));
            Reflection.getField(entityLook.getClass().getSuperclass(), "g").set(entityLook, false);
            Reflection.getField(entityLook.getClass().getSuperclass(), "h").set(entityLook, true);

            //rotate head
            PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
            Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityID);
            Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, angle(yaw));

            broadcastPackets(entityLook, entityHeadRotation);

            this.location.setYaw(yaw);
            this.location.setPitch(pitch);
        }
    }

    //Coordinates as delta
    public void look(double x, double y, double z) {
        float yaw = calcYaw(x, z);
        float pitch = calcPitch(x, y, z);

        look(yaw, pitch);
    }

    public void look(Location location) {
        // calculate yaw and pitch
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - (this.location.getY() + EYE_HEIGHT);
        double differenceZ = location.getZ() - this.location.getZ();

        look(differenceX, differenceY, differenceZ);
    }

    //Coordinates as delta
    public void move(double x, double y, double z) {
        if(this.living) {
            if(Math.abs(x) < 8 && Math.abs(y) < 8 && Math.abs(z) < 8) {
				int changeX;
				int changeY;
				int changeZ;

				if(this.noClip) {
					changeX = (int) ((((this.location.getX() + x) * 32) - (this.location.getX() * 32)) * 128);
					changeY = (int) ((((this.location.getY() + y) * 32) - (this.location.getY() * 32)) * 128);
					changeZ = (int) ((((this.location.getZ() + z) * 32) - (this.location.getZ() * 32)) * 128);
				} else {
					//TODO add moving with collision.
					return;
				}

				float yaw = this.location.getYaw();
				float pitch = this.location.getPitch();

				PacketPlayOutRelEntityMoveLook relEntityMoveLook = new PacketPlayOutRelEntityMoveLook();
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "a").set(relEntityMoveLook, this.entityID);
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "b").set(relEntityMoveLook, changeX);
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "c").set(relEntityMoveLook, changeY);
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "d").set(relEntityMoveLook, changeZ);
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "e").set(relEntityMoveLook, angle(yaw));
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "f").set(relEntityMoveLook, angle(pitch));
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "g").set(relEntityMoveLook, true); // onGround
				Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "h").set(relEntityMoveLook, true);

				broadcastPackets(relEntityMoveLook);

				this.location.add(x, y, z);
			} else
				System.err.println("[NPC] Error in move input: difference cant be >= 8");
        }
    }

    public void move(Location location) {
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - this.location.getY();
        double differenceZ = location.getZ() - this.location.getZ();

        move(differenceX, differenceY, differenceZ);
    }

    public void step(float yaw, float pitch) {
        yaw = (float) Math.toRadians(yaw);
        pitch = (float) Math.toRadians(pitch);
        double changeX = -Math.sin(yaw);
        double changeY = -Math.sin(pitch);
        double changeZ = Math.cos(yaw);

        move(changeX * this.moveSpeed, changeY * this.moveSpeed, changeZ * this.moveSpeed);
    }

    //Coordinates as delta
    public void step(double x, double y, double z) {
        float yaw = calcYaw(x, z);
        float pitch = calcPitch(x, y, z);

        step(yaw, pitch);
    }

    public void step(Location location) {
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - this.location.getY();
        double differenceZ = location.getZ() - this.location.getZ();

        step(differenceX, differenceY, differenceZ);
    }

    public void fall() {
        //TODO
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

        broadcastPackets(entityTeleport, entityHeadRotation);

        this.location = location;
    }

    public void equip(EnumEquipmentSlot slot, ItemStack item) {
        PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment();
        Reflection.getField(entityEquipment.getClass(), "a").set(entityEquipment, this.entityID);
        Reflection.getField(entityEquipment.getClass(), "b").set(entityEquipment, slot.getNMS());
        Reflection.getField(entityEquipment.getClass(), "c").set(entityEquipment, CraftItemStack.asNMSCopy(item));		//itemStack
        broadcastPackets(entityEquipment);

        this.equip[slot.getId()] = item;
    }

    public void playAnimation(EnumAnimation anim) {
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation();
        Reflection.getField(animation.getClass(), "a").set(animation, this.entityID);
        Reflection.getField(animation.getClass(), "b").set(animation, anim.getId());
        broadcastPackets(animation);
    }

    public void setStatus(EnumEntityStatus status) {
        PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus();
        Reflection.getField(entityStatus.getClass(), "a").set(entityStatus, this.entityID);
        Reflection.getField(entityStatus.getClass(), "b").set(entityStatus, (byte) status.getId());
        broadcastPackets(entityStatus);
    }

    public void updateMetadata() {
        broadcastPackets(new PacketPlayOutEntityMetadata(this.entityID, this.dataWatcher.toNMS(), true));
    }



    // ### GETTER ###

    public int getEntityID() {
        return this.entityID;
    }

    public String getName() {
        return this.gameProfile.getName();
    }

    public boolean isAlive() {
        return this.living;
    }

    public float getHealth() {
        if(this.isAlive()) {
            return this.health;
        }
        return 0;
    }

    public double getMoveSpeed() {
        return this.moveSpeed;
    }

    public double getEyeHeight() {
        return EYE_HEIGHT;
    }

    public Location getLocation() {
        if(this.location != null)
            return this.location;
        return null;
    }

    public Location getEyeLocation() {
        if(this.location != null)
            return this.location.add(0, EYE_HEIGHT, 0);
        return null;
    }

    public boolean hasCollision() {
        return !this.noClip;
    }

    public boolean hasGravity() {
        return this.gravity;
    }

    public boolean hasTarget() {
        return this.target != null;
    }

    public LivingEntity getTarget() {
        if(this.hasTarget())
            return this.target;
        return null;
    }

    public boolean isOnFire() {
        return this.status[EnumStatus.FIRE.getId()];
    }

    public boolean isSneaking() {
        return this.status[EnumStatus.SNEAK.getId()];
    }

    public boolean isSprinting() {
        return this.status[EnumStatus.SPRINT.getId()];
    }

    public boolean isInvisible() {
        return this.status[EnumStatus.INVISIBLE.getId()];
    }

    public boolean isGlowing() {
        return this.status[EnumStatus.GLOW.getId()];
    }

    public boolean isElytraUsed() {
        return this.status[EnumStatus.ELYTRA.getId()];
    }

    public boolean hasEquipment(EnumEquipmentSlot slot) {
        return this.equip[slot.getId()] != null;
    }

    public ItemStack getEquipment(EnumEquipmentSlot slot) {
        return equip[slot.getId()];
    }

    // ### SETTER ###

    public void updateGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
        if(this.living) {
            this.spawn(this.location);
        }
    }

    public void setHealth(float health) {
        if(health == 0) {
            this.setStatus(EnumEntityStatus.DEAD);
            this.living = false;
        } else if(this.health > health) {
            this.setStatus(EnumEntityStatus.HURT);
        } else if(this.health == 0 && health > 0) {
            this.spawn(this.location);
        }
        this.health = health;
    }

    public void setMoveSpeed(double speed) {
        this.moveSpeed = speed / 20;
    }

    public void enableNoClip(boolean state) {
        this.noClip = state;
    }

    public void enableGravity(boolean state) {
        this.gravity = state;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public void setOnFire(boolean state) {
        updateStatus(EnumStatus.FIRE, state);
    }

    public void setSneaking(boolean state) {
        updateStatus(EnumStatus.SNEAK, state);
        if(state) {
            this.moveSpeed = SNEAK_SPEED;
        } else {
            this.moveSpeed = WALK_SPEED;
        }
    }

    public void setSprinting(boolean state) {
        updateStatus(EnumStatus.SPRINT, state);
        if(state) {
            this.moveSpeed = SPRINT_SPEED;
        } else {
            this.moveSpeed = WALK_SPEED;
        }
    }

    public void setInvisible(boolean state) {
        updateStatus(EnumStatus.INVISIBLE, state);
    }

    public void setGlowing(boolean state) {
        updateStatus(EnumStatus.GLOW, state);
    }

    public void useElytra(boolean state) {
        updateStatus(EnumStatus.ELYTRA, state);
    }



    // ### DATAWATCHER BITMASKS ###

    // 0
    public void updateStatus(boolean fire, boolean sneak, boolean sprint, boolean use, boolean invisible, boolean glow, boolean elytra) {
        byte bitMask = 0;
        bitMask = changeBit(bitMask, EnumStatus.FIRE.getId(), fire);
        bitMask = changeBit(bitMask, EnumStatus.SNEAK.getId(), sneak);
        bitMask = changeBit(bitMask, EnumStatus.SPRINT.getId(), sprint);
        bitMask = changeBit(bitMask, EnumStatus.USE.getId(), use);
        bitMask = changeBit(bitMask, EnumStatus.INVISIBLE.getId(), invisible);
        bitMask = changeBit(bitMask, EnumStatus.GLOW.getId(), glow);
        bitMask = changeBit(bitMask, EnumStatus.ELYTRA.getId(), elytra);

        this.dataWatcher.set(EnumDataWatcherObject.ENTITY_STATUS_BITMASK_00, bitMask);

        for(EnumStatus current : EnumStatus.values()) {
            this.status[current.getId()] = readBit(bitMask, current.getId());
        }
    }

    public void updateStatus(EnumStatus status, boolean state) {
        byte bitMask = 0;
        for(EnumStatus current : EnumStatus.values()) {
            bitMask = changeBit(bitMask, current.getId(), this.status[current.getId()]);
        }
        bitMask = changeBit(bitMask, status.getId(), state);

        this.dataWatcher.set(EnumDataWatcherObject.ENTITY_STATUS_BITMASK_00, bitMask);

        this.status[status.getId()] = readBit(bitMask, status.getId());
    }

    //10
    public void updateSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
        byte bitMask = 0;
        bitMask = changeBit(bitMask, EnumSkinFlag.CAPE.getId(), cape);
        bitMask = changeBit(bitMask, EnumSkinFlag.JACKET.getId(), jacket);
        bitMask = changeBit(bitMask, EnumSkinFlag.LEFT_SLEEVE.getId(), leftArm);
        bitMask = changeBit(bitMask, EnumSkinFlag.RIGHT_SLEEVE.getId(), rightArm);
        bitMask = changeBit(bitMask, EnumSkinFlag.LEFT_PANTS.getId(), leftLeg);
        bitMask = changeBit(bitMask, EnumSkinFlag.RIGHT_PANTS.getId(), rightLeg);
        bitMask = changeBit(bitMask, EnumSkinFlag.HAT.getId(), hat);

        this.dataWatcher.set(EnumDataWatcherObject.HUMAN_SKIN_BITBASK_12, bitMask);
    }



    // ### UTIL ###

    private boolean readBit(byte bitMask, int bit) {
        return (bitMask & (1 << bit)) != 0;
    }

    private byte changeBit(byte bitMask, int bit, boolean state) {
        if(state) {
            return (byte) (bitMask | (1 << bit));
        } else {
            return (byte) (bitMask & ~(1 << bit));
        }
    }

    //Coordinates as delta
    private float calcYaw(double x, double z) {
        return (float) ((Math.atan2(z, x) * 180D / Math.PI) - 90F);
    }

    //Coordinates as delta
    private float calcPitch(double x, double y, double z) {
        return (float) -(Math.atan2(y, Math.sqrt(x * x + z * z)) * 180D / Math.PI);
    }

    private byte angle(float value) {
        return (byte) ((int) (value * 256F / 360F));
    }

    private void sendPackets(Player player, Packet<?>... packets) {
        for(Packet<?> packet : packets) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    private void broadcastPackets(Packet<?>... packets) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPackets(player, packets));
    }
}
