package edu.kit.informatik.ragnarok.visitor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.Visitor;

/**
 * This annotation has to be applied to Methods which shall be executed after a
 * visit of a {@link Visitor} to a {@link Visitable}
 *
 * @author Dominik Fuchß
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterVisit {

}
