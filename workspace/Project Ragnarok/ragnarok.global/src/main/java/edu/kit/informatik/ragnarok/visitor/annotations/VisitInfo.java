package edu.kit.informatik.ragnarok.visitor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ResourceBundle;

import edu.kit.informatik.ragnarok.visitor.visitors.ResourceBundleVisitor;

/**
 * This annotation must be applied to a Class which shall be visited by a
 * {@link ResourceBundleVisitor}.
 *
 * @author Dominik Fuchß
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitInfo {
	/**
	 * Indicates whether you want a visit.
	 *
	 * @return {@code true} if visit is allowed, {@code false} otherwise
	 */
	public boolean visit();

	/**
	 * Get the path to the corresponding property file.
	 *
	 * @return the path to the property file
	 * @see ResourceBundle
	 */
	public String res();
}
