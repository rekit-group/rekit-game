package edu.kit.informatik.ragnarok.visitor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VisitInfo {
	public boolean visit();

	public String res();
}
