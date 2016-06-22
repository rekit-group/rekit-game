package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

public class IntParser implements Parser {
	@Override
	public boolean parse(Object obj, Field field, String definition) throws Exception {
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
