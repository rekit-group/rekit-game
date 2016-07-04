package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Boss;
import edu.kit.informatik.ragnarok.logic.levelcreator.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

public class RektSmasher extends Boss {

	private RektKiller innerRektKiller;
	private float speed = 0.5f;

	public RektSmasher() {
		super();
	}

	public RektSmasher(Vec startPos) {
		// Configure own attributes
		super(startPos, new Vec(), new Vec(2f, 2f));
		// Configure innerRektKiller
		this.innerRektKiller = new RektKiller(startPos, 15, this.getSize());
		this.innerRektKiller.setCurrentDirection(Direction.DOWN);
		this.innerRektKiller.prepare();
		this.setLifes(3);
	}

	@Override
	public void addDamage(int damage) {
		super.addDamage(damage);
		this.speed = 0.5f + (3 - this.getLifes()) * 0.25f;
	}

	@Override
	public void internalRender(Field f) {
		// Update innerRektKiller
		this.innerRektKiller.setPos(this.getPos());
		// Render innerRektKiller
		this.innerRektKiller.internalRender(f);
		// Add face image above regular innerRektKiller visualization
		int lifes = this.getLifes() > 3 ? 3 : this.getLifes();
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
		if (Math.random() > 0.8) {
			newDir = Direction.getRandom();
		}

		// If direction did not change, pick another randomly
		while (this.innerRektKiller.getCurrentDirection() == newDir) {
			newDir = Direction.getRandom();
		}
		this.innerRektKiller.setCurrentDirection(newDir);

		// Randomly remove spikes on colliding side sometimes
		if (Math.random() > 0.8 && this.innerRektKiller.hasSide(dir.getOpposite())) {
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
		if (this.getLifes() <= 0) {
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
		BossStructure structure = new BossStructure(new int[][] {
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 2, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 1, 0, 2, 0, 2, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 1, 0, 0, 2, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
				{ 1, 0, 2, 0, 2, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
				{ 1, 2, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 } }, this);
		this.setBossStructure(structure);
		return structure;
	}

	@Override
	public Entity create(Vec startPos) {
		RektSmasher clone = new RektSmasher(startPos);
		clone.setTarget(this.target);
		clone.setBossStructure(this.bossStructure);
		return clone;
	}

	@Override
	public int getID() {
		return 100;
	}

}
