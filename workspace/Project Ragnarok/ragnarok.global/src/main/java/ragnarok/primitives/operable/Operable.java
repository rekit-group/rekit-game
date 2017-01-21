package ragnarok.primitives.operable;

/**
 * This interface defines the kind of Objects a {@link OpProgress} will handle.
 * <br>
 * <b>USAGE: class XYZ implements Operable&lt;XYZ&gt;</b>
 *
 * @param <T>
 *            the class itself !
 */
public interface Operable<T extends Operable<T>> {
	/**
	 * Apply scalar.
	 *
	 * @param scalar
	 *            the scalar
	 * @return the object itself
	 */
	T scalar(float scalar);

	/**
	 * Apply multiplicand.
	 *
	 * @param other
	 *            the other
	 * @return the object itself
	 */
	T multiply(T other);

	/**
	 * Apply summand.
	 *
	 * @param other
	 *            the other
	 * @return the object itself
	 */
	T add(T other);

	/**
	 * Apply subtrahend.
	 *
	 * @param other
	 *            the other
	 * @return the object itself
	 */
	T sub(T other);

	/**
	 * <b>Always ! Enter the code: {@code return this;}</b>
	 *
	 * @return {@code this}
	 */
	T get();

}
