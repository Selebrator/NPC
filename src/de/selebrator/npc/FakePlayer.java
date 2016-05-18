package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
import de.selebrator.fetcher.PacketFetcher;
import de.selebrator.npc.attribute.FakeAttributeInstance;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.event.NPCDamageEvent;
import de.selebrator.npc.event.NPCDespawnEvent;
import de.selebrator.npc.event.NPCEquipEvent;
import de.selebrator.npc.event.NPCMoveEvent;
import de.selebrator.npc.event.NPCSpawnEvent;
import de.selebrator.npc.event.NPCTeleportEvent;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.metadata.FakeMetadata;
import de.selebrator.reflection.Reflection;
import de.selebrator.reflection.ServerPackage;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePlayer implements NPC {

	private final int entityId;
	public GameProfile gameProfile;
	public FakeMetadata meta;
	public FakeEquipment equip;

	private Map<PotionEffectType, PotionEffect> effects = new HashMap<>();
	private Map<Attribute, FakeAttributeInstance> attributes = new HashMap<>();

	private boolean frozen;
	private boolean living;
	private Location location;
	private LivingEntity target;
	private EnumNature nature;

	private int fireTicks;
	private int noDamageTicks;
	private boolean invulnerable;
	private Location respawnLocation;

	private static final AttributeModifier MOVEMENT_SPEED_MODIFIER_SNEAKING = new AttributeModifier("Sneaking speed reduction", -0.7D, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	private static final AttributeModifier MOVEMENT_SPEED_MODIFIER_SPRINTING = new AttributeModifier("Sprinting speed boost", 0.30000001192092896D, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	private static final double EYE_HEIGHT_STANDING = 1.62D;
	private static final double EYE_HEIGHT_SNEAKING = 1.2D;


	public FakePlayer(GameProfile gameProfile) {
		Class<?> entityClass = Reflection.getClass(ServerPackage.NMS, "Entity");
		this.entityId = (int) Reflection.getField(entityClass, "entityCount").get(null);
		Reflection.getField(entityClass, "entityCount").set(null, this.entityId + 1);

		this.gameProfile = gameProfile;

		this.meta = new FakeMetadata();
		this.meta.setStatus(false, false, false, false, false, false);
		this.meta.setSkinFlags(true, true, true, true, true, true, true);
		this.meta.setHealth(20.0F);
		this.meta.setAir(300);
		this.meta.setName(this.getName());

		this.equip = new FakeEquipment(this);

		this.effects = new HashMap<>();

		this.attributes = new HashMap<>();
		this.attributes.put(Attribute.GENERIC_MAX_HEALTH, new FakeAttributeInstance(Attribute.GENERIC_MAX_HEALTH, 20));
		this.attributes.put(Attribute.GENERIC_FOLLOW_RANGE, new FakeAttributeInstance(Attribute.GENERIC_FOLLOW_RANGE, 32));
		this.attributes.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new FakeAttributeInstance(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0));
		this.attributes.put(Attribute.GENERIC_MOVEMENT_SPEED, new FakeAttributeInstance(Attribute.GENERIC_MOVEMENT_SPEED, 0.699999988079071D));
		this.attributes.put(Attribute.GENERIC_ATTACK_DAMAGE, new FakeAttributeInstance(Attribute.GENERIC_ATTACK_DAMAGE, 1));
		this.attributes.put(Attribute.GENERIC_ARMOR, new FakeAttributeInstance(Attribute.GENERIC_ARMOR, 0));
		this.attributes.put(Attribute.GENERIC_ATTACK_SPEED, new FakeAttributeInstance(Attribute.GENERIC_ATTACK_SPEED, 4));
		this.attributes.put(Attribute.GENERIC_LUCK, new FakeAttributeInstance(Attribute.GENERIC_LUCK, 0));

		this.nature = EnumNature.PASSIVE;
		this.fireTicks = -20;
	}

	// ### PACKET MANIPULATION ###

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

		for(EquipmentSlot slot : EquipmentSlot.values()) {
			this.equip(slot, this.equip.get(slot));
		}

		this.look(location.getYaw(), location.getPitch());
	}

	@Override
	public void respawn(Location location) {
		if(!this.isAlive()) {
			this.softReset();
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
		float yaw = MathHelper.calcYaw(x, z);
		float pitch = MathHelper.calcPitch(x, y, z);

		if(Math.abs(x) < 8 && Math.abs(y) < 8 && Math.abs(z) < 8) {
			PacketFetcher.broadcastPackets(
					PacketFetcher.relEntityMoveLook(this.entityId, x, y, z, yaw, pitch)
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
		Vector direction = MathHelper.calcDirectionVector(this.getMoveSpeed() / 20, yaw, pitch);

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

	public void equip(EquipmentSlot slot, ItemStack item) {
		NPCEquipEvent event = new NPCEquipEvent(this, slot, item);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) { return; }

		slot = event.getSlot();
		item = event.getItem();

		PacketFetcher.broadcastPackets(
				PacketFetcher.entityEquipment(this.entityId, CraftEquipmentSlot.getNMS(slot), CraftItemStack.asNMSCopy(item))
		);
	}

	@Override
	public void playAnimation(NPCAnimationEvent.Animation animation) {
		NPCAnimationEvent event = new NPCAnimationEvent(this, animation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) { return; }

		animation = event.getAnimation();

		PacketFetcher.broadcastPackets(
				PacketFetcher.animation(this.entityId, animation.getId())
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
	public FakeMetadata getMeta() {
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
	public boolean isFrozen() {
		return this.frozen;
	}

	@Override
	public float getHealth() {
		return this.isAlive() ? this.meta.getHealth() : 0;
	}

	@Override
	public int getNoDamageTicks() {
		return  this.noDamageTicks;
	}

	@Override
	public int getRemainingAir() {
		return this.meta.getAir();
	}

	@Override
	public int getFireTicks() {
		return this.fireTicks;
	}

	@Override
	public double getMoveSpeed() {
		return this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * 6.16714296217D;
	}

	@Override
	public double getEyeHeight() {
		return this.getEyeHeight(false);
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
		return this.hasLocation() ? this.location.clone().add(0, this.getEyeHeight(), 0) : null;

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
	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	@Override
	public EnumNature getNature() {
		return this.nature;
	}

	@Override
	public FakeEquipment getEquipment() {
		return this.equip;
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType type) {
		return this.effects.containsKey(type);
	}

	@Override
	public PotionEffect getPotionEffect(PotionEffectType type) {
		return this.effects.get(type);
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return this.effects.values();
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		PotionEffectType type = effect.getType();
		if(!type.isInstant()) {
			this.effects.put(type, effect);
			this.setParticles(this.getActivePotionEffects());
		}

		byte level = (byte) (effect.getAmplifier() + 1);

		if(type.equals(PotionEffectType.SPEED)) {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(
					new AttributeModifier("potion.moveSpeed" + level, 0.20000000298023224D * level, AttributeModifier.Operation.MULTIPLY_SCALAR_1)
			);
		} else if(type.equals(PotionEffectType.SLOW)) {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(
					new AttributeModifier("potion.moveSlowdown" + level, -0.15000000596046448D * level, AttributeModifier.Operation.MULTIPLY_SCALAR_1)
			);
		} else if(type.equals(PotionEffectType.INCREASE_DAMAGE)) {
			this.attributes.get(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(
					new AttributeModifier("potion.damageBoost" + level, 3.0D * level, AttributeModifier.Operation.ADD_NUMBER)
			);
		} else if(type.equals(PotionEffectType.WEAKNESS)) {
			this.attributes.get(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(
					new AttributeModifier("potion.weakness" + level, -4.0D * level, AttributeModifier.Operation.ADD_NUMBER)
			);
		} else if(type.equals(PotionEffectType.HEALTH_BOOST)) {
			this.attributes.get(Attribute.GENERIC_MAX_HEALTH).addModifier(
					new AttributeModifier("potion.healthBoost" + level, 4.0D * level, AttributeModifier.Operation.ADD_NUMBER)
			);
		} else if(type.equals(PotionEffectType.ABSORPTION)) {
			this.meta.setAbsorption(4 * level);
			updateMetadata();
		} else if(type.equals(PotionEffectType.HEAL)) {
			this.setHealth(this.getHealth() + (4.0F * (float) Math.pow(2.0D, level - 1.0D)));
			this.removePotionEffect(PotionEffectType.HEAL);
		} else if(type.equals(PotionEffectType.HARM)) {
			this.damage((6.0F * (float) Math.pow(2.0D, level - 1.0D)), EntityDamageEvent.DamageCause.MAGIC);
			this.removePotionEffect(PotionEffectType.HARM);
		} else if(type.equals(PotionEffectType.INVISIBILITY)) {
			this.meta.setInvisibleTemp(true);
			updateMetadata();
		} else if(type.equals(PotionEffectType.GLOWING)) {
			this.meta.setGlowingTemp(true);
			updateMetadata();
		} else if(type.equals(PotionEffectType.LUCK)) {
			this.attributes.get(Attribute.GENERIC_LUCK).addModifier(
					new AttributeModifier("potion.luck" + level, 1.0D * level, AttributeModifier.Operation.ADD_NUMBER)
			);
		} else if(type.equals(PotionEffectType.UNLUCK)) {
			this.attributes.get(Attribute.GENERIC_LUCK).addModifier(
					new AttributeModifier("potion.unluck" + level, -1.0D * level, AttributeModifier.Operation.ADD_NUMBER)
			);
		}
	}

	@Override
	public void addPotionEffects(Collection<PotionEffect> effects) {
		effects.forEach(this::addPotionEffect);
	}

	@Override
	public void removePotionEffect(PotionEffectType type) {
		if(this.hasPotionEffect(type)) {
			PotionEffect effect = this.getPotionEffect(type);
			byte level = (byte) (effect.getAmplifier() + 1);

			this.effects.remove(type);
			this.setParticles(this.getActivePotionEffects());

			if(type.equals(PotionEffectType.SPEED)) {
				this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier("potion.moveSpeed" + level);
			} else if(type.equals(PotionEffectType.SLOW)) {
				this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier("potion.moveSlowdown" + level);
			} else if(type.equals(PotionEffectType.INCREASE_DAMAGE)) {
				this.attributes.get(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier("potion.damageBoost" + level);
			} else if(type.equals(PotionEffectType.WEAKNESS)) {
				this.attributes.get(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier("potion.weakness" + level);
			} else if(type.equals(PotionEffectType.HEALTH_BOOST)) {
				this.attributes.get(Attribute.GENERIC_MAX_HEALTH).removeModifier("potion.healthBoost" + level);
			} else if(type.equals(PotionEffectType.ABSORPTION)) {
				this.meta.setAbsorption(0);
				updateMetadata();
			} else if(type.equals(PotionEffectType.INVISIBILITY)) {
				this.meta.setInvisibleTemp(false);
				updateMetadata();
			} else if(type.equals(PotionEffectType.GLOWING)) {
				this.meta.setGlowingTemp(false);
				updateMetadata();
			} else if(type.equals(PotionEffectType.LUCK)) {
				this.attributes.get(Attribute.GENERIC_LUCK).removeModifier("potion.luck" + level);
			} else if(type.equals(PotionEffectType.UNLUCK)) {
				this.attributes.get(Attribute.GENERIC_LUCK).removeModifier("potion.unluck" + level);
			}
		}
	}

	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		return this.attributes.get(attribute);
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
	public void setMeta(FakeMetadata meta) {
		this.meta = meta;
		this.updateMetadata();
	}

	@Override
	public void freeze(boolean frozen) {
		this.frozen = frozen;
	}

	@Override
	public void setHealth(float health) {
		if(health > this.attributes.get(Attribute.GENERIC_MAX_HEALTH).getValue()) {
			health = (float) this.attributes.get(Attribute.GENERIC_MAX_HEALTH).getValue();
		}
		if(health == 0) {
			this.setEntityStatus(EnumEntityStatus.DEAD);
			this.fireTicks = -20;
			playSound(Sound.ENTITY_GENERIC_DEATH);
			this.living = false;
		} else if(this.getHealth() > health) {
			this.setEntityStatus(EnumEntityStatus.HURT);
			playSound(Sound.ENTITY_GENERIC_HURT);
		} else if(this.getHealth() == 0 && health > 0) {
			if(this.hasLocation()) {
				this.meta.setHealth(health);
				this.spawn(this.location);
				return;
			}
		}
		this.meta.setHealth(health);
	}

	@Override
	public  void damage(float amount) {
		damage(amount, EntityDamageEvent.DamageCause.CUSTOM);
	}

	@Override
	public void damage(float amount, EntityDamageEvent.DamageCause cause) {
		boolean immune = (this.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && (cause == EntityDamageEvent.DamageCause.LAVA
				||  cause == EntityDamageEvent.DamageCause.FIRE
				||  cause == EntityDamageEvent.DamageCause.FIRE_TICK))
				|| this.hasPotionEffect(PotionEffectType.WATER_BREATHING) && (cause == EntityDamageEvent.DamageCause.DROWNING);
		if(this.noDamageTicks == 0 && !immune && !this.invulnerable) {
			NPCDamageEvent event = new NPCDamageEvent(this, amount, cause);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()) { return; }

			float current = this.getHealth();
			float absorption = this.meta.getAbsorption();
			int resistance = this.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) ? this.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1 : 0;
			if(resistance > 5) { resistance = 5; }
			float damage = event.getAmount();
			damage = damage - damage * 0.2F * resistance;
			absorption = absorption - damage > 0 ? absorption - damage : 0;
			damage = damage - (absorption + this.meta.getAbsorption());

			this.setHealth(current - damage);

			this.noDamageTicks = 10;
		}
	}

	@Override
	public void setNoDamageTicks(int noDamageTicks) {
		this.noDamageTicks = noDamageTicks;
	}

	@Override
	public void setRemainingAir(int air) {
		this.meta.setAir(air);
	}

	@Override
	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	@Override
	public void setRespawnLocation(Location location) {
		this.respawnLocation = location;
	}

	@Override
	public void setTarget(LivingEntity target) {
		this.target = target;
	}

	@Override
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	@Override
	public void setNature(EnumNature nature) {
		this.nature = nature;
	}

	@Override
	public void setSneaking(boolean state) {
		this.meta.setSneaking(state);
		if(state) {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(MOVEMENT_SPEED_MODIFIER_SNEAKING);
			this.meta.setSprinting(false);
		}
		else
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(MOVEMENT_SPEED_MODIFIER_SNEAKING);
		updateMetadata();
	}

	@Override
	public void setSprinting(boolean state) {
		this.meta.setSprinting(state);
		if(state) {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(MOVEMENT_SPEED_MODIFIER_SPRINTING);
			this.meta.setSneaking(false);
		} else {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(MOVEMENT_SPEED_MODIFIER_SPRINTING);
		}
		updateMetadata();
	}

	public void ignite(int fireTicks) {
		this.meta.setOnFire(true);
		if(this.fireTicks < fireTicks) {
			this.fireTicks = fireTicks;
		}
		updateMetadata();
	}

	public void extinguish() {
		this.meta.setOnFire(false);
		this.fireTicks = -20;
		updateMetadata();
	}

	// ### LIVE ###

	private void playSound(Sound sound) {
		if(!this.meta.isSilent()) {
			this.location.getWorld().playSound(this.location, sound, 1, 1);
		}
	}

	public static int calcPotionColor(Collection<PotionEffect> effects) {
		if(effects.isEmpty()) {
			return 0;
		} else {
			float red = 0.0F;
			float green = 0.0F;
			float blue = 0.0F;
			int totalAmplifier = 0;

			for(PotionEffect effect : effects) {
				if(effect.hasParticles()) {
					PotionEffectType type = effect.getType();
					System.out.println(type.getName());
					int color = 0xffffff;
					if(effect.getColor() != null) { color = effect.getColor().asRGB(); }
					else if(type.equals(PotionEffectType.SPEED)) { color = 8171462; }
					else if(type.equals(PotionEffectType.SLOW)) { color = 5926017; }
					else if(type.equals(PotionEffectType.FAST_DIGGING)) { color = 14270531; }
					else if(type.equals(PotionEffectType.SLOW_DIGGING)) { color = 4866583; }
					else if(type.equals(PotionEffectType.INCREASE_DAMAGE)) { color = 9643043; }
					else if(type.equals(PotionEffectType.HEAL)) { color = 16262179; }
					else if(type.equals(PotionEffectType.HARM)) { color = 4393481; }
					else if(type.equals(PotionEffectType.JUMP)) { color = 2293580; }
					else if(type.equals(PotionEffectType.CONFUSION)) { color = 5578058; }
					else if(type.equals(PotionEffectType.REGENERATION)) { color = 13458603; }
					else if(type.equals(PotionEffectType.DAMAGE_RESISTANCE)) { color = 10044730; }
					else if(type.equals(PotionEffectType.FIRE_RESISTANCE)) { color = 14981690; }
					else if(type.equals(PotionEffectType.WATER_BREATHING)) { color = 3035801; }
					else if(type.equals(PotionEffectType.INVISIBILITY)) { color = 8356754; }
					else if(type.equals(PotionEffectType.BLINDNESS)) { color = 2039587; }
					else if(type.equals(PotionEffectType.NIGHT_VISION)) { color = 2039713; }
					else if(type.equals(PotionEffectType.HUNGER)) { color = 5797459; }
					else if(type.equals(PotionEffectType.WEAKNESS)) { color = 4738376; }
					else if(type.equals(PotionEffectType.POISON)) { color = 5149489; }
					else if(type.equals(PotionEffectType.WITHER)) { color = 3484199; }
					else if(type.equals(PotionEffectType.HEALTH_BOOST)) { color = 16284963; }
					else if(type.equals(PotionEffectType.ABSORPTION)) { color = 2445989; }
					else if(type.equals(PotionEffectType.SATURATION)) { color = 16262179; }
					else if(type.equals(PotionEffectType.GLOWING)) { color = 9740385; }
					else if(type.equals(PotionEffectType.LEVITATION)) { color = 13565951; }
					else if(type.equals(PotionEffectType.LUCK)) { color = 3381504; }
					else if(type.equals(PotionEffectType.UNLUCK)) { color = 12624973; }
					int amplifier = effect.getAmplifier() + 1;
					red   += (float) (amplifier * (color >> 16 & 255)) / 255.0F;
					green += (float) (amplifier * (color >>  8 & 255)) / 255.0F;
					blue  += (float) (amplifier * (color >>  0 & 255)) / 255.0F;
					totalAmplifier += amplifier;
				}
			}

			if(totalAmplifier == 0) {
				return 0;
			} else {
				red   = red   / (float)totalAmplifier * 255.0F;
				green = green / (float)totalAmplifier * 255.0F;
				blue  = blue  / (float)totalAmplifier * 255.0F;
				return (int)red << 16 | (int)green << 8 | (int)blue;
			}
		}
	}

	public void setParticles(Collection<PotionEffect> effects) {
		boolean ambient = true;
		for(PotionEffect effect : effects) {
			if(!effect.isAmbient()) {
				ambient = false;
			}
		}
		this.meta.setPotionAmbient(ambient);
		this.meta.setPotionColor(calcPotionColor(effects));
		updateMetadata();
	}

	@Override
	public boolean touches(Block block) {
		double x = (block.getX() + 0.5) - this.location.getX();
		double y = block.getY() - this.location.getY();
		double z = (block.getZ() + 0.5) - this.location.getZ();

		return ((-0.8D < x && x < -0.2D) || (-0.2D < x && x < 0.2D) || (0.2 < x && x < 0.8D))
			&& (-1 < y && y < (0.2D + this.getEyeHeight()))
			&& ((-0.8D < z && z < -0.2D) || (-0.2D < z && z < 0.2D) || (0.2 < z && z < 0.8D));
	}

	@Override
	public List<Block> getTouchedBlocks() {
		List<Block> surrounding = getSurroundingBlocks();
		List<Block> touched = new ArrayList<>();
		surrounding.forEach( (block) -> {
			if(this.touches(block)) {
				touched.add(block);
			}
		});
		return touched;
	}

	@Override
	public List<Block> getSurroundingBlocks() {
		List<Block> blocks = new ArrayList<>();
		Block block = this.location.getBlock();
		blocks.add(block.getRelative(-1, -1, -1));
		blocks.add(block.getRelative(-1, -1, 0));
		blocks.add(block.getRelative(-1, -1, 1));
		blocks.add(block.getRelative(0, -1, -1));
		blocks.add(block.getRelative(0, -1, 0));
		blocks.add(block.getRelative(0, -1, 1));
		blocks.add(block.getRelative(1, -1, -1));
		blocks.add(block.getRelative(1, -1, 0));
		blocks.add(block.getRelative(1, -1, 1));

		blocks.add(block.getRelative(-1, 0, -1));
		blocks.add(block.getRelative(-1, 0, 0));
		blocks.add(block.getRelative(-1, 0, 1));
		blocks.add(block.getRelative(0, 0, -1));
		blocks.add(block.getRelative(0, 0, 0));
		blocks.add(block.getRelative(0, 0, 1));
		blocks.add(block.getRelative(1, 0, -1));
		blocks.add(block.getRelative(1, 0, 0));
		blocks.add(block.getRelative(1, 0, 1));

		blocks.add(block.getRelative(-1, 1, -1));
		blocks.add(block.getRelative(-1, 1, 0));
		blocks.add(block.getRelative(-1, 1, 1));
		blocks.add(block.getRelative(0, 1, -1));
		blocks.add(block.getRelative(0, 1, 0));
		blocks.add(block.getRelative(0, 1, 1));
		blocks.add(block.getRelative(1, 1, -1));
		blocks.add(block.getRelative(1, 1, 0));
		blocks.add(block.getRelative(1, 1, 1));

		blocks.add(block.getRelative(-1, 2, -1));
		blocks.add(block.getRelative(-1, 2, 0));
		blocks.add(block.getRelative(-1, 2, 1));
		blocks.add(block.getRelative(0, 2, -1));
		blocks.add(block.getRelative(0, 2, 0));
		blocks.add(block.getRelative(0, 2, 1));
		blocks.add(block.getRelative(1, 2, -1));
		blocks.add(block.getRelative(1, 2, 0));
		blocks.add(block.getRelative(1, 2, 1));

		return blocks;
	}

	public void applyAmbientDamage() {
		this.getTouchedBlocks().forEach( (block) -> {
			//lava
			if(block.getType() == Material.STATIONARY_LAVA) {
				this.ignite(160);
				this.damage(4.0F, EntityDamageEvent.DamageCause.LAVA);
			}
			//fire(contact)
			if(block.getType() == Material.FIRE) {
				this.ignite(160);
				this.damage(1.0F, EntityDamageEvent.DamageCause.FIRE);
			}

			//fire(tick)
			if(block.getType() == Material.STATIONARY_WATER
					|| this.location.getWorld().hasStorm()
					|| this.location.getWorld().isThundering()) {
				this.extinguish();
			}

			if(this.fireTicks > 0) {
				if(this.fireTicks % 20 == 0) {
					this.damage(1.0F, EntityDamageEvent.DamageCause.FIRE_TICK);
				}
			} else if(this.fireTicks == 0) {
				this.extinguish();
			}

			//cactus
			if(block.getType() == Material.CACTUS) {
				this.damage(1.0F, EntityDamageEvent.DamageCause.CONTACT);
			}
		});

		//drown
		if(this.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER) {
			this.meta.setAir(this.meta.getAir() - 1);
		} else if(!this.getEyeLocation().getBlock().getType().isSolid() && !this.getEyeLocation().getBlock().isLiquid()) {
			this.meta.setAir(300);
		}
		if(this.meta.getAir() <= -20) {
			this.damage(2.0F, EntityDamageEvent.DamageCause.DROWNING);
			this.meta.setAir(0);
		}
	}

	public void tickPotions() {
		Collection<PotionEffect> effectsToProcess = this.effects.values();


		//--duration
		for(PotionEffect effect : effectsToProcess) {
			int duration = effect.getDuration();
			if(duration <= 0) {
				this.removePotionEffect(effect.getType());
			} else {
				Reflection.getField(effect.getClass(), "duration").set(effect, duration - 1);
			}
		}
	}

	@Override
	public void attack(LivingEntity target) {
		if(this.isAlive() && !target.isDead()) {
			this.playAnimation(NPCAnimationEvent.Animation.SWING_ARM);
			target.damage(this.attributes.get(Attribute.GENERIC_ATTACK_DAMAGE).getValue());
			Vector distance = MathHelper.calcDistanceVector(this.getLocation(), target.getLocation());

			float yaw = MathHelper.calcYaw(distance.getX(), distance.getZ());
			float pitch = MathHelper.calcPitch(distance.getX(), distance.getY(), distance.getZ());

			Vector direction = MathHelper.calcDirectionVector(0.4, yaw, pitch);

			target.setVelocity(direction);
		}
	}

	@Override
	public void tick() {
		if(this.isAlive() && !this.frozen) {
			if(this.fireTicks > 0) {
				--this.fireTicks;
			}

			tickPotions();
			applyAmbientDamage();

			if(this.getNoDamageTicks() > 0) {
				--this.noDamageTicks;
			}
		}
	}

	public void softReset() {
		this.meta.setAir(300);
		this.meta.setSilent(false);
		this.meta.setHealth(20.0F);

		this.equip = new FakeEquipment(this);

		this.effects = new HashMap<>();

		this.attributes.forEach((attribute, fakeAttributeInstance) -> fakeAttributeInstance.removeAllModifiers());

		this.frozen = false;
		this.living = false;
		this.location = null;
		this.target = null;
		this.nature = EnumNature.PASSIVE;

		this.extinguish();
		this.noDamageTicks = 0;
		this.invulnerable = false;
	}
}
