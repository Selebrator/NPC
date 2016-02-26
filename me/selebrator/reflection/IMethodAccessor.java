package me.selebrator.reflection;

import java.lang.reflect.Method;

public interface IMethodAccessor {

	public Method getMethod();
	
	public Object invoke(Object target, Object... args);
}
