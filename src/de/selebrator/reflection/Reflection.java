package de.selebrator.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
	
	public static Class<?> getClass(ServerPackage path, String name) {
		try {
			return Class.forName(path.toString() + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static IConstructorAccessor getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		
		for(Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			if(parameterTypes == constructor.getParameterTypes()) {
				constructor.setAccessible(true);
				return new IConstructorAccessor() {
					
					@Override
					public Object newInstance(Object... parameters) {
						try {
							return constructor.newInstance(parameters);
						} catch (IllegalAccessException e) {
							throw new IllegalStateException("Cannot use reflection.", e);
						} catch (InstantiationException e) {
							throw new RuntimeException("Cannot instantiate object.", e);
						} catch (InvocationTargetException e) {
							throw new RuntimeException("An internal error occured.", e.getCause());
						} catch (IllegalArgumentException e) {
							throw e;
						}
					}
					
					@Override
					public Constructor<?> getConstructor() {
						return constructor;
					}
				};
			}
		}
		if(clazz.getSuperclass() != null) {
			return getConstructor(clazz.getSuperclass(), parameterTypes);
		}
		return null;
	}
	
	public static IMethodAccessor getMethod(Class<?> clazz, String name, Class<?>... patameterTypes) {
		
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(name)) {
				method.setAccessible(true);
				return new IMethodAccessor() {
					
					@Override
					public Object invoke(Object target, Object... args) {
						try {
							return method.invoke(target, args);
						} catch (IllegalAccessException e) {
							throw new IllegalStateException("Cannot use reflection.", e);
						} catch (InvocationTargetException e) {
							return null; // TODO how to handle ?
						} catch (IllegalArgumentException e) {
							throw e;
						}
					}
					
					@Override
					public Method getMethod() {
						return method;
					}
				};
			}
		}
		if(clazz.getSuperclass() != null) {
			return getMethod(clazz, name, patameterTypes);
		}
		return null;
	}
	
	public static IFieldAccessor getField(Class<?> clazz, String name) {
		
		try {
			Field field;
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return new IFieldAccessor() {
				
				@Override
				public void set(Object instance, Object value) {
					try {
						field.set(instance, value);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException("Cannot use reflection.", e);
					} catch (IllegalArgumentException e) {
						throw e;
					}
					
				}
				
				@Override
				public Object get(Object instance) {
					try {
						return field.get(instance);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException("Cannot use reflection.", e);
					} catch (IllegalArgumentException e) {
						throw e;
					}
				}
				
				@Override
				public Field getField() {
					return field;
				}
			};
		} catch (NoSuchFieldException e) {
			new RuntimeException("Cannot find field", e);
		} catch (SecurityException e) {
			new SecurityException("Couldnot access field");
		}
		if(clazz.getSuperclass() != null) {
			return getField(clazz, name);
		}
		return null;
	}
}
