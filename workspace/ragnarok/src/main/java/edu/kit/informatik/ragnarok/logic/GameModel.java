package edu.kit.informatik.ragnarok.logic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.player.Player;

public class GameModel {

	/**
	 * <pre>
	 *           1..1     1..*
	 * GameModel ------------------------- GameElement
	 *           gameModel        &gt;       gameElement
	 * </pre>
	 */
	private Set<GameElement> gameElements;

	private Player player;

	public long lastTime;

	private LevelCreator levelCreator;

	public GameModel() {
		// Initialize Set of all gameElements that need rendering and logic
		this.gameElements = new HashSet<GameElement>();

		// Create Player and add him to game
		this.player = new Player(new Vec2D(3, 3));
		this.addGameElement(player);

		// Create LevelCreator
		this.levelCreator = new LevelCreator(this);

		// Initialize all other attributes
		this.lastTime = System.currentTimeMillis();
	}

	public void start() {
		Thread t = new Thread() {
			public void run() {

				while (true) {
					logicLoop();
					try {
						Thread.sleep(c.logicDelta);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Adds a GameElement to the Model
	 * 
	 * @param element
	 *            the GameElement to add
	 */
	public void addGameElement(GameElement element) {
		this.gameElements.add(element);
	}

	/**
	 * Removes a GameElement from the Model
	 * 
	 * @param element
	 *            the GameElement to remove
	 */
	public void removeGameElement(GameElement element) {
		this.gameElements.remove(element);
	}

	/**
	 * Supplies an Iterator for all saved GameElements
	 * 
	 * @return
	 */
	public Iterator<GameElement> getGameElementIterator() {
		return this.gameElements.iterator();
	}

	/**
	 * Calculate DeltaTime Get Collisions .. & Invoke ReactCollision Iterate
	 * over Elements --> invoke GameElement:logicLoop()
	 */
	public void logicLoop() {

		// calculate time difference since last physics loop
		long timeNow = System.currentTimeMillis();
		long timeDelta = timeNow - this.lastTime;

		// iterate all GameElements to invoke logicLoop
		Iterator<GameElement> it = this.getGameElementIterator();
		while (it.hasNext()) {
			GameElement e = it.next();
			e.logicLoop(timeDelta / 1000.f);
		}

		// iterate all GameElements to detect collision
		Iterator<GameElement> it1 = this.getGameElementIterator();
		while (it1.hasNext()) {
			GameElement e1 = it1.next();
			Iterator<GameElement> it2 = this.getGameElementIterator();
			while (it2.hasNext()) {
				GameElement e2 = it2.next();
				if (e1 != e2) {
					if (e1.getCollisionFrame().collidesWith(
							e2.getCollisionFrame())) {
						if (e1 instanceof Inanimate && e2 instanceof Player) {
							// hook
							// System.out.println("hook");
						}
						
						// Reset e2s y-component to last
						Vec2D e2lastYVec = new Vec2D(e2.getPos().getX(), e2
								.getLastPos().getY());
						Frame e2lastYFrame = new Frame(e2lastYVec.add(e2
								.getSize().multiply(-0.5f)), e2lastYVec.add(e2
								.getSize().multiply(0.5f)));

						// It he doesnt collide anymore it means he collided because of going up or down 
						if (!e1.getCollisionFrame().collidesWith(e2lastYFrame)) {
							e1.reactToCollision(e2, (e2.getPos().getY() > e2
									.getLastPos().getY()) ? Direction.DOWN
									: Direction.UP);
						} else {
							Vec2D e2lastXVec = new Vec2D(
									e2.getLastPos().getX(), e2.getPos().getY());
							Frame e2lastXFrame = new Frame(e2lastXVec.add(e2
									.getSize().multiply(-0.5f)),
									e2lastXVec.add(e2.getSize().multiply(0.5f)));
							
							if (!e1.getCollisionFrame().collidesWith(e2lastXFrame)) {
								e1.reactToCollision(e2, (e2.getPos().getX() > e2
										.getLastPos().getX()) ? Direction.RIGHT
										: Direction.LEFT);
							} else {
								// TODO - does this happen?
								e1.reactToCollision(e2, Direction.DOWN);
							}
						}

						// e1.reactToCollision(e2, Direction.DOWN);
					}
				}
			}
		}

		// update time
		this.lastTime = timeNow;
	}

	/**
	 * Return player
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}

}
