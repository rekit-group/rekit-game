package edu.kit.informatik.ragnarok.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.util.InputHelper;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * This class establishes a Listener to Components of the Shell<br>
 * Also {@link Observer Observers} can register to receive events
 *
 * @author Dominik Fuchß
 *
 */
final class InputHelperImpl implements InputHelper {

	/**
	 * Private constructor to prevent instantiation of static used class
	 */
	public InputHelperImpl() {
		Thread daemon = new Thread() {
			@Override
			public void run() {
				while (true) {
					InputHelperImpl.this.notifyObservers();
					ThreadUtils.sleep(GameConf.logicDelta);
				}
			}
		};
		daemon.setDaemon(true);
		daemon.start();
	}

	/**
	 * List of all observers that registered to key changes
	 */
	private List<Observer> observers = new ArrayList<>();

	/**
	 * List of all keyCodes that are currently pressed
	 */
	private Set<Integer> pressedKeys = new HashSet<Integer>();

	/**
	 * List of all keyCodes that have just been released
	 */
	private Set<Integer> releasedKeys = new HashSet<Integer>();

	/**
	 * Adds a pressed keys keyCode to the List and notifies observers
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
	 * Get an Iterator to iterate over all pressed keys keyCodes
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public final Iterator<Integer> getPressedKeyIterator() {
		return this.pressedKeys.iterator();
	}

	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public final Iterator<Integer> getReleasedKeyIterator() {
		return this.releasedKeys.iterator();
	}

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	private static final Object SYNC = new Object();

	/**
	 * Used to tell all Observers that something important changed Iterates all
	 * Observers and invokes every update();
	 */
	private void notifyObservers() {

		List<Observer> obs = new ArrayList<Observer>();
		synchronized (InputHelperImpl.SYNC) {
			// Kind of hacky but works: blockingly (what kind of adverb is
			// this??)
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
	 * important changes
	 *
	 * @param observer
	 *            The Observer that wants to listen
	 */
	public void register(Observer observer) {
		synchronized (InputHelperImpl.SYNC) {
			this.observers.add(observer);
		}
	}

	/**
	 * Removes an Observer from the List to prevent further notification of
	 * changes
	 *
	 * @param observer
	 *            The Observer that does not want to listen anymore
	 */
	public void unregister(Observer observer) {
		synchronized (InputHelperImpl.SYNC) {
			this.observers.remove(observer);
		}
	}

}
