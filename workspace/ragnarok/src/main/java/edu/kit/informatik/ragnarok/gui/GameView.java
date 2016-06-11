package edu.kit.informatik.ragnarok.gui;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

/**
 * Main class of the View.
 * Manages the window and a canvas an periodically renders all
 * GameElements that GameModel.getGameElementIterator() provides
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class GameView {
	/**
	 * Reference to the model, that holds all information that are required for
	 * rendering
	 */
	private GameModel model;
	
	/**
	 * Display object holds the shell 
	 */
	private Display display;
	
	/**
	 * Represents a graphic window 
	 */
	private Shell shell;
	
	/**
	 * Graphic context, that allows graphic operation on the canvas
	 */
	private GC gc;

	/**
	 * Constructor that creates a new window with a canvas and prepares all required attributes
	 */
	public GameView() {
		// Create window
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Project Ragnarok");

		// Create and position a canvas
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setSize(c.gridW * c.pxPerUnit, c.gridH * c.pxPerUnit);
		canvas.setLocation(0, 0);

		// Open Shell
		shell.open();
		shell.setSize(c.gridW * c.pxPerUnit, c.gridH * c.pxPerUnit);

		// Create Graphic context
		this.gc = new GC(canvas);
	}
	
	/**
	 * Starts the View by periodically invoking renderLoop()
	 */
	public void start() {
		Thread updateThread = new Thread() {
			public void run() {
				while (true) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							renderLoop();
						}
					});

					try {
						Thread.sleep(c.renderDelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		// background thread
		updateThread.setDaemon(true);
		updateThread.start();

		// Wait for window to be closed, holds the window open
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	/**
	 * Setter for the GameModel
	 * @param value the GameModel to set
	 */
	public void setModel(GameModel model) {
		this.model = model;
	}

	/**
	 * Getter for the GameModel
	 * @return the reference to the GameModel
	 */
	public GameModel getModel() {
		return this.model;
	}
	
	/**
	 * Games main render loop that is periodically called.
	 * It updates the canvas by iterating over all GameElements that
	 * GameMode.getGameElementIterator() supplies and invoking each render()
	 */
	public void renderLoop() {
		// Draw background
		gc.setBackground(new Color(display, 100, 100, 255));
		gc.fillRectangle(0, 0, c.gridW * c.pxPerUnit, c.gridH * c.pxPerUnit);

		// Iterate all GameElements
		Iterator<GameElement> it = this.model.getGameElementIterator();
		while (it.hasNext()) {
			// Render next element
			it.next().render(this.gc);
		}
	}

}
