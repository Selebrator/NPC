package de.selebrator.npc.metadata;

import de.selebrator.reflection.*;

public class MetadataObject<T> {
	private static final Class CLASS_DataWatcher = Reflection.getMinecraftClass("DataWatcher");
	private static final Class<?> CLASS_DataWatcherObject = Reflection.getMinecraftClass("DataWatcherObject");
	private static final MethodAccessor METHOD_DataWatcher_registerObject = Reflection.getMethod(CLASS_DataWatcher, null, "registerObject", CLASS_DataWatcherObject, Object.class);

	private T value;
	private Object dataWatcher;
	private Object dataWatcherObject;

	MetadataObject(Object dataWatcher, T defaultValue, String parent, String fieldName, int fieldNumber) {
		this.dataWatcher = dataWatcher;
		Class<?> parentClazz = Reflection.getMinecraftClass(parent);
		FieldAccessor field;
		if(ServerPackage.getVersion().equals("v1_12_R1"))
			field = Reflection.getField(parentClazz, fieldName);
		else
			field = Reflection.getField(parentClazz, CLASS_DataWatcherObject, fieldNumber);
		this.dataWatcherObject = field.get(null);
		this.set(defaultValue);
	}

	public T get() {
		return this.value;
	}

	public void set(T value) {
		this.value = value;
		METHOD_DataWatcher_registerObject.invoke(this.dataWatcher, this.dataWatcherObject, this.value);
	}
}
