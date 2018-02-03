package de.selebrator.npc.entity.metadata;

import de.selebrator.reflection.*;

import static de.selebrator.npc.fake.Imports.METHOD_DataWatcher_registerObject;

public class MetadataObject<T> {
	private T value;
	private Object dataWatcher;
	private Object dataWatcherObject;

	public MetadataObject(Object dataWatcher, FieldAccessor field, T defaultValue) {
		this.dataWatcher = dataWatcher;
		this.dataWatcherObject = field.get(null);
		this.set(defaultValue);
	}

	public static boolean getBitmaskValue(byte bitmask, int target) {
		return (bitmask & target) != 0;
	}

	public static byte setBitmaskValue(byte bitmask, int target, boolean state) {
		return (byte) (state ? bitmask | target : bitmask & ~target);
	}

	public T get() {
		return this.value;
	}

	public void set(T value) {
		this.value = value;
		METHOD_DataWatcher_registerObject.invoke(this.dataWatcher, this.dataWatcherObject, this.value);
	}

	public interface StatusFlag {
		int BURNING = 0x01;
		int SNEAKING = 0x02;
		int SPRINTING = 0x08;
		int INVISIBLE = 0x20;
		int GLOWING = 0x40;
		int GLIDING = 0x80;
	}

	public interface ArrowFlag {
		int CRITICAL = 0x01;
	}

	public interface LivingFlag {
		int HAND_ACTIVE = 0x01;
		int ACTIVE_HAND = 0x02;
	}

	public interface SkinFlag {
		int CAPE = 0x01;
		int JACKET = 0x02;
		int LEFT_SLEEVE = 0x04;
		int RIGHT_SLEEVE = 0x08;
		int LEFT_PANTS = 0x10;
		int RIGHT_PANTS = 0x20;
		int HAT = 0x40;
	}

	public interface ArmorStandFlag {
		int SMALL = 0x01;
		int ARMS = 0x04;
		int NO_BASE_PLATE = 0x08;
		int MARKER = 0x10;
	}

	public interface InsentientFlag {
		int NO_AI = 0x01;
		int LEFT_HANDED = 0x02;
	}

	public interface BatFlag {
		int HANGING = 0x01;
	}

	public interface AbstractHorseFlag {
		int TAMED = 0x02;
		int SADDLED = 0x04;
		int BRED = 0x08;
		int EATING = 0x10;
		int REARING = 0x20;
		int MOUTH_OPEN = 0x40;
	}

	public interface WoolFlag {
		int COLOR = 0x0F;
		int SHEARED = 0x10;
	}

	public interface TameableAnimalFlag {
		int SITTING = 0x01;
		int ANGRY = 0x02;
		int TAMED = 0x04;
	}

	public interface IronGolemFlag {
		int PLAYER_CREATED = 0x01;
	}

	public interface SnowmanFlag {
		int NO_PUMPKIN_HEAD = 0x10;
	}

	public interface BlazeFlag {
		int ON_FIRE = 0x01;
	}

	public interface VexFlag {
		int ATTACKING = 0x01;
	}

	public interface VindicationIllagerFlag {
		int HAS_TARGET = 0x01;
	}

	public interface SpiderFlag {
		int CLIMBING = 0x01;
	}
}
