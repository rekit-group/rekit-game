package edu.kit.informatik.ragnarok.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.widgets.Shell;

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
	 * Bool that signals if the InputHelper has been initialized with the shell
	 */
	private static boolean init = false;
	
	/**
	 * List of all keyCodes that are currently pressed 
	 */
	private static Set<Integer> keys = new HashSet<Integer>();
	
	/**
	 * Initializes the InputHelper with a shell by attaching keyListeners to it.
	 * Each time a key is pressed/released the corresponding keyCode is saved in/deleted from
	 * the Set keys.
	 * @param shell
	 */
	public static void init(Shell shell) {
		// Check if shell is set
		if (shell == null) {
			return;
		}
		// Add our custom KeyListener to the shell
		shell.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				synchronized (InputHelper.class) {
					InputHelper.press(e.keyCode);
				}
			}

			@Override
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
				synchronized (InputHelper.class) {
					InputHelper.release(e.keyCode);
				}
			}
		});
		// Save that we just initialized
				InputHelper.init = true;
	}

	/**
	 * Adds a pressed keys keyCode to the List and notifies observers
	 * @param code the keyCode of the just pressed key
	 */
	private static final void press(int code) {
		InputHelper.keys.add(code);
		InputHelper.notifyObservers();
	}
	
	/**
	 * Remove a released keys keyCode to the List and notifies observers.
	 * @param code the keyCode of the just released key
	 */
	private static final void release(int code) {
		InputHelper.keys.remove(code);
		InputHelper.notifyObservers();

	}
	
	/**
	 * Get an Iterator to iterate over all pressed keys keyCodes
	 * @return the Iterator for all pressed keyCodes
	 */
	public static final Iterator<Integer> getKeyIterator() {
		return InputHelper.keys.iterator();
	}
	
	/**
	 * Synchronization Object that is used as a lock variable for blocking operations
	 */
	private static final Object SYNC = new Object();
	
	/**
	 * Used to tell all Observers that something important changed
	 * Iterates all Observers and invokes every update();
	 */
	private static void notifyObservers() {
		if (!InputHelper.init) {
			return;
		}
		// Kind of hacky but works: blocking add all Observers to regular List...
		List<Observer> obs = new ArrayList<Observer>();
		synchronized (InputHelper.SYNC) {
			for (Observer o : InputHelper.observers) {
				obs.add(o);
			}
		}
		//... iterate this List and invoke each Observers update();
		for (Observer o : obs) {
			o.update();
		}
	}

	/**
	 * Adds an Observer to the List that will be notified
	 * every time something important changes
	 * @param observer The Observer that wants to listen
	 */
	public static void register(Observer observer) {
		synchronized (InputHelper.SYNC) {
			InputHelper.observers.add(observer);
		}
	}
	
	/**
	 * Removes an Observer from the List to prevent further
	 * notification of changes
	 * @param observer The Observer that does not want to listen anymore
	 */
	public static void unregister(Observer observer) {
		synchronized (InputHelper.SYNC) {
			InputHelper.observers.remove(observer);
		}
	}

}
