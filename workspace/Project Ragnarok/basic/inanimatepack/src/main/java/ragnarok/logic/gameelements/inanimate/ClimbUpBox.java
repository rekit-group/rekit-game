package ragnarok.logic.gameelements.inanimate;

import home.fox.configuration.Configurable;
import home.fox.configuration.annotations.NoSet;
import home.fox.configuration.annotations.SetterInfo;
import ragnarok.config.GameConf;
import ragnarok.core.GameElement;
import ragnarok.core.GameGrid;
import ragnarok.core.Team;
import ragnarok.logic.gameelements.particles.ParticleSpawner;
import ragnarok.logic.gameelements.type.DynamicInanimate;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.primitives.image.RGBColor;
import ragnarok.primitives.time.Timer;
import ragnarok.util.ReflectUtils.LoadMe;

/**
 * This Box realizes an {@link Inanimate} which the Player can climb up.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
@SetterInfo(res = "conf/climbup")
public final class ClimbUpBox extends DynamicInanimate implements Configurable {
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
	 * All strategies.
	 */
	@NoSet
	private ClimbBoxStrategy[] strategies;
	/**
	 * The current strategy.
	 */
	@NoSet
	private int current = 0;
	/**
	 * The outer color.
	 */
	private static RGBAColor OUTER_COLOR;
	/**
	 * A dark color.
	 */
	private static RGBAColor DARK_COLOR;
	/**
	 * The energy's color.
	 */
	private static RGBColor ENERGY_COLOR;
	/**
	 * The timer (how long climb enables?).
	 */
	@NoSet
	private Timer timer;
	/**
	 * The particles of the ClimbUpBox.
	 */
	private static ParticleSpawner PARTICLES;

	/**
	 * Prototype Constructor.
	 */
	public ClimbUpBox() {
		super();
	}

	/**
	 * Create a ClimbUpBox.
	 *
	 * @param pos
	 *            the position
	 * @param offset
	 *            the initial time offset in millis
	 */
	protected ClimbUpBox(Vec pos, long offset) {
		super(pos, new Vec(1), ClimbUpBox.OUTER_COLOR);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);

		// instantiate the two strategies
		this.strategies = new ClimbBoxStrategy[] { new NoClimb(this), new BoostClimb(this) };

		this.timer = new Timer(ClimbUpBox.PERIOD);
		this.timer.offset(offset);

	}

	@Override
	public void logicLoop() {

		// update timer
		this.timer.logicLoop();
		// Get new strategy from strategy map
		if (this.timer.timeUp()) {
			this.current = (this.current + 1) % this.strategies.length;
			this.timer.reset();
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.strategies[this.current].reactToCollision(element, dir);
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);
		f.drawRectangle(this.getPos().addY(-0.1f), this.getSize().scalar(0.2f, 0.8f), ClimbUpBox.DARK_COLOR);
		f.drawRectangle(this.getPos().addY(0.4f), this.getSize().scalar(1, 0.2f), ClimbUpBox.DARK_COLOR);

		this.renderEnergy(f, //
				this.strategies[this.current].getEnergyStart(this.timer.getProgress()),
				this.strategies[this.current].getEnergyEnd(this.timer.getProgress()));
		this.strategies[this.current].internalRender(f);
	}

	/**
	 * Render the energy.
	 *
	 * @param f
	 *            the field
	 * @param start
	 *            the start level
	 * @param end
	 *            the end level
	 */
	public void renderEnergy(GameGrid f, float start, float end) {

		float h = end - start;
		f.drawRectangle(this.getPos().addY(h / 2f - this.getSize().getY() / 2f + start), this.getSize().scalar(0.2f, h), ClimbUpBox.ENERGY_COLOR);

		if (end == 1) {
			f.drawRectangle(this.getPos().addY(0.4f), this.getSize().scalar(1, 0.2f), ClimbUpBox.ENERGY_COLOR);
		}

	}

	@Override
	public ClimbUpBox create(Vec startPos, String[] options) {
		long offset = 0;
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-9]+")) {
			// TODO Check offset
			offset = (Long.parseLong(options[0]) * ClimbUpBox.PERIOD) / 2000;
		}
		return new ClimbUpBox(startPos, offset);
	}

	/**
	 * This class is the base class for all different behaviors of a
	 * {@link ClimbUpBox}.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	private abstract class ClimbBoxStrategy {
		/**
		 * The parent.
		 */
		protected ClimbUpBox parent;

		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		ClimbBoxStrategy(ClimbUpBox parent) {
			this.parent = parent;
		}

		/**
		 * Same as {@link ClimbUpBox#reactToCollision(GameElement, Direction)}.
		 *
		 * @param element
		 *            the element
		 * @param dir
		 *            the direction
		 */
		public void reactToCollision(GameElement element, Direction dir) {
			ClimbUpBox.this.innerBox.reactToCollision(element, dir);
		}

		/**
		 * Same as {@link ClimbUpBox#internalRender(GameGrid)}.
		 *
		 * @param f
		 *            the field
		 */
		public void internalRender(GameGrid f) {

		}

		/**
		 * Get the energy's start level.
		 *
		 * @param progress
		 *            the progress
		 * @return the calculated level
		 */
		public abstract float getEnergyStart(float progress);

		/**
		 * Get the energy's end level.
		 *
		 * @param progress
		 *            the progress
		 * @return the calculated level
		 */
		public abstract float getEnergyEnd(float progress);
	}

	/**
	 * The default strategy: A normal {@link InanimateBox}.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	private class NoClimb extends ClimbBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		NoClimb(ClimbUpBox parent) {
			super(parent);
		}

		@Override
		public float getEnergyStart(float progress) {
			return 0;
		}

		@Override
		public float getEnergyEnd(float progress) {
			return progress;
		}
	}

	/**
	 * The boost strategy: A Climbing is possible.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	private class BoostClimb extends ClimbBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		BoostClimb(ClimbUpBox parent) {
			super(parent);
		}

		@Override
		public void internalRender(GameGrid f) {
			f.drawRectangle(ClimbUpBox.this.getPos().addY(0.4f), ClimbUpBox.this.getSize().scalar(1, 0.2f), ClimbUpBox.ENERGY_COLOR);

			Vec pos = this.parent.getPos().addY(this.parent.getSize().getY() / 2f + 1).addX(-0.5f);
			pos = pos.addX(this.parent.getSize().getX() * GameConf.PRNG.nextFloat());

			ClimbUpBox.PARTICLES.spawn(this.parent.getScene(), pos);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
			if (element.getTeam() == Team.PLAYER && dir == Direction.DOWN) {
				element.setVel(element.getVel().addY(4 * GameConf.PLAYER_BOTTOM_BOOST));
			}

		}

		@Override
		public float getEnergyStart(float progress) {
			return progress;
		}

		@Override
		public float getEnergyEnd(float progress) {
			return 1;
		}
	}

}
