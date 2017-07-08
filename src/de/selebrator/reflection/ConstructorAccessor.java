package de.selebrator.reflection;

@FunctionalInterface
public interface ConstructorAccessor<T> {

	T newInstance(Object... parameters);
}
