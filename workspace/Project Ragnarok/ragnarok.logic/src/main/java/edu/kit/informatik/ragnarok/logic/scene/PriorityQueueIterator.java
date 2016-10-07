package edu.kit.informatik.ragnarok.logic.scene;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * Iterator for PrioriyQueues. It differs from the regular Iterator fore it
 * enables traversing in ascending priority order. This is achieved by taking
 * advantage of the fact that a removed PriorityQueue always has the lowest
 * priority. <b>Note:</b> Since it clones a PriorityQueue it is not as
 * efficient!
 *
 * @author Angelo Aracri
 * @version 1.0
 * @param <T>
 *            the type of the aggregates
 *
 */
final class PriorityQueueIterator<T> implements Iterator<T> {
	/**
	 * The inner iterator.
	 */
	private PriorityQueue<T> queue;

	/**
	 * Creates an Iterator for a given PriorityQueue.
	 *
	 * @param queue
	 *            the PriorityQueue to iterate over
	 */
	public PriorityQueueIterator(PriorityQueue<T> queue) {

		// create own Queue in Iterator
		this.queue = new PriorityQueue<>();

		// add elements unsortedly
		Iterator<T> it = queue.iterator();
		while (it.hasNext()) {
			this.queue.add(it.next());
		}
	}

	@Override
	public boolean hasNext() {
		return this.queue.size() != 0;
	}

	@Override
	public T next() {
		if (this.queue.isEmpty()) {
			throw new NoSuchElementException("Queue is empty");
		}
		return this.queue.remove();
	}

}