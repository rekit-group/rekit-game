package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 * This {@link Parser} is used for parsing {@link String Strings}
 *
 * @author Dominik Fuchß
 *
 */
public final class StringParser implements Parser {
	@Override
	public boolean parse(Visitable obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		field.set(obj, definition);
		return true;
	}
}