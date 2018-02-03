package de.selebrator.npc.entity.metadata;

import de.selebrator.npc.MathHelper;
import de.selebrator.reflection.*;

import static de.selebrator.npc.fake.Imports.CLASS_DataWatcherObject;
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

	public static boolean getBitmaskValue(MetadataObject<Byte> bitmask, byte id) {
		return MathHelper.getBit(bitmask.get(), id);
	}

	public static void setBitmaskValue(MetadataObject<Byte> bitmask, byte id, boolean state) {
		bitmask.set(MathHelper.setBit(bitmask.get(), id, state));
	}

	public T get() {
		return this.value;
	}

	public void set(T value) {
		this.value = value;
		METHOD_DataWatcher_registerObject.invoke(this.dataWatcher, this.dataWatcherObject, this.value);
	}
}
