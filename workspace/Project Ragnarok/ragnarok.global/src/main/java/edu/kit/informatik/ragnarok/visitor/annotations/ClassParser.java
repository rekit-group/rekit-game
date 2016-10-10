package edu.kit.informatik.ragnarok.visitor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.parser.Parser;

/**
 * This annotation has to be applied to Fields of a {@link Visitable} which
 * shall be visited by a specified {@link Parser}. This will override the
 * default parser.
 *
 * @author Dominik Fuchß
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassParser {
	/**
	 * Get the parser-type.
	 *
	 * @return the parser type
	 */
	Class<? extends Parser> value();
}
