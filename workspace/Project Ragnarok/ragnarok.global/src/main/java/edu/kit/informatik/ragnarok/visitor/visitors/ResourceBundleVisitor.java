package edu.kit.informatik.ragnarok.visitor.visitors;

import java.util.ResourceBundle;

import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.Visitor;

/**
 * This class realizes the default {@link Visitor} which will use a
 * {@link ResourceBundle} as configured by {@link VisitInfo}
 *
 * @author Dominik Fuchß
 *
 */
public final class ResourceBundleVisitor extends Visitor {
	/**
	 * The bundle
	 */
	private ResourceBundle bundle;

	@Override
	protected boolean createSource(Visitable v) {
		VisitInfo info = v.getClass().getAnnotation(VisitInfo.class);
		if (info == null || !info.visit()) {
			System.out.println("INFO: No info defined or disabled for Object of Class " + v.getClass().getSimpleName());
			return false;
		}
		this.bundle = ResourceBundle.getBundle(info.res());
		return true;

	}

	@Override
	protected boolean createSource(Class<? extends Visitable> v) {
		VisitInfo info = v.getAnnotation(VisitInfo.class);
		if (info == null || !info.visit()) {
			System.out.println("INFO: No info defined or disabled for Class " + v.getSimpleName());
			return false;
		}
		this.bundle = ResourceBundle.getBundle(info.res());
		return true;
	}

	@Override
	protected String getValue(String key) {
		if (this.bundle == null) {
			return null;
		}
		return this.bundle.containsKey(key) ? this.bundle.getString(key) : null;
	}

}
