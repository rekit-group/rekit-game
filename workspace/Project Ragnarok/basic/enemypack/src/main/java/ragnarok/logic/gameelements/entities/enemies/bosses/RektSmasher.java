package ragnarok.logic.gameelements.entities.enemies.bosses;

import home.fox.visitors.Visitable;
import home.fox.visitors.annotations.NoVisit;
import home.fox.visitors.annotations.VisitInfo;
import ragnarok.config.GameConf;
import ragnarok.core.GameElement;
import ragnarok.core.GameGrid;
import ragnarok.logic.gameelements.entities.Entity;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.entities.enemies.RektKiller;
import ragnarok.logic.gameelements.inanimate.Inanimate;
import ragnarok.logic.gameelements.type.Boss;
import ragnarok.logic.level.bossstructure.BossStructure;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Frame;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.time.Timer;
import ragnarok.util.ReflectUtils.LoadMe;
import ragnarok.util.ThreadUtils;

/**
 *
 * This class realizes a simple {@link Boss}:<br>
 * This boss is a square with <i>sharp</i> triangles at each side, which can
 * hurt a {@link Player}; these will disappear from time to time.
 *
 */
@LoadMe
@VisitInfo(res = "conf/rektsmasher", visit = true)
public final class RektSmasher extends Boss implements Visitable {
	/**
	 * The internal {@link RektKiller}.
	 */
	@NoVisit
	private RektKiller innerRektKiller;
	/**
	 * The base movement speed.
	 */
	private static float BASE_SPEED;
	/**
	 * The size of the boss.
	 */
	private static Vec SIZE;
	/**
	 * The bosses default lives.
	 */
	private static int LIVES;

	/**
	 * The time until spikes return.
	 */
	private static int SPIKE_TIME;

	/**
	 * The name of the boss.
	 */
	private static String NAME;

	/**
	 * Prototype Constructor.
	 */
	public RektSmasher() {
		super();
	}

	/**
	 * Create a RektSmasher by start position.
	 *
	 * @param startPos
	 *            the start position
	 */
	public RektSmasher(Vec startPos) {
		// Configure own attributes
		super(startPos, new Vec(), RektSmasher.SIZE);
		// Configure innerRektKiller
		this.innerRektKiller = new RektKiller(startPos, this.getSize(), 0b1111);
		this.innerRektKiller.setCurrentDirection(Direction.RIGHT);
		this.innerRektKiller.prepare();
		this.setLives(RektSmasher.LIVES);
	}

	@Override
	public void addDamage(int damage) {
		super.addDamage(damage);
	}

	@Override
	public void internalRender(GameGrid f) {
		// Update innerRektKiller
		this.innerRektKiller.setPos(this.getPos());
		// Render innerRektKiller
		this.innerRektKiller.internalRender(f);
		// Add face image above regular innerRektKiller visualization
		int lifes = this.getLives() > RektSmasher.LIVES ? RektSmasher.LIVES : this.getLives();
		f.drawImage(this.getPos(), this.getSize().scalar(0.8f), "rektSmasher_" + lifes + ".png");
	}

	@Override
	public void collidedWith(Frame collision, final Direction dir) {
		Vec dif = this.getPos().add(this.target.getPos().scalar(-1));
		super.collidedWith(collision, dir);

		Direction newDir;

		if (dir == Direction.UP || dir == Direction.DOWN) {
			if (dif.getX() > 0) {
				newDir = Direction.LEFT;
			} else {
				newDir = Direction.RIGHT;
			}
		} else {
			if (dif.getY() > 0) {
				newDir = Direction.UP;
			} else {
				newDir = Direction.DOWN;
			}
		}

		// Randomly change direction sometimes
		if (GameConf.PRNG.nextDouble() > 0.8) {
			newDir = Direction.getRandom();
		}

		// If direction did not change, pick another randomly
		while (this.innerRektKiller.getCurrentDirection() == newDir) {
			newDir = Direction.getRandom();
		}
		this.innerRektKiller.setCurrentDirection(newDir);

		// Randomly remove spikes on colliding side sometimes
		if (GameConf.PRNG.nextDouble() > 0.8 && this.innerRektKiller.hasSide(Direction.getOpposite(dir))) {
			// remove side
			this.innerRektKiller.setSide(Direction.getOpposite(dir), false);

			// start thread to re-add spikes after time
			ThreadUtils.runThread("RektSmasher-Spikes", () -> {
				Timer.sleep(RektSmasher.SPIKE_TIME);
				this.innerRektKiller.setSide(Direction.getOpposite(dir), true);

			});

		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHarmless) {
			return;
		}
		if (this.getTeam().isHostile(element.getTeam())) {
			// Touched harmless side
			if (!this.innerRektKiller.hasSide(dir)) {
				// Let the player jump if he landed on top
				if (dir == Direction.UP) {
					element.setVel(element.getVel().setY(GameConf.PLAYER_KILL_BOOST));
				}
				// kill the enemy
				this.addDamage(1);
			}
			// Touched dangerous side
			else {
				// Give player damage
				element.addDamage(1);
			}
		}

	}

	@Override
	protected void innerLogicLoop() {
		// if no invincibility or invincibility time is up
		if (this.invincibility == null || this.invincibility.timeUp()) {
			this.isHarmless = false;
		}
		// if invincible
		if (this.invincibility != null && !this.invincibility.timeUp()) {
			this.isHarmless = true;
		}
		// we dont want him damaging the player when hes actually dead
		if (this.getLives() <= 0) {
			this.isHarmless = true;
		}
		float speed = RektSmasher.BASE_SPEED + (RektSmasher.LIVES - this.getLives()) * 0.25f;
		this.setVel(this.innerRektKiller.getCurrentDirection().getVector().scalar(speed * GameConf.PLAYER_WALK_MAX_SPEED));
		super.innerLogicLoop();
	}

	@Override
	public String getName() {
		return RektSmasher.NAME;
	}

	@Override
	public BossStructure getBossStructure() {
		String i = Inanimate.class.getSimpleName();
		String n = null;
		String r = RektKiller.class.getSimpleName();
		String[][] struct = new String[][] { //
				{ i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i },
				{ i, r, n, n, n, r, i, n, n, n, n, n, i, n, n, n, i, i, n, n, n, i, n, n, n, n, n, n },
				{ i, n, r, n, r, n, i, n, n, n, n, n, i, n, n, n, n, n, n, n, n, i, n, n, n, n, n, n },
				{ i, n, n, r, n, n, i, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, i, n },
				{ i, n, r, n, r, n, i, i, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, i, i, n },
				{ i, r, n, n, n, r, i, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n },
				{ i, i, i, i, i, i, i, n, n, n, n, n, n, i, n, i, i, i, i, n, i, n, n, n, n, n, n, n },
				{ n, n, n, n, n, n, n, n, n, n, n, n, n, i, n, i, i, i, i, n, i, n, n, n, n, n, n, n },
				{ i, i, i, i, i, i, i, i, i, i, i, i, i, i, n, i, i, i, i, n, i, i, i, i, i, i, i, i } //
		};

		BossStructure structure = new BossStructure(struct, this);
		this.setBossStructure(structure);
		return structure;
	}

	@Override
	public Vec getStartPos() {
		return new Vec(16, GameConf.GRID_H / 2 + 1);
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		RektSmasher clone = new RektSmasher(startPos);
		clone.setTarget(this.target);
		clone.setBossStructure(this.bossStructure);

		return clone;
	}

}
