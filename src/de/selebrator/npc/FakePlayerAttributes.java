package de.selebrator.npc;

import org.bukkit.attribute.Attribute;

public class FakePlayerAttributes {
	private double maxHealth = 20;
	private double followRange = 32;
	private double knockbackResistance = 0;
	private double movementSpeed = 4.3D;
	private double attackDamage = 1;
	private double armor = 0;

	// ##### MAX_HEALTH #####
	public double getMaxHealth() {
		return this.maxHealth;
	}

	public void setMaxHealth(double maxHealth) {
		set(Attribute.GENERIC_MAX_HEALTH, maxHealth);
	}

	// ##### FOLLOW_RANGE #####
	public double getFollowRange() {
		return this.followRange;
	}

	public void setFollowRange(double followRange) {
		set(Attribute.GENERIC_FOLLOW_RANGE, followRange);
	}

	// ##### KNOCKBACK_RESISTANCE #####
	public double getKnockbackResistance() {
		return this.knockbackResistance;
	}

	public void setKnockbackResistance(double knockbackResistance) {
		set(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistance);
	}

	// ##### MOVEMENT_SPEED #####
	public double getMoveSpeed() {
		return this.movementSpeed;
	}

	public void setMoveSpeed(double movementSpeed) {
		set(Attribute.GENERIC_MOVEMENT_SPEED, movementSpeed);
	}

	// ##### ATTACK_DAMAGE #####
	public double getAttackDamage() {
		return this.attackDamage;
	}

	public void setAttackDamage(double attackDamage) {
		set(Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);
	}

	// ##### ARMOR #####
	public double getArmor() {
		return this.armor;
	}

	public void setArmor(double armor) {
		set(Attribute.GENERIC_ARMOR, armor);
	}

	// ##### GENERAL #####
	public double get(Attribute attribute) {
		switch(attribute) {
			case GENERIC_MAX_HEALTH:
				return this.maxHealth;
			case GENERIC_FOLLOW_RANGE:
				return this.followRange;
			case GENERIC_KNOCKBACK_RESISTANCE:
				return this.knockbackResistance;
			case GENERIC_MOVEMENT_SPEED:
				return this.movementSpeed;
			case GENERIC_ATTACK_DAMAGE:
				return this.attackDamage;
			case GENERIC_ATTACK_SPEED:
				throw new UnsupportedOperationException("Not supported.");
			case GENERIC_ARMOR:
				return this.armor;
			case GENERIC_LUCK:
				throw new UnsupportedOperationException("Not supported.");
			case HORSE_JUMP_STRENGTH:
				throw new UnsupportedOperationException("Not supported.");
			case ZOMBIE_SPAWN_REINFORCEMENTS:
				throw new UnsupportedOperationException("Not supported.");
		}
		throw new IllegalArgumentException();
	}

	public void set(Attribute attribute, double value) throws IllegalArgumentException {
		switch(attribute) {
			case GENERIC_MAX_HEALTH:
				if(0 <= value && value <= 1024) {
					this.maxHealth = value;
					return;
				}
				break;
			case GENERIC_FOLLOW_RANGE:
				if(0 <= value && value <= 2048) {
					this.followRange = value;
					return;
				}
				break;
			case GENERIC_KNOCKBACK_RESISTANCE:
				if(0 <= value && value <= 1) {
					this.knockbackResistance = value;
					return;
				}
				break;
			case GENERIC_MOVEMENT_SPEED:
				if(0 <= value && value <= 1024) {
					this.movementSpeed = value;
					return;
				}
				break;
			case GENERIC_ATTACK_DAMAGE:
				if(0 <= value && value <= 2048) {
					this.attackDamage = value;
					return;
				}
				break;
			case GENERIC_ATTACK_SPEED:
				throw new UnsupportedOperationException("Not supported.");
			case GENERIC_ARMOR:
				if(0 <= value && value <= 30) {
					this.armor = value;
					return;
				}
				break;
			case GENERIC_LUCK:
				throw new UnsupportedOperationException("Not supported.");
			case HORSE_JUMP_STRENGTH:
				throw new UnsupportedOperationException("Not supported.");
			case ZOMBIE_SPAWN_REINFORCEMENTS:
				throw new UnsupportedOperationException("Not supported.");
		}
		throw new IllegalArgumentException("Value out of range.");
	}
}
