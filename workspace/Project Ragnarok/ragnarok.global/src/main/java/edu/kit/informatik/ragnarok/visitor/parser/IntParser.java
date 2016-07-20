package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 * This {@link Parser} is used for parsing {@link Integer Integers}
 *
 * @author Dominik Fuchß
 *
 */
public final class IntParser implements Parser {
	@Override
	public boolean parse(Visitable obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		if (!definition.matches("(-|\\+)?[0-9]+")) {
			return false;
		}
		field.set(obj, Integer.parseInt(definition));
		return true;
	}
}
