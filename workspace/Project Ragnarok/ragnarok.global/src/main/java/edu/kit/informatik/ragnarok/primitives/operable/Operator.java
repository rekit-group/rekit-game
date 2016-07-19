package edu.kit.informatik.ragnarok.primitives.operable;

/**
 *
 * @author Dominik Fuchß
 *
 * @param <T>
 */
public interface Operator<T extends Operator<T>> {
	/**
	 * <b>Always ! Enter the code: {@code return this;}</b>
	 *
	 * @return {@code this}
	 */
	Operable<T> getOperable();
}
