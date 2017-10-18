package de.selebrator.npc.metadata;

import de.selebrator.reflection.*;

import static de.selebrator.npc.Imports.CLASS_DataWatcherObject;
import static de.selebrator.npc.Imports.METHOD_DataWatcher_registerObject;

public class MetadataObject<T> {
	private T value;
	private Object dataWatcher;
	private Object dataWatcherObject;

	public MetadataObject(Object dataWatcher, T defaultValue, String parent, String fieldName, int fieldNumber) {
		this(dataWatcher, parent, fieldName, fieldNumber, defaultValue);
	}

	public MetadataObject(Object dataWatcher, String parent, String fieldName, int fieldNumber, T defaultValue) {
		this(
				dataWatcher,
				Reflection.getMinecraftClass(parent),
				fieldName,
				fieldNumber,
				defaultValue
		);
	}

	public MetadataObject(Object dataWatcher, Class parentClazz, String fieldName, int fieldNumber, T defaultValue) {
		this(
				dataWatcher,
				ServerPackage.getVersion().equals("v1_12_R1") ? Reflection.getField(parentClazz, fieldName) : Reflection.getField(parentClazz, CLASS_DataWatcherObject, fieldNumber),
				defaultValue
		);
	}

	public MetadataObject(Object dataWatcher, FieldAccessor field, T defaultValue) {
		this.dataWatcher = dataWatcher;
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
