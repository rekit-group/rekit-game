package ragnarok.logic.level.bossstructure;

import java.util.HashMap;

import ragnarok.config.GameConf;
import ragnarok.core.GameElement;
import ragnarok.core.IScene;
import ragnarok.logic.gameelements.GameElementFactory;
import ragnarok.logic.gameelements.entities.FixedCameraTarget;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.inanimate.InanimateDoor;
import ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import ragnarok.logic.gameelements.particles.ParticleSpawner;
import ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import ragnarok.logic.gameelements.type.Boss;
import ragnarok.logic.gui.Text;
import ragnarok.logic.gui.TimeDecorator;
import ragnarok.logic.level.Structure;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.time.Progress;
import ragnarok.primitives.time.Timer;
import ragnarok.util.CalcUtil;
import ragnarok.util.TextOptions;
import ragnarok.util.ThreadUtils;

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
		InanimateTrigger.createTrigger(this.triggerPos, new Vec(1), this::startBattle);
		return width;
	}

	/**
	 * Start the battle.
	 */
	public void startBattle() {
		IScene scene = this.door.getScene();
		GameElement player = scene.getPlayer();

		// calculate where to put camera
		this.cameraTarget = this.levelX + 5 + GameConf.PLAYER_CAMERA_OFFSET + player.getSize().getX() / 2;

		// Prepare boss
		this.boss = (Boss) this.boss.create(this.boss.getStartPos().addX(this.levelX), new String[0]);
		this.boss.setBossStructure(this);
		this.boss.setTarget(player);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// keep walking right to the right camera position
			while (player.getPos().getX() < BossStructure.this.cameraTarget) {
				player.setVel(player.getVel().setX(1.8f));
				Timer.sleep(GameConf.LOGIC_DELTA);
			}
			scene.setCameraTarget(new FixedCameraTarget(BossStructure.this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET));
			// Spawn Boss
			GameElementFactory.generate(this.boss);
			// Close door
			GameElementFactory.generateInanimate((int) BossStructure.this.triggerPos.getX(), (int) BossStructure.this.triggerPos.getY());

			// Boss text
			TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 30, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
			Text bossText = new Text(scene, op).setText(BossStructure.this.boss.getName());
			bossText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
			scene.addGuiElement(new TimeDecorator(scene, bossText, new Timer(3000)));
		});

	}

	/**
	 * End the battle.
	 *
	 * @param scene
	 *            the scene
	 */
	public void endBattle(IScene scene) {
		final Player player = (Player) scene.getPlayer();

		final Timer timer = new Timer(7000);

		// Needed for animating camera movement
		Progress cameraMover = new Progress( //
				this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET, //
				player.getPos().getX() - GameConf.PLAYER_CAMERA_OFFSET //
		);

		// Needed for animating door movement
		Progress doorMover = new Progress( //
				this.door.getPos().getY(), //
				this.door.getPos().getY() - 10 //
		);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// save Players current velocity
			Vec[] save = { player.getVel(), player.getPos(), this.boss.getPos() };
			// while timer has time left...
			while (!timer.timeUp()) {
				// freeze player and pos
				player.setVel(new Vec());
				player.setPos(save[1]);
				this.boss.setVel(new Vec());
				this.boss.setPos(save[2]);
				// wait for time to be up
				Timer.sleep(GameConf.LOGIC_DELTA);
				this.phase(scene, timer, doorMover, cameraMover);
				timer.logicLoop();
			}

			// re-apply velocity to Player
			player.setVel(save[0]);
			// give player full health
			if (player.getLives() < GameConf.PLAYER_LIVES) {
				player.setLives(GameConf.PLAYER_LIVES);
			}
			// set camera back to player
			player.resetCameraOffset();
			scene.setCameraTarget(player);
		});

	}

	// TODO JDoc.
	private final void phase(IScene scene, Timer timer, Progress doorMover, Progress cameraMover) {
		// phase one: show explosions
		if (timer.getProgress() < 0.4) {
			this.phase1(scene, timer);
		}
		// phase two: show fireworks
		else if (timer.getProgress() < 0.9) {
			this.phase2(scene, timer, doorMover);
		}
		// phase three: re-move camera to player position
		else {
			this.phase3(scene, timer, cameraMover);
		}
	}

	private final void phase1(IScene scene, Timer timer) {
		if (GameConf.PRNG.nextDouble() > 0.9) {
			Vec randPos = BossStructure.this.boss.getPos()
					.add(new Vec((float) GameConf.PRNG.nextDouble() * 2 - 1, (float) GameConf.PRNG.nextDouble() * 2f - 1));
			BossStructure.explosionParticles.spawn(scene, randPos);
		}
	}

	private final void phase2(IScene scene, Timer timer, Progress doorMover) {
		// remove boss of last phase
		scene.markForRemove(BossStructure.this.boss);

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

	private final void phase3(IScene scene, Timer timer, Progress cameraMover) {
		// remove door of last phase
		this.door.destroy();
		float prog = (timer.getProgress() - 0.9f) * 10f;
		scene.setCameraTarget(new FixedCameraTarget(cameraMover.getNow(prog)));
	}
}
