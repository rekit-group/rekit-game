package edu.kit.informatik.ragnarok.logic.levelcreator;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.FixedCameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.Rocket;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import edu.kit.informatik.ragnarok.primitives.ProgressDependency;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class BossRoom {

	private LevelStructure roomStructure;
	private int x;
	private LevelCreator levelCreator;
	private InanimateDoor door;
	private Vec2D triggerPos;
	private Boss boss;
	private float cameraTarget;

	private static ParticleSpawner explosionParticles = null;
	static {
		BossRoom.explosionParticles = new ParticleSpawner();
		BossRoom.explosionParticles.angle = new ParticleSpawnerOption(0,
				(float) (2 * Math.PI), 0, 0);
		BossRoom.explosionParticles.colorR = new ParticleSpawnerOption(200,
				230, 10, 25);
		BossRoom.explosionParticles.colorG = new ParticleSpawnerOption(200,
				250, -130, -110);
		BossRoom.explosionParticles.colorB = new ParticleSpawnerOption(150,
				200, -130, -110);
		BossRoom.explosionParticles.colorA = new ParticleSpawnerOption(230,
				250, -120, -200);
		BossRoom.explosionParticles.timeMin = 0.1f;
		BossRoom.explosionParticles.timeMax = 0.3f;
		BossRoom.explosionParticles.amountMin = 40;
		BossRoom.explosionParticles.amountMax = 50;
		BossRoom.explosionParticles.speed = new ParticleSpawnerOption(4, 9, -1,
				1);
	}

	private static ParticleSpawner fireworkParticles = null;
	static {
		BossRoom.fireworkParticles = new ParticleSpawner();
		BossRoom.fireworkParticles.angle = new ParticleSpawnerOption(0,
				(float) (2 * Math.PI), 0, (float) (Math.PI));
		BossRoom.fireworkParticles.colorR = new ParticleSpawnerOption(100, 250,
				-100, 5);
		BossRoom.fireworkParticles.colorG = new ParticleSpawnerOption(100, 250,
				-100, 5);
		BossRoom.fireworkParticles.colorB = new ParticleSpawnerOption(100, 250,
				-100, 5);
		BossRoom.fireworkParticles.colorA = new ParticleSpawnerOption(230, 250,
				-120, -200);
		BossRoom.fireworkParticles.timeMin = 0.5f;
		BossRoom.fireworkParticles.timeMax = 1f;
		BossRoom.fireworkParticles.amountMin = 40;
		BossRoom.fireworkParticles.amountMax = 50;
		BossRoom.fireworkParticles.speed = new ParticleSpawnerOption(3, 5, -1,
				1);
	}

	public BossRoom(Boss boss, LevelStructure roomStructure,
			final LevelCreator levelCreator) {
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
		levelCreator.generateEvenFloor(x + 6 + roomStructure.getWidth(), x + 6
				+ roomStructure.getWidth() + 5);

		// generate door after room
		this.door = new InanimateDoor(new Vec2D(x + 5
				+ roomStructure.getWidth(),
				(float) Math.ceil(GameConf.gridH / 2)));
		levelCreator.generateGameElement(door);

		this.triggerPos = new Vec2D(x + 6, GameConf.gridH - 2);
		InanimateTrigger trigger = new InanimateTrigger(triggerPos, new Vec2D(
				1, 1)) {
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
		cameraTarget = x + 5 + GameConf.playerCameraOffset
				+ player.getSize().getX() / 2;

		// Prepare boss
		this.boss.setBossRoom(this);
		this.boss.setPos(new Vec2D(x + 6 + roomStructure.getWidth() / 2,
				GameConf.gridH / 2 + 1));
		this.boss.setTarget(player);

		// Create thread for asynchronous stuff
		Thread newThread = new Thread() {
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

				gameModel.setCameraTarget(new FixedCameraTarget(cameraTarget
						- GameConf.playerCameraOffset));

				gameModel.addBossText(boss.getName());

				// Spawn Boss
				levelCreator.generateGameElement((GameElement) boss);

				// Close door
				levelCreator.generateBox((int) triggerPos.getX(),
						(int) triggerPos.getY());
			}
		};

		newThread.start();
	}

	public void endBattle() {
		final GameModel gameModel = this.levelCreator.getGameModel();
		final Player player = gameModel.getPlayer();

		final TimeDependency timer = new TimeDependency(7f);

		// Needed for animating camera movement
		ProgressDependency cameraMover = new ProgressDependency(cameraTarget
				- GameConf.playerCameraOffset, player.getPos().getX() - GameConf.playerCameraOffset);
		
		// Needed for animating door movement
		ProgressDependency doorMover = new ProgressDependency(this.door.getPos().getY(), this.door.getPos().getY() - 10);

		// Create thread for asynchronous stuff
		Thread newThread = new Thread() {
			@Override
			public void run() {

				// save Players current velocity
				Vec2D playerVelSave = player.getVel();
				Vec2D playerPosSave = player.getPos();
				Vec2D bossPosSave = ((GameElement) boss).getPos();
				
				
				// while timer has time left...
				while (!timer.timeUp()) {

					// freeze player and pos
					player.setVel(new Vec2D());
					player.setPos(playerPosSave);
					((GameElement) boss).setVel(new Vec2D());
					((GameElement) boss).setPos(bossPosSave);

					// wait for time to be up
					try {
						Thread.sleep(GameConf.logicDelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// phase one: show explosions
					if (timer.getProgress() < 0.4) {
						if (Math.random() > 0.9) {
							Vec2D randPos = ((GameElement) boss).getPos().add(
									new Vec2D((float) Math.random() * 2 - 1,
											(float) Math.random() * 2f - 1));
							explosionParticles.spawn(gameModel, randPos);
						}
					}
					// phase two: show fireworks
					else if (timer.getProgress() < 0.9) {
						// remove boss of last phase
						gameModel.removeGameElement((GameElement) boss);

						// show fireworks
						if (Math.random() > 0.9) {
							float deltaX = GameConf.gridW / 2f;
							float midX = x + deltaX;

							float deltaY = GameConf.gridH / 2f;
							float midY = deltaY;

							Vec2D randPos = new Vec2D(midX
									+ (float) Math.random() * deltaX * 2
									- deltaX, midY + (float) Math.random()
									* deltaY * 2 - deltaY);
							fireworkParticles.spawn(gameModel, randPos);
						}

						// open door slowly
						float prog = (timer.getProgress() - 0.4f) * 2f;
						door.setPos(door.getPos().setY(doorMover.getNow(prog)));
					}
					// phase three: re-move camera to player position
					else {
						// remove door of last phase
						BossRoom.this.door.destroy();
						
						float prog = (timer.getProgress() - 0.9f) * 10f;
						gameModel.setCameraTarget(new FixedCameraTarget(cameraMover.getNow(prog)));
					}

					timer.removeTime(GameConf.logicDelta / 1000f);
				}

				// re-apply velocity to Player
				player.setVel(playerVelSave);

				// set camera back to player
				player.resetCameraOffset();
				gameModel.setCameraTarget(player);

			}
		};

		newThread.start();

	}
}
