package rekit.logic.gameelements.inanimate;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.core.GameGrid;
import rekit.core.GameTime;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.DynamicInanimate;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This Box realizes an {@link Inanimate} which boosts the Player from time to
 * time.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
@SetterInfo(res = "conf/boost")
public final class BoostBox extends DynamicInanimate implements Configurable {
	/**
	 * The inner inanimate box.
	 */
	@NoSet
	InanimateBox innerBox;
	/**
	 * The time between strategy changes.
	 */
	private static long PERIOD;
	/**
	 * The current time offset.
	 */
	@NoSet
	private long offset = 0;
	/**
	 * All strategies.
	 */
	@NoSet
	private BoostBoxStrategy[] strategies;
	/**
	 * The current strategy id.
	 */
	@NoSet
	private int current = 0;
	/**
	 * The particle spawner.
	 */
	private static ParticleSpawner PARTICLES;

	/**
	 * This bool indicates whether particles shall spawn.
	 */
	@NoSet
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
	@NoSet
	private long lastTime = GameTime.getTime();

	@Override
	public void logicLoop() {
		// Get new strategy from strategy map
		long deltaTime = GameTime.getTime() - this.lastTime;
		this.lastTime += deltaTime;
		this.offset += deltaTime;

        this.sparkling = this.offset > BoostBox.PERIOD - 750;
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
	public void internalRender(GameGrid f) {
		this.strategies[this.current].internalRender(f);
		if (this.sparkling) {
			BoostBox.PARTICLES.spawn(this.getScene(), this.getPos());
		}
	}

	@Override
	public BoostBox create(Vec startPos, String... options) {
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
		 * Same as {@link BoostBox#internalRender(GameGrid)}.
		 *
		 * @param f
		 *            the field
		 */
		public void internalRender(GameGrid f) {
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
			element.collidedWithSolid(this.parent.getFrame(), dir);
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
			element.collidedWithSolid(this.parent.getFrame(), dir);

			if (element.getTeam() == Team.PLAYER && dir == Direction.UP) {
				element.setVel(element.getVel().addY(Player.FLOOR_BOOST));
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
			element.collidedWithSolid(this.parent.getFrame(), dir);
			if (element.getTeam() == Team.PLAYER && dir == Direction.UP) {
				element.setVel(element.getVel().addY(4 * Player.FLOOR_BOOST));
			}
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(255, 30, 30, 255);
		}

	}

}
