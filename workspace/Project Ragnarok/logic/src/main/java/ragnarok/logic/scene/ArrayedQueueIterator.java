package ragnarok.logic.scene;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * This class represents an iterator for Arrays of Queues.
 *
 * @author Dominik Fuchss
 * @param <T>
 *            the type of the aggregates
 *
 */
public final class ArrayedQueueIterator<T> implements Iterator<T> {
	/**
	 * The internal queue.
	 */
	private Queue<T> queue = new LinkedList<>();

	/**
	 * Creates an Iterator for a given PriorityQueue.
	 *
	 * @param queues
	 *            the array of queues to iterate over
	 */
	public ArrayedQueueIterator(Queue<T>[] queues) {
		for (int i = queues.length - 1; i >= 0; i--) {
			queues[i].forEach(this.queue::add);
		}
	}

	@Override
	public boolean hasNext() {
		return this.queue.isEmpty();
	}

	@Override
	public T next() {
		if (this.queue.isEmpty()) {
			throw new NoSuchElementException();
		}
		return this.queue.remove();
	}

}