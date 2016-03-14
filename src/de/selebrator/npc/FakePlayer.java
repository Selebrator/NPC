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
import net.minecraft.server.v1_9_R1.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Arrays;

public class FakePlayer implements NPC {

    private final int entityId;
    private GameProfile gameProfile;
    private FakePlayerMeta meta;

    private boolean living;
    private Location location;
    private LivingEntity target;
    private EnumNature nature = EnumNature.PASSIVE;

    private float health = 20F;
    private double moveSpeed = 4.3D / 20;
    private Location respawnLocation;

    private ItemStack[] equip = new ItemStack[6];

    private static final double WALK_SPEED = 4.3D / 20;
    private static final double SNEAK_SPEED = 1.3D / 20;
    private static final double SPRINT_SPEED = 5.6D / 20;
    private static final double EYE_HEIGHT_STANDING = 1.62D;
    private static final double EYE_HEIGHT_SNEAKING = 1.2D;


    public FakePlayer(GameProfile gameProfile) {
        this.entityId = (int) Reflection.getField(Entity.class, "entityCount").get(null);
        Reflection.getField(Entity.class, "entityCount").set(null, this.entityId + 1);

        this.gameProfile = gameProfile;

        this.meta = new FakePlayerMeta();
        this.meta.setStatus(false, false, false, false, false, false);
        this.meta.setSkinFlags(true, true, true, true, true, true, true);
        this.meta.setHealth(this.health);
        this.meta.setName(this.getName());
    }

    @Override
    public FakePlayer spawn(Location location) {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();
        Reflection.getField(playerInfo.getClass(), "a").set(playerInfo, EnumPlayerInfoAction.ADD_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(
                playerInfo.new PlayerInfoData(this.gameProfile, 0, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(this.gameProfile.getName()))));

        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn();
        Reflection.getField(namedEntitySpawn.getClass(), "a").set(namedEntitySpawn, this.entityId);
        Reflection.getField(namedEntitySpawn.getClass(), "b").set(namedEntitySpawn, this.gameProfile.getId());
        Reflection.getField(namedEntitySpawn.getClass(), "c").set(namedEntitySpawn, location.getX());
        Reflection.getField(namedEntitySpawn.getClass(), "d").set(namedEntitySpawn, location.getY());
        Reflection.getField(namedEntitySpawn.getClass(), "e").set(namedEntitySpawn, location.getZ());
        Reflection.getField(namedEntitySpawn.getClass(), "f").set(namedEntitySpawn, MathHelper.angle(location.getYaw()));
        Reflection.getField(namedEntitySpawn.getClass(), "g").set(namedEntitySpawn, MathHelper.angle(location.getPitch()));
        Reflection.getField(namedEntitySpawn.getClass(), "h").set(namedEntitySpawn, this.meta.getDataWatcher());

        broadcastPackets(playerInfo, namedEntitySpawn);

        this.location = location;
        if(this.respawnLocation == null) { this.respawnLocation = location; }
        this.living = this.meta.getHealth() > 0;

        for(EnumEquipmentSlot slot : EnumEquipmentSlot.values()) {
            this.equip(slot, this.equip[slot.getId()]);
        }

        this.look(location.getYaw(), location.getPitch());

        return this;
    }

    @Override
    public void respawn(@Nullable Location location) {
        if(!this.isAlive()) {
            this.target = null;
            this.location = null;
            this.health = 20F;
            this.equip = new ItemStack[6];
            this.moveSpeed = WALK_SPEED;
            this.nature = EnumNature.PASSIVE;

            this.meta = new FakePlayerMeta();
            this.meta.setStatus(false, false, false, false, false, false);
            this.meta.setSkinFlags(true, true, true, true, true, true, true);
            this.meta.setHealth(this.health);
            this.meta.setName(this.getName());

            this.spawn(location != null ? location : this.respawnLocation);
        }
    }

    @Override
    public void despawn() {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, null, null)));

        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy();
        Reflection.getField(entityDestroy.getClass(), "a").set(entityDestroy, new int[] { this.entityId});

        broadcastPackets(playerInfo, entityDestroy);

        this.location = null;
        this.living = false;
    }

	@Override
    public void look(float yaw, float pitch) {
        //rotate body
        PacketPlayOutEntityLook entityLook = new PacketPlayOutEntityLook();
        Reflection.getField(entityLook.getClass().getSuperclass(), "a").set(entityLook, this.entityId);
        Reflection.getField(entityLook.getClass().getSuperclass(), "e").set(entityLook, MathHelper.angle(yaw));
        Reflection.getField(entityLook.getClass().getSuperclass(), "f").set(entityLook, MathHelper.angle(pitch));
        Reflection.getField(entityLook.getClass().getSuperclass(), "g").set(entityLook, false);
        Reflection.getField(entityLook.getClass().getSuperclass(), "h").set(entityLook, true);

        //rotate head
        PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
        Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityId);
        Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, MathHelper.angle(yaw));

        broadcastPackets(entityLook, entityHeadRotation);

        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
    }

	@Override
    public void look(double x, double y, double z) {
        float yaw = MathHelper.calcYaw(x, z);
        float pitch = MathHelper.calcPitch(x, y, z);

        look(yaw, pitch);
    }

	@Override
    public void look(Location location) {
        Vector distance = MathHelper.calcDistanceVector(this.getEyeLocation(), location);

        look(distance.getX(), distance.getY(), distance.getZ());
    }

	@Override
    public void move(double x, double y, double z) {
        if(Math.abs(x) < 8 && Math.abs(y) < 8 && Math.abs(z) < 8) {
            int changeX = (int) ((((this.location.getX() + x) * 32) - (this.location.getX() * 32)) * 128);
            int changeY = (int) ((((this.location.getY() + y) * 32) - (this.location.getY() * 32)) * 128);
            int changeZ = (int) ((((this.location.getZ() + z) * 32) - (this.location.getZ() * 32)) * 128);
            float yaw = this.location.getYaw();
            float pitch = this.location.getPitch();

            PacketPlayOutRelEntityMoveLook relEntityMoveLook = new PacketPlayOutRelEntityMoveLook();
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "a").set(relEntityMoveLook, this.entityId);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "b").set(relEntityMoveLook, changeX);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "c").set(relEntityMoveLook, changeY);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "d").set(relEntityMoveLook, changeZ);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "e").set(relEntityMoveLook, MathHelper.angle(yaw));
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "f").set(relEntityMoveLook, MathHelper.angle(pitch));
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "g").set(relEntityMoveLook, true); //onGround
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "h").set(relEntityMoveLook, true);

            broadcastPackets(relEntityMoveLook);

            this.location.add(x, y, z);
        } else
            System.err.println("[NPC] Error in move input: difference cant be > 8");
    }

	@Override
    public void move(Location location) {
        Vector distance = MathHelper.calcDistanceVector(this.location, location);

        move(distance.getX(), distance.getY(), distance.getZ());
    }

	@Override
    public void step(float yaw, float pitch) {
        Vector direction = MathHelper.calcDirectionVector(this.moveSpeed, yaw, pitch);

        move(direction.getX(), direction.getY(), direction.getZ());
    }

	@Override
    public void step(double x, double y, double z) {
        float yaw = MathHelper.calcYaw(x, z);
        float pitch = MathHelper.calcPitch(x, y, z);

        step(yaw, pitch);
    }

	@Override
    public void step(Location location) {
        Vector distance = MathHelper.calcDistanceVector(this.location, location);

        step(distance.getX(), distance.getY(), distance.getZ());
    }

    @Override
    public void teleport(Location location) {
        PacketPlayOutEntityTeleport entityTeleport = new PacketPlayOutEntityTeleport();
        Reflection.getField(entityTeleport.getClass(), "a").set(entityTeleport, this.entityId);
        Reflection.getField(entityTeleport.getClass(), "b").set(entityTeleport, location.getX());
        Reflection.getField(entityTeleport.getClass(), "c").set(entityTeleport, location.getY());
        Reflection.getField(entityTeleport.getClass(), "d").set(entityTeleport, location.getZ());
        Reflection.getField(entityTeleport.getClass(), "e").set(entityTeleport, MathHelper.angle(location.getYaw()));
        Reflection.getField(entityTeleport.getClass(), "f").set(entityTeleport, MathHelper.angle(location.getPitch()));
        Reflection.getField(entityTeleport.getClass(), "g").set(entityTeleport, false);		//onGround

        PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation();
        Reflection.getField(entityHeadRotation.getClass(), "a").set(entityHeadRotation, this.entityId);
        Reflection.getField(entityHeadRotation.getClass(), "b").set(entityHeadRotation, MathHelper.angle(location.getYaw()));

        broadcastPackets(entityTeleport, entityHeadRotation);

        this.location = location;
    }

    @Override
    public void equip(EnumEquipmentSlot slot, ItemStack item) {
        PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment();
        Reflection.getField(entityEquipment.getClass(), "a").set(entityEquipment, this.entityId);
        Reflection.getField(entityEquipment.getClass(), "b").set(entityEquipment, slot.getNMS());
        Reflection.getField(entityEquipment.getClass(), "c").set(entityEquipment, CraftItemStack.asNMSCopy(item)); //itemStack
        broadcastPackets(entityEquipment);

        this.equip[slot.getId()] = item;
    }

    @Override
    public void playAnimation(EnumAnimation anim) {
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation();
        Reflection.getField(animation.getClass(), "a").set(animation, this.entityId);
        Reflection.getField(animation.getClass(), "b").set(animation, anim.getId());
        broadcastPackets(animation);
    }

    @Override
    public void setEntityStatus(EnumEntityStatus status) {
        PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus();
        Reflection.getField(entityStatus.getClass(), "a").set(entityStatus, this.entityId);
        Reflection.getField(entityStatus.getClass(), "b").set(entityStatus, (byte) status.getId());
        broadcastPackets(entityStatus);
    }

    @Override
    public void updateMetadata() {
        broadcastPackets(new PacketPlayOutEntityMetadata(this.entityId, this.meta.getDataWatcher(), true));
    }



    // ### GETTER ###

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public FakePlayerMeta getMeta() {
        return this.meta;
    }

    public String getName() {
        return ChatColor.stripColor(this.gameProfile.getName());
    }

    public String getDisplayName() {
        return this.gameProfile.getName();
    }

    @Override
    public boolean isAlive() {
        return this.living;
    }

    public float getHealth() {
        return this.isAlive() ? this.health : 0;
    }

    public double getMoveSpeed() {
        return this.moveSpeed;
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        return ignoreSneaking ? EYE_HEIGHT_STANDING : (this.meta.isSneaking() ? EYE_HEIGHT_SNEAKING : EYE_HEIGHT_STANDING);
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public Location getLocation() {
            return this.location;
    }

    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    public Location getEyeLocation() {
        return this.hasLocation() ? this.location.clone().add(0, this.getEyeHeight(false), 0) : null;

    }

    public boolean hasTarget() {
        return this.target != null && !this.target.isDead();
    }

    public LivingEntity getTarget() {
        return this.hasTarget() ? this.target : null;
    }

    public EnumNature getNature() {
        return this.nature;
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

    public void setMeta(FakePlayerMeta meta) {
        this.meta = meta;
        this.updateMetadata();
    }

    public void setHealth(float health) {
        if(health == 0) {
            this.setEntityStatus(EnumEntityStatus.DEAD);
            this.living = false;
        } else if(this.health > health) {
            this.setEntityStatus(EnumEntityStatus.HURT);
        } else if(this.health == 0 && health > 0) {
            if(this.hasLocation()) {
                this.health = health;
                this.meta.setHealth(health);
                this.spawn(this.location);
                return;
            }
        }
        this.health = health;
        this.meta.setHealth(health);
    }

    public void setRespawnLocation(Location location) {
        this.respawnLocation = location;
    }

    public void setMoveSpeed(double speed) {
        this.moveSpeed = speed / 20;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public void setNature(EnumNature nature) {
        this.nature = nature;
    }

    public void setSneaking(boolean state) {
        this.meta.setSneaking(state);
        this.moveSpeed = state ? SNEAK_SPEED : WALK_SPEED;
    }

    public void setSprinting(boolean state) {
        this.meta.setSprinting(state);
        this.moveSpeed = state ? SPRINT_SPEED : WALK_SPEED;
    }


    // ##### UTIL #####

    private static void sendPackets(Player player, Packet<?>... packets) {
        for(Packet<?> packet : packets) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    private static void broadcastPackets(Packet<?>... packets) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPackets(player, packets));
    }
}
