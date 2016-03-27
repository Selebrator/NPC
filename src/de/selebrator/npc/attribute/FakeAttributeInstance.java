package de.selebrator.npc.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class FakeAttributeInstance implements AttributeInstance {
	private final Attribute attribute;
	private double base;
	private final double min;
	private final double max;
	private Collection<AttributeModifier> modifiers = new ArrayList<>();

	public FakeAttributeInstance(Attribute attribute, double baseValue) {
		this.attribute = attribute;
		this.base = baseValue;
		switch(attribute) {
			case GENERIC_MAX_HEALTH:
				this.min = 0;
				this.max = 1024;
				break;
			case GENERIC_FOLLOW_RANGE:
				this.min = 0;
				this.max = 2048;
				break;
			case GENERIC_KNOCKBACK_RESISTANCE:
				this.min = 0;
				this.max = 1;
				break;
			case GENERIC_MOVEMENT_SPEED:
				this.min = 0;
				this.max = 1024;
				break;
			case GENERIC_ATTACK_DAMAGE:
				this.min = 0;
				this.max = 2048;
				break;
			case GENERIC_ATTACK_SPEED:
				this.min = 0;
				this.max = 1024;
				break;
			case GENERIC_ARMOR:
				this.min = 0;
				this.max = 30;
				break;
			case GENERIC_LUCK:
				this.min = -1024;
				this.max = 1024;
				break;
			case HORSE_JUMP_STRENGTH:
				throw new UnsupportedOperationException("Not supported.");
			case ZOMBIE_SPAWN_REINFORCEMENTS:
				throw new UnsupportedOperationException("Not supported.");
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

	@Override
	public void addModifier(AttributeModifier attributeModifier) {
		this.modifiers.add(attributeModifier);
	}

	@Override
	public void removeModifier(AttributeModifier attributeModifier) {
		this.modifiers.remove(attributeModifier);
	}

	public void removeAllModifiers() {
		this.modifiers.clear();
	}

	private Collection<AttributeModifier> byOperation(AttributeModifier.Operation operation) {
		return this.modifiers.stream().filter(attributeModifier -> attributeModifier.getOperation() == operation).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public double getValue() {
		double x = this.base;
		for(AttributeModifier attributeModifier : byOperation(AttributeModifier.Operation.ADD_NUMBER))
			x += attributeModifier.getAmount();

		double y = x;
		for(AttributeModifier attributeModifier : byOperation(AttributeModifier.Operation.ADD_SCALAR))
			y += x * attributeModifier.getAmount();

		for(AttributeModifier attributeModifier : byOperation(AttributeModifier.Operation.MULTIPLY_SCALAR_1))
			y *= 1D + attributeModifier.getAmount();

		return y < this.min ? this.min : (y > this.max ? max : y);
	}
}