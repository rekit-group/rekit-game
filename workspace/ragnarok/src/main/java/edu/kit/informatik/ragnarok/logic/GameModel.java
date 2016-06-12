package edu.kit.informatik.ragnarok.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.EnemyFactory;

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
	
	private Thread loopThread;

	public GameModel() {
		this.init();
	}
	
	public void init() {
		// Initialize Set of all gameElements that need rendering and logic
		this.gameElements = new HashSet<GameElement>();

		// Create Player and add him to game
		this.player = new Player(new Vec2D(3, 0));
		this.currentOffset = 3;
		this.addGameElement(player);
		
		// Init EnemyFactory with model
		EnemyFactory.init(this);
		
		// Create LevelCreator
		this.levelCreator = new LevelCreator(this);
		this.levelCreator.generate();
		
		// Initialize all other attributes
		this.lastTime = System.currentTimeMillis();
	}
	
	public void restart() {
		final GameModel that = this;
		Thread restartThread = new Thread() {
			public void run() {
				// wait 2 seconds
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// reset all data structures
				that.init();
				// restart logic thread
				that.start();
			}
		};
		restartThread.start();
	}

	public void start() {
		this.loopThread = new Thread() {
			public void run() {
				// repeat until player is dead
				while (!getPlayer().deleteMe) {
					try {
						logicLoop();
						Thread.sleep(c.logicDelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// restart game
				restart();
			}
		};
		loopThread.setDaemon(true);
		loopThread.start();
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
	 * @throws InterruptedException 
	 */
	public void logicLoop() throws InterruptedException {
		
		// calculate time difference since last physics loop
		long timeNow = System.currentTimeMillis();
		long timeDelta = timeNow - this.lastTime;

		// iterate all GameElements to invoke logicLoop
		List<GameElement> gameElementsToDelete = new ArrayList<GameElement>();
		synchronized (SYNC) {
			Iterator<GameElement> it = this.getGameElementIterator();
			while (it.hasNext()) {
				GameElement e = it.next();
				
				// if this GameElement is marked for destruction
				if (e.deleteMe) {
					it.remove();
				}
				
				// check if we can delete this
				if (e.getPos().getX() < this.currentOffset - c.playerDist - 1) {
					gameElementsToDelete.add(e);
				} else {
					e.logicLoop(timeDelta / 1000.f);
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
						checkCollision(e1, e2, e1.getLastPos(), e2.getLastPos());
					}
				}
			}
		}

		// update time
		this.lastTime = timeNow;
		
	}
	
	private void checkCollision(GameElement e1, GameElement e2, Vec2D e1lastPos, Vec2D e2lastPos) {
		// Return if there is no collision
		if (!e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
			return;
		}
		/*
		float velRelX = e1.getPos().getX() - e2.getPos().getX();
		float velRelY = e1.getPos().getY() - e2.getPos().getY();
		
		if (Math.abs(velRelX) < Math.abs(velRelY)) {
			if (velRelX > 0) {
				e1.reactToCollision(e2, Direction.LEFT);
			}
			if (velRelX < 0) {
				e1.reactToCollision(e2, Direction.RIGHT);
			}
		} else {
			if (velRelY > 0) {
				e1.reactToCollision(e2, Direction.UP);
			}
			if (velRelY < 0) {
				e1.reactToCollision(e2, Direction.DOWN);
			}
		}
		*/
		
		// Simulate CollisionFrame with last Y position
		Vec2D e1lastYVec = new Vec2D(e1.getPos().getX(), e1lastPos.getY());
		Frame e1lastYFrame = new Frame(
				e1lastYVec.add(e1.getSize().multiply(-0.5f)),
				e1lastYVec.add(e1.getSize().multiply(0.5f)));
		
		// Simulate CollisionFrame with last X position
		Vec2D e1lastXVec = new Vec2D(e1lastPos.getX(), e1.getPos().getY());
		Frame e1lastXFrame = new Frame(e1lastXVec.add(e1
				.getSize().multiply(-0.5f)),
				e1lastXVec.add(e1.getSize().multiply(0.5f)));
		
		// Simulate CollisionFrame with last Y position
		Vec2D e2lastYVec = new Vec2D(e2.getPos().getX(), e2lastPos.getY());
		Frame e2lastYFrame = new Frame(
				e2lastYVec.add(e2.getSize().multiply(-0.5f)),
				e2lastYVec.add(e2.getSize().multiply(0.5f)));
		
		// Simulate CollisionFrame with last X position
		Vec2D e2lastXVec = new Vec2D(e2lastPos.getX(), e2.getPos().getY());
		Frame e2lastXFrame = new Frame(e2lastXVec.add(e2
				.getSize().multiply(-0.5f)),
				e2lastXVec.add(e2.getSize().multiply(0.5f)));

		// If they still collide with the old x positions:
		// it must be because of the y position
		if (e1lastXFrame.collidesWith(e2lastXFrame)) {
			// If relative movement is in positive y direction (down)
			if (e2.getPos().getY() + e1.getPos().getY() > e2lastPos.getY() + e1lastPos.getY()) { 
				e1.reactToCollision(e2, Direction.UP);	
			} else
			// If relative movement is in negative y direction (up)
			if (e2.getPos().getY() + e1.getPos().getY() < e2lastPos.getY() + e1lastPos.getY()) {
				e1.reactToCollision(e2, Direction.DOWN);
			}
			else {
				return;
			}
			// check if he is still colliding even with last x position
			checkCollision(e1, e2, new Vec2D(e1lastPos.getX(), e1.getPos().getY()), new Vec2D(e2lastPos.getX(), e2.getPos().getY()));
		} else
		// If they still collide with the old y positions:
		// it must be because of the x position
		if (e1lastYFrame.collidesWith(e2lastYFrame)) {
			// If he moved in positive x direction (right)
			if (e2.getPos().getX() + e1.getPos().getX() > e2lastPos.getX() + e1lastPos.getX()) { 
				e1.reactToCollision(e2, Direction.RIGHT);	
			} else
			// If he moved in negative x direction (left)
			if (e2.getPos().getX() + e1.getPos().getX() < e2lastPos.getX() + e1lastPos.getX()) {
				e1.reactToCollision(e2, Direction.LEFT);
			}
			else {
				return;
			}
			// check if he is still colliding even with last x position
			checkCollision(e1, e2, new Vec2D(e1.getPos().getX(), e1lastPos.getY()), new Vec2D(e2.getPos().getX(), e2lastPos.getY()));
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
	
	public int getPoints() {
		return (int) this.getCurrentOffset() + this.getPlayer().getPoints();
	}

}
