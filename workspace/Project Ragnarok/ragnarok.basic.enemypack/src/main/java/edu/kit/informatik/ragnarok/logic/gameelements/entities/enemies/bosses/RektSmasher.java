package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.level.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 *
 * This class realizes a simple {@link Boss}:<br>
 * This boss is a square with <i>sharp</i> triangles at each side, which can
 * hurt a {@link Player}; these will disappear from time to time
 *
 */
@LoadMe
public class RektSmasher extends Boss {
	/**
	 * The internal {@link RektKiller}
	 */
	private RektKiller innerRektKiller;
	/**
	 * The current movement speed
	 */
	private float speed = 0.5f;

	/**
	 * Prototype Constructor
	 */
	public RektSmasher() {
		super();
	}

	/**
	 * Create a RektSmasher by start position
	 *
	 * @param startPos
	 *            the start position
	 */
	public RektSmasher(Vec startPos) {
		// Configure own attributes
		super(startPos, new Vec(), new Vec(2f, 2f));
		// Configure innerRektKiller
		this.innerRektKiller = new RektKiller(startPos, this.getSize(), 15);
		this.innerRektKiller.setCurrentDirection(Direction.DOWN);
		this.innerRektKiller.prepare();
		this.setLives(3);
	}

	@Override
	public void addDamage(int damage) {
		super.addDamage(damage);
		this.speed = 0.5f + (3 - this.getLives()) * 0.25f;
	}

	@Override
	public void internalRender(Field f) {
		// Update innerRektKiller
		this.innerRektKiller.setPos(this.getPos());
		// Render innerRektKiller
		this.innerRektKiller.internalRender(f);
		// Add face image above regular innerRektKiller visualization
		int lifes = this.getLives() > 3 ? 3 : this.getLives();
		f.drawImage(this.getPos(), this.getSize().multiply(0.8f), "rektSmasher_" + lifes + ".png");
	}

	@Override
	public void collidedWith(Frame collision, final Direction dir) {
		Vec dif = this.getPos().add(this.target.getPos().multiply(-1));
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
		if (GameConf.PRNG.nextDouble() > 0.8 && this.innerRektKiller.hasSide(dir.getOpposite())) {
			// remove side
			this.innerRektKiller.setSide(dir.getOpposite(), false);

			// start thread to re-add spikes after time
			ThreadUtils.runThread(() -> {
				ThreadUtils.sleep(5000);
				this.innerRektKiller.setSide(dir.getOpposite(), true);
			});

		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHarmless) {
			return;
		}
		if (this.team.isHostile(element.getTeam())) {
			// Touched harmless side
			if (!this.innerRektKiller.hasSide(dir)) {
				// Let the player jump if he landed on top
				if (dir == Direction.UP) {
					element.setVel(element.getVel().setY(GameConf.PLAYER_JUMP_BOOST));
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
	public void logicLoop(float deltaTime) {
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

		this.setVel(this.innerRektKiller.getCurrentDirection().getVector().multiply(this.speed * GameConf.PLAYER_WALK_MAX_SPEED));
		super.logicLoop(deltaTime);
	}

	@Override
	public String getName() {
		return "RektSmasher";
	}

	@Override
	public BossStructure getBossStructure() {
		// TODO Refactor to new Layout

		int[][][] oldStruct = new int[][][] {
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } },
				{ { 1 }, { 2, 0 }, { 0 }, { 0 }, { 0 }, { 2, 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 1 }, { 1 },
						{ 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 2, 0 }, { 0 }, { 2, 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 2, 0 }, { 0 }, { 0 }, { 1 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 } },
				{ { 1 }, { 0 }, { 2, 0 }, { 0 }, { 2, 0 }, { 0 }, { 1 }, { 1 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 1 }, { 0 } },
				{ { 1 }, { 2, 0 }, { 0 }, { 0 }, { 0 }, { 2, 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 0 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 0 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } } };
		String[][] struct = new String[oldStruct.length][];
		for (int i = 0; i < oldStruct.length; i++) {
			String[] l = new String[oldStruct[i].length];
			for (int j = 0; j < oldStruct[i].length; j++) {
				if (oldStruct[i][j][0] == 0) {
					l[j] = null;
				} else if (oldStruct[i][j][0] == 1) {
					l[j] = Inanimate.class.getName();
				} else if (oldStruct[i][j][0] == 2) {
					l[j] = RektKiller.class.getName();
				}
			}
			struct[i] = l;
		}
		BossStructure structure = new BossStructure(struct, this);
		this.setBossStructure(structure);
		return structure;
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		RektSmasher clone = new RektSmasher(startPos);
		clone.setTarget(this.target);
		clone.setBossStructure(this.bossStructure);
		return clone;
	}

}
