// Generated by delombok at Sun Dec 02 13:03:44 UTC 2018
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
 */
public final class Tuple3<A, B, C> implements Serializable {
	private static final long serialVersionUID = 3512228977823082170L;
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
	 * Create a {@link Tuple3} by {@link Tuple2}
	 *
	 * @param <X>
	 *            the first type
	 * @param <Y>
	 *            the second type
	 * @param <Z>
	 *            the third type
	 *
	 * @param wxy
	 *            the first two values
	 * @param z
	 *            the third value
	 * @return the tuple
	 */
	static <X, Y, Z> Tuple3<X, Y, Z> of(Tuple2<X, Y> xy, Z z) {
		return Tuple3.of(xy.getFirst(), xy.getSecond(), z);
	}

	private Tuple3(final A first, final B second, final C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public static <A, B, C> Tuple3<A, B, C> of(final A first, final B second, final C third) {
		return new Tuple3<>(first, second, third);
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

	@Override
	public int hashCode() {
		return Objects.hash(this.first, this.second, this.third);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Tuple3<?, ?, ?> other = (Tuple3<?, ?, ?>) obj;
		return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second) && Objects.equals(this.third, other.third);
	}

	@Override
	public String toString() {
		return "Tuple3(first=" + this.getFirst() + ", second=" + this.getSecond() + ", third=" + this.getThird() + ")";
	}
}
