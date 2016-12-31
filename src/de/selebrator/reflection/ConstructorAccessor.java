package de.selebrator.reflection;

public interface ConstructorAccessor<T> {

	T newInstance(Object... parameters);
}
