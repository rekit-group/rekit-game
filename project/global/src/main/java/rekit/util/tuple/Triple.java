package rekit.util.tuple;

/**
 * This class defines a triple.
 *
 * @author Dominik Fuchss
 *
 * @param <T>
 *            type1
 * @param <U>
 *            type2
 * @param <V>
 *            type3
 */
public final class Triple<T, U, V> {
	/**
	 * First entry.
	 */
	private final T t;
	/**
	 * Second entry.
	 */
	private final U u;
	/**
	 * Third entry.
	 */
	private final V v;

	/**
	 * Create a new Triple.
	 *
	 * @param <X>
	 *            the type of the first parameter
	 * @param <Y>
	 *            the type of the second parameter
	 * @param <Z>
	 *            the type of the third parameter
	 *
	 * @param x
	 *            the first parameter
	 * @param y
	 *            the second parameter
	 * @param z
	 *            the third parameter
	 * @return the triple
	 */
	public static <X, Y, Z> Triple<X, Y, Z> create(X x, Y y, Z z) {
		return new Triple<>(x, y, z);
	}

	/**
	 * Instantiate a new Triple.
	 *
	 * @param t
	 *            the first entry
	 * @param u
	 *            the second entry
	 * @param v
	 *            the third entry
	 */
	private Triple(T t, U u, V v) {
		this.t = t;
		this.u = u;
		this.v = v;
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

	/**
	 * Get the third entry.
	 *
	 * @return the third entry
	 */
	public V getV() {
		return this.v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.t == null) ? 0 : this.t.hashCode());
		result = prime * result + ((this.u == null) ? 0 : this.u.hashCode());
		result = prime * result + ((this.v == null) ? 0 : this.v.hashCode());
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
		Triple other = (Triple) obj;
		boolean equal = true;
		equal &= (this.t == null ? other.t == null : this.t.equals(other.t));
		equal &= (this.u == null ? other.u == null : this.u.equals(other.u));
		equal &= (this.v == null ? other.v == null : this.v.equals(other.v));
		return equal;
	}

	@Override
	public String toString() {
		return "(" + this.t + ", " + this.u + ", " + this.v + ")";
	}
}
