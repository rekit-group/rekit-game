package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.GameTime;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import home.fox.visitors.Visitable;
import home.fox.visitors.annotations.NoVisit;
import home.fox.visitors.annotations.VisitInfo;

/**
 * This Box realizes an {@link Inanimate} which boosts the Player from time to
 * time.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
@VisitInfo(res = "conf/boost", visit = true)
public final class BoostBox extends DynamicInanimate implements Visitable {
	/**
	 * The inner inanimate box.
	 */
	@NoVisit
	protected InanimateBox innerBox;
	/**
	 * The time between strategy changes.
	 */
	protected static long PERIOD;
	/**
	 * The current time offset.
	 */
	@NoVisit
	protected long offset = 0;
	/**
	 * All strategies.
	 */
	@NoVisit
	private BoostBoxStrategy[] strategies;
	/**
	 * The current strategy id.
	 */
	@NoVisit
	private int current = 0;
	/**
	 * The particle spawner.
	 */
	private static ParticleSpawner PARTICLES;

	/**
	 * This bool indicates whether particles shall spawn.
	 */
	@NoVisit
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
	@NoVisit
	private long lastTime = GameTime.getTime();

	@Override
	public void logicLoop() {
		// Get new strategy from strategy map
		long deltaTime = GameTime.getTime() - this.lastTime;
		this.lastTime += deltaTime;
		this.offset += deltaTime;

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
			BoostBox.PARTICLES.spawn(this.getScene(), this.getPos());
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
	 * @author Dominik Fuchss
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
	 * @author Dominik Fuchss
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
	 * @author Dominik Fuchss
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
	 * @author Dominik Fuchss
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
