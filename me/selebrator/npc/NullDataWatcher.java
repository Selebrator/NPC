package me.selebrator.npc;

import org.apache.commons.lang3.ObjectUtils;

import gnu.trove.map.TIntObjectMap;
import me.selebrator.reflection.Reflection;
import net.minecraft.server.v1_8_R3.DataWatcher;

public class NullDataWatcher extends DataWatcher {
	
	private TIntObjectMap<WatchableObject> data;

	@SuppressWarnings("unchecked")
	public NullDataWatcher() {
		super(null);
		this.data = (TIntObjectMap<WatchableObject>) Reflection.getField(DataWatcher.class, "dataValues").get(this);
	}
	
	public <T> void update(int index, T object) {
		WatchableObject datawatcher_watchableobject = this.data.get(index);
		if (ObjectUtils.notEqual(object, datawatcher_watchableobject.b())) {
			datawatcher_watchableobject.a(object);
			datawatcher_watchableobject.a(true);
		}
	}

}
