package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

/**
 * This Box realizes an {@link Inanimate} which the Player can climb up
 *
 * @author Dominik Fuchß
 *
 */
public class ClimbUpBox extends DynamicInanimate {

	protected InanimateBox innerBox;

	protected static final long PERIOD = 4000;

	protected long offset = 0;

	private Map<Integer, ClimbBoxStrategy> strategies;

	private int current = 0;

	private ClimbBoxStrategy strategy;

	private static ParticleSpawner particles = null;

	static {
		ClimbUpBox.particles = new ParticleSpawner();
		ClimbUpBox.particles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), (float) (2 * Math.PI), (float) (4 * Math.PI));
		ClimbUpBox.particles.colorR = new ParticleSpawnerOption(250, 0);
		ClimbUpBox.particles.colorG = new ParticleSpawnerOption(250, -250);
		ClimbUpBox.particles.colorB = new ParticleSpawnerOption(150);
		ClimbUpBox.particles.colorA = new ParticleSpawnerOption(220, -220);
		ClimbUpBox.particles.timeMin = 0.2f;
		ClimbUpBox.particles.timeMax = 0.4F;
		ClimbUpBox.particles.amountMax = 1;

		ClimbUpBox.particles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	private boolean sparkling;

	/**
	 * Prototype Constructor
	 */
	public ClimbUpBox() {
		super();
	}

	protected ClimbUpBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);
		// and set color
		this.innerBox.color = new RGBAColor(80, 80, 255, 255);

		// instantiate the two strategies
		this.strategies = new HashMap<>();
		this.strategies.put(0, new NoClimb(this));
		this.strategies.put(1, new BoostClimb(this));
		this.strategy = this.strategies.get(0);
	}

	private long lastTime = 0;

	@Override
	public void logicLoop(float deltaTime) {
		// Get new strategy from strategy map

		this.offset += (this.scene.getTime() - this.lastTime);
		this.lastTime = this.scene.getTime();
		if (this.offset > ClimbUpBox.PERIOD - 750) {
			this.sparkling = true;
		} else {
			this.sparkling = false;
		}
		if (this.offset < ClimbUpBox.PERIOD) {
			return;
		}
		this.offset = 0;
		this.current = (this.current + 1) % this.strategies.size();
		this.strategy = this.strategies.get(this.current);

	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.strategy.reactToCollision(element, dir);
	}

	@Override
	public void internalRender(Field f) {
		this.strategy.internalRender(f);
		if (this.sparkling) {
			ClimbUpBox.particles.spawn(this.scene, this.pos);
		}
	}

	@Override
	public int getID() {
		return 86;
	}

	@Override
	public ClimbUpBox create(Vec startPos, int[] options) {
		return new ClimbUpBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255));
	}

	private abstract class ClimbBoxStrategy {
		protected ClimbUpBox parent;

		ClimbBoxStrategy(ClimbUpBox parent) {
			this.parent = parent;
		}

		public void reactToCollision(GameElement element, Direction dir) {
			// Do nothing
		}

		public abstract RGBAColor getColor();

		public void internalRender(Field f) {
			this.parent.innerBox.color = this.getColor();
			// Call decorated InanimateBoxes internalRender
			this.parent.innerBox.internalRender(f);
		}
	}

	private class NoClimb extends ClimbBoxStrategy {
		NoClimb(ClimbUpBox parent) {
			super(parent);
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(80, 80, 80, 0);
		}

	}

	private class BoostClimb extends ClimbBoxStrategy {
		BoostClimb(ClimbUpBox parent) {
			super(parent);
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(255, 255, 255, 255);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			if (element.getTeam() == Team.PLAYER && dir == Direction.DOWN) {
				element.collidedWith(this.parent.getCollisionFrame(), dir);
				element.setVel(element.getVel().addY(4 * GameConf.PLAYER_BOTTOM_BOOST));
			}

		}
	}

}
