package de.selebrator.npc.entity;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.*;
import de.selebrator.npc.attribute.FakeAttributeInstance;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.fetcher.PacketFetcher;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.metadata.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

import static de.selebrator.npc.Imports.FIELD_PotionEffect_duration;

public class FakePlayer extends FakeLiving implements PlayerNPC {
	private static final double EYE_HEIGHT_STANDING = 1.62D;
	private static final double EYE_HEIGHT_SNEAKING = 1.2D;
	private GameProfile gameProfile;
	private FakeEquipment equip;
	private LivingEntity target;

	public FakePlayer(GameProfile gameProfile) {
		super(EntityType.PLAYER);
		this.gameProfile = gameProfile;

		this.setMeta(new FakeHumanMetadata());
		this.getMeta().setSkinFlags(true, true, true, true, true, true, true);
		this.getMeta().setHealth(20.0F);
		this.getMeta().setName(this.getName());

		this.setEquipment(new FakeEquipment(this));

		this.initAttribute(Attribute.GENERIC_ATTACK_SPEED);
		this.initAttribute(Attribute.GENERIC_LUCK);

		this.setFireTicks(-20);
	}

	@Override
	public void spawn(Location location) {
		this.setLocation(location);

		PacketFetcher.broadcastPackets(
				PacketFetcher.playerInfo(this.gameProfile, "ADD_PLAYER"),
				PacketFetcher.namedEntitySpawn(this.getEntityId(), this.gameProfile, this.getLocation(), this.getMeta())
		);

		this.setLiving(this.getMeta().getHealth() > 0);

		for(EquipmentSlot slot : EquipmentSlot.values())
			this.equip(slot, this.getEquipment().get(slot));

		this.look(this.getLocation().getYaw(), this.getLocation().getPitch());
	}

	@Override
	public void despawn() {
		super.despawn();
		PacketFetcher.broadcastPackets(
				PacketFetcher.playerInfo(this.gameProfile, "REMOVE_PLAYER")
		);
	}

	public void equip(EquipmentSlot slot, ItemStack item) {
		PacketFetcher.broadcastPackets(
				PacketFetcher.entityEquipment(this.getEntityId(), slot, item)
		);
	}

	@Override
	public FakeHumanMetadata getMeta() {
		return (FakeHumanMetadata) super.getMeta();
	}

	@Override
	public void setMeta(FakeMetadata meta) {
		super.setMeta(meta);
	}

	public GameProfile getGameProfile() {
		return this.gameProfile;
	}

	public void setGameProfile(GameProfile gameProfile) {
		if(this.isAlive()) {
			PacketFetcher.broadcastPackets(
					PacketFetcher.playerInfo(this.getGameProfile(), "REMOVE_PLAYER"),
					PacketFetcher.playerInfo(gameProfile, "ADD_PLAYER"),
					PacketFetcher.namedEntitySpawn(this.getEntityId(), gameProfile, this.getLocation(), this.getMeta())
			);
		}
		this.gameProfile = gameProfile;
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
	public double getMoveSpeed() {
		return this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * 6.16714296217D;
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		return ignoreSneaking ? EYE_HEIGHT_STANDING : (this.getMeta().isSneaking() ? EYE_HEIGHT_SNEAKING : EYE_HEIGHT_STANDING);
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

	public FakeEquipment getEquipment() {
		return this.equip;
	}

	public void setEquipment(FakeEquipment equip) {
		this.equip = equip;
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		super.addPotionEffect(effect);

		PotionEffectType type = effect.getType();
		byte level = (byte) (effect.getAmplifier() + 1);

		if(type.equals(PotionEffectType.FAST_DIGGING))
			this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(FakeAttributeInstance.EFFECT_HASTE.apply(level)); //TODO Haste: he cant mine yet :(
		else if(type.equals(PotionEffectType.SLOW_DIGGING))
			this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(FakeAttributeInstance.EFFECT_MINING_FATIGUE.apply(level)); //TODO Mining Fatigue
		else if(type.equals(PotionEffectType.ABSORPTION)) {
			this.getMeta().setAbsorption(4 * level);
			updateMetadata();
		} else if(type.equals(PotionEffectType.LUCK))
			this.getAttribute(Attribute.GENERIC_LUCK).addModifier(FakeAttributeInstance.EFFECT_LUCK.apply(level));
		else if(type.equals(PotionEffectType.UNLUCK))
			this.getAttribute(Attribute.GENERIC_LUCK).addModifier(FakeAttributeInstance.EFFECT_UNLUCK.apply(level));
	}

	@Override
	public void removePotionEffect(PotionEffectType type) {
		super.removePotionEffect(type);

		if(this.hasPotionEffect(type)) {
			byte level = (byte) (this.getPotionEffect(type).getAmplifier() + 1);

			if(type.equals(PotionEffectType.FAST_DIGGING))
				this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(FakeAttributeInstance.EFFECT_HASTE.apply(level));
			else if(type.equals(PotionEffectType.SLOW_DIGGING))
				this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(FakeAttributeInstance.EFFECT_MINING_FATIGUE.apply(level));
			else if(type.equals(PotionEffectType.ABSORPTION)) {
				this.getMeta().setAbsorption(0);
				updateMetadata();
			} else if(type.equals(PotionEffectType.LUCK))
				this.getAttribute(Attribute.GENERIC_LUCK).removeModifier(FakeAttributeInstance.EFFECT_LUCK.apply(level));
			else if(type.equals(PotionEffectType.UNLUCK))
				this.getAttribute(Attribute.GENERIC_LUCK).removeModifier(FakeAttributeInstance.EFFECT_UNLUCK.apply(level));
		}
	}

	@Override
	public void setSneaking(boolean state) {
		this.getMeta().setSneaking(state);
		if(state) {
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.MOVEMENT_SPEED_SNEAKING);
			this.getMeta().setSprinting(false);
		} else
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.MOVEMENT_SPEED_SNEAKING);
		updateMetadata();
	}

	@Override
	public void setSprinting(boolean state) {
		this.getMeta().setSprinting(state);
		if(state) {
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.MOVEMENT_SPEED_SPRINTING);
			this.getMeta().setSneaking(false);
		} else
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.MOVEMENT_SPEED_SPRINTING);
		updateMetadata();
	}

	@Override
	void die() {
		this.dropEquip();
		super.die();
	}

	@Override
	void revive() {
		super.revive();
	}

	private void dropEquip() {
		World world = this.getLocation().getWorld();
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			this.equip(slot, null);
			ItemStack item = this.getEquipment().get(slot);
			if(item != null)
				world.dropItemNaturally(this.getLocation(), item);
		}
		this.getEquipment().clear();
	}

	public boolean touches(Block block) {
		double x = (block.getX() + 0.5) - this.getLocation().getX();
		double y = block.getY() - this.getLocation().getY();
		double z = (block.getZ() + 0.5) - this.getLocation().getZ();

		return ((-0.8D < x && x < -0.2D) || (-0.2D < x && x < 0.2D) || (0.2 < x && x < 0.8D))
				&& (-1 < y && y < (0.2D + this.getEyeHeight()))
				&& ((-0.8D < z && z < -0.2D) || (-0.2D < z && z < 0.2D) || (0.2 < z && z < 0.8D));
	}

	public List<Block> getTouchedBlocks() {
		return getSurroundingBlocks().stream()
				.filter(this::touches)
				.collect(Collectors.toList());
	}

	public List<Block> getSurroundingBlocks() {
		List<Block> blocks = new ArrayList<>();
		Block block = this.getLocation().getBlock();

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
					|| this.getLocation().getWorld().hasStorm()
					|| this.getLocation().getWorld().isThundering()) {
				this.extinguish();
			}

			if(this.getFireTicks() > 0) {
				if(this.getFireTicks() % 20 == 0) {
					this.damage(1.0F, EntityDamageEvent.DamageCause.FIRE_TICK);
				}
			} else if(this.getFireTicks() == 0) {
				this.extinguish();
			}

			//cactus
			if(block.getType() == Material.CACTUS)
				this.damage(1.0F, EntityDamageEvent.DamageCause.CONTACT);
		});

		//drown
		if(this.getEyeLocation().getBlock().getType() == Material.STATIONARY_WATER)
			this.getMeta().setAir(this.getMeta().getAir() - 1);
		else if(!this.getEyeLocation().getBlock().getType().isSolid() && !this.getEyeLocation().getBlock().isLiquid())
			this.getMeta().setAir(300);

		if(this.getMeta().getAir() <= -20) {
			this.damage(2.0F, EntityDamageEvent.DamageCause.DROWNING);
			this.getMeta().setAir(0);
		}
	}

	public void tickPotions() {
		Collection<PotionEffect> effectsToProcess = this.getActivePotionEffects();


		//--duration
		for(PotionEffect effect : effectsToProcess) {
			int duration = effect.getDuration();
			if(duration <= 0)
				this.removePotionEffect(effect.getType());
			else
				FIELD_PotionEffect_duration.set(effect, duration - 1);
		}
	}

	public void attack(LivingEntity target) {
		if(this.isAlive() && !target.isDead()) {
			this.playAnimation(NPCAnimationEvent.Animation.SWING_MAINHAND);
			target.damage(this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue());
			Vector distance = MathHelper.calcDistanceVector(this.getLocation(), target.getLocation());

			float yaw = MathHelper.calcYaw(distance.getX(), distance.getZ());
			float pitch = MathHelper.calcPitch(distance.getX(), distance.getY(), distance.getZ());

			Vector direction = MathHelper.calcDirectionVector(0.4, yaw, pitch);

			target.setVelocity(direction);
		}
	}

	public void tick() {
		if(this.isAlive() && !this.isFrozen()) {
			if(this.getFireTicks() > 0)
				this.setFireTicks(this.getFireTicks() - 1);

			tickPotions();
			applyAmbientDamage();

			if(this.getNoDamageTicks() > 0)
				this.setNoDamageTicks(this.getNoDamageTicks() - 1);
		}
	}

	public void softReset() {
		this.getMeta().setAir(300);
		this.getMeta().setSilent(false);
		this.getMeta().setHealth(20.0F);

		this.setEquipment(new FakeEquipment(this));

		this.clearPotionEffects();

		for(Attribute attribute : Attribute.values())
			((FakeAttributeInstance) this.getAttribute(attribute)).removeAllModifiers();

		this.setFrozen(false);
		this.setLiving(false);
		this.setLocation(null);
		this.setTarget(null);

		this.extinguish();
		this.setNoDamageTicks(0);
		this.setInvulnerable(false);
	}
}
