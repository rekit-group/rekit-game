package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This Box realizes an {@link Inanimate} which boosts the Player from time to
 * time.
 *
 * @author Dominik Fuchß
 *
 */
@LoadMe
public final class BoostBox extends DynamicInanimate {
	/**
	 * The inner inanimate box.
	 */
	protected InanimateBox innerBox;
	/**
	 * The time between strategy changes.
	 */
	protected static final long PERIOD = 4500;
	/**
	 * The current time offset.
	 */
	protected long offset = 0;
	/**
	 * All strategies.
	 */
	private BoostBoxStrategy[] strategies;
	/**
	 * The current strategy id.
	 */
	private int current = 0;
	/**
	 * The particle spawner.
	 */
	private static ParticleSpawner particles = null;

	static {
		BoostBox.particles = new ParticleSpawner();
		BoostBox.particles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), (float) (2 * Math.PI), (float) (4 * Math.PI));
		BoostBox.particles.colorR = new ParticleSpawnerOption(250, 0);
		BoostBox.particles.colorG = new ParticleSpawnerOption(250, -250);
		BoostBox.particles.colorB = new ParticleSpawnerOption(150);
		BoostBox.particles.colorA = new ParticleSpawnerOption(220, -220);
		BoostBox.particles.timeMin = 0.2f;
		BoostBox.particles.timeMax = 0.4F;
		BoostBox.particles.amountMax = 1;

		BoostBox.particles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}
	/**
	 * This bool indicates whether particles shall spawn.
	 */
	private boolean sparkling;

	/**
	 * Prototype Constructor.
	 */
	public BoostBox() {
		super();
	}

	/**
	 * Create a BoostBox.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	protected BoostBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);
		// and set color
		this.innerBox.color = new RGBAColor(80, 80, 255, 255);
		// instantiate the two strategies
		this.strategies = new BoostBoxStrategy[] { new NoBoost(this), new BoostFirstState(this), new BoostMaxState(this) };

	}

	/**
	 * The last time when {@link #logicLoop(float)} was invoked.
	 */
	private long lastTime = 0;

	@Override
	public void logicLoop(float deltaTime) {
		// Get new strategy from strategy map

		this.offset += (this.getScene().getTime() - this.lastTime);
		this.lastTime = this.getScene().getTime();
		if (this.offset > BoostBox.PERIOD - 750) {
			this.sparkling = true;
		} else {
			this.sparkling = false;
		}
		if (this.offset < BoostBox.PERIOD) {
			return;
		}
		this.offset = 0;
		this.current = (this.current + 1) % this.strategies.length;

	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.strategies[this.current].reactToCollision(element, dir);
	}

	@Override
	public void internalRender(Field f) {
		this.strategies[this.current].internalRender(f);
		if (this.sparkling) {
			BoostBox.particles.spawn(this.getScene(), this.getPos());
		}
	}

	@Override
	public BoostBox create(Vec startPos, String[] options) {
		return new BoostBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255));
	}

	/**
	 * This class is the base class for all different behaviors of a
	 * {@link BoostBox}.
	 *
	 * @author Dominik Fuchß
	 *
	 */
	private abstract class BoostBoxStrategy {
		/**
		 * The parent.
		 */
		protected BoostBox parent;

		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		BoostBoxStrategy(BoostBox parent) {
			this.parent = parent;
		}

		/**
		 * Same as {@link BoostBox#reactToCollision(GameElement, Direction)}.
		 *
		 * @param element
		 *            the element
		 * @param dir
		 *            the direction
		 */
		public void reactToCollision(GameElement element, Direction dir) {
			// Do nothing
		}

		/**
		 * Get the current color of the box.
		 *
		 * @return the color
		 */
		public abstract RGBAColor getColor();

		/**
		 * Same as {@link BoostBox#internalRender(Field)}.
		 *
		 * @param f
		 *            the field
		 */
		public void internalRender(Field f) {
			this.parent.innerBox.color = this.getColor();
			// Call decorated InanimateBoxes internalRender
			this.parent.innerBox.internalRender(f);
		}
	}

	/**
	 * The default strategy: A normal {@link InanimateBox}.
	 *
	 * @author Dominik Fuchß
	 *
	 */
	private class NoBoost extends BoostBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		NoBoost(BoostBox parent) {
			super(parent);
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(80, 80, 80, 255);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
		}

	}

	/**
	 * The first level strategy: A small boost upwards.
	 *
	 * @author Dominik Fuchß
	 *
	 */
	private class BoostFirstState extends BoostBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		BoostFirstState(BoostBox parent) {
			super(parent);
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(30, 255, 30, 255);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);

			if (element.getTeam() == Team.PLAYER && dir == Direction.UP) {
				element.setVel(element.getVel().addY(GameConf.PLAYER_BOTTOM_BOOST));
			}

		}
	}

	/**
	 * The max level strategy: A huge boost upwards.
	 *
	 * @author Dominik Fuchß
	 *
	 */
	private class BoostMaxState extends BoostBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		BoostMaxState(BoostBox parent) {
			super(parent);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
			if (element.getTeam() == Team.PLAYER && dir == Direction.UP) {
				element.setVel(element.getVel().addY(4 * GameConf.PLAYER_BOTTOM_BOOST));
			}
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(255, 30, 30, 255);
		}

	}

}
