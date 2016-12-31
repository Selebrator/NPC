package de.selebrator.reflection;

public interface FieldAccessor<T> {

	T get(Object instance);

	void set(Object instance, T value);
}
