package edu.kit.informatik.ragnarok.visitor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils;
import edu.kit.informatik.ragnarok.visitor.parser.FloatParser;
import edu.kit.informatik.ragnarok.visitor.parser.IntParser;
import edu.kit.informatik.ragnarok.visitor.parser.Parser;
import edu.kit.informatik.ragnarok.visitor.parser.RGBColorParser;
import edu.kit.informatik.ragnarok.visitor.parser.StringParser;

/**
 * This class supports the setting of Values and/or Attributes to Classes and
 * Objects which implements the {@link Visitable} interface
 *
 * @author Dominik Fuchß
 * @see VisitInfo
 * @see AfterVisit
 *
 */
public final class Visitor {

	/**
	 * Visit all Classes which shall be visited
	 */
	public static final void visitAllStatic() {
		Set<Class<? extends Visitable>> toVisit = ReflectUtils.getClasses("edu.kit.informatik", Visitable.class);
		Visitor visitor = Visitor.getNewVisitor();
		for (Class<? extends Visitable> v : toVisit) {
			visitor.visitMeStatic(v);
		}
	}

	/**
	 * Set values / attributes of classes (only static)
	 *
	 * @param clazz
	 *            the class
	 */
	public static final void visitStatic(Class<? extends Visitable> clazz) {
		new Visitor().visitMeStatic(clazz);
	}

	/**
	 * Set values / attributes of objects (only non-static)
	 *
	 * @param v
	 *            the object
	 */
	public static final void visit(Visitable v) {
		new Visitor().visitMe(v);
	}

	/**
	 * Get a new modifiable visitor
	 *
	 * @return the new visior
	 * @see #setParser(Class, Parser)
	 */
	public static final Visitor getNewVisitor() {
		return new Visitor();
	}

	/**
	 * Set a parser for a target type to be visited later on <br>
	 * {@code parser} == {@code null} indicates that you want to delete a parser
	 * for the target type
	 *
	 * @param target
	 *            the target type
	 * @param parser
	 *            the parser
	 */
	public synchronized void setParser(Class<?> target, Parser parser) {
		if (parser == null) {
			this.parsers.remove(target);
		}
		this.parsers.put(target, parser);
	}

	/**
	 * Prevent illegal instantiation
	 */
	private Visitor() {
	}

	/**
	 * A map of parsers for the visit
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
			this.put(String.class, new StringParser());
		}
	};

	/**
	 * Visit a visitable (only non-static)
	 *
	 * @param v
	 *            the visitable
	 */
	private synchronized void visitMe(Visitable v) {
		VisitInfo info = v.getClass().getAnnotation(VisitInfo.class);
		if (info == null || !info.visit()) {
			System.err.println("WARNING: No info defined or disabled for " + v.getClass().getSimpleName());
			return;
		}
		ResourceBundle bundle = ResourceBundle.getBundle(info.res());
		for (Field field : v.getClass().getDeclaredFields()) {
			this.applyObject(v, field, bundle);
		}
		for (Method m : v.getClass().getDeclaredMethods()) {
			this.afterObject(v, m);
		}
	}

	/**
	 * Visit a visitable (only static)
	 *
	 * @param v
	 *            the visitable
	 */
	private synchronized void visitMeStatic(Class<? extends Visitable> v) {
		VisitInfo info = v.getAnnotation(VisitInfo.class);
		if (info == null || !info.visit()) {
			System.err.println("WARNING: No info defined or disabled for " + v.getSimpleName());
			return;
		}
		ResourceBundle bundle = ResourceBundle.getBundle(info.res());
		for (Field field : v.getDeclaredFields()) {
			this.applyStatic(field, bundle);
		}
		for (Method m : v.getDeclaredMethods()) {
			this.afterStatic(m);
		}
	}

	/**
	 * Invoke method of a visitable (only static)
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
			System.err.println("Cannot apply to field: " + m.getName());
		}

	}

	/**
	 * Apply a value to a field (only static)
	 *
	 * @param field
	 *            the field
	 * @param bundle
	 *            the bundle
	 */
	private void applyStatic(Field field, ResourceBundle bundle) {
		try {
			if (field.getAnnotation(NoVisit.class) != null) {
				return;
			}
			if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || !bundle.containsKey(field.getName())) {
				System.err.println("WARNING: Field " + field.getName() + " is not static or final or has no definition");
				return;
			}
			field.setAccessible(true);
			Parser parser = this.parsers.get(field.getType());
			if (parser == null) {
				return;
			}
			parser.parse(null, field, bundle.getString(field.getName()));

		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + field.getName());
		}
	}

	/**
	 * Invoke method of a visitable (only non-static)
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
			System.err.println("Cannot apply to field: " + m.getName());
		}

	}

	/**
	 * Apply a value to a field (only non-static)
	 *
	 * @param v
	 *            the visitable
	 * @param field
	 *            the field
	 * @param bundle
	 *            the bundle
	 */
	private void applyObject(Visitable v, Field field, ResourceBundle bundle) {
		try {
			if (field.getAnnotation(NoVisit.class) != null) {
				return;
			}
			if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || !bundle.containsKey(field.getName())) {
				System.err.println("WARNING: Field " + field.getName() + " is static or final or has no definition");
				return;
			}
			field.setAccessible(true);
			Parser parser = this.parsers.get(field.getType());
			if (parser == null) {
				return;
			}
			parser.parse(v, field, bundle.getString(field.getName()));

		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + field.getName());
		}

	}

}
