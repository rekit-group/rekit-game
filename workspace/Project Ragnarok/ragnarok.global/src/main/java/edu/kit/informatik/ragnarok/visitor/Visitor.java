package edu.kit.informatik.ragnarok.visitor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import edu.kit.informatik.ragnarok.util.RGBColor;
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
public class Visitor {
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

	private void visitMe(Visitable v) {
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

	private void visitMeStatic(Class<? extends Visitable> v) {
		VisitInfo info = v.getAnnotation(VisitInfo.class);
		if (info == null || !info.visit()) {
			System.err.println("WARNING: No info defined or disabled for " + v.getSimpleName());
			return;
		}
		ResourceBundle bundle = ResourceBundle.getBundle(info.res());
		for (Field field : v.getFields()) {
			this.applyStatic(field, bundle);
		}
		for (Method m : v.getMethods()) {
			this.afterStatic(m);
		}
	}

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
			System.out.println("Invoked StaticMethod " + m.getName());
		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + m.getName());
		}

	}

	private void applyStatic(Field field, ResourceBundle bundle) {
		try {
			if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())
					|| !bundle.containsKey(field.getName())) {
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
			System.out.println("Invoked ObjectMethod " + m.getName());
		} catch (Exception e) {
			System.err.println("Cannot apply to field: " + m.getName());
		}

	}

	private void applyObject(Visitable v, Field field, ResourceBundle bundle) {
		try {
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
