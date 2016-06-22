package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;

public interface Parser {
	default boolean parse(Object obj, Field field, String definition) throws Exception {
		if (field == null || definition == null) {
			return false;
		}
		return true;
	}
}
