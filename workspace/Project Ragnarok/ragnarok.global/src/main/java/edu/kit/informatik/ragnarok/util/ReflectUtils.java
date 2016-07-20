package edu.kit.informatik.ragnarok.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

/**
 * This class contains several methods for using Java Reflections in a proper
 * way
 *
 * @author Dominik Fuchß
 *
 */
public final class ReflectUtils {
	/**
	 * Prevent instantiation
	 */
	private ReflectUtils() {
	}

	/**
	 * Load all implementations that shall be loaded (see {@link LoadMe}) of a
	 * class by search path (-> classpath) <br>
	 * If a class wants to be loaded, the class needs a default constructor
	 *
	 * @param searchPath
	 *            the search path (e.g. java.lang)
	 * @param type
	 *            the class
	 * @return a set of instances of the found classes
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Set<T> get(String searchPath, Class<T> type) {
		Set<Class<? extends T>> classes = new Reflections(searchPath).getSubTypesOf(type);
		Set<T> objects = new HashSet<>();
		for (Class<?> clazz : classes) {
			if (Modifier.isAbstract(clazz.getModifiers()) || clazz.getAnnotation(LoadMe.class) == null) {
				continue;
			}
			try {
				Constructor<?> c = clazz.getDeclaredConstructor();
				c.setAccessible(true);
				objects.add((T) c.newInstance());
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.err.println(clazz.getSimpleName() + " not loaded !");
			}
		}
		return objects;

	}

	/**
	 * Load all implementations of a class by search path (-> classpath) <br>
	 *
	 * @param searchPath
	 *            the search path (e.g. java.lang)
	 * @param type
	 *            the class
	 * @return a set of the found classes
	 */
	public static final <T> Set<Class<? extends T>> getClasses(String searchPath, Class<T> type) {
		return new Reflections(searchPath).getSubTypesOf(type);
	}

	/**
	 * This annotation has to be applied to Classes which shall be loaded as
	 * implementation of a specific class
	 *
	 * @author Dominik Fuchß
	 * @see ReflectUtils#get(String, Class)
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface LoadMe {
	}

}
