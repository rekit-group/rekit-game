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
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.primitives.time.Timer;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.annotations.NoVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.VisitInfo;

/**
 * This Box realizes an {@link Inanimate} which the Player can climb up.
 *
 * @author Dominik Fuchß
 *
 */
@LoadMe
@VisitInfo(res = "conf/climbup", visit = true)
public final class ClimbUpBox extends DynamicInanimate {
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
	private ClimbBoxStrategy[] strategies;
	/**
	 * The current strategy.
	 */
	@NoVisit
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
	@NoVisit
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
	 *            the initial time offset
	 */
	protected ClimbUpBox(Vec pos, long offset) {
		super(pos, new Vec(1), ClimbUpBox.OUTER_COLOR);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);

		// instantiate the two strategies
		this.strategies = new ClimbBoxStrategy[] { new NoClimb(this), new BoostClimb(this) };

		this.offset = offset;
		this.timer = new Timer(ClimbUpBox.PERIOD);
	}

	/**
	 * The last time {@link #logicLoop(float)} was invoked or {@code -1}
	 * (initial).
	 */
	@NoVisit
	private long lastTime = -1;

	@Override
	public void logicLoop() {

		// get time
		long nowTime = GameTime.getTime();
		// init lastTime in first run
		if (this.lastTime == -1) {
			this.lastTime = nowTime - this.offset - ((nowTime) % ClimbUpBox.PERIOD);
			if ((nowTime / ClimbUpBox.PERIOD) % 2 == 0) {
				this.current = (this.current + 1) % this.strategies.length;
			}
		}
		// update timer
		this.timer.logicLoop();
		// this.timer.removeTime(nowTime - this.lastTime);
		// save current time for next iteration
		this.lastTime = nowTime;

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
	public void internalRender(Field f) {
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
	public void renderEnergy(Field f, float start, float end) {

		float h = end - start;
		f.drawRectangle(this.getPos().addY(h / 2f - this.getSize().getY() / 2f + start), this.getSize().scalar(0.2f, h), ClimbUpBox.ENERGY_COLOR);

		if (end == 1) {
			f.drawRectangle(this.getPos().addY(0.4f), this.getSize().scalar(1, 0.2f), ClimbUpBox.ENERGY_COLOR);
		}

	}

	@Override
	public ClimbUpBox create(Vec startPos, String[] options) {
		long offset = 0;
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-9]")) {
			offset = Integer.parseInt(options[0]) * ClimbUpBox.PERIOD / 2;
		}
		return new ClimbUpBox(startPos, offset);
	}

	/**
	 * This class is the base class for all different behaviors of a
	 * {@link ClimbUpBox}.
	 *
	 * @author Dominik Fuchß
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
		 * Same as {@link ClimbUpBox#internalRender(Field)}.
		 *
		 * @param f
		 *            the field
		 */
		public void internalRender(Field f) {

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
	 * @author Dominik Fuchß
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
	 * @author Dominik Fuchß
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
		public void internalRender(Field f) {
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
