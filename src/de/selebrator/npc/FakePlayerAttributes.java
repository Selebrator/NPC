package de.selebrator.npc;

public class FakePlayerAttributes {
	private double maxHealth = Attribute.GENERIC_MAX_HEALTH.defaultValue;
	private double followRange = Attribute.GENERIC_FOLLOW_RANGE.defaultValue;
	private double knockbackResistance = Attribute.GENERIC_KNOCKBACK_RESISTANCE.defaultValue;
	private double movementSpeed = Attribute.GENERIC_MOVEMENT_SPEED.defaultValue;
	private double attackDamage = Attribute.GENERIC_ATTACK_DAMAGE.defaultValue;
	private double armor = Attribute.GENERIC_ARMOR.defaultValue;

	// ##### MAX_HEALTH #####
	public double getMaxHealth() {
		return this.maxHealth;
	}

	public void setMaxHealth(double maxHealth) {
		if(maxHealth > Attribute.GENERIC_MAX_HEALTH.min && maxHealth < Attribute.GENERIC_MAX_HEALTH.max) {
			this.maxHealth = maxHealth;
		} else {
			throw new IllegalArgumentException("Value out of range.");
		}
	}

	// ##### FOLLOW_RANGE #####
	public double getFollowRange() {
		return this.followRange;
	}

	public void setFollowRange(double followRange) {
		if(followRange > Attribute.GENERIC_FOLLOW_RANGE.min && followRange < Attribute.GENERIC_FOLLOW_RANGE.max) {
			this.followRange = followRange;
		} else {
			throw new IllegalArgumentException("Value out of range.");
		}
	}

	// ##### KNOCKBACK_RESISTANCE #####


	public double getKnockbackResistance() {
		return this.knockbackResistance;
	}

	public void setKnockbackResistance(double knockbackResistance) {
		if(knockbackResistance > Attribute.GENERIC_KNOCKBACK_RESISTANCE.min && knockbackResistance < Attribute.GENERIC_KNOCKBACK_RESISTANCE.max) {
			this.knockbackResistance = knockbackResistance;
		} else {
			throw new IllegalArgumentException("Value out of range.");
		}
	}

	// ##### MOVEMENT_SPEED #####
	public double getMoveSpeed() {
		return this.movementSpeed;
	}

	public void setMoveSpeed(double movementSpeed) {
		if(movementSpeed > Attribute.GENERIC_MOVEMENT_SPEED.min && movementSpeed < Attribute.GENERIC_MOVEMENT_SPEED.max) {
			this.movementSpeed = movementSpeed;
		} else {
			throw new IllegalArgumentException("Value out of range.");
		}
	}

	// ##### ATTACK_DAMAGE #####
	public double getAttackDamage() {
		return this.attackDamage;
	}

	public void setAttackDamage(double attackDamage) {
		if(attackDamage > Attribute.GENERIC_ATTACK_DAMAGE.min && attackDamage < Attribute.GENERIC_ATTACK_DAMAGE.max) {
			this.attackDamage = attackDamage;
		} else {
			throw new IllegalArgumentException("Value out of range.");
		}
	}

	// ##### ARMOR #####
	public double getArmor() {
		return this.armor;
	}

	public void setArmor(double armor) {
		if(armor > Attribute.GENERIC_ARMOR.min && armor < Attribute.GENERIC_ARMOR.max) {
			this.armor = armor;
		} else {
			throw new IllegalArgumentException("Value out of range.");
		}
	}

	public enum Attribute {
		GENERIC_MAX_HEALTH(20, 0, 1024),
		GENERIC_FOLLOW_RANGE(32, 0, 2048),
		GENERIC_KNOCKBACK_RESISTANCE(0, 0, 1),
		GENERIC_MOVEMENT_SPEED(EnumMoveSpeed.WALKING.getSpeed(), 0, 1024),
		GENERIC_ATTACK_DAMAGE(1, 0, 2048),
		GENERIC_ARMOR(0, 0, 30);

		private double defaultValue;
		private double min;
		private double max;

		Attribute(double defaultValue, double min, double max) {
			this.defaultValue = defaultValue;
			this.min = min;
			this.max = max;
		}
	}
}
