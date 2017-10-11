package de.selebrator.npc.metadata;

import de.selebrator.npc.MathHelper;
import de.selebrator.reflection.*;

public class FakeMetadata {
	private static final Class CLASS_DataWatcher = Reflection.getMinecraftClass("DataWatcher");
	private static final Class CLASS_Entity = Reflection.getMinecraftClass("Entity");
	private static final ConstructorAccessor CONSTRUCTOR_DataWatcher = Reflection.getConstructor(CLASS_DataWatcher, CLASS_Entity);
	private Object dataWatcher;

	public FakeMetadata() {
		this.dataWatcher = CONSTRUCTOR_DataWatcher.newInstance(new Object[] { null });
	}

	public static boolean getBitmaskValue(MetadataObject<Byte> bitmask, byte id) {
		return MathHelper.getBit(bitmask.get(), id);
	}

	public static void setBitmaskValue(MetadataObject<Byte> bitmask, byte id, boolean state) {
		bitmask.set(MathHelper.setBit(bitmask.get(), id, state));
	}

	public Object getDataWatcher() {
		return this.dataWatcher;
	}
}
