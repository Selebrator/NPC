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

import java.util.Arrays;

public class FakePlayer implements NPC {

    private final int entityID;
    private GameProfile gameProfile;
    private FakePlayerMeta meta;

    private boolean living;
    private Location location;
    private LivingEntity target;
    private EnumNature nature = EnumNature.PASSIVE;

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

        this.meta = new FakePlayerMeta();
        this.meta.setStatus(false, false, false, false, false, false);
        this.meta.setSkinFlags(true, true, true, true, true, true, true);
        this.meta.setHealth(this.health);
        this.meta.setName(this.getName());
    }

    @Override
    public void spawn(Location location) {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo();
        Reflection.getField(playerInfo.getClass(), "a").set(playerInfo, EnumPlayerInfoAction.ADD_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(
                playerInfo.new PlayerInfoData(this.gameProfile, 0, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(this.gameProfile.getName()))));

        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn();
        Reflection.getField(namedEntitySpawn.getClass(), "a").set(namedEntitySpawn, this.entityID);
        Reflection.getField(namedEntitySpawn.getClass(), "b").set(namedEntitySpawn, this.gameProfile.getId());
        Reflection.getField(namedEntitySpawn.getClass(), "c").set(namedEntitySpawn, location.getX());
        Reflection.getField(namedEntitySpawn.getClass(), "d").set(namedEntitySpawn, location.getY());
        Reflection.getField(namedEntitySpawn.getClass(), "e").set(namedEntitySpawn, location.getZ());
        Reflection.getField(namedEntitySpawn.getClass(), "f").set(namedEntitySpawn, angle(location.getYaw()));
        Reflection.getField(namedEntitySpawn.getClass(), "g").set(namedEntitySpawn, angle(location.getPitch()));
        Reflection.getField(namedEntitySpawn.getClass(), "h").set(namedEntitySpawn, this.meta.getDataWatcher());

        broadcastPackets(playerInfo, namedEntitySpawn);

        this.location = location;
        this.living = this.meta.getHealth() > 0;

        for(EnumEquipmentSlot slot : EnumEquipmentSlot.values()) {
            this.equip(slot, this.equip[slot.getId()]);
        }

        this.look(location.getYaw(), location.getPitch());
    }

    @Override
    public void despawn() {
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER);
        Reflection.getField(playerInfo.getClass(), "b").set(playerInfo, Arrays.asList(playerInfo.new PlayerInfoData(this.gameProfile, 0, null, null)));

        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy();
        Reflection.getField(entityDestroy.getClass(), "a").set(entityDestroy, new int[] { this.entityID });

        broadcastPackets(playerInfo, entityDestroy);

        this.location = null;
        this.living = false;
    }

	@Override
    public void look(float yaw, float pitch) {
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

	@Override
    public void look(double x, double y, double z) {
        float yaw = calcYaw(x, z);
        float pitch = calcPitch(x, y, z);

        look(yaw, pitch);
    }

	@Override
    public void look(Location location) {
        // calculate yaw and pitch
        double differenceX = location.getX() - this.location.getX();
        double differenceY = location.getY() - (this.location.getY() + EYE_HEIGHT);
        double differenceZ = location.getZ() - this.location.getZ();

        look(differenceX, differenceY, differenceZ);
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
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "a").set(relEntityMoveLook, this.entityID);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "b").set(relEntityMoveLook, changeX);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "c").set(relEntityMoveLook, changeY);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "d").set(relEntityMoveLook, changeZ);
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "e").set(relEntityMoveLook, angle(yaw));
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "f").set(relEntityMoveLook, angle(pitch));
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "g").set(relEntityMoveLook, true); //onGround
            Reflection.getField(relEntityMoveLook.getClass().getSuperclass(), "h").set(relEntityMoveLook, true);

            broadcastPackets(relEntityMoveLook);

            this.location.add(x, y, z);
        } else
            System.err.println("[NPC] Error in move input: difference cant be >= 8");
    }

	@Override
    public void move(Location location) {
        Vector distance = calcDistanceVector(this.location, location);

        move(distance.getX(), distance.getY(), distance.getZ());
    }

	@Override
    public void step(float yaw, float pitch) {
        Vector direction = calcDirectionVector(this.moveSpeed, yaw, pitch);

        move(direction.getX(), direction.getY(), direction.getZ());
    }

	@Override
    public void step(double x, double y, double z) {
        float yaw = calcYaw(x, z);
        float pitch = calcPitch(x, y, z);

        step(yaw, pitch);
    }

	@Override
    public void step(Location location) {
        Vector distance = calcDistanceVector(this.location, location);

        step(distance.getX(), distance.getY(), distance.getZ());
    }

    @Override
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

    @Override
    public void equip(EnumEquipmentSlot slot, ItemStack item) {
        PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment();
        Reflection.getField(entityEquipment.getClass(), "a").set(entityEquipment, this.entityID);
        Reflection.getField(entityEquipment.getClass(), "b").set(entityEquipment, slot.getNMS());
        Reflection.getField(entityEquipment.getClass(), "c").set(entityEquipment, CraftItemStack.asNMSCopy(item)); //itemStack
        broadcastPackets(entityEquipment);

        this.equip[slot.getId()] = item;
    }

    @Override
    public void playAnimation(EnumAnimation anim) {
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation();
        Reflection.getField(animation.getClass(), "a").set(animation, this.entityID);
        Reflection.getField(animation.getClass(), "b").set(animation, anim.getId());
        broadcastPackets(animation);
    }

    @Override
    public void setEntityStatus(EnumEntityStatus status) {
        PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus();
        Reflection.getField(entityStatus.getClass(), "a").set(entityStatus, this.entityID);
        Reflection.getField(entityStatus.getClass(), "b").set(entityStatus, (byte) status.getId());
        broadcastPackets(entityStatus);
    }

    @Override
    public void updateMetadata() {
        broadcastPackets(new PacketPlayOutEntityMetadata(this.entityID, this.meta.getDataWatcher(), true));
    }



    // ### GETTER ###

    public int getEntityID() {
        return this.entityID;
    }

    public FakePlayerMeta getMeta() {
        return this.meta;
    }

    public String getName() {
        return ChatColor.stripColor(this.gameProfile.getName());
    }

    public String getDisplayName() {
        return this.gameProfile.getName();
    }

    public boolean isAlive() {
        return this.living;
    }

    public float getHealth() {
        return this.isAlive() ? this.health : 0;
    }

    public double getMoveSpeed() {
        return this.moveSpeed;
    }

    public double getEyeHeight() {
        return EYE_HEIGHT;
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public Location getLocation() {
            return this.hasLocation() ? this.location :  null;
    }

    public Location getEyeLocation() {
        return this.hasLocation() ? this.location.add(0, EYE_HEIGHT, 0) :  null;
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
        meta.setSneaking(state);
        this.moveSpeed = state ? SNEAK_SPEED : WALK_SPEED;
    }

    public void setSprinting(boolean state) {
        meta.setSprinting(state);
        this.moveSpeed = state ? SPRINT_SPEED : WALK_SPEED;
    }


    // ##### UTIL #####

	/**
     *
     * @param x relative x
     * @param y relative y
     * @param z relative z
     * @return distance aka ρ (rho)
     */
    public static double calcDistanceVector(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

	/**
     *
     * @param x relative x
     * @param z relative z
     * @return yaw aka θ (theta) in degree
     */
    public static float calcYaw(double x, double z) {
        return (float) Math.toDegrees(Math.atan2(z, x)) - 90F;
    }

	/**
     *
     * @param x relative x
     * @param y relative y
     * @param z relative z
     * @return pitch aka φ (phi) in degree
     */
    public static float calcPitch(double x, double y, double z) {
        return (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));
    }

	/**
     *
     * @param distance aka ρ (rho)
     * @param yaw aka θ (theta) in degrees
     * @param pitch aka φ (phi) in degrees
     * @return rectangular coordinates
     */
    public static Vector calcDirectionVector(double distance, float yaw, float pitch) {
        Vector direction = new Vector();
        direction.setX(-Math.sin(Math.toRadians(yaw)));
        direction.setY(-Math.sin(Math.toRadians(pitch)));
        direction.setZ(Math.cos(Math.toRadians(yaw)));
        return direction.multiply(distance);
    }

	/**
     *
     * @param startX current location x
     * @param startY current location y
     * @param startZ current location z
     * @param destinationX target location x
     * @param destinationY target location y
     * @param destinationZ target location z
     * @return distance between start location and destination location
	 */
    public static Vector calcDistanceVector(double startX, double startY, double startZ, double destinationX, double destinationY, double destinationZ) {
        Vector distance = new Vector();
        distance.setX(destinationX - startX);
        distance.setY(destinationY - startY);
        distance.setZ(destinationZ - startZ);
        return distance;
    }

	/**
     *
     * @param start current location
     * @param destination target location
     * @return distance between start location and destination location
     */
    public static Vector calcDistanceVector(Location start, Location destination) {
        return calcDistanceVector(start.getX(), start.getY(), start.getZ(), destination.getX(), destination.getY(), destination.getZ());
    }

	/**
     * prepare angle for packet
     * @param value angle in degrees
     * @return rotation in 1/265 steps
     */
    private static byte angle(float value) {
        return (byte) ((int) (value * 256F / 360F));
    }

    private static void sendPackets(Player player, Packet<?>... packets) {
        for(Packet<?> packet : packets) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    private static void broadcastPackets(Packet<?>... packets) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPackets(player, packets));
    }
}
