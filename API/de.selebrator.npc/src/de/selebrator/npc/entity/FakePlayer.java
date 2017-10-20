package de.selebrator.npc.entity;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.*;
import de.selebrator.npc.attribute.FakeAttributeInstance;
import de.selebrator.npc.event.NPCAnimationEvent;
import de.selebrator.npc.fetcher.PacketFetcher;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.metadata.MetadataObject;
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

import static de.selebrator.npc.Imports.*;

public class FakePlayer extends FakeLiving implements PlayerNPC {
	private static final double EYE_HEIGHT_STANDING = 1.62D;
	private static final double EYE_HEIGHT_SNEAKING = 1.2D;
	private GameProfile gameProfile;
	private FakeEquipment equip;
	private LivingEntity target;

	private MetadataObject<Float> absorption;
	private MetadataObject<Integer> score;
	private MetadataObject<Byte> skinFlags;
	private MetadataObject<Byte> mainHand;
	private MetadataObject<?> leftShoulder;
	private MetadataObject<?> rightShoulder;

	public FakePlayer(GameProfile gameProfile) {
		super();
		this.gameProfile = gameProfile;

		this.setSkinFlags(true, true, true, true, true, true, true);
		this.health.set(20.0F);
		this.setCustomName(this.getDisplayName());

		this.setEquipment(new FakeEquipment(this));

		this.initAttribute(Attribute.GENERIC_ATTACK_SPEED);
		this.initAttribute(Attribute.GENERIC_LUCK);

		this.setFireTicks(-20);
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	void initMetadata() {
		super.initMetadata();
		this.absorption = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHuman_a, 0.0f); //11
		this.score = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHuman_b, 0); //12
		this.skinFlags = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHuman_br, (byte) 0); //13
		this.mainHand = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHuman_bs, (byte) 1); //14
		this.leftShoulder = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHuman_bt, CONSTRUCTOR_NBTTagCompound.newInstance()); //15
		this.rightShoulder = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHuman_bu, CONSTRUCTOR_NBTTagCompound.newInstance()); //16
	}

	@Override
	public void spawn(Location location) {
		this.setLocation(location);

		PacketFetcher.broadcastPackets(
				PacketFetcher.playerInfo(this.gameProfile, "ADD_PLAYER"),
				PacketFetcher.namedEntitySpawn(this.getEntityId(), this.gameProfile, this.getLocation(), this.getDataWatcher())
		);

		this.setLiving(this.health.get() > 0);

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

	public GameProfile getGameProfile() {
		return this.gameProfile;
	}

	public void setGameProfile(GameProfile gameProfile) {
		if(this.isAlive()) {
			PacketFetcher.broadcastPackets(
					PacketFetcher.playerInfo(this.getGameProfile(), "REMOVE_PLAYER"),
					PacketFetcher.playerInfo(gameProfile, "ADD_PLAYER"),
					PacketFetcher.namedEntitySpawn(this.getEntityId(), gameProfile, this.getLocation(), this.getDataWatcher())
			);
		}
		this.gameProfile = gameProfile;
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
		return ignoreSneaking ? EYE_HEIGHT_STANDING : (this.isSneaking() ? EYE_HEIGHT_SNEAKING : EYE_HEIGHT_STANDING);
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
			this.setAbsorption(4 * level);
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
				this.setAbsorption(0);
				updateMetadata();
			} else if(type.equals(PotionEffectType.LUCK))
				this.getAttribute(Attribute.GENERIC_LUCK).removeModifier(FakeAttributeInstance.EFFECT_LUCK.apply(level));
			else if(type.equals(PotionEffectType.UNLUCK))
				this.getAttribute(Attribute.GENERIC_LUCK).removeModifier(FakeAttributeInstance.EFFECT_UNLUCK.apply(level));
		}
	}

	@Override
	public void setSneaking(boolean state) {
		this.setStatus(Status.SNEAK, state);
		if(state) {
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.MOVEMENT_SPEED_SNEAKING);
			this.setStatus(Status.SPRINT, false);
		} else
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.MOVEMENT_SPEED_SNEAKING);
		updateMetadata();
	}

	@Override
	public void setSprinting(boolean state) {
		this.setStatus(Status.SPRINT, state);
		if(state) {
			this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(FakeAttributeInstance.MOVEMENT_SPEED_SPRINTING);
			this.setStatus(Status.SNEAK, false);
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
			this.setRemainingAir(this.getRemainingAir() - 1);
		else if(!this.getEyeLocation().getBlock().getType().isSolid() && !this.getEyeLocation().getBlock().isLiquid())
			this.setRemainingAir(300);

		if(this.getRemainingAir() <= -20) {
			this.damage(2.0F, EntityDamageEvent.DamageCause.DROWNING);
			this.setRemainingAir(0);
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
		this.setRemainingAir(300);
		this.setSilent(false);
		this.health.set(20.0F);

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

	public float getAbsorption() {
		return this.absorption.get();
	}

	public void setAbsorption(float absorption) {
		this.absorption.set(absorption > 0 ? absorption : 0);
	}

	public int getScore() {
		return this.score.get();
	}

	public void setScore(int score) {
		this.score.set(score);
	}

	public boolean getSkinFlag(SkinFlag target) {
		return FakeEntity.getBitmaskValue(this.skinFlags, target.getId());
	}

	public void setSkinFlag(SkinFlag target, boolean state) {
		FakeEntity.setBitmaskValue(this.skinFlags, target.getId(), state);
	}

	public void setSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
		this.skinFlags.set((byte) ((cape ? 1 : 0) << SkinFlag.CAPE.getId() | (jacket ? 1 : 0) << SkinFlag.JACKET.getId() | (leftArm ? 1 : 0) << SkinFlag.LEFT_SLEEVE.getId() | (rightArm ? 1 : 0) << SkinFlag.RIGHT_SLEEVE.getId() | (leftLeg ? 1 : 0) << SkinFlag.LEFT_PANTS.getId() | (rightLeg ? 1 : 0) << SkinFlag.RIGHT_PANTS.getId() | (hat ? 1 : 0) << SkinFlag.HAT.getId()));
	}

	public MainHand getMainHand() {
		return this.mainHand.get() == 1 ? MainHand.RIGHT : MainHand.LEFT;
	}

	public void setMainHand(MainHand mainHand) {
		switch(mainHand) {
			case LEFT:
				this.mainHand.set((byte) 0);
				break;
			case RIGHT:
				this.mainHand.set((byte) 1);
				break;
		}
	}
}
