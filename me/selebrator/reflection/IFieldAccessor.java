package me.selebrator.reflection;

import java.lang.reflect.Field;

public interface IFieldAccessor {
	
	public Field getField();
	
	public Object get(Object instance);
	
	public void set(Object instance, Object value);
}
