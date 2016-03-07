package de.selebrator.npc;

import net.minecraft.server.v1_9_R1.DataWatcher;
import de.selebrator.reflection.IMethodAccessor;
import de.selebrator.reflection.Reflection;
import de.selebrator.reflection.ServerPackage;

public class NullDataWatcher {
	
	private final DataWatcher dataWatcher;
	
	private static final Class<?> CLASS_DataWatcher = Reflection.getClass(ServerPackage.NMS, "DataWatcher");
	private static final Class<?> CLASS_DataWatcherObject = Reflection.getClass(ServerPackage.NMS, "DataWatcherObject");
	
	private static final IMethodAccessor METHOD_DataWatcher_register = Reflection.getMethod(CLASS_DataWatcher, "registerObject", CLASS_DataWatcherObject, Object.class);
	
	public NullDataWatcher() {
		this.dataWatcher = new DataWatcher(null);
	}
	
	public NullDataWatcher set(EnumDataWatcherObject dataWatcherObject, Object value) {
		METHOD_DataWatcher_register.invoke(this.dataWatcher, dataWatcherObject.getClazz(), value);
		return this;
	}
	
	public DataWatcher toNMS() {
		return this.dataWatcher;
	}	
}
