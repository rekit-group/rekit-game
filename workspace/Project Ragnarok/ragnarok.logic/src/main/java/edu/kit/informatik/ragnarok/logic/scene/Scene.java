package edu.kit.informatik.ragnarok.logic.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.PriorityQueueIterator;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.GuiElement;
import edu.kit.informatik.ragnarok.logic.parallax.ParallaxContainer;

public abstract class Scene implements CameraTarget {
	
	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	private final Object sync = new Object();
	
	protected GameModel model;
	
	private PriorityQueue<GuiElement> guiElements;

	private PriorityQueue<GameElement> gameElements;

	private ArrayList<GameElement> gameElementAddQueue;

	private ArrayList<GameElement> gameElementRemoveQueue;


	public Scene(GameModel model) {
		this.model = model;
	}
	
	/**
	 * Initialize the scene.
	 * e.g. build Level/GUI so Scene is ready to be drawn
	 * Must be called on restart.
	 */
	public void init() {
		this.guiElements = new PriorityQueue<>();
		this.gameElements = new PriorityQueue<GameElement>();
		this.gameElementAddQueue = new ArrayList<GameElement>();
		this.gameElementRemoveQueue = new ArrayList<GameElement>();
	}
	
	/**
	 * Start the scene. Begin drawing.
	 * e.g. Player/Enemies will begin to move
	 */
	public void start() {};
	
	public void end() {
		this.model.switchScene(0);
	};
	
	public void restart() {
		this.init();
		this.start();
	}
	
	public void logicLoop(float timeDelta) {
		
		logicLoopPre(timeDelta);
		
		// add GameElements that have been added
		this.addGameElements();

		// iterate all GameElements to invoke logicLoop
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.getGameElementIterator();
			while (it.hasNext()) {
				logicLoopGameElement(timeDelta, it);
			}
		}
		
		this.getBackground().logicLoop(this.getCameraOffset());

		// remove GameElements that must be removed
		this.removeGameElements();

		logicLoopAfter();
		
		// after all game related logic update GuiElements
		synchronized (this.synchronize())
		{
			Iterator<GuiElement> it = this.getGuiElementIterator();
			while (it.hasNext()) {
				GuiElement e = it.next();
				e.logicLoop(timeDelta);
			}
		}

	}

	protected void logicLoopAfter() {}

	protected void logicLoopPre(float timeDelta) {}

	protected void logicLoopGameElement(float timeDelta, Iterator<GameElement> it) {
		GameElement e = it.next();

		// if this GameElement is marked for destruction
		if (e.deleteMe) {
			it.remove();
		}

		e.logicLoop(timeDelta);
	}
	
	/**
	 * Adds a GameElement to the Model. The elements will not directly be added
	 * to the internal data structure to prevent concurrency errors. Instead
	 * there is an internal list to hold all waiting GameElements that will be
	 * added in the next call of logicLoop
	 *
	 * @param element
	 *            the GameElement to add
	 */
	public void addGameElement(GameElement element) {
		// Put GameElement in waiting list
		synchronized (this.synchronize()) {
			this.gameElementAddQueue.add(element);
		}
	}

	/**
	 * Internal method to add all waiting GameElements. See addGameElement for
	 * more info.
	 */
	private void addGameElements() {
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.gameElementAddQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				this.gameElements.add(element);
				element.setScene(this);

				it.remove();
			}
		}
	}

	/**
	 * Removes a GameElement from the Model The elements will not directly be
	 * removed from the internal data structure to prevent concurrency errors.
	 * Instead there is an internal list to hold all waiting GameElements that
	 * will be removed in the next call of logicLoop
	 *
	 * @param element
	 *            the GameElement to remove
	 */
	public void removeGameElement(GameElement element) {
		synchronized (this.synchronize()) {
			this.gameElementRemoveQueue.add(element);
		}
	}

	/**
	 * Internal method to remove all waiting GameElements. See removeGameElement
	 * for more info.
	 */
	private void removeGameElements() {
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.gameElementRemoveQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				it.remove();
				this.gameElements.remove(element);
			}
		}
	}
	
	public Iterator<GameElement> getOrderedGameElementIterator() {
		return new PriorityQueueIterator<GameElement>(this.gameElements);
	}
	
	public Iterator<GameElement> getGameElementIterator() {
		return this.gameElements.iterator();
	}
	
	/**
	 * Adds a GuiElement to the GameModel. 
	 * @param e the GuiElement to add
	 */
	public void addGuiElement(GuiElement e)
	{
		this.guiElements.add(e);
	}
	
	public void removeGuiElement(GuiElement e)
	{
		this.guiElements.remove(e);
	}
	
	public Iterator<GuiElement> getGuiElementIterator() {
		return this.guiElements.iterator();
	}

	public Player getPlayer() {
		return null;
	}
	
	public ParallaxContainer getBackground() {
		return null;
	}
	
	@Override
	public float getCameraOffset() {
		return 0;
	}
	
	public void setCameraTarget(CameraTarget cameraTarget) {
		
	}
	
	public Object synchronize() {
		return this.sync;
	}

}
