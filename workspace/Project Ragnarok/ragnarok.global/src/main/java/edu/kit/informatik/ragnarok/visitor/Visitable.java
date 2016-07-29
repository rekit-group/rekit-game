package edu.kit.informatik.ragnarok.visitor;

import edu.kit.informatik.ragnarok.visitor.visitors.MapVisitor;
import edu.kit.informatik.ragnarok.visitor.visitors.ResourceBundleVisitor;

/**
 * This interface has to be implemented by classes which shall be visited by a
 * {@link Visitor}
 *
 * @author Dominik Fuchß
 * @see VisitInfo
 * @see AfterVisit
 * @see NoVisit
 * @see ResourceBundleVisitor
 * @see MapVisitor
 *
 */
public interface Visitable {
}
