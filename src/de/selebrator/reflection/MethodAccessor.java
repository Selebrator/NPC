package de.selebrator.reflection;

public interface MethodAccessor<T> {

	T invoke(Object instance, Object... args);
}
