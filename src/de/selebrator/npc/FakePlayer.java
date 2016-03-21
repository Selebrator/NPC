package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
import de.selebrator.event.npc.NPCAnimationEvent;
import de.selebrator.event.npc.NPCDespawnEvent;
import de.selebrator.event.npc.NPCEquipEvent;
import de.selebrator.event.npc.NPCMoveEvent;
import de.selebrator.event.npc.NPCSpawnEvent;
import de.selebrator.event.npc.NPCTeleportEvent;
import de.selebrator.fetcher.PacketFetcher;
import de.selebrator.reflection.Reflection;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FakePlayer implements NPC {

    private final int entityId;
    public GameProfile gameProfile;
    private FakePlayerMeta meta;

    private boolean living;
    private Location location;
    private LivingEntity target;
    private EnumNature nature = EnumNature.PASSIVE;

    private float health = 20F;
    private double moveSpeed = EnumMoveSpeed.WALKING.getSpeed() / 20;
    private Location respawnLocation;

    private ItemStack[] equip = new ItemStack[6];

    private int speedAmplifier = -1;

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
    public void spawn(Location location) {
        NPCSpawnEvent event = new NPCSpawnEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) { return; }

        PacketFetcher.broadcastPackets(
                PacketFetcher.playerInfo(this.gameProfile, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER),
                PacketFetcher.namedEntitySpawn(this.entityId, this.gameProfile, location, this.meta.getDataWatcher())
        );

        this.location = location;
        if(this.respawnLocation == null) { this.respawnLocation = location; }
        this.living = this.meta.getHealth() > 0;

        for(EnumEquipmentSlot slot : EnumEquipmentSlot.values()) {
            this.equip(slot, this.equip[slot.getId()]);
        }

        this.look(location.getYaw(), location.getPitch());
    }

    @Override
    public void respawn(Location location) {
        if(!this.isAlive()) {
            this.target = null;
            this.location = null;
            this.health = 20F;
            this.equip = new ItemStack[6];
            this.setMoveSpeed(EnumMoveSpeed.WALKING.getSpeed());
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
        NPCDespawnEvent event = new NPCDespawnEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) { return; }

        PacketFetcher.broadcastPackets(
                PacketFetcher.playerInfo(this.gameProfile, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER),
                PacketFetcher.entityDestroy(this.entityId)
        );

        this.location = null;
        this.living = false;
    }

	@Override
    public void look(float yaw, float pitch) {
        PacketFetcher.broadcastPackets(
                PacketFetcher.entityLook(this.entityId, yaw, pitch), //body
                PacketFetcher.headRotation(this.entityId, yaw) //head
        );

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
        NPCMoveEvent event = new NPCMoveEvent(this, this.location.clone().add(x, y, z));
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) { return; }

        Vector distance = MathHelper.calcDistanceVector(this.location, event.getDestination());
        x = distance.getX();
        y = distance.getY();
        z = distance.getZ();

        if(Math.abs(x) < 8 && Math.abs(y) < 8 && Math.abs(z) < 8) {
            int changeX = (int) ((((this.location.getX() + x) * 32) - (this.location.getX() * 32)) * 128);
            int changeY = (int) ((((this.location.getY() + y) * 32) - (this.location.getY() * 32)) * 128);
            int changeZ = (int) ((((this.location.getZ() + z) * 32) - (this.location.getZ() * 32)) * 128);

            PacketFetcher.broadcastPackets(
                    PacketFetcher.relEntityMove(this.entityId, changeX, changeY, changeZ)
            );

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
        NPCTeleportEvent event = new NPCTeleportEvent(this, location);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) { return; }

        location = event.getDestination();

        PacketFetcher.broadcastPackets(
                PacketFetcher.entityTeleport(this.entityId, location),
                PacketFetcher.headRotation(this.entityId, location.getYaw())
        );

        this.location = location;
    }

    @Override
    public void equip(EnumEquipmentSlot slot, ItemStack item) {
        NPCEquipEvent event = new NPCEquipEvent(this, slot, item);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) { return; }

        slot = event.getSlot();
        item = event.getItem();

        PacketFetcher.broadcastPackets(
                PacketFetcher.entityEquipment(this.entityId, slot.getNMS(), CraftItemStack.asNMSCopy(item))
        );

        this.equip[slot.getId()] = item;
    }

    @Override
    public void playAnimation(EnumAnimation anim) {
        NPCAnimationEvent event = new NPCAnimationEvent(this, anim);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) { return; }

        anim = event.getAnimation();

        PacketFetcher.broadcastPackets(
                PacketFetcher.animation(this.entityId, anim.getId())
        );
    }

    @Override
    public void setEntityStatus(EnumEntityStatus status) {
        PacketFetcher.broadcastPackets(
                PacketFetcher.entityStatus(this.entityId, status.getId())
        );
    }

    @Override
    public void updateMetadata() {
        PacketFetcher.broadcastPackets(new PacketPlayOutEntityMetadata(this.entityId, this.meta.getDataWatcher(), true));
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

    @Override
    public String getName() {
        return ChatColor.stripColor(this.gameProfile.getName());
    }

    @Override
    public String getDisplayName() {
        return this.gameProfile.getName();
    }

    @Override
    public boolean isAlive() {
        return this.living;
    }

    @Override
    public float getHealth() {
        return this.isAlive() ? this.health : 0;
    }

    @Override
    public double getMoveSpeed() {
        return this.moveSpeed * 20;
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        return ignoreSneaking ? EYE_HEIGHT_STANDING : (this.meta.isSneaking() ? EYE_HEIGHT_SNEAKING : EYE_HEIGHT_STANDING);
    }

    @Override
    public boolean hasLocation() {
        return this.location != null;
    }

    @Override
    public Location getLocation() {
            return this.location;
    }

    @Override
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    @Override
    public Location getEyeLocation() {
        return this.hasLocation() ? this.location.clone().add(0, this.getEyeHeight(false), 0) : null;

    }

    @Override
    public boolean hasTarget() {
        return this.target != null && !this.target.isDead();
    }

    @Override
    public LivingEntity getTarget() {
        return this.hasTarget() ? this.target : null;
    }

    @Override
    public EnumNature getNature() {
        return this.nature;
    }

    @Override
    public boolean hasEquipment(EnumEquipmentSlot slot) {
        return this.equip[slot.getId()] != null;
    }

    @Override
    public ItemStack getEquipment(EnumEquipmentSlot slot) {
        return equip[slot.getId()];
    }

    // ### SETTER ###

    @Override
    public void updateGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
        if(this.living) {
            this.spawn(this.location);
        }
    }

    @Override
    public void setMeta(FakePlayerMeta meta) {
        this.meta = meta;
        this.updateMetadata();
    }

    @Override
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

    @Override
    public void setRespawnLocation(Location location) {
        this.respawnLocation = location;
    }

    @Override
    public void setMoveSpeed(double speed) {
        this.moveSpeed = speed / 20;
    }

    @Override
    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void setNature(EnumNature nature) {
        this.nature = nature;
    }

    @Override
    public void setSneaking(boolean state) {
        this.meta.setSneaking(state);
        this.setMoveSpeed(MathHelper.calcMoveSpeed(state ? EnumMoveSpeed.SNEAKING : EnumMoveSpeed.WALKING, this.speedAmplifier));
        if(state)
            this.meta.setSprinting(false);
        updateMetadata();
    }

    @Override
    public void setSprinting(boolean state) {
        this.meta.setSprinting(state);
        this.setMoveSpeed(MathHelper.calcMoveSpeed(state ? EnumMoveSpeed.SPRINTING : EnumMoveSpeed.WALKING, this.speedAmplifier));
        if(state)
            this.meta.setSneaking(false);
        updateMetadata();
    }
}
