package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 * This {@link Parser} is able to parse options from a config<br>
 * Syntax: name1::optionValue1;name2::optionValue2
 *
 * @author Dominik Fuchß
 *
 */
public abstract class OptionParser implements Parser {

	protected Map<String, String> mapping = new HashMap<>();
	private static final String REGEX = "((\\w|\\d|_)+::(\\w|\\d|\\+|-|\\.|,)+;)*(\\w|\\d|_)+::(\\w|\\d|\\+|-|\\.|,)+";

	@Override
	public final boolean parse(Visitable obj, Field field, String definition) throws Exception {
		// reset mapping
		this.mapping.clear();
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		if (!definition.matches(OptionParser.REGEX)) {
			return false;
		}
		Pattern p = Pattern.compile("::");
		for (String kv : definition.split(";")) {
			String[] split = p.split(kv);
			if (this.mapping.put(split[0], split[1]) != null) {
				System.err.println("WARING: Double definition for " + split[0]);
			}
		}
		this.apply(obj, field);
		return true;
	}

	/**
	 * This method will be invoked by {@link #parse(Visitable, Field, String)}
	 * and shall set the field
	 *
	 * @param obj
	 *            the Visitable Object or {@code null} if static visit (class
	 *            visit)
	 * @param field
	 *            the current field
	 * @throws Exception
	 *             will thrown by Reflect stuff
	 *
	 */
	protected abstract void apply(Visitable obj, Field field) throws Exception;

}
