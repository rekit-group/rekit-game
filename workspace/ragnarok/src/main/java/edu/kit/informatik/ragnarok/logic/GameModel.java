package edu.kit.informatik.ragnarok.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.player.Player;

public class GameModel {
	
	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	public static final Object SYNC = new Object();
	
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
	
	private float currentOffset;	

	public GameModel() {
		// Initialize Set of all gameElements that need rendering and logic
		this.gameElements = new HashSet<GameElement>();

		// Create Player and add him to game
		this.player = new Player(new Vec2D(3, 0));
		this.currentOffset = 3;
		this.addGameElement(player);

		// Create LevelCreator
		this.levelCreator = new LevelCreator(this);
		this.levelCreator.generate();
		
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
		synchronized (SYNC) {
			this.gameElements.add(element);
		}
	}

	/**
	 * Removes a GameElement from the Model
	 * 
	 * @param element
	 *            the GameElement to remove
	 */
	public void removeGameElement(GameElement element) {
		synchronized (SYNC) {
			this.gameElements.remove(element);
		}
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
		List<GameElement> gameElementsToDelete = new ArrayList<GameElement>();
		synchronized (SYNC) {
			Iterator<GameElement> it = this.getGameElementIterator();
			while (it.hasNext()) {
				GameElement e = it.next();
				e.logicLoop(timeDelta / 1000.f);
				
				// check if we can delete this
				if (e.getPos().getX() < this.currentOffset - c.playerDist + 1) {
					gameElementsToDelete.add(e);
				}
			}
		}
		for (GameElement e : gameElementsToDelete) {
			this.removeGameElement(e);
		}
		
		Player player = this.getPlayer();
		// get maximum player x
		if (player.getPos().getX() > this.currentOffset) {
			this.currentOffset = player.getPos().getX();
			this.levelCreator.generate();
		}
		// dont allow player to go behind currentOffset
		if (player.getPos().getX() < this.currentOffset - c.playerDist) {
			player.setPos(player.getPos().setX(this.currentOffset - c.playerDist));
		}

		synchronized (SYNC) {
			// iterate all GameElements to detect collision
			Iterator<GameElement> it1 = this.getGameElementIterator();
			while (it1.hasNext()) {
				GameElement e1 = it1.next();
				Iterator<GameElement> it2 = this.getGameElementIterator();
				while (it2.hasNext()) {
					GameElement e2 = it2.next();
					if (e1 != e2) {
						if (e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
							processCollision(e1, e2);
						}
					}
				}
			}
		}

		// update time
		this.lastTime = timeNow;
		
	}
	
	private void processCollision(GameElement e1, GameElement e2) {

		// Reset e2s y-component to last
		Vec2D e2lastYVec = new Vec2D(e2.getPos().getX(), e2
				.getLastPos().getY());
		Frame e2lastYFrame = new Frame(
				e2lastYVec.add(e2.getSize().multiply(-0.5f)),
				e2lastYVec.add(e2.getSize().multiply(0.5f)));

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
				// TODO - does this happen with enemies?
				e1.reactToCollision(e2, Direction.DOWN);
			}
		}
	}

	/**
	 * Return player
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	public float getCurrentOffset() {
		return this.currentOffset;
	}

}
