package de.selebrator.npc.metadata;

import de.selebrator.npc.MathHelper;

import static de.selebrator.npc.Imports.CONSTRUCTOR_DataWatcher;

public class FakeMetadata {
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
