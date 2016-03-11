package de.selebrator.npc;

import de.selebrator.reflection.IMethodAccessor;
import de.selebrator.reflection.Reflection;
import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;

public class NullDataWatcher {
	
	private final DataWatcher dataWatcher;
	
	private static final IMethodAccessor METHOD_DataWatcher_registerObject = Reflection.getMethod(DataWatcher.class, "registerObject", DataWatcherObject.class, Object.class);

	public NullDataWatcher() {
		this.dataWatcher = new DataWatcher(null);
	}
	
	public NullDataWatcher set(EnumDataWatcherObject dataWatcherObject, Object value) {
		METHOD_DataWatcher_registerObject.invoke(this.dataWatcher, dataWatcherObject.getObject(), value);
		return this;
	}
	
	public DataWatcher toNMS() {
		return this.dataWatcher;
	}	
}
