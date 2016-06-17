package edu.kit.informatik.ragnarok.gui;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

/**
 * Main class of the View. Manages the window and a canvas an periodically
 * renders all GameElements that GameModel.getGameElementIterator() provides
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

	private long lastRenderTime;
	
	private Queue<Float> fpsQueue = new ArrayDeque<Float>();
	
	/**
	 * The canvas that is drawn upon
	 */
	private Canvas canvas;

	/**
	 * The Field that manages the graphic context
	 */
	private Field field;

	private GC gc;

	/**
	 * Constructor that creates a new window with a canvas and prepares all
	 * required attributes
	 */
	public GameView() {
		// Create window
		this.display = new Display();
		this.shell = new Shell(this.display, SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL | SWT.NO_BACKGROUND);
		this.shell.setText("Project Ragnarok");

		// Create and position a canvas
		this.canvas = new Canvas(this.shell, SWT.NONE);
		this.canvas.setSize(GameConf.gridW * GameConf.pxPerUnit, GameConf.gridH * GameConf.pxPerUnit);
		this.canvas.setLocation(0, 0);

		// Open Shell
		this.shell.open();
		this.shell.setSize(GameConf.gridW * GameConf.pxPerUnit, GameConf.gridH * GameConf.pxPerUnit);

		// Create Graphic context
		this.gc = new GC(this.canvas);
		this.field = new Field(this);
	}

	/**
	 * Starts the View by periodically invoking renderLoop()
	 */
	public void start() {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							GameView.this.renderLoop();
						}
					});

					try {
						Thread.sleep(GameConf.renderDelta);
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
		while (!this.shell.isDisposed()) {
			if (!this.display.readAndDispatch()) {
				this.display.sleep();
			}
		}
		this.display.dispose();
	}

	/**
	 * Setter for the GameModel
	 *
	 * @param value
	 *            the GameModel to set
	 */
	public void setModel(GameModel model) {
		this.model = model;
	}

	/**
	 * Getter for the GameModel
	 *
	 * @return the reference to the GameModel
	 */
	public GameModel getModel() {
		return this.model;
	}

	/**
	 * Getter for the canvas
	 *
	 * @return the canvas
	 */
	public Canvas getCanvas() {
		return this.canvas;
	}

	/**
	 * Games main render loop that is periodically called. It updates the canvas
	 * by iterating over all GameElements that GameMode.getGameElementIterator()
	 * supplies and invoking each render()
	 */
	public void renderLoop() {
		if (this.shell.isDisposed()) {
			return;
		}
		// Create temporary GC on new Image and let field draw on that
		Image image = new Image(this.shell.getDisplay(), this.canvas.getBounds());
		GC tempGC = new GC(image);
		this.field.setGC(tempGC);

		// Draw background
		this.field.setBackground(new RGB(110, 170, 255));

		// Iterate all GameElements
		synchronized (GameModel.SYNC) {
			Iterator<GameElement> it = this.model.getGameElementIterator();
			while (it.hasNext()) {
				// Render next element
				it.next().render(this.field);
			}
		}

		// Draw UI (lifes, score)
		this.field.refreshUI(this.getModel().getPlayer().getLifes(), this.getModel().getPoints());

		// draw temporary image on actual cavans
		this.gc.drawImage(image, 0, 0);

		// put trash outside
		image.dispose();
		tempGC.dispose();
		
		// draw FPS
		float fps = getFPS();
		this.field.setGC(this.gc);
		this.field.drawFPS(fps);

	}
	
	private float getFPS() {
		long thisTime = System.currentTimeMillis();
		long deltaTime = thisTime - this.lastRenderTime;
		this.lastRenderTime = System.currentTimeMillis();
		
		float fps = deltaTime;
		fpsQueue.add(fps);
		float sum = 0;
		for (float f : fpsQueue) {
			sum += f;
		}
		
		if (fpsQueue.size() > 5) {
			fpsQueue.poll();
		}
		
		return (int)(10000f / (sum / fpsQueue.size()) / 10f);
	}


  

}
