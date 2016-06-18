package edu.kit.informatik.ragnarok.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * This class establishes a {@link KeyListener} to Components of the Shell<br>
 * Also {@link Observer Observers} can register to receive events
 *
 * @author Dominik Fuchß
 *
 */
public final class InputHelper {

	/**
	 * Private constructor to prevent instantiation of static used class
	 */
	private InputHelper() {
	}

	/**
	 * List of all observers that registered to key changes
	 */
	private static List<Observer> observers = new ArrayList<>();

	/**
	 * List of all keyCodes that are currently pressed
	 */
	private static Set<Integer> keys = new HashSet<Integer>();
	/**
	 * All registered {@link Control Controls} with their key adapters
	 */
	private static ConcurrentHashMap<Control, KeyAdapter> attatched = new ConcurrentHashMap<>();

	// Start the daemon
	static {
		Thread daemon = new Thread() {
			@Override
			public void run() {
				while (true) {
					InputHelper.notifyObservers();
					ThreadUtils.sleep(GameConf.logicDelta);
				}
			}
		};
		daemon.setDaemon(true);
		daemon.start();
	}

	/**
	 * Attach a {@link Control} to the Listener
	 *
	 * @param listenerControl
	 *            the control
	 * @return {@code true} if successfully attached, {@code false} otherwise
	 */
	public static synchronized boolean attach(Control listenerControl) {
		// Check if listenerControl is set
		if (listenerControl == null || InputHelper.attatched.containsKey(listenerControl)) {
			return false;
		}

		// Add our custom KeyListener to an object
		KeyAdapter adapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				InputHelper.press(e.keyCode);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				InputHelper.release(e.keyCode);
			}
		};
		InputHelper.attatched.put(listenerControl, adapter);
		listenerControl.addKeyListener(adapter);
		return true;
	}

	/**
	 * Detach a {@link Control} from the listener
	 *
	 * @param listenerControl
	 *            the control
	 * @return {@code true} if detached or {@code false} if not detached
	 */
	public static synchronized boolean detach(Control listenerControl) {
		KeyAdapter adapter = InputHelper.attatched.remove(listenerControl);
		if (adapter == null) {
			return false;
		}
		listenerControl.removeKeyListener(adapter);
		return true;
	}

	/**
	 * Adds a pressed keys keyCode to the List and notifies observers
	 *
	 * @param code
	 *            the keyCode of the just pressed key
	 */
	private static final synchronized void press(int code) {
		InputHelper.keys.add(code);
	}

	/**
	 * Remove a released keys keyCode to the List and notifies observers.
	 *
	 * @param code
	 *            the keyCode of the just released key
	 */
	private static final synchronized void release(int code) {
		InputHelper.keys.remove(code);
	}

	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes
	 *
	 * @return the Iterator for all pressed keyCodes
	 */
	public static final Iterator<Integer> getKeyIterator() {
		return InputHelper.keys.iterator();
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
	private static void notifyObservers() {
		List<Observer> obs = new ArrayList<Observer>();

		synchronized (InputHelper.SYNC) {
			if (InputHelper.attatched.isEmpty()) {
				return;
			}
			// Kind of hacky but works: blockingly (what kind of adverb is
			// this??)
			// add all Observers to regular List...
			for (Observer o : InputHelper.observers) {
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
	public static void register(Observer observer) {
		synchronized (InputHelper.SYNC) {
			InputHelper.observers.add(observer);
		}
	}

	/**
	 * Removes an Observer from the List to prevent further notification of
	 * changes
	 *
	 * @param observer
	 *            The Observer that does not want to listen anymore
	 */
	public static void unregister(Observer observer) {
		synchronized (InputHelper.SYNC) {
			InputHelper.observers.remove(observer);
		}
	}

}
