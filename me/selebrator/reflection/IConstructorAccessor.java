package me.selebrator.reflection;

import java.lang.reflect.Constructor;

public interface IConstructorAccessor {

	public Constructor<?> getConstructor();
	
	public Object newInstance(Object... parameters);
}
