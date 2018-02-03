package de.selebrator.npc.fake.entity;

import de.selebrator.npc.*;
import de.selebrator.npc.entity.EntityNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import de.selebrator.npc.fake.packet.FakePacket;
import org.bukkit.*;

import java.util.*;

import static de.selebrator.npc.fake.Imports.*;

public abstract class FakeEntity implements EntityNPC {
	private final int entityId;
	private final UUID uniqueId;
	Random random;
	MetadataObject<Byte> status;
	MetadataObject<Integer> air;
	MetadataObject<String> name;
	MetadataObject<Boolean> nameVisible;
	MetadataObject<Boolean> silent;
	MetadataObject<Boolean> noGravity;
	private Object dataWatcher;
	private Location location;
	private int noDamageTicks;
	private int fireTicks;
	private boolean invulnerable;
	private boolean frozen;

	public FakeEntity() {
		this.random = new Random();
		this.entityId = FakeEntity.getNextEID();
		this.uniqueId = FakeEntity.getNewUUID(this.random);
		initMetadata();
	}

	private static int getNextEID() {
		int EID = FIELD_Entity_entityCount.get(null);
		FIELD_Entity_entityCount.set(null, EID + 1);
		return EID;
	}

	private static UUID getNewUUID(Random random) {
		long mostSigBits = random.nextLong() & 0xffffffffffff0fffL | 0x2000L;
		long leastSigBits = random.nextLong() & 0x3fffffffffffffffL | 0x8000000000000000L;
		return new UUID(mostSigBits, leastSigBits);
	}

	void initMetadata() {
		this.dataWatcher = CONSTRUCTOR_DataWatcher.newInstance(new Object[] { null });
		this.status = new MetadataObject<>(this.getDataWatcher(), FIELD_Entity_Z, (byte) 0); //0
		this.air = new MetadataObject<>(this.getDataWatcher(), FIELD_Entity_aA, 300); //1
		this.name = new MetadataObject<>(this.getDataWatcher(), FIELD_Entity_aB, ""); //2
		this.nameVisible = new MetadataObject<>(this.getDataWatcher(), FIELD_Entity_aC, false); //3
		this.silent = new MetadataObject<>(this.getDataWatcher(), FIELD_Entity_aD, false); //4
		this.noGravity = new MetadataObject<>(this.getDataWatcher(), FIELD_Entity_aE, false); //5
	}

	public Object getDataWatcher() {
		return this.dataWatcher;
	}

	public void spawn(Location location) {
		this.setLocation(location);
		FakePacket.broadcastPackets(
				FakePacket.spawnEntityLiving(this.getEntityId(), this.getUniqueId(), this.getType(), this.getLocation(), this.getDataWatcher())
		);
	}

	public void despawn() {
		this.setLocation(null);
		FakePacket.broadcastPackets(
				FakePacket.entityDestroy(this.getEntityId())
		);
	}

	public void look(float yaw, float pitch) {
		this.getLocation().setYaw(yaw);
		this.getLocation().setPitch(pitch);
		FakePacket.broadcastPackets(
				FakePacket.entityLook(this.getEntityId(), this.getLocation().getYaw(), this.getLocation().getPitch()), //body
				FakePacket.headRotation(this.getEntityId(), this.getLocation().getYaw()) //head
		);
	}

	public void move(double dx, double dy, double dz) {
		float yaw = MathHelper.calcYaw(dx, dz);
		float pitch = MathHelper.calcPitch(dx, dy, dz);

		if(Math.abs(dx) < 8 && Math.abs(dy) < 8 && Math.abs(dz) < 8) {
			this.getLocation().add(dx, dy, dz);
			this.getLocation().setYaw(yaw);
			this.getLocation().setPitch(pitch);
			FakePacket.broadcastPackets(
					FakePacket.relEntityMoveLook(this.getEntityId(), dx, dy, dz, this.getLocation().getYaw(), this.getLocation().getPitch())
			);
		} else
			System.err.println("[NPC] " + "Error in move input: difference cant be >= 8");
	}

	public void teleport(Location location) {
		this.setLocation(location);
		FakePacket.broadcastPackets(
				FakePacket.entityTeleport(this.getEntityId(), this.getLocation()),
				FakePacket.headRotation(this.getEntityId(), this.getLocation().getYaw())
		);
	}

	public void playEffect(EntityEffect effect) {
		FakePacket.broadcastPackets(
				FakePacket.entityStatus(this.getEntityId(), effect.getData())
		);
	}

	public void playAnimation(Animation animation) {
		FakePacket.broadcastPackets(
				FakePacket.animation(this.getEntityId(), animation.getId())
		);
	}

	public void updateMetadata() {
		FakePacket.broadcastPackets(
				FakePacket.entityMetadata(this.getEntityId(), this.getDataWatcher())
		);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public UUID getUniqueId() {
		return this.uniqueId;
	}

	public boolean hasLocation() {
		return this.getLocation() != null;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void updateLocation() {
		FakePacket.broadcastPackets(
				FakePacket.entityTeleport(this.entityId, this.location)
		);
	}

	public boolean isAlive() {
		return false;
	}

	public int getNoDamageTicks() {
		return this.noDamageTicks;
	}

	public void setNoDamageTicks(int noDamageTicks) {
		this.noDamageTicks = noDamageTicks;
	}

	public int getFireTicks() {
		return this.fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public void playSound(Sound sound) {
		if(!this.isSilent())
			this.getLocation().getWorld().playSound(this.getLocation(), sound, 1, 1);
	}

	public void ignite(int fireTicks) {
		this.setBurning(true);
		if(this.getFireTicks() < fireTicks)
			this.setFireTicks(fireTicks);
		updateMetadata();
	}

	public void extinguish() {
		this.setBurning(false);
		this.setFireTicks(-20);
		updateMetadata();
	}

	public boolean isFrozen() {
		return this.frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	@Override
	public void tick() {
		//TODO
	}

	public byte getStatus() {
		return this.status.get();
	}

	@Override
	public void setStatus(byte value) {
		this.status.set(value);
	}

	public int getRemainingAir() {
		return this.air.get();
	}

	public void setRemainingAir(int air) {
		this.air.set(air);
	}

	public String getCustomName() {
		return this.name.get();
	}

	public void setCustomName(String name) {
		this.name.set(name);
	}

	public boolean isCustomNameVisible() {
		return this.nameVisible.get();
	}

	public void setCustomNameVisible(boolean nameVisible) {
		this.nameVisible.set(nameVisible);
	}

	public boolean isSilent() {
		return this.silent.get();
	}

	public void setSilent(boolean silent) {
		this.silent.set(silent);
	}

	public boolean hasGravity() {
		return !this.noGravity.get();
	}

	public void setGravity(boolean gravity) {
		this.noGravity.set(!gravity);
	}
}
