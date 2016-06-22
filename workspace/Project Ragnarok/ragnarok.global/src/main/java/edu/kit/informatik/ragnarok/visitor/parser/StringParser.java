package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

public class StringParser implements Parser {
	@Override
	public boolean parse(Object obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		field.set(obj, definition);
		return true;
	}
}