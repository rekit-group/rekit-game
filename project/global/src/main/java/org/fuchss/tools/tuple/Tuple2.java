package org.fuchss.tools.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple tuple of two values
 *
 * @author Dominik Fuchss
 *
 * @param <A>
 *            the first type
 * @param <B>
 *            the second type
 */
public final class Tuple2<A, B> implements Serializable {
	private static final long serialVersionUID = 8620745419750320286L;
	/**
	 * The first value.
	 */
	private final A first;
	/**
	 * The second value.
	 */
	private final B second;

	private Tuple2(final A first, final B second) {
		this.first = first;
		this.second = second;
	}

	public static <A, B> Tuple2<A, B> of(final A first, final B second) {
		return new Tuple2<>(first, second);
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

	@Override
	public int hashCode() {
		return Objects.hash(this.first, this.second);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Tuple2<?, ?> other = (Tuple2<?, ?>) obj;
		return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
	}

	@Override
	public String toString() {
		return "Tuple2(first=" + this.getFirst() + ", second=" + this.getSecond() + ")";
	}
}
