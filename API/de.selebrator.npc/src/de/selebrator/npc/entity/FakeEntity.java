package de.selebrator.npc.entity;

import de.selebrator.npc.*;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.fetcher.PacketFetcher;
import de.selebrator.npc.metadata.*;
import de.selebrator.reflection.*;
import org.bukkit.*;
import org.bukkit.entity.EntityType;

import java.util.*;

public class FakeEntity {
	private static final Class CLASS_Entity = Reflection.getMinecraftClass("Entity");
	private static final FieldAccessor<Integer> FIELD_Entity_entityCount = Reflection.getField(CLASS_Entity, int.class, "entityCount");
	private final int entityId;
	private final UUID uniqueId;
	private final EntityType type;
	Random random;
	private Location location;
	private FakeMetadata meta;
	private int noDamageTicks;
	private int fireTicks;
	private boolean invulnerable;
	private boolean frozen;

	public FakeEntity(EntityType type) {
		this.random = new Random();
		this.entityId = FakeEntity.getNextEID();
		this.uniqueId = FakeEntity.getNewUUID(this.random);
		this.setMeta(new FakeEntityMetadata());
		this.type = type;
	}

	private static int getNextEID() {
		int EID = FIELD_Entity_entityCount.get(null);
		FIELD_Entity_entityCount.set(null, EID + 1);
		return EID;
	}

	private static UUID getNewUUID(Random random) {
		long mostSigBits = random.nextLong() & 0xffffffffffff0fffL | 0x4000L;
		long leastSigBits = random.nextLong() & 0x3fffffffffffffffL | 0x8000000000000000L;
		return new UUID(mostSigBits, leastSigBits);
	}

	public void spawn(Location location) {
		this.setLocation(location);
		PacketFetcher.broadcastPackets(
				PacketFetcher.spawnEntityLiving(this.getEntityId(), this.getUniqueId(), this.getType(), this.getLocation(), this.getMeta())
		);
	}

	public void despawn() {
		this.setLocation(null);
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityDestroy(this.getEntityId())
		);
	}

	public void look(float yaw, float pitch) {
		this.getLocation().setYaw(yaw);
		this.getLocation().setPitch(pitch);
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityLook(this.getEntityId(), this.getLocation().getYaw(), this.getLocation().getPitch()), //body
				PacketFetcher.headRotation(this.getEntityId(), this.getLocation().getYaw()) //head
		);
	}

	public void move(double dx, double dy, double dz) {
		float yaw = MathHelper.calcYaw(dx, dz);
		float pitch = MathHelper.calcPitch(dx, dy, dz);

		if(Math.abs(dx) < 8 && Math.abs(dy) < 8 && Math.abs(dz) < 8) {
			this.getLocation().add(dx, dy, dz);
			this.getLocation().setYaw(yaw);
			this.getLocation().setPitch(pitch);
			PacketFetcher.broadcastPackets(
					PacketFetcher.relEntityMoveLook(this.getEntityId(), dx, dy, dz, this.getLocation().getYaw(), this.getLocation().getPitch())
			);
		} else
			System.err.println("[NPC] " + "Error in move input: difference cant be >= 8");
	}

	public void teleport(Location location) {
		this.setLocation(location);
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityTeleport(this.getEntityId(), this.getLocation()),
				PacketFetcher.headRotation(this.getEntityId(), this.getLocation().getYaw())
		);
	}

	public void playEntityStatus(EnumEntityStatus status) {
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityStatus(this.getEntityId(), status.getId())
		);
	}

	public void playAnimation(NPCAnimationEvent.Animation animation) {
		PacketFetcher.broadcastPackets(
				PacketFetcher.animation(this.getEntityId(), animation.getId())
		);
	}

	public void updateMetadata() {
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityMetadata(this.getEntityId(), this.getMeta())
		);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public UUID getUniqueId() {
		return this.uniqueId;
	}

	public EntityType getType() {
		return this.type;
	}

	public FakeEntityMetadata getMeta() {
		return (FakeEntityMetadata) this.meta;
	}

	public void setMeta(FakeMetadata meta) {
		this.meta = meta;
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
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityTeleport(this.entityId, this.location)
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
		if(!this.getMeta().isSilent())
			this.getLocation().getWorld().playSound(this.getLocation(), sound, 1, 1);
	}

	public void ignite(int fireTicks) {
		this.getMeta().setOnFire(true);
		if(this.getFireTicks() < fireTicks)
			this.setFireTicks(fireTicks);
		updateMetadata();
	}

	public void extinguish() {
		this.getMeta().setOnFire(false);
		this.setFireTicks(-20);
		updateMetadata();
	}

	public boolean isFrozen() {
		return this.frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
}
