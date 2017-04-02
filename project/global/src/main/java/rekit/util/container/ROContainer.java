package rekit.util.container;

/**
 * This class represents a container or pointer, which can be used to set
 * variables in lambdas and can only set one time to a value not equal to
 * {@code null}.
 *
 * @author Dominik Fuchss
 *
 * @param <E>
 *            the element type
 */

public final class ROContainer<E> {
	/**
	 * The element.
	 */
	private E e;

	/**
	 * Set the element.
	 *
	 * @param e
	 *            the element
	 */
	public void set(E e) {
		if (this.e != null) {
			return;
		}
		this.e = e;
	}

	/**
	 * Get the element
	 *
	 * @return the element
	 */
	public E get() {
		return this.e;
	}

	@Override
	public String toString() {
		return "" + this.e;
	}
}
