package edu.kit.informatik.ragnarok.visitor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.visitor.annotations.AdditionalParsers;
import edu.kit.informatik.ragnarok.visitor.annotations.AfterVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.NoVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.parser.FloatParser;
import edu.kit.informatik.ragnarok.visitor.parser.IntParser;
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
		this.addParsers(v.getClass());
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
		this.addParsers(v);
		for (Field field : v.getDeclaredFields()) {
			this.applyStatic(field);
		}
		for (Method m : v.getDeclaredMethods()) {
			this.afterStatic(m);
		}
	}

	/**
	 * This method will add more parsers if necessary.
	 *
	 * @param clazz
	 *            the class (of the object) which shall be visited
	 */
	private void addParsers(Class<? extends Visitable> clazz) {
		AdditionalParsers additionalParsers = null;
		if ((additionalParsers = clazz.getAnnotation(AdditionalParsers.class)) == null) {
			return;
		}

		Class<? extends Parser>[] aParser = additionalParsers.parsers();
		Class<?>[] types = additionalParsers.types();
		if (types.length != aParser.length) {
			System.err.println("Error in conf for additional parsers for " + clazz.getSimpleName());
			return;
		}
		try {
			for (int i = 0; i < aParser.length; i++) {
				if (this.parsers.containsKey(types[i]) && this.parsers.get(types[i]).getClass() != aParser[i]) {
					System.err.println("WARNING: Multiple parsers defined for class " + types[i]
							+ "no parsers can be added for security reasons. Please check your config!");
					continue;
				}
				this.parsers.put(types[i], aParser[i].getDeclaredConstructor().newInstance());
			}
		} catch (Exception e) {
			System.err.println("Error while loading additional parsers for " + clazz.getSimpleName());
			return;
		}
		System.out.println("INFO: Successfully loaded parsers for " + clazz.getSimpleName());
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
			Parser parser = this.parsers.get(field.getType());
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
			Parser parser = this.parsers.get(field.getType());
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
