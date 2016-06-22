package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

public class FloatParser implements Parser {
	@Override
	public boolean parse(Object obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		if (!definition.matches("(-|\\+)?[0-9]+\\.[0-9]+(f|F)")) {
			return false;
		}
		field.set(obj, Float.parseFloat(definition));
		return true;
	}
}