package edu.kit.informatik.ragnarok.primitives.operable;

public interface Operable<T extends Operator<T>> {

	public T scalar(float scalar);

	public T multiply(T other);

	public T add(T other);

	public T sub(T other);

	/**
	 * <b>Always ! Enter the code: {@code return this;}</b>
	 *
	 * @return {@code this}
	 */
	public T get();
}
