package rekit.util;

/**
 * This class defines a tuple.
 *
 * @author Dominik Fuchss
 *
 * @param <T>
 *            type1
 * @param <U>
 *            type2
 */
public final class Tuple<T, U> {
	/**
	 * First entry.
	 */
	private final T t;
	/**
	 * Second entry.
	 */
	private final U u;

	/**
	 * Create a new Tuple.
	 *
	 * @param <V>
	 *            the type of the first parameter
	 * @param <W>
	 *            the type of the second parameter
	 * @param v
	 *            the first parameter
	 * @param w
	 *            the second parameter
	 * @return the tuple
	 */
	public static final <V, W> Tuple<V, W> create(V v, W w) {
		return new Tuple<>(v, w);
	}

	/**
	 * Instantiate a new Tuple.
	 *
	 * @param t
	 *            the first entry
	 * @param u
	 *            the second entry
	 */
	private Tuple(T t, U u) {
		this.t = t;
		this.u = u;
	}

	/**
	 * Get the first entry.
	 *
	 * @return the first entry
	 */
	public T getT() {
		return this.t;
	}

	/**
	 * Get the second entry.
	 *
	 * @return the second entry
	 */
	public U getU() {
		return this.u;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.t == null) ? 0 : this.t.hashCode());
		result = prime * result + ((this.u == null) ? 0 : this.u.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Tuple other = (Tuple) obj;
		boolean equal = true;
		equal &= (this.t == null ? other.t == null : this.t.equals(other.t));
		equal &= (this.u == null ? other.u == null : this.u.equals(other.u));
		return equal;
	}

	@Override
	public String toString() {
		return "(" + this.t + ", " + this.u + ")";
	}
}
