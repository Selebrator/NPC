package de.selebrator.npc.fake.attribute;

import org.bukkit.attribute.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="http://minecraft.gamepedia.com/Attribute#Modifiers">Reference</a>
 */
public class FakeAttributeInstance implements AttributeInstance {

	public static final AttributeModifier MOVEMENT_SPEED_SNEAKING = new AttributeModifier("Sneaking speed reduction", -0.7D, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	public static final AttributeModifier MOVEMENT_SPEED_SPRINTING = new AttributeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"), "Sprinting speed boost", 0.30000001192092896D, AttributeModifier.Operation.MULTIPLY_SCALAR_1); //net.minecraft.server.<version>.EntityLiving

	public static final Function<Byte, AttributeModifier> EFFECT_SPEED = level -> new AttributeModifier(UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635"), "effect.moveSpeed" + level, 0.20000000298023224D * level, AttributeModifier.Operation.MULTIPLY_SCALAR_1); //net.minecraft.server.<version>.MobEffectList
	public static final Function<Byte, AttributeModifier> EFFECT_SLOWNESS = level -> new AttributeModifier(UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890"), "effect.moveSlowdown" + level, -0.15000000596046448D * level, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	public static final Function<Byte, AttributeModifier> EFFECT_HASTE = level -> new AttributeModifier(UUID.fromString("AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3"), "effect.digSpeed" + level, 0.10000000149011612D * level, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	public static final Function<Byte, AttributeModifier> EFFECT_MINING_FATIGUE = level -> new AttributeModifier(UUID.fromString("55FCED67-E92A-486E-9800-B47F202C4386"), "effect.digSlowDown" + level, -0.10000000149011612D * level, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
	public static final Function<Byte, AttributeModifier> EFFECT_STRENGTH = level -> new AttributeModifier(UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"), "effect.damageBoost" + level, 3.0D * level, AttributeModifier.Operation.ADD_NUMBER);
	public static final Function<Byte, AttributeModifier> EFFECT_WEAKNESS = level -> new AttributeModifier(UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5"), "effect.weakness" + level, -4.0D * level, AttributeModifier.Operation.ADD_NUMBER);
	public static final Function<Byte, AttributeModifier> EFFECT_HEALTH_BOOST = level -> new AttributeModifier(UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC"), "effect.healthBoost" + level, 4.0D * level, AttributeModifier.Operation.ADD_NUMBER);
	public static final Function<Byte, AttributeModifier> EFFECT_LUCK = level -> new AttributeModifier(UUID.fromString("03C3C89D-7037-4B42-869F-B146BCB64D2E"), "effect.luck" + level, 1.0D * level, AttributeModifier.Operation.ADD_NUMBER);
	public static final Function<Byte, AttributeModifier> EFFECT_UNLUCK = level -> new AttributeModifier(UUID.fromString("CC5AF142-2BD2-4215-B636-2605AED11727"), "effect.unluck" + level, -1.0D * level, AttributeModifier.Operation.ADD_NUMBER);

	private final Attribute attribute;
	private final double min;
	private final double max;
	private final double defaultValue;
	private double base;
	private Collection<AttributeModifier> modifiers = new ArrayList<>();

	public FakeAttributeInstance(Attribute attribute) {
		this(attribute, 0);
		this.base = this.defaultValue;
	}

	public FakeAttributeInstance(Attribute attribute, double baseValue) {
		this.attribute = attribute;
		this.base = baseValue;
		switch(attribute) {
			case GENERIC_MAX_HEALTH:
				this.min = 0;
				this.max = 1024;
				this.defaultValue = 20;
				break;
			case GENERIC_FOLLOW_RANGE:
				this.min = 0;
				this.max = 2048;
				this.defaultValue = 32;
				break;
			case GENERIC_KNOCKBACK_RESISTANCE:
				this.min = 0;
				this.max = 1;
				this.defaultValue = 0;
				break;
			case GENERIC_MOVEMENT_SPEED:
				this.min = 0;
				this.max = 1024;
				this.defaultValue = 0.699999988079071D;
				break;
			case GENERIC_FLYING_SPEED:
				this.min = 0;
				this.max = 1024;
				this.defaultValue = 0.4000000059604645D;
				break;
			case GENERIC_ATTACK_DAMAGE:
				this.min = 0;
				this.max = 2048;
				this.defaultValue = 2;
				break;
			case GENERIC_ATTACK_SPEED:
				this.min = 0;
				this.max = 1024;
				this.defaultValue = 4;
				break;
			case GENERIC_ARMOR:
				this.min = 0;
				this.max = 30;
				this.defaultValue = 0;
				break;
			case GENERIC_ARMOR_TOUGHNESS:
				this.min = 0;
				this.max = 20;
				this.defaultValue = 0;
				break;
			case GENERIC_LUCK:
				this.min = -1024;
				this.max = 1024;
				this.defaultValue = 0;
				break;
			case HORSE_JUMP_STRENGTH:
				this.min = 0;
				this.max = 2;
				this.defaultValue = 0.69999999999999996D;
				break;
			case ZOMBIE_SPAWN_REINFORCEMENTS:
				this.min = 0;
				this.max = 1;
				this.defaultValue = 0;
				break;
			default:
				throw new IllegalArgumentException("Not supported yet.");
		}
	}

	@Override
	public Attribute getAttribute() {
		return this.attribute;
	}

	@Override
	public double getBaseValue() {
		return base;
	}

	@Override
	public void setBaseValue(double value) {
		if(this.min < value && value < this.max)
			this.base = value;
		else
			throw new IllegalArgumentException("Value out of range");
	}

	@Override
	public Collection<AttributeModifier> getModifiers() {
		return this.modifiers;
	}

	private Collection<AttributeModifier> getModifiersByOperation(AttributeModifier.Operation operation) {
		return this.modifiers.stream().filter(attributeModifier -> attributeModifier.getOperation() == operation).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void addModifier(AttributeModifier attributeModifier) {
		this.modifiers.add(attributeModifier);
	}

	@Override
	public void removeModifier(AttributeModifier attributeModifier) {
		this.modifiers.remove(attributeModifier);
	}

	public void removeModifier(String name) {
		for(AttributeModifier modifier : this.modifiers) {
			if(modifier.getName().equals(name)) {
				this.modifiers.remove(modifier);
				break;
			}
		}
	}

	public void removeAllModifiers() {
		this.modifiers.clear();
	}

	@Override
	public double getValue() {
		double x = this.base;
		for(AttributeModifier attributeModifier : getModifiersByOperation(AttributeModifier.Operation.ADD_NUMBER))
			x += attributeModifier.getAmount();

		double y = x;
		for(AttributeModifier attributeModifier : getModifiersByOperation(AttributeModifier.Operation.ADD_SCALAR))
			y += x * attributeModifier.getAmount();

		for(AttributeModifier attributeModifier : getModifiersByOperation(AttributeModifier.Operation.MULTIPLY_SCALAR_1))
			y *= 1D + attributeModifier.getAmount();

		return y < this.min ? this.min : (y > this.max ? max : y);
	}

	@Override
	public double getDefaultValue() {
		return this.defaultValue;
	}
}
