package rekit.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import rekit.config.GameConf;
import rekit.gui.InputHelper;
import rekit.util.LambdaUtil;
import rekit.util.ThreadUtils;

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
	InputHelperImpl() {
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
	 * The lock to synchronize {@link #press(int)} and {@link #release(int)}.
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * Adds a pressed keys keyCode to the List and notifies observers.
	 *
	 * @param code
	 *            the keyCode of the just pressed key
	 */
	@Override
	public void press(int code) {
		try {
			this.lock.lock();
			this.pressedKeys.add(code);
			this.releasedKeys.remove(code);
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Remove a released keys keyCode to the List and notifies observers.
	 *
	 * @param code
	 *            the keyCode of the just released key
	 */
	@Override
	public void release(int code) {
		try {
			this.lock.lock();
			this.releasedKeys.add(code);
			this.pressedKeys.remove(code);
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes.
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public Iterator<Integer> getPressedKeyIterator() {
		return this.pressedKeys.iterator();
	}

	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes.
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public Iterator<Integer> getReleasedKeyIterator() {
		return this.releasedKeys.iterator();
	}

	/**
	 * Synchronization Lock that is used as a lock variable for blocking
	 * operations.
	 */
	private final Lock observerLock = new ReentrantLock();

	/**
	 * Used to tell all Observers that something important changed Iterates all
	 * Observers and invokes every {@link Observer#update()}.
	 */
	private void notifyObservers() {
		List<Observer> obs = new ArrayList<>();
		try {
			this.observerLock.lock();
			this.observers.forEach(obs::add);
		} finally {
			this.observerLock.unlock();
		}
		LambdaUtil.invoke(() -> obs.forEach(o -> o.update()));
	}

	/**
	 * Adds an Observer to the List that will be notified every time something
	 * important changes.
	 *
	 * @param observer
	 *            The Observer that wants to listen
	 */
	public void register(Observer observer) {
		try {
			this.observerLock.lock();
			this.observers.add(observer);
		} finally {
			this.observerLock.unlock();
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
		try {
			this.observerLock.lock();
			this.observers.remove(observer);
		} finally {
			this.observerLock.unlock();
		}
	}

}
