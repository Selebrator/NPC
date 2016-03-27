package de.selebrator.npc;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.event.NPCDamageEvent;
import de.selebrator.npc.event.NPCDespawnEvent;
import de.selebrator.npc.event.NPCEquipEvent;
import de.selebrator.npc.event.NPCMoveEvent;
import de.selebrator.npc.event.NPCSpawnEvent;
import de.selebrator.npc.event.NPCTeleportEvent;
import de.selebrator.fetcher.PacketFetcher;
import de.selebrator.npc.attribute.FakeAttributeInstance;
import de.selebrator.reflection.Reflection;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.MobEffect;
import net.minecraft.server.v1_9_R1.MobEffectList;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PotionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
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
	public FakePlayerMeta meta;
	public FakePlayerEquipment equip;

	private boolean frozen;
	private boolean living;
	private Location location;
	private LivingEntity target;
	private EnumNature nature;

	private int fireTicks;
	private int noDamageTicks;
	private Location respawnLocation;

	private Map<MobEffectList, MobEffect> effects = new HashMap<>();
	private Map<Attribute, FakeAttributeInstance> attributes = new HashMap<>();

	private final AttributeModifier MOVEMENT_SPEED_MODIFIER_SNEAKING = new AttributeModifier("Sneaking speed reduction", -0.7D, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	private final AttributeModifier MOVEMENT_SPEED_MODIFIER_SPRINTING = new AttributeModifier("Sprinting speed boost", 0.30000001192092896D, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	private static final double EYE_HEIGHT_STANDING = 1.62D;
	private static final double EYE_HEIGHT_SNEAKING = 1.2D;


	public FakePlayer(GameProfile gameProfile) {
		this.entityId = (int) Reflection.getField(Entity.class, "entityCount").get(null);
		Reflection.getField(Entity.class, "entityCount").set(null, this.entityId + 1);

		this.gameProfile = gameProfile;

		initialize();
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

		for(EnumEquipmentSlot slot : EnumEquipmentSlot.values()) {
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

	public void equip(EnumEquipmentSlot slot, ItemStack item) {
		NPCEquipEvent event = new NPCEquipEvent(this, slot, item);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) { return; }

		slot = event.getSlot();
		item = event.getItem();

		PacketFetcher.broadcastPackets(
				PacketFetcher.entityEquipment(this.entityId, slot.getNMS(), CraftItemStack.asNMSCopy(item))
		);
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
	public FakePlayerEquipment getEquipment() {
		return this.equip;
	}

	public boolean hasEffect(MobEffectList effectList) {
		return this.effects.containsKey(effectList);
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType effectType) {
		return this.hasEffect(MathHelper.convertEffectType(effectType));
	}

	public MobEffect getEffect(MobEffectList effectList) {
		return this.effects.get(effectList);
	}

	public Collection<MobEffect> getEffects() {
		return this.effects.values();
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		Collection<PotionEffect> potionEffects = new ArrayList<>();
		this.effects.values().forEach( effect ->
				potionEffects.add(
						new PotionEffect(
								PotionEffectType.getByName(effect.getMobEffect().a()),
								effect.getDuration(),
								effect.getAmplifier(),
								effect.isAmbient(),
								effect.isShowParticles())
				));
		return potionEffects;
	}

	public void addEffect(MobEffect effect) {
		this.effects.put(effect.getMobEffect(), effect);
		this.calcPotionColor(this.getEffects());
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		this.addEffect(
				MathHelper.convertEffect(effect)
		);
	}

	public void addEffects(Collection<MobEffect> effects) {
		for(MobEffect effect : effects) {
			this.effects.put(effect.getMobEffect(), effect);
		}
		this.calcPotionColor(this.getEffects());
	}

	@Override
	public void addPotionEffects(Collection<PotionEffect> effects) {
		for(PotionEffect effect : effects) {
			this.effects.put(
					MathHelper.convertEffectType(effect.getType()),
					MathHelper.convertEffect(effect)
			);
		}
		this.calcPotionColor(this.getEffects());
	}

	public void removeEffect(MobEffectList effectList) {
		this.effects.remove(effectList);
		this.calcPotionColor(this.getEffects());
	}

	@Override
	public void removePotionEffect(PotionEffectType effectType) {
		this.effects.remove(
				MathHelper.convertEffectType(effectType)
		);
	}

	@Override
	public FakeAttributeInstance getAttribute(Attribute attribute) {
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
	public void setMeta(FakePlayerMeta meta) {
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
	public  void damage(int amount) {
		damage(amount, EntityDamageEvent.DamageCause.CUSTOM);
	}

	@Override
	public void damage(int amount, EntityDamageEvent.DamageCause cause) {
		if(this.noDamageTicks == 0) {
			NPCDamageEvent event = new NPCDamageEvent(this, amount, cause);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()) { return; }

			this.setHealth(this.getHealth() - event.getAmount());

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

	// ### LIVE ###

	private void playSound(Sound sound) {
		if(!this.meta.isSilent()) {
			this.location.getWorld().playSound(this.location, sound, 1, 1);
		}
	}

	public void calcPotionColor(Collection<MobEffect> effects) {
		boolean ambient = true;
		for(MobEffect effect : effects) {
			if(!effect.isAmbient()) {
				ambient = false;
			}
		}
		this.meta.setPotionAmbient(ambient);
		this.meta.setPotionColor(effects.isEmpty() ? 0 : PotionUtil.a(effects));
		updateMetadata();
	}

	@Override
	public boolean touches(Block block) {
		double x = (block.getX() + 0.5) - this.location.getX();
		double y = block.getY() - this.location.getY();
		double z = (block.getZ() + 0.5) - this.location.getZ();

		return ((-0.8D < x && x < -0.2D) || (-0.2D < x && x < 0.2D) || (0.2 < x && x < 0.8D))
				&& (-1 < y && y < (0.2D + this.getEyeHeight(false)))
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

	public void applyAmbientDamage() {
		this.getTouchedBlocks().forEach( (block) -> {
			//lava
			if(block.getType() == Material.STATIONARY_LAVA) {
				this.ignite(160);
				this.damage(4, EntityDamageEvent.DamageCause.LAVA);
			}

			//fire(contact)
			if(block.getType() == Material.FIRE) {
				this.ignite(160);
				this.damage(1, EntityDamageEvent.DamageCause.FIRE);
			}

			//fire(tick)
			if(block.getType() == Material.STATIONARY_WATER
					|| this.location.getWorld().hasStorm()
					|| this.location.getWorld().isThundering()) {
				this.extinguish();
			}

			if(this.fireTicks > 0) {
				if(this.fireTicks % 20 == 0) {
					this.damage(1, EntityDamageEvent.DamageCause.FIRE_TICK);
				}
			} else if(this.fireTicks == 0) {
				this.extinguish();
			}

			//cactus
			if(block.getType() == Material.CACTUS) {
				this.damage(1, EntityDamageEvent.DamageCause.CONTACT);
			}
		});

		//drown
		if(this.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER) {
			this.meta.setAir(this.meta.getAir() - 1);
		} else if(!this.getEyeLocation().getBlock().getType().isSolid() && !this.getEyeLocation().getBlock().isLiquid()) {
			this.meta.setAir(300);
		}
		if(this.meta.getAir() <= -20) {
			this.damage(2, EntityDamageEvent.DamageCause.DROWNING);
			this.meta.setAir(0);
		}
	}

	public void tickPotions() {
		//--duration
		Collection<MobEffect> effectsToProcess = this.getEffects();
		for(MobEffect effect : effectsToProcess) {
			int duration = effect.getDuration();
			if(duration <= 0) {
				this.effects.remove(effect.getMobEffect());
			} else {
				Reflection.getField(effect.getClass(), "duration").set(effect, duration - 1);
			}
		}
		this.addEffects(effectsToProcess);
	}

	@Override
	public void attack(LivingEntity target) {
		if(this.isAlive() && !target.isDead()) {
			this.playAnimation(EnumAnimation.SWING_ARM);
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

	public void initialize() {
		this.meta = new FakePlayerMeta();
		this.meta.setStatus(false, false, false, false, false, false);
		this.meta.setSkinFlags(true, true, true, true, true, true, true);
		this.meta.setHealth(20);
		this.meta.setAir(300);
		this.meta.setName(this.getName());

		this.attributes = new HashMap<>();
		this.attributes.put(Attribute.GENERIC_MAX_HEALTH, new FakeAttributeInstance(Attribute.GENERIC_MAX_HEALTH, 20));
		this.attributes.put(Attribute.GENERIC_FOLLOW_RANGE, new FakeAttributeInstance(Attribute.GENERIC_FOLLOW_RANGE, 32));
		this.attributes.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new FakeAttributeInstance(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0));
		this.attributes.put(Attribute.GENERIC_MOVEMENT_SPEED, new FakeAttributeInstance(Attribute.GENERIC_MOVEMENT_SPEED, 0.699999988079071D));
		this.attributes.put(Attribute.GENERIC_ATTACK_DAMAGE, new FakeAttributeInstance(Attribute.GENERIC_ATTACK_DAMAGE, 1));
		this.attributes.put(Attribute.GENERIC_ARMOR, new FakeAttributeInstance(Attribute.GENERIC_ARMOR, 0));
		this.attributes.put(Attribute.GENERIC_ATTACK_SPEED, new FakeAttributeInstance(Attribute.GENERIC_ATTACK_SPEED, 4));
		this.attributes.put(Attribute.GENERIC_LUCK, new FakeAttributeInstance(Attribute.GENERIC_LUCK, 0));

		this.frozen = false;
		this.living = false;
		this.location = null;
		this.target = null;
		this.nature = EnumNature.PASSIVE;
		this.fireTicks = -20;
		this.noDamageTicks = 0;

		this.equip = new FakePlayerEquipment(this);

		this.effects = new HashMap<>();
	}

	public void softReset() {
		this.meta.setAir(300);
		this.meta.setSilent(false);
		this.meta.setHealth(20F);

		this.attributes.forEach((attribute, fakeAttributeInstance) -> fakeAttributeInstance.removeAllModifiers());

		this.frozen = false;
		this.living = false;
		this.location = null;
		this.target = null;
		this.nature = EnumNature.PASSIVE;

		this.extinguish();
		this.noDamageTicks = 0;

		this.equip = new FakePlayerEquipment(this);

		this.effects = new HashMap<>();
	}
}
