package de.selebrator.npc.entity;

import de.selebrator.npc.*;
import de.selebrator.npc.attribute.FakeAttributeInstance;
import de.selebrator.npc.metadata.MetadataObject;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.*;

import java.util.*;

import static de.selebrator.npc.Imports.*;

public class FakeLiving extends FakeEntity implements LivingNPC {
	private static final Map<PotionEffectType, Integer> potionColors = Map.ofEntries(
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
	MetadataObject<Byte> activeHand;
	MetadataObject<Float> health;
	MetadataObject<Integer> potionColor;
	MetadataObject<Boolean> potionAmbient;
	MetadataObject<Integer> arrows;
	private Map<Attribute, FakeAttributeInstance> attributes;
	private Map<PotionEffectType, PotionEffect> effects;
	private boolean living;

	public FakeLiving(EntityType type) {
		super(type);

		this.attributes = new HashMap<>();
		this.initAttribute(Attribute.GENERIC_MAX_HEALTH);
		this.initAttribute(Attribute.GENERIC_FOLLOW_RANGE);
		this.initAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
		this.initAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		this.initAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
		this.initAttribute(Attribute.GENERIC_ARMOR);
		this.initAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);

		this.effects = new HashMap<>();
	}

	public static Color calcPotionColor(Collection<PotionEffect> effects) {
		if(effects == null || effects.isEmpty())
			return Color.BLACK;

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
			return Color.BLACK;
		else {
			red = red / (float) totalAmplifier * 255.0F;
			green = green / (float) totalAmplifier * 255.0F;
			blue = blue / (float) totalAmplifier * 255.0F;
			return Color.fromRGB((int) red, (int) green, (int) blue);
		}
	}

	@Override
	void initMetadata() {
		super.initMetadata();
		this.activeHand = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLiving_at, (byte) 0); //6
		this.health = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLiving_HEALTH, 1.0f); //7
		this.potionColor = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLiving_g, 0); //8
		this.potionAmbient = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLiving_h, false); //9
		this.arrows = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityLiving_br, 0); //10
	}

	@Override
	public void despawn() {
		super.despawn();
		this.setLiving(false);
	}

	void initAttribute(Attribute attribute) {
		this.attributes.put(attribute, new FakeAttributeInstance(attribute));
	}

	public AttributeInstance getAttribute(Attribute attribute) {
		return this.attributes.get(attribute);
	}

	@Override
	public double getMoveSpeed() {
		return 0;
	}

	public boolean hasPotionEffect(PotionEffectType type) {
		return this.effects.containsKey(type);
	}

	public PotionEffect getPotionEffect(PotionEffectType type) {
		return this.effects.get(type);
	}

	public Collection<PotionEffect> getActivePotionEffects() {
		return this.effects.values();
	}

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
		else if(type.equals(PotionEffectType.INCREASE_DAMAGE))
			this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(FakeAttributeInstance.EFFECT_STRENGTH.apply(level));
		else if(type.equals(PotionEffectType.HEAL)) {
			this.setHealth(this.getHealth() + (4.0F * (float) Math.pow(2.0D, level - 1.0D)));
			this.removePotionEffect(PotionEffectType.HEAL);
		} else if(type.equals(PotionEffectType.HARM)) {
			this.damage((6.0F * (float) Math.pow(2.0D, level - 1.0D)), EntityDamageEvent.DamageCause.MAGIC); //TODO
			this.removePotionEffect(PotionEffectType.HARM);
		} else if(type.equals(PotionEffectType.INVISIBILITY)) {
			this.setInvisible(true);
			updateMetadata();
		} else if(type.equals(PotionEffectType.WEAKNESS))
			this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(FakeAttributeInstance.EFFECT_WEAKNESS.apply(level));
		else if(type.equals(PotionEffectType.HEALTH_BOOST))
			this.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(FakeAttributeInstance.EFFECT_HEALTH_BOOST.apply(level));
		else if(type.equals(PotionEffectType.GLOWING)) {
			this.setGlowing(true);
			updateMetadata();
		}
	}

	public void addPotionEffects(Collection<PotionEffect> effects) {
		effects.forEach(this::addPotionEffect);
	}

	public void removePotionEffect(PotionEffectType type) {
		if(this.hasPotionEffect(type)) {
			byte level = (byte) (this.getPotionEffect(type).getAmplifier() + 1);

			this.effects.remove(type);
			this.setParticles(this.getActivePotionEffects());

			if(type.equals(PotionEffectType.SPEED))
				this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.EFFECT_SPEED.apply(level));
			else if(type.equals(PotionEffectType.SLOW))
				this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(FakeAttributeInstance.EFFECT_SLOWNESS.apply(level));
			else if(type.equals(PotionEffectType.INCREASE_DAMAGE))
				this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier(FakeAttributeInstance.EFFECT_STRENGTH.apply(level));
			else if(type.equals(PotionEffectType.INVISIBILITY)) {
				this.setInvisible(false);
				updateMetadata();
			} else if(type.equals(PotionEffectType.WEAKNESS))
				this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier(FakeAttributeInstance.EFFECT_WEAKNESS.apply(level));
			else if(type.equals(PotionEffectType.HEALTH_BOOST))
				this.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(FakeAttributeInstance.EFFECT_HEALTH_BOOST.apply(level));
			else if(type.equals(PotionEffectType.GLOWING)) {
				this.setGlowing(false);
				updateMetadata();
			}
		}
	}

	public void removePotionEffects(Collection<PotionEffectType> types) {
		this.getActivePotionEffects().stream()
				.map(PotionEffect::getType)
				.filter(types::contains)
				.forEach(this::removePotionEffect);
	}

	public void clearPotionEffects() {
		this.effects = new HashMap<>();
		this.setParticles(null);
	}

	public void setParticles(Collection<PotionEffect> effects) {
		this.setPotionAmbient(effects != null && effects.stream().allMatch(PotionEffect::isAmbient));
		this.setPotionColor(calcPotionColor(effects));
		updateMetadata();
	}

	@Override
	public boolean isAlive() {
		return this.living;
	}

	public void setLiving(boolean living) {
		this.living = living;
	}

	public float getHealth() {
		return this.isAlive() ? this.health.get() : 0;
	}

	public void setHealth(float health) {
		final float before = this.getHealth();
		health = Math.min(Math.max(0.0f, health), (float) this.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		this.health.set(health);

		if(health == 0)
			this.die();
		else if(before > health)
			this.hurt();
		else if(before == 0 && health > 0)
			this.revive();
	}

	public Color getPotionColor() {
		return Color.fromRGB(this.potionColor.get());
	}

	public void setPotionColor(Color potionColor) {
		this.potionColor.set(potionColor.asRGB());
	}

	public boolean isPotionAmbient() {
		return this.potionAmbient.get();
	}

	public void setPotionAmbient(boolean potionAmbient) {
		this.potionAmbient.set(potionAmbient);
	}

	public int getArrows() {
		return this.arrows.get();
	}

	public void setArrows(int arrows) {
		this.arrows.set(arrows);
	}

	void die() {
		this.playEntityStatus(EnumEntityStatus.DEATH);
		this.playSound(Sound.ENTITY_GENERIC_DEATH);
		this.setFireTicks(-20);
		this.setLiving(false);
	}

	void hurt() {
		this.playEntityStatus(EnumEntityStatus.HURT);
		playSound(Sound.ENTITY_GENERIC_HURT);
	}

	void revive() {
		this.setLiving(true);
		if(getHealth() <= 0.0f)
			this.setHealth((float) this.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		this.spawn(this.getLocation());
	}

	public boolean isInvulnerable(EntityDamageEvent.DamageCause cause) {
		switch(cause) {
			case LAVA:
			case FIRE:
			case FIRE_TICK:
				return this.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);
			case DROWNING:
				return this.hasPotionEffect(PotionEffectType.WATER_BREATHING);
			case VOID:
			default:
				return false;
		}
	}

	public void damage(float amount, EntityDamageEvent.DamageCause cause) {
		if(this.getNoDamageTicks() == 0 && !this.isInvulnerable() && !this.isInvulnerable(cause)) {
			//heal
			if(amount < 0.0f) {
				this.setHealth(this.getHealth() + amount);
				return;
			}

			//resistance
			if(this.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) && cause != EntityDamageEvent.DamageCause.STARVATION && cause != EntityDamageEvent.DamageCause.VOID) {
				int resistance = Math.min(this.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1, 5);
				amount -= amount * 0.2f * resistance;
			}

			//absorption
			if(this instanceof FakePlayer) {
				FakePlayer fakePlayer = (FakePlayer) this;
				float newAbsorption = Math.max(fakePlayer.getAbsorption() - amount, 0.0f);
				float absorbed = fakePlayer.getAbsorption() - newAbsorption;

				amount -= absorbed;

				if(absorbed > 0.0f)
					this.hurt();

				fakePlayer.setAbsorption(newAbsorption);
			}

			this.setHealth(this.getHealth() - amount);
			this.setNoDamageTicks(10);
		}
	}
}
