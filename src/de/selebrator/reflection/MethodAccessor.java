package de.selebrator.reflection;

@FunctionalInterface
public interface MethodAccessor<T> {

	T invoke(Object instance, Object... args);
}
