package edu.kit.informatik.ragnarok.primitives.operable;

public interface Operable<T extends Operable<T>> {

	T scalar(float scalar);

	T multiply(T other);

	T add(T other);

	T sub(T other);

	/**
	 * <b>Always ! Enter the code: {@code return this;}</b>
	 *
	 * @return {@code this}
	 */
	T get();

}
