package ragnarok.logic.scene;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import ragnarok.config.GameConf;
import ragnarok.core.CameraTarget;
import ragnarok.core.GameElement;
import ragnarok.core.GameTime;
import ragnarok.core.GuiElement;
import ragnarok.core.IScene;
import ragnarok.core.Team;
import ragnarok.logic.GameModel;

/**
 * Based on the concept of scenes in Unity. <br>
 * "Scenes contain the objects of your game. They can be used to create a main
 * menu, individual levels, and anything else. Think of each unique Scene file
 * as a unique level. In each Scene, you will place your environments,
 * obstacles, and decorations, essentially designing and building your game in
 * pieces." <a href=" https://docs.unity3d.com/Manual/CreatingScenes.html">Unity
 * Manual</a>
 * <p>
 * A new Scene needs an entry in {@link Scenes} and a method with the Signature:
 * {@code public static Scene create(GameModel, String[])}, for the GameModel to
 * be able to start that Scene.<br>
 * For Scene switching take a look at
 * {@link GameModel#switchScene(Scenes, String[])} <br>
 * <br>
 * <b>IMPORTANT:</b> all {@link Scene Scenes} must provide a static method <br>
 * <b><em>public static Scene create(GameModel model, String[]
 * options)</em></b><br>
 * so that {@link Scenes#getNewScene(GameModel, String[])} can work
 *
 * @author Matthias Schmitt
 *
 * @version 1.0
 */
abstract class Scene implements CameraTarget, IScene {

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations.
	 */
	private final Object sync = new Object();
	/**
	 * The model.
	 */
	private GameModel model;
	/**
	 * All gui elements.
	 */
	private Queue<GuiElement> guiElements;
	/**
	 * All game elements.
	 */
	private Queue<GameElement>[] gameElements;
	/**
	 * GameElements which shall be added.
	 */
	private ConcurrentLinkedQueue<GameElement> gameElementAddQueue;
	/**
	 * GameElements which shall be removed.
	 */
	private ConcurrentLinkedQueue<GameElement> gameElementRemoveQueue;
	/**
	 * Stats of the gameElements for debugging.
	 */
	private ConcurrentHashMap<Class<?>, Long> gameElementDurations = new ConcurrentHashMap<>();
	/**
	 * Indicates whether the scene is paused.
	 */
	private boolean paused = false;
	/**
	 * Last time of invoking {@link #logicLoop()}.
	 */
	private long lastTime = GameTime.getTime();
	/**
	 * The latest deltaTime in {@link #logicLoop()}.
	 */
	protected long deltaTime;

	/**
	 * Create the scene.
	 *
	 * @param model
	 *            the model
	 */
	public Scene(GameModel model) {
		this.model = model;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		// Byte: [-128, 127]
		final int length = 256;
		this.guiElements = new LinkedList<>();

		this.gameElements = (Queue<GameElement>[]) new Queue<?>[length];
		for (int i = 0; i < this.gameElements.length; i++) {
			this.gameElements[i] = new LinkedList<>();
		}

		this.gameElementAddQueue = new ConcurrentLinkedQueue<>();
		this.gameElementRemoveQueue = new ConcurrentLinkedQueue<>();
	}

	@Override
	public void togglePause() {
		if (this.paused) {
			GameTime.resume();
		} else {
			GameTime.pause();
		}
		this.paused = !this.paused;
	}

	@Override
	public boolean isPaused() {
		return this.paused;
	}

	@Override
	public void end(boolean won) {
		this.model.switchScene(Scenes.MENU);
	}

	@Override
	public final void logicLoop() {
		this.deltaTime = GameTime.getTime() - this.lastTime;
		this.lastTime += this.deltaTime;
		this.innerLogicLoop();
	}

	/**
	 * This method will be invoked in {@link #logicLoop()}.
	 */
	protected void innerLogicLoop() {
		synchronized (this.synchronize()) {
			this.logicLoopPre();
			// add GameElements that have been added
			this.addGameElements();
			if (!this.paused) {
				// iterate all GameElements to invoke logicLoop
				this.getGameElementIterator().forEachRemaining(this::logicLoopGameElement);
			}
			// remove GameElements that must be removed
			this.removeGameElements();
			this.logicLoopAfter();
			// after all game related logic update GuiElements
			this.getGuiElementIterator().forEachRemaining((e) -> e.logicLoop());
		}
	}

	/**
	 * Will be invoked after all {@link GameElement#logicLoop(float)}.
	 */
	protected void logicLoopAfter() {
	}

	/**
	 * Will be invoked before all {@link GameElement#logicLoop(float)}.
	 *
	 */
	protected void logicLoopPre() {
	}

	/**
	 * Invoke {@link GameElement#logicLoop(float)} for all game elements.
	 *
	 * @param e
	 *            the elements
	 */
	protected void logicLoopGameElement(GameElement e) {

		// if this GameElement is marked for destruction
		// TODO This is a bugfix for inanimates which wont be deleted upon time
		if (e.getDeleteMe() || (e.getTeam() == Team.INANIMATE && this.getModel().getCameraOffset() - 20 > e.getPos().getX())) {
			this.markForRemove(e);
		}

		// Debug: Save time before logicLoop
		long timeBefore = 0;
		if (GameConf.DEBUG) {
			timeBefore = GameTime.getTime();
		}

		e.logicLoop();

		// Debug: Compare and save logicLoop Duration
		if (GameConf.DEBUG) {
			synchronized (this.gameElementDurations) {
				long timeAfter = GameTime.getTime();
				Class<?> clazz = e.getClass();
				long dur = (timeAfter - timeBefore);
				if (this.gameElementDurations.containsKey(clazz)) {
					long newTime = this.gameElementDurations.get(clazz) + dur;
					this.gameElementDurations.put(clazz, newTime);
				} else {
					this.gameElementDurations.put(clazz, dur);
				}
			}
		}
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
	@Override
	public void addGameElement(GameElement element) {
		synchronized (this.gameElementAddQueue) {
			// Put GameElement in waiting list
			this.gameElementAddQueue.add(element);
		}
	}

	/**
	 * Internal method to add all waiting GameElements. See addGameElement for
	 * more info.
	 */
	private void addGameElements() {
		synchronized (this.gameElementAddQueue) {
			Iterator<GameElement> it = this.gameElementAddQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				this.gameElements[Scene.zToIndex(element.getZ())].add(element);
				element.setScene(this);
			}
			this.gameElementAddQueue.clear();
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
	@Override
	public void markForRemove(GameElement element) {
		synchronized (this.gameElementRemoveQueue) {
			this.gameElementRemoveQueue.add(element);
		}
	}

	/**
	 * Internal method to remove all waiting GameElements. See removeGameElement
	 * for more info.
	 */
	private void removeGameElements() {
		synchronized (this.gameElementRemoveQueue) {
			this.gameElementRemoveQueue.forEach(e -> this.gameElements[Scene.zToIndex(e.getZ())].remove(e));
			this.gameElementRemoveQueue.clear();
		}
	}

	@Override
	public Iterator<GameElement> getGameElementIterator() {
		return new ArrayedQueueIterator<>(this.gameElements);
	}

	/**
	 * Adds a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to add
	 */
	@Override
	public void addGuiElement(GuiElement e) {
		synchronized (this.synchronize()) {
			this.guiElements.add(e);
		}
	}

	@Override
	public void removeGuiElement(GuiElement e) {
		synchronized (this.synchronize()) {
			this.guiElements.remove(e);
		}
	}

	@Override
	public Iterator<GuiElement> getGuiElementIterator() {
		synchronized (this.synchronize()) {
			Queue<GuiElement> newQueue = new LinkedList<>();
			this.guiElements.forEach(newQueue::add);
			return newQueue.iterator();
		}
	}

	@Override
	public float getCameraOffset() {
		return 0;
	}

	@Override
	public void setCameraTarget(CameraTarget cameraTarget) {

	}

	@Override
	public int getGameElementCount() {
		int size = 0;
		for (Queue<GameElement> queue : this.gameElements) {
			size += queue.size();
		}
		return size;
	}

	@Override
	public Object synchronize() {
		return this.sync;
	}

	@Override
	public Map<Class<?>, Long> getGameElementDurations() {
		synchronized (this.gameElementDurations) {
			// Reset debug info
			Map<Class<?>, Long> ret = this.gameElementDurations;
			this.gameElementDurations = new ConcurrentHashMap<>();
			return ret;
		}
	}

	@Override
	public final GameModel getModel() {
		return this.model;
	}

	/**
	 * Get index based on z-value of position.
	 *
	 * @param zvalue
	 *            the zvalue
	 * @return the index
	 */
	private static final int zToIndex(byte zvalue) {
		return zvalue + 128;
	}
}
