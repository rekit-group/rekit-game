package edu.kit.informatik.ragnarok.logic.level.bossstructure;

import java.util.HashMap;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.FixedCameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.gui.Text;
import edu.kit.informatik.ragnarok.logic.gui.TimeDecorator;
import edu.kit.informatik.ragnarok.logic.level.Structure;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.time.Progress;
import edu.kit.informatik.ragnarok.primitives.time.Timer;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 *
 * This class realizes a {@link Structure} for bosses.
 *
 */
public final class BossStructure extends Structure {
	/**
	 * The door.
	 */
	private InanimateDoor door;
	/**
	 * The trigger's position.
	 */
	private Vec triggerPos;
	/**
	 * The boss.
	 */
	private Boss boss;
	/**
	 * The camera target offset.
	 */
	private float cameraTarget;
	/**
	 * The x pos of the level.
	 */
	private int levelX;
	// TODO Config file!
	/**
	 * Explosion particles.
	 */
	private static ParticleSpawner explosionParticles = null;
	static {
		BossStructure.explosionParticles = new ParticleSpawner();
		BossStructure.explosionParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, 0);
		BossStructure.explosionParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
		BossStructure.explosionParticles.colorG = new ParticleSpawnerOption(200, 250, -130, -110);
		BossStructure.explosionParticles.colorB = new ParticleSpawnerOption(150, 200, -130, -110);
		BossStructure.explosionParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		BossStructure.explosionParticles.timeMin = 0.1f;
		BossStructure.explosionParticles.timeMax = 0.3f;
		BossStructure.explosionParticles.amountMin = 40;
		BossStructure.explosionParticles.amountMax = 50;
		BossStructure.explosionParticles.speed = new ParticleSpawnerOption(4, 9, -1, 1);
	}
	/**
	 * Fireworks particles.
	 */
	private static ParticleSpawner fireworksParticles = null;
	static {
		BossStructure.fireworksParticles = new ParticleSpawner();
		BossStructure.fireworksParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, (float) (Math.PI));
		BossStructure.fireworksParticles.colorR = new ParticleSpawnerOption(100, 250, -100, 5);
		BossStructure.fireworksParticles.colorG = new ParticleSpawnerOption(100, 250, -100, 5);
		BossStructure.fireworksParticles.colorB = new ParticleSpawnerOption(100, 250, -100, 5);
		BossStructure.fireworksParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		BossStructure.fireworksParticles.timeMin = 0.5f;
		BossStructure.fireworksParticles.timeMax = 1f;
		BossStructure.fireworksParticles.amountMin = 40;
		BossStructure.fireworksParticles.amountMax = 50;
		BossStructure.fireworksParticles.speed = new ParticleSpawnerOption(3, 5, -1, 1);
	}

	/**
	 * Create a boss structure.
	 *
	 * @param structure
	 *            the structure definition
	 * @param boss
	 *            the boss
	 */
	public BossStructure(String[][] structure, Boss boss) {
		super(structure, new HashMap<>());
		this.boss = boss;
	}

	@Override
	public int build(int levelX, boolean autoCoinSpawn) {
		int width = super.build(levelX, false);
		this.levelX = levelX;

		// generate and add door after room
		this.door = new InanimateDoor(new Vec(levelX + width - 1, (float) Math.ceil(GameConf.GRID_H / 2)));
		GameElementFactory.generate(this.door);

		// generate trigger at door entrance
		this.triggerPos = new Vec(levelX + 6, GameConf.GRID_H - 2);
		// add its perform-method
		InanimateTrigger trigger = new InanimateTrigger(this.triggerPos, new Vec(1, 1)) {
			@Override
			public void perform() {
				if (!this.deleteMe) {
					BossStructure.this.startBattle(this.getScene());
				}
			}
		};
		// and add it to game
		GameElementFactory.generate(trigger);

		return width;
	}

	/**
	 * Start the battle.
	 *
	 * @param scene
	 *            the scene
	 */
	public void startBattle(IScene scene) {

		GameElement player = scene.getPlayer();

		// calculate where to put camera
		this.cameraTarget = this.levelX + 5 + GameConf.PLAYER_CAMERA_OFFSET + player.getSize().getX() / 2;

		// Prepare boss
		this.boss = (Boss) this.boss.create(this.boss.getStartPos().addX(this.levelX), new String[] {});
		this.boss.setBossStructure(this);
		this.boss.setTarget(player);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// keep walking right to the right camera position
			while (player.getPos().getX() < BossStructure.this.cameraTarget) {
				player.setVel(player.getVel().setX(1.8f));
				ThreadUtils.sleep(GameConf.LOGIC_DELTA);
			}
			scene.setCameraTarget(new FixedCameraTarget(BossStructure.this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET));
			// Spawn Boss
			GameElementFactory.generate(this.boss);
			// Close door
			GameElementFactory.generateInanimate((int) BossStructure.this.triggerPos.getX(), (int) BossStructure.this.triggerPos.getY());

			// Boss text

			TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 30, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
			Text bossText = new Text(scene, op);
			bossText.setText(BossStructure.this.boss.getName());
			bossText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
			TimeDecorator bossTextGui = new TimeDecorator(scene, bossText, new Timer(3000));
			scene.addGuiElement(bossTextGui);
		});

	}

	/**
	 * End the battle.
	 *
	 * @param scene
	 *            the scene
	 */
	public void endBattle(IScene scene) {
		final Entity player = scene.getPlayer();

		final Timer timer = new Timer(7000);

		// Needed for animating camera movement
		Progress cameraMover = new Progress(this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET,
				player.getPos().getX() - GameConf.PLAYER_CAMERA_OFFSET);

		// Needed for animating door movement
		Progress doorMover = new Progress(this.door.getPos().getY(), this.door.getPos().getY() - 10);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// save Players current velocity
			Vec playerVelSave = player.getVel();
			Vec playerPosSave = player.getPos();
			Vec bossPosSave = this.boss.getPos();
			// while timer has time left...
			while (!timer.timeUp()) {
				// freeze player and pos
				player.setVel(new Vec());
				player.setPos(playerPosSave);
				this.boss.setVel(new Vec());
				this.boss.setPos(bossPosSave);
				// wait for time to be up
				ThreadUtils.sleep(GameConf.LOGIC_DELTA);
				// phase one: show explosions
				if (timer.getProgress() < 0.4) {
					if (GameConf.PRNG.nextDouble() > 0.9) {
						Vec randPos = BossStructure.this.boss.getPos()
								.add(new Vec((float) GameConf.PRNG.nextDouble() * 2 - 1, (float) GameConf.PRNG.nextDouble() * 2f - 1));
						BossStructure.explosionParticles.spawn(scene, randPos);
					}
				}
				// phase two: show fireworks
				else if (timer.getProgress() < 0.9) {
					// remove boss of last phase
					scene.removeGameElement(BossStructure.this.boss);

					// show fireworks
					if (GameConf.PRNG.nextDouble() > 0.9) {
						float deltaX = GameConf.GRID_W / 2f;
						float midX = BossStructure.this.levelX + deltaX;

						float deltaY = GameConf.GRID_H / 2f;
						float midY = deltaY;

						Vec randPos = new Vec(midX + (float) GameConf.PRNG.nextDouble() * deltaX * 2 - deltaX,
								midY + (float) GameConf.PRNG.nextDouble() * deltaY * 2 - deltaY);
						BossStructure.fireworksParticles.spawn(scene, randPos);
					}

					// open door slowly
					float prog = (timer.getProgress() - 0.4f) * 2f;
					BossStructure.this.door.setPos(BossStructure.this.door.getPos().setY(doorMover.getNow(prog)));
				}
				// phase three: re-move camera to player position
				else {
					// remove door of last phase
					this.door.destroy();
					float prog = (timer.getProgress() - 0.9f) * 10f;
					scene.setCameraTarget(new FixedCameraTarget(cameraMover.getNow(prog)));
				}
				timer.logicLoop();
				// if (!GameTime.isPaused()) {
				// timer.removeTime(GameConf.LOGIC_DELTA);
				// }
			}

			// re-apply velocity to Player
			player.setVel(playerVelSave);
			// give player full health
			if (player.getLives() < GameConf.PLAYER_LIVES) {
				player.setLives(GameConf.PLAYER_LIVES);
			}
			// set camera back to player
			((Player) player).resetCameraOffset();
			scene.setCameraTarget((Player) player);
		});

	}
}
