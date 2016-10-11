package edu.kit.informatik.ragnarok.visitor.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import edu.kit.informatik.ragnarok.visitor.parser.Parser;

/**
 * This annotation shall be used specify necessary parsers for this class
 * {@link #parsers()} and {@link #types()} will be used to establish a mapping
 * 
 * @author Dominik Fuchß
 *
 */
@Retention(RUNTIME)
public @interface AdditionalParsers {
	/**
	 * Get the defined parsers
	 * 
	 * @return the parsers
	 */
	Class<? extends Parser>[] parsers();

	/**
	 * Get the defined types, the {@link #parsers()} shall be parse
	 * 
	 * @return the types
	 */
	Class<?>[] types();

}
