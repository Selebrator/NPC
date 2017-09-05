package de.selebrator.npc.entity;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.EnumEntityStatus;
import de.selebrator.npc.EnumNature;
import de.selebrator.npc.MathHelper;
import de.selebrator.npc.NPC;
import de.selebrator.npc.attribute.FakeAttributeInstance;
import de.selebrator.npc.event.*;
import de.selebrator.npc.fetcher.PacketFetcher;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.metadata.FakeMetadata;
import de.selebrator.reflection.Reflection;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class FakePlayer implements NPC {

	public static final Map<PotionEffectType, Integer> potionColors = Map.ofEntries(
			Map.entry(PotionEffectType.SPEED, 0x7cafc6),
			Map.entry(PotionEffectType.SLOW, 0x5a6c81),
			Map.entry(PotionEffectType.FAST_DIGGING, 0xd9c043),
			Map.entry(PotionEffectType.SLOW_DIGGING, 0x4a4217),
			Map.entry(PotionEffectType.INCREASE_DAMAGE, 0x932423),
			Map.entry(PotionEffectType.HEAL, 0xf82423),
			Map.entry(PotionEffectType.HARM, 0x430a09),
			Map.entry(PotionEffectType.JUMP, 0x22ff4c),
			Map.entry(PotionEffectType.CONFUSION, 0x551d4a),
			Map.entry(PotionEffectType.REGENERATION, 0xcd5cab),
			Map.entry(PotionEffectType.DAMAGE_RESISTANCE, 0x99453a),
			Map.entry(PotionEffectType.FIRE_RESISTANCE, 0xe49a3a),
			Map.entry(PotionEffectType.WATER_BREATHING, 0x2e5299),
			Map.entry(PotionEffectType.INVISIBILITY, 0x7f8392),
			Map.entry(PotionEffectType.BLINDNESS, 0x1f1f23),
			Map.entry(PotionEffectType.NIGHT_VISION, 0x1f1fa1),
			Map.entry(PotionEffectType.HUNGER, 0x587653),
			Map.entry(PotionEffectType.WEAKNESS, 0x484d48),
			Map.entry(PotionEffectType.POISON, 0x4e9331),
			Map.entry(PotionEffectType.WITHER, 0x352a27),
			Map.entry(PotionEffectType.HEALTH_BOOST, 0xf87d23),
			Map.entry(PotionEffectType.ABSORPTION, 0x2552a5),
			Map.entry(PotionEffectType.SATURATION, 0xf82423),
			Map.entry(PotionEffectType.GLOWING, 0x94a061),
			Map.entry(PotionEffectType.LEVITATION, 0xceffff),
			Map.entry(PotionEffectType.LUCK, 0x339900),
			Map.entry(PotionEffectType.UNLUCK, 0xc0a44d)
	);
	private static final double EYE_HEIGHT_STANDING = 1.62D;
	private static final double EYE_HEIGHT_SNEAKING = 1.2D;

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


	public FakePlayer(GameProfile gameProfile) {
		Class<?> entityClass = Reflection.getMinecraftClass("Entity");
		this.entityId = (int) Reflection.getField(entityClass, "entityCount").get(null);
		Reflection.getField(entityClass, int.class, "entityCount").set(null, this.entityId + 1);

		this.gameProfile = gameProfile;

		this.meta = new FakeMetadata();
		this.meta.setStatus(false, false, false, false, false, false);
		this.meta.setSkinFlags(true, true, true, true, true, true, true);
		this.meta.setGravity(false);
		this.meta.setHealth(20.0F);
		this.meta.setAir(300);
		this.meta.setName(this.getName());

		this.equip = new FakeEquipment(this);

		this.effects = new HashMap<>();

		this.attributes = new HashMap<>();
		this.attributes.put(Attribute.GENERIC_MAX_HEALTH, new FakeAttributeInstance(Attribute.GENERIC_MAX_HEALTH));
		this.attributes.put(Attribute.GENERIC_FOLLOW_RANGE, new FakeAttributeInstance(Attribute.GENERIC_FOLLOW_RANGE));
		this.attributes.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new FakeAttributeInstance(Attribute.GENERIC_KNOCKBACK_RESISTANCE));
		this.attributes.put(Attribute.GENERIC_MOVEMENT_SPEED, new FakeAttributeInstance(Attribute.GENERIC_MOVEMENT_SPEED));
		this.attributes.put(Attribute.GENERIC_FLYING_SPEED, new FakeAttributeInstance(Attribute.GENERIC_FLYING_SPEED));
		this.attributes.put(Attribute.GENERIC_ATTACK_DAMAGE, new FakeAttributeInstance(Attribute.GENERIC_ATTACK_DAMAGE));
		this.attributes.put(Attribute.GENERIC_ATTACK_SPEED, new FakeAttributeInstance(Attribute.GENERIC_ATTACK_SPEED));
		this.attributes.put(Attribute.GENERIC_ARMOR, new FakeAttributeInstance(Attribute.GENERIC_ARMOR));
		this.attributes.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new FakeAttributeInstance(Attribute.GENERIC_ARMOR_TOUGHNESS));
		this.attributes.put(Attribute.GENERIC_LUCK, new FakeAttributeInstance(Attribute.GENERIC_LUCK));

		this.nature = EnumNature.PASSIVE;
		this.fireTicks = -20;
	}

	public static int calcPotionColor(Collection<PotionEffect> effects) {
		if(effects.isEmpty())
			return 0;

		float red = 0.0F;
		float green = 0.0F;
		float blue = 0.0F;
		int totalAmplifier = 0;

		for(PotionEffect effect : effects) {
			if(effect.hasParticles()) {
				int color = effect.getColor() != null ? effect.getColor().asRGB() : potionColors.get(effect.getType());
				int amplifier = effect.getAmplifier() + 1;
				red += (float) (amplifier * (color >> 16 & 255)) / 255.0F;
				green += (float) (amplifier * (color >> 8 & 255)) / 255.0F;
				blue += (float) (amplifier * (color & 255)) / 255.0F;
				totalAmplifier += amplifier;
			}
		}

		if(totalAmplifier == 0)
			return 0;
		else {
			red = red / (float) totalAmplifier * 255.0F;
			green = green / (float) totalAmplifier * 255.0F;
			blue = blue / (float) totalAmplifier * 255.0F;
			return (int) red << 16 | (int) green << 8 | (int) blue;
		}
	}

	// ### PACKET MANIPULATION ###

	@Override
	public void spawn(Location location) {
		NPCSpawnEvent event = new NPCSpawnEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;

		PacketFetcher.broadcastPackets(
				PacketFetcher.playerInfo(this.gameProfile, "ADD_PLAYER"),
				PacketFetcher.namedEntitySpawn(this.entityId, this.gameProfile, location, this.meta.getDataWatcher())
		);

		this.location = location;
		if(this.respawnLocation == null)
			this.respawnLocation = location;
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
		if(event.isCancelled()) return;

		PacketFetcher.broadcastPackets(
				PacketFetcher.playerInfo(this.gameProfile, "REMOVE_PLAYER"),
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
	public void move(double dx, double dy, double dz) {
		NPCMoveEvent event = new NPCMoveEvent(this, this.location.clone().add(dx, dy, dz));
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;

		Vector distance = MathHelper.calcDistanceVector(this.location, event.getDestination());
		double x = distance.getX();
		double y = distance.getY();
		double z = distance.getZ();
		float yaw = MathHelper.calcYaw(x, z);
		float pitch = MathHelper.calcPitch(x, y, z);

		if(Math.abs(x) < 8 && Math.abs(y) < 8 && Math.abs(z) < 8) {
			PacketFetcher.broadcastPackets(
					PacketFetcher.relEntityMoveLook(this.entityId, x, y, z, yaw, pitch)
			);

			this.location.add(x, y, z);
		} else
			System.err.println("[NPC] " + "Error in move input: difference cant be > 8");
	}

	@Override
	public void teleport(Location location) {
		NPCTeleportEvent event = new NPCTeleportEvent(this, location);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;

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
		if(event.isCancelled()) return;

		slot = event.getSlot();
		item = event.getItem();

		PacketFetcher.broadcastPackets(
				PacketFetcher.entityEquipment(this.entityId, slot, item)
		);
	}

	@Override
	public void playAnimation(NPCAnimationEvent.Animation animation) {
		NPCAnimationEvent event = new NPCAnimationEvent(this, animation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;

		animation = event.getAnimation();

		PacketFetcher.broadcastPackets(
				PacketFetcher.animation(this.entityId, animation.getId())
		);
	}

	@Override
	public void playEntityStatus(EnumEntityStatus status) {
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityStatus(this.entityId, status.getId())
		);
	}

	@Override
	public void updateMetadata() {
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityMetadata(this.entityId, this.meta.getDataWatcher())
		);
	}

	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public FakeMetadata getMeta() {
		return this.meta;
	}

	@Override
	public void setMeta(FakeMetadata meta) {
		this.meta = meta;
		this.updateMetadata();
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
	public void setHealth(float health) {
		if(health > this.attributes.get(Attribute.GENERIC_MAX_HEALTH).getValue())
			health = (float) this.attributes.get(Attribute.GENERIC_MAX_HEALTH).getValue();
		if(health == 0)
			this.die();
		else if(this.getHealth() > health) {
			this.playEntityStatus(EnumEntityStatus.HURT);
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
	public int getNoDamageTicks() {
		return this.noDamageTicks;
	}

	@Override
	public void setNoDamageTicks(int noDamageTicks) {
		this.noDamageTicks = noDamageTicks;
	}

	@Override
	public int getRemainingAir() {
		return this.meta.getAir();
	}

	@Override
	public void setRemainingAir(int air) {
		this.meta.setAir(air);
	}

	@Override
	public int getFireTicks() {
		return this.fireTicks;
	}

	@Override
	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	@Override
	public double getMoveSpeed() {
		return this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * 6.16714296217D;
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
	public void setRespawnLocation(Location location) {
		this.respawnLocation = location;
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
	public void setTarget(LivingEntity target) {
		this.target = target;
	}

	@Override
	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	@Override
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	// ### SETTER ###

	@Override
	public EnumNature getNature() {
		return this.nature;
	}

	@Override
	public void setNature(EnumNature nature) {
		this.nature = nature;
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

		if(type.equals(PotionEffectType.SPEED))
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.EFFECT_SPEED.apply(level));
		else if(type.equals(PotionEffectType.SLOW))
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.EFFECT_SLOWNESS.apply(level));
		else if(type.equals(PotionEffectType.FAST_DIGGING))
			this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(FakeAttributeInstance.EFFECT_HASTE.apply(level)); //TODO Haste: he cant mine yet :(
		else if(type.equals(PotionEffectType.SLOW_DIGGING))
			this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(FakeAttributeInstance.EFFECT_MINING_FATIGUE.apply(level)); //TODO Mining Fatigue
		else if(type.equals(PotionEffectType.INCREASE_DAMAGE))
			this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(FakeAttributeInstance.EFFECT_STRENGTH.apply(level));
		else if(type.equals(PotionEffectType.HEAL)) {
			this.setHealth(this.getHealth() + (4.0F * (float) Math.pow(2.0D, level - 1.0D)));
			this.removePotionEffect(PotionEffectType.HEAL);
		} else if(type.equals(PotionEffectType.HARM)) {
			this.damage((6.0F * (float) Math.pow(2.0D, level - 1.0D)), EntityDamageEvent.DamageCause.MAGIC);
			this.removePotionEffect(PotionEffectType.HARM);
		} else if(type.equals(PotionEffectType.INVISIBILITY)) {
			this.meta.setInvisibleTemp(true);
			updateMetadata();
		} else if(type.equals(PotionEffectType.WEAKNESS))
			this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(FakeAttributeInstance.EFFECT_WEAKNESS.apply(level));
		else if(type.equals(PotionEffectType.HEALTH_BOOST))
			this.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(FakeAttributeInstance.EFFECT_HEALTH_BOOST.apply(level));
		else if(type.equals(PotionEffectType.ABSORPTION)) {
			this.meta.setAbsorption(4 * level);
			updateMetadata();
		} else if(type.equals(PotionEffectType.GLOWING)) {
			this.meta.setGlowingTemp(true);
			updateMetadata();
		} else if(type.equals(PotionEffectType.LUCK))
			this.getAttribute(Attribute.GENERIC_LUCK).addModifier(FakeAttributeInstance.EFFECT_LUCK.apply(level));
		else if(type.equals(PotionEffectType.UNLUCK))
			this.getAttribute(Attribute.GENERIC_LUCK).addModifier(FakeAttributeInstance.EFFECT_UNLUCK.apply(level));
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

			if(type.equals(PotionEffectType.SPEED))
				this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.EFFECT_SPEED.apply(level));
			else if(type.equals(PotionEffectType.SLOW))
				this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.EFFECT_SLOWNESS.apply(level));
			else if(type.equals(PotionEffectType.FAST_DIGGING))
				this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(FakeAttributeInstance.EFFECT_HASTE.apply(level));
			else if(type.equals(PotionEffectType.SLOW_DIGGING))
				this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(FakeAttributeInstance.EFFECT_MINING_FATIGUE.apply(level));
			else if(type.equals(PotionEffectType.INCREASE_DAMAGE))
				this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier(FakeAttributeInstance.EFFECT_STRENGTH.apply(level));
			else if(type.equals(PotionEffectType.INVISIBILITY)) {
				this.meta.setInvisibleTemp(false);
				updateMetadata();
			} else if(type.equals(PotionEffectType.WEAKNESS))
				this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier(FakeAttributeInstance.EFFECT_WEAKNESS.apply(level));
			else if(type.equals(PotionEffectType.HEALTH_BOOST))
				this.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(FakeAttributeInstance.EFFECT_HEALTH_BOOST.apply(level));
			else if(type.equals(PotionEffectType.ABSORPTION)) {
				this.meta.setAbsorption(0);
				updateMetadata();
			} else if(type.equals(PotionEffectType.GLOWING)) {
				this.meta.setGlowingTemp(false);
				updateMetadata();
			} else if(type.equals(PotionEffectType.LUCK))
				this.getAttribute(Attribute.GENERIC_LUCK).removeModifier(FakeAttributeInstance.EFFECT_LUCK.apply(level));
			else if(type.equals(PotionEffectType.UNLUCK))
				this.getAttribute(Attribute.GENERIC_LUCK).removeModifier(FakeAttributeInstance.EFFECT_UNLUCK.apply(level));
		}
	}

	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		return this.attributes.get(attribute);
	}

	@Override
	public void updateGameProfile(GameProfile gameProfile) {
		this.gameProfile = gameProfile;
		if(this.living)
			this.spawn(this.location);
	}

	@Override
	public void freeze(boolean frozen) {
		this.frozen = frozen;
	}

	@Override
	public void damage(float amount, EntityDamageEvent.DamageCause cause) {
		boolean immune = (this.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && (cause == EntityDamageEvent.DamageCause.LAVA
				|| cause == EntityDamageEvent.DamageCause.FIRE
				|| cause == EntityDamageEvent.DamageCause.FIRE_TICK))
				|| this.hasPotionEffect(PotionEffectType.WATER_BREATHING) && (cause == EntityDamageEvent.DamageCause.DROWNING);
		if(this.noDamageTicks == 0 && !immune && !this.invulnerable) {
			NPCDamageEvent event = new NPCDamageEvent(this, amount, cause);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()) return;

			float current = this.getHealth();
			float absorption = this.meta.getAbsorption();
			int resistance = this.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) ? this.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1 : 0;
			if(resistance > 5)
				resistance = 5;
			float damage = event.getAmount();
			damage = damage - damage * 0.2F * resistance;
			absorption = absorption - damage > 0 ? absorption - damage : 0;
			damage = damage - (absorption + this.meta.getAbsorption());

			this.setHealth(current - damage);

			this.noDamageTicks = 10;
		}
	}

	@Override
	public void setSneaking(boolean state) {
		this.meta.setSneaking(state);
		if(state) {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.MOVEMENT_SPEED_SNEAKING);
			this.meta.setSprinting(false);
		} else
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.MOVEMENT_SPEED_SNEAKING);
		updateMetadata();
	}

	@Override
	public void setSprinting(boolean state) {
		this.meta.setSprinting(state);
		if(state) {
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.MOVEMENT_SPEED_SPRINTING);
			this.meta.setSneaking(false);
		} else
			this.attributes.get(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.MOVEMENT_SPEED_SPRINTING);
		updateMetadata();
	}

	public void ignite(int fireTicks) {
		this.meta.setOnFire(true);
		if(this.fireTicks < fireTicks)
			this.fireTicks = fireTicks;
		updateMetadata();
	}

	public void extinguish() {
		this.meta.setOnFire(false);
		this.fireTicks = -20;
		updateMetadata();
	}

	private void die() {
		this.dropEquip();
		this.playEntityStatus(EnumEntityStatus.DEATH);
		playSound(Sound.ENTITY_GENERIC_DEATH);
		this.fireTicks = -20;
		this.living = false;
	}

	private void dropEquip() {
		World world = this.location.getWorld();
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			this.equip(slot, null);
			ItemStack item = this.equip.get(slot);
			if(item != null)
				world.dropItemNaturally(this.location, item);
		}
		this.equip.clear();
	}

	private void playSound(Sound sound) {
		if(!this.meta.isSilent())
			this.location.getWorld().playSound(this.location, sound, 1, 1);
	}

	public void setParticles(Collection<PotionEffect> effects) {
		this.meta.setPotionAmbient(effects.stream().allMatch(PotionEffect::isAmbient));
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
		return getSurroundingBlocks().stream()
				.filter(this::touches)
				.collect(Collectors.toList());
	}

	@Override
	public List<Block> getSurroundingBlocks() {
		List<Block> blocks = new ArrayList<>();
		Block block = this.location.getBlock();

		for(int y = -1; y <= 2; y++) {
			for(int x = -1; x <= 1; x++) {
				for(int z = -1; z <= 1; z++) {
					blocks.add(block.getRelative(x, y, z));
				}
			}
		}
		return blocks;
	}

	public void applyAmbientDamage() {
		this.getTouchedBlocks().forEach((block) -> {
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
			if(block.getType() == Material.CACTUS)
				this.damage(1.0F, EntityDamageEvent.DamageCause.CONTACT);
		});

		//drown
		if(this.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER)
			this.meta.setAir(this.meta.getAir() - 1);
		else if(!this.getEyeLocation().getBlock().getType().isSolid() && !this.getEyeLocation().getBlock().isLiquid())
			this.meta.setAir(300);

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
			if(duration <= 0)
				this.removePotionEffect(effect.getType());
			else
				Reflection.getField(effect.getClass(), int.class, "duration").set(effect, duration - 1);
		}
	}

	@Override
	public void attack(LivingEntity target) {
		if(this.isAlive() && !target.isDead()) {
			this.playAnimation(NPCAnimationEvent.Animation.SWING_MAINHAND);
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
			if(this.fireTicks > 0)
				--this.fireTicks;

			tickPotions();
			applyAmbientDamage();

			if(this.getNoDamageTicks() > 0)
				--this.noDamageTicks;
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
