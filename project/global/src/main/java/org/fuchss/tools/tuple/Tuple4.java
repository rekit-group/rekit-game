package org.fuchss.tools.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple tuple of three values
 *
 * @author Dominik Fuchss
 *
 * @param <A>
 *            the first type
 * @param <B>
 *            the second type
 * @param <C>
 *            the third type
 * @param <D>
 *            the fourth type
 */
public final class Tuple4<A, B, C, D> implements Serializable {
	private static final long serialVersionUID = -5255702882082756985L;
	/**
	 * The first value.
	 */
	private final A first;
	/**
	 * The second value.
	 */
	private final B second;
	/**
	 * The third value.
	 */
	private final C third;
	/**
	 * The fourth value.
	 */
	private final D fourth;

	/**
	 * Create a {@link Tuple4} by {@link Tuple3}
	 *
	 * @param <W>
	 *            the first type
	 * @param <X>
	 *            the second type
	 * @param <Y>
	 *            the third type
	 * @param <Z>
	 *            the fourth type
	 *
	 * @param wxy
	 *            the first three values
	 * @param z
	 *            the fourth value
	 * @return the tuple
	 */
	public static <W, X, Y, Z> Tuple4<W, X, Y, Z> of(Tuple3<W, X, Y> wxy, Z z) {
		return Tuple4.of(wxy.getFirst(), wxy.getSecond(), wxy.getThird(), z);
	}

	private Tuple4(final A first, final B second, final C third, final D fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final A first, final B second, final C third, final D fourth) {
		return new Tuple4<>(first, second, third, fourth);
	}

	/**
	 * The first value.
	 *
	 * @return the first value
	 */

	public A getFirst() {
		return this.first;
	}

	/**
	 * The second value.
	 *
	 * @return the second value
	 */
	public B getSecond() {
		return this.second;
	}

	/**
	 * The third value.
	 *
	 * @return the third value
	 */
	public C getThird() {
		return this.third;
	}

	/**
	 * The fourth value.
	 *
	 * @return the fourth value
	 */
	public D getFourth() {
		return this.fourth;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.first, this.fourth, this.second, this.third);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
		return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second) && Objects.equals(this.third, other.third) && Objects.equals(this.fourth, other.fourth);
	}

	@Override
	public String toString() {
		return "Tuple4(first=" + this.getFirst() + ", second=" + this.getSecond() + ", third=" + this.getThird() + ", fourth=" + this.getFourth() + ")";
	}
}
