package de.selebrator.reflection;

import java.lang.reflect.*;
import java.util.Arrays;

@FunctionalInterface
public interface ConstructorAccessor<T> {

	Constructor getConstructor();

	@SuppressWarnings("unchecked")
	default T newInstance(Object... parameters) {
		try {
			return (T) this.getConstructor().newInstance(parameters);
		} catch(InstantiationException e) {
			throw new RuntimeException("Cannot initiate object.", e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException("Cannot access Reflection.", e);
		} catch(InvocationTargetException e) {
			throw new RuntimeException(String.format("Cannot invoke constructor %s (%s).", this.getConstructor().getName(), Arrays.asList(this.getConstructor().getParameterTypes())));
		}
	}
}
