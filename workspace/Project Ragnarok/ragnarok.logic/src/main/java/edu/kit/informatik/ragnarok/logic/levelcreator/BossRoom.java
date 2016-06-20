package edu.kit.informatik.ragnarok.logic.levelcreator;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.FixedCameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class BossRoom {

	private LevelStructure roomStructure;
	private int x;
	private LevelCreator levelCreator;
	private InanimateDoor door;
	private Vec2D triggerPos;
	private Boss boss;
	
	public BossRoom(Boss boss, LevelStructure roomStructure, final LevelCreator levelCreator) {
		this.roomStructure = roomStructure;
		this.levelCreator = levelCreator;
		this.boss = boss;
	}
	
	public void generate(int x) {
		this.x = x;
		
		// generate floor before boss room 
		levelCreator.generateEvenFloor(x, x + 5);
		// generate boss room structure
		roomStructure.buildStructure(levelCreator, x + 6, GameConf.gridH);
		levelCreator.generatedUntil += roomStructure.getWidth();
		// generate floor after boss room
		levelCreator.generateEvenFloor(x + 6 + roomStructure.getWidth(), x + 6 + roomStructure.getWidth() + 5);
		
		// generate door after room
		this.door = new InanimateDoor(new Vec2D(x + 5 + roomStructure.getWidth(), (float)Math.ceil(GameConf.gridH / 2)));
		levelCreator.generateGameElement(door);
		
		this.triggerPos = new Vec2D(x + 6, GameConf.gridH - 2);
		InanimateTrigger trigger = new InanimateTrigger(triggerPos, new Vec2D(1, 1)) {
			@Override
			public void perform() {
				if (!this.deleteMe) {
					startBattle(this);	
				}
			}
		};
		levelCreator.generateGameElement(trigger);
	}
	
	public void startBattle(GameElement trigger) {
		
		// destroy invisible InanimateTrigger
		trigger.destroy();
		
		final GameModel gameModel = levelCreator.getGameModel();
		final Player player = gameModel.getPlayer();
		
		// calculate where to put camera
		final float cameraTarget = x + 5 + GameConf.playerCameraOffset + player.getSize().getX() / 2;
		
		// Prepare boss 
		this.boss.setPos(new Vec2D(x + 6 + roomStructure.getWidth() / 2, GameConf.gridH / 2));
				
		// Create thread for asynchronous stuff
		Thread newThread = new Thread(){
			@Override
			public void run() {
				
				// keep walking right to the right camera position
				while (player.getPos().getX() < cameraTarget) {
					player.setVel(player.getVel().setX(1.8f));
					try {
						Thread.sleep(GameConf.logicDelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				gameModel.setCameraTarget(new FixedCameraTarget(cameraTarget - GameConf.playerCameraOffset));

				gameModel.addBossText(boss.getName());
				
				// Spawn Boss
				levelCreator.generateGameElement((GameElement)boss);
				
				// Close door
				levelCreator.generateBox((int)triggerPos.getX(), (int)triggerPos.getY());
			}
		};
		
		newThread.start();
	}
	
	
	public void endBattle() {
		this.door.destroy();
		this.levelCreator.getGameModel().setCameraTarget(this.levelCreator.getGameModel().getPlayer());
	}
}
