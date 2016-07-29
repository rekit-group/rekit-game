package edu.kit.informatik.ragnarok.visitor.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import edu.kit.informatik.ragnarok.visitor.parser.Parser;

/**
 * This annotation shall be used specify necessary parsers for this class
 *
 * @author Dominik Fuchß
 *
 */
@Retention(RUNTIME)
public @interface AdditionalParsers {

	Class<? extends Parser>[] parsers();

	Class<?>[] types();

}
