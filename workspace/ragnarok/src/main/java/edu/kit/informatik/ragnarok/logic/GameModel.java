package edu.kit.informatik.ragnarok.logic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Player;

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
	
	public GameModel () {
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
	
	public void addGameElement(GameElement e) {
		this.gameElements.add(e);
	}
	public void removeGameElement(GameElement e) {
		this.gameElements.remove(e);
	}

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
		
		// iterate all GameElements
		Iterator<GameElement> it1 = this.getGameElementIterator();
		while (it1.hasNext()) {
			GameElement e1 = it1.next();
			Iterator<GameElement> it2 = this.getGameElementIterator();
			while (it2.hasNext()) {
				GameElement e2 = it2.next();
				if (e1 != e2) {
					if (e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
						e1.reactToCollision(e2, Direction.DOWN);
					}
				}
			}
		}
				
		// update time
		this.lastTime = timeNow;
	}

}
