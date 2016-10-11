package edu.kit.informatik.ragnarok.visitor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.visitor.annotations.AfterVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.ClassParser;
import edu.kit.informatik.ragnarok.visitor.annotations.NoVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.SetParser;
import edu.kit.informatik.ragnarok.visitor.annotations.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.parser.FloatParser;
import edu.kit.informatik.ragnarok.visitor.parser.IntParser;
import edu.kit.informatik.ragnarok.visitor.parser.LongParser;
import edu.kit.informatik.ragnarok.visitor.parser.Parser;
import edu.kit.informatik.ragnarok.visitor.parser.RGBColorParser;
import edu.kit.informatik.ragnarok.visitor.parser.StringParser;
import edu.kit.informatik.ragnarok.visitor.parser.VecParser;
import edu.kit.informatik.ragnarok.visitor.visitors.ResourceBundleVisitor;

/**
 * This class supports the setting of Values and/or Attributes to Classes and
 * Objects which implements the {@link Visitable} interface.
 *
 * @author Dominik Fuchß
 * @see VisitInfo
 * @see AfterVisit
 *
 */
public abstract class Visitor {

	/**
	 * Get a new modifiable visitor (default visitor:
	 * {@link ResourceBundleVisitor}).
	 *
	 * @return the new visior
	 * @see #setParser(Class, Parser)
	 */
	public static final Visitor getNewVisitor() {
		return new ResourceBundleVisitor();
	}

	/**
	 * Prevent illegal instantiation.
	 */
	protected Visitor() {
	}

	/**
	 * A map of parsers for the visit.
	 */
	private final Map<Class<?>, Parser> parsers = new HashMap<Class<?>, Parser>() {
		/**
		 * SUID
		 */
		private static final long serialVersionUID = -1233333524870450644L;

		{
			this.put(Integer.class, new IntParser());
			this.put(Integer.TYPE, new IntParser());
			this.put(Float.class, new FloatParser());
			this.put(Float.TYPE, new FloatParser());
			this.put(Long.class, new LongParser());
			this.put(Long.TYPE, new LongParser());

			this.put(RGBColor.class, new RGBColorParser());
			this.put(Vec.class, new VecParser());
			this.put(String.class, new StringParser());
		}
	};

	/**
	 * This method will be invoked before visiting by {@link #visit(Visitable)}.
	 *
	 * @param v
	 *            the visitable
	 * @return {@code true} if the source for KV-Mapping established,
	 *         {@code false} if failed
	 */
	protected abstract boolean createSource(Visitable v);

	/**
	 * This method will be invoked before visiting by
	 * {@link #visitStatic(Class)}.
	 *
	 * @param v
	 *            the visitable
	 * @return {@code true} if the source for KV-Mapping established,
	 *         {@code false} if failed
	 */
	protected abstract boolean createSource(Class<? extends Visitable> v);

	/**
	 * Get value by key.
	 *
	 * @param key
	 *            the key ({@link Field#getName()})
	 * @return {@code null} if no value found, the value otherwise
	 */
	protected abstract String getValue(String key);

	/**
	 * Visit a visitable (only non-static).
	 *
	 * @param v
	 *            the visitable
	 */
	public final synchronized void visit(Visitable v) {
		System.out.println("INFO: Visit object of class " + v.getClass().getSimpleName());
		if (!this.createSource(v)) {
			return;
		}
		for (Field field : v.getClass().getDeclaredFields()) {
			this.applyObject(v, field);
		}
		for (Method m : v.getClass().getDeclaredMethods()) {
			this.afterObject(v, m);
		}
	}

	/**
	 * Visit a visitable (only static).
	 *
	 * @param v
	 *            the visitable
	 */
	public final synchronized void visit(Class<? extends Visitable> v) {
		System.out.println("INFO: Visit class " + v.getSimpleName());
		if (!this.createSource(v)) {
			return;
		}
		for (Field field : v.getDeclaredFields()) {
			this.applyStatic(field);
		}
		for (Method m : v.getDeclaredMethods()) {
			this.afterStatic(m);
		}
	}

	/**
	 * Get the specified parser for a field.
	 *
	 * @param field
	 *            the field
	 * @return the parser or {@code null} if none suitable found
	 * @throws SecurityException
	 *             reflect stuff
	 * @throws NoSuchMethodException
	 *             reflect stuff
	 * @throws InvocationTargetException
	 *             reflect stuff
	 * @throws IllegalArgumentException
	 *             reflect stuff
	 * @throws IllegalAccessException
	 *             reflect stuff
	 * @throws InstantiationException
	 *             reflect stuff
	 */
	private Parser getParser(Field field) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		// First the field specific.
		SetParser manual = field.getDeclaredAnnotation(SetParser.class);
		if (manual != null) {
			return manual.value().getDeclaredConstructor().newInstance();
		}
		// Then the class specific.
		ClassParser clazzParser = field.getType().getDeclaredAnnotation(ClassParser.class);
		if (clazzParser != null) {
			return clazzParser.value().getDeclaredConstructor().newInstance();
		}
		// Then the default.
		return this.parsers.get(field.getType());
	}

	/**
	 * Invoke method of a visitable (only static).
	 *
	 * @param m
	 *            the method
	 */
	private void afterStatic(Method m) {
		try {
			if (!Modifier.isStatic(m.getModifiers())) {
				return;
			}
			AfterVisit afterVisit = m.getAnnotation(AfterVisit.class);
			if (afterVisit == null) {
				return;
			}
			m.setAccessible(true);
			m.invoke(null);
		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + m.getName() + " because " + e.getMessage());
		}

	}

	/**
	 * Invoke method of a visitable (only non-static).
	 *
	 * @param v
	 *            the visitable
	 * @param m
	 *            the method
	 */
	private void afterObject(Visitable v, Method m) {
		try {
			if (Modifier.isStatic(m.getModifiers())) {
				return;
			}
			AfterVisit afterVisit = m.getDeclaredAnnotation(AfterVisit.class);
			if (afterVisit == null) {
				return;
			}
			m.setAccessible(true);
			m.invoke(v);
		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + m.getName() + " because " + e.getMessage());
		}

	}

	/**
	 * Apply a value to a field (only static).
	 *
	 * @param field
	 *            the field
	 */
	private void applyStatic(Field field) {
		try {
			if (field.getAnnotation(NoVisit.class) != null) {
				return;
			}
			String val = null;
			int mod = field.getModifiers();
			if (!Modifier.isStatic(mod) || Modifier.isFinal(mod) || (val = this.getValue(field.getName())) == null) {
				System.out.println("WARNING: Field " + field.getName() + " is not static or final or has no definition");
				return;
			}
			field.setAccessible(true);
			Parser parser = this.getParser(field);
			if (parser == null) {
				return;
			}
			if (!parser.parse(null, field, val)) {
				System.err.println("Syntax-Error: Parser rejected content for " + field.getName());
			}

		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + field.getName() + " because " + e.getMessage());
		}
	}

	/**
	 * Apply a value to a field (only non-static).
	 *
	 * @param v
	 *            the visitable
	 * @param field
	 *            the field
	 *
	 */
	private void applyObject(Visitable v, Field field) {
		try {
			if (field.getAnnotation(NoVisit.class) != null) {
				return;
			}
			String val = null;
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod) || (val = this.getValue(field.getName())) == null) {
				System.out.println("WARNING: Field " + field.getName() + " is static or final or has no definition");
				return;
			}
			field.setAccessible(true);
			Parser parser = this.getParser(field);
			if (parser == null) {
				return;
			}
			if (!parser.parse(v, field, val)) {
				System.err.println("Syntax-Error: Parser rejected content for " + field.getName());
			}

		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + field.getName() + " because " + e.getMessage());
		}

	}

}
