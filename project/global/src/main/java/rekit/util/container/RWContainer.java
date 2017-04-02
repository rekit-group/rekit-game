package rekit.util.container;

/**
 * This class represents a container or pointer, which can be used to set
 * variables in lambdas.
 * 
 * @author Dominik Fuchss
 *
 * @param <E>
 *            the element type
 */

public final class RWContainer<E> {
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
