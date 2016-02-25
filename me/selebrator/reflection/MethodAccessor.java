package me.selebrator.reflection;

import java.lang.reflect.Method;

public interface MethodAccessor {

	public Method getMethod();
	
	public Object invoke(Object target, Object... args);
}
