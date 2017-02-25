package ragnarok.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import ragnarok.config.GameConf;
import ragnarok.util.InputHelper;
import ragnarok.util.ThreadUtils;

/**
 * This class establishes a Listener to Components of the Shell<br>
 * Also {@link Observer Observers} can register to receive events.
 *
 * @author Dominik Fuchss
 *
 */
final class InputHelperImpl implements InputHelper {

	/**
	 * Instantiate the InputHelper.
	 */
	public InputHelperImpl() {
		ThreadUtils.runDaemon("InputHelper", this::run);
	}

	/**
	 * The notifying method.
	 */
	private void run() {
		while (true) {
			this.notifyObservers();
			ThreadUtils.sleep(GameConf.LOGIC_DELTA);
		}
	}

	/**
	 * List of all observers that registered to key changes.
	 */
	private List<Observer> observers = new ArrayList<>();

	/**
	 * List of all keyCodes that are currently pressed.
	 */
	private ConcurrentSkipListSet<Integer> pressedKeys = new ConcurrentSkipListSet<>();

	/**
	 * List of all keyCodes that have just been released.
	 */
	private ConcurrentSkipListSet<Integer> releasedKeys = new ConcurrentSkipListSet<>();

	/**
	 * Adds a pressed keys keyCode to the List and notifies observers.
	 *
	 * @param code
	 *            the keyCode of the just pressed key
	 */
	@Override
	public final synchronized void press(int code) {
		this.pressedKeys.add(code);
		this.releasedKeys.remove(code);
	}

	/**
	 * Remove a released keys keyCode to the List and notifies observers.
	 *
	 * @param code
	 *            the keyCode of the just released key
	 */
	@Override
	public final synchronized void release(int code) {
		this.releasedKeys.add(code);
		this.pressedKeys.remove(code);
	}

	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes.
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public final Iterator<Integer> getPressedKeyIterator() {
		return this.pressedKeys.iterator();
	}

	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes.
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public final Iterator<Integer> getReleasedKeyIterator() {
		return this.releasedKeys.iterator();
	}

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations.
	 */
	private final Object observerSync = new Object();

	/**
	 * Used to tell all Observers that something important changed Iterates all
	 * Observers and invokes every {@link Observer#update()}.
	 */
	private void notifyObservers() {

		List<Observer> obs = new ArrayList<>();
		synchronized (this.observerSync) {
			// Kind of hacky but works
			// add all Observers to regular List...
			for (Observer o : this.observers) {
				obs.add(o);
			}
		}
		// ... iterate this List and invoke each Observers update();
		for (Observer o : obs) {
			o.update();
		}
	}

	/**
	 * Adds an Observer to the List that will be notified every time something
	 * important changes.
	 *
	 * @param observer
	 *            The Observer that wants to listen
	 */
	public void register(Observer observer) {
		synchronized (this.observerSync) {
			this.observers.add(observer);
		}
	}

	/**
	 * Removes an Observer from the List to prevent further notification of
	 * changes.
	 *
	 * @param observer
	 *            The Observer that does not want to listen anymore
	 */
	public void unregister(Observer observer) {
		synchronized (this.observerSync) {
			this.observers.remove(observer);
		}
	}

}
