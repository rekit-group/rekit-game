package rekit.logic.gameelements.inanimate;

import java.util.HashMap;
import java.util.Map;

import rekit.core.GameGrid;
import rekit.core.GameTime;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.type.DynamicInanimate;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class realizes a box which toggles between solid an not-solid state from
 * time to time.
 *
 */
@LoadMe
public final class ToggleBox extends DynamicInanimate {
	/**
	 * The inner {@link InanimateBox}.
	 */
	InanimateBox innerBox;
	/**
	 * The period between toggling.
	 */
	private static final long PERIOD = 4000;
	/**
	 * The current time offset.
	 */
	private long offset = 0;
	/**
	 * The different strategies.
	 */
	private Map<Boolean, ToggleBoxStrategy> strategies;
	/**
	 * The currently activated strategy.
	 */
	private ToggleBoxStrategy currentStrategy;

	/**
	 * Prototype Constructor.
	 */
	public ToggleBox() {
		super();
	}

	/**
	 * Create a ToggleBox.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	protected ToggleBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);
		// and set color
		this.innerBox.color = new RGBAColor(80, 80, 255, 255);

		// instantiate the two strategies
		this.strategies = new HashMap<>();
		this.strategies.put(true, new ToggleBoxStrategyVisible(this));
		this.strategies.put(false, new ToggleBoxStrategyInvisible(this));
		this.currentStrategy = this.strategies.get(true);
	}

	@Override
	public void logicLoop() {
		// Get new strategy from strategy map
		this.currentStrategy = this.strategies.get((GameTime.getTime() + this.offset) % ToggleBox.PERIOD < ToggleBox.PERIOD / 2);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.currentStrategy.reactToCollision(element, dir);
	}

	@Override
	public void internalRender(GameGrid f) {
		this.currentStrategy.internalRender(f);
	}

	@Override
	public ToggleBox create(Vec startPos, String... options) {
		ToggleBox inst = new ToggleBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255));
		if (options.length >= 1) {
			if (options[0].equals("1")) {
				inst.offset = ToggleBox.PERIOD / 2;
			}
		}
		return inst;
	}

	/**
	 * This class is the base class for all different behaviors of a
	 * {@link ToggleBox}.
	 *
	 *
	 */
	private abstract class ToggleBoxStrategy {
		/**
		 * The parent.
		 */
		protected ToggleBox parent;

		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		ToggleBoxStrategy(ToggleBox parent) {
			this.parent = parent;
		}

		/**
		 * Same as {@link ToggleBox#reactToCollision(GameElement, Direction)}.
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
		 * Get the current alpha value of the color.
		 *
		 * @return the alpha value [0;255]
		 */
		public abstract int getAlpha();

		/**
		 * Same as {@link ToggleBox#internalRender(GameGrid)}.
		 *
		 * @param f
		 *            the field
		 */
		public void internalRender(GameGrid f) {
			// Fill alpha of color with value given of
			RGBAColor c = this.parent.color;
			this.parent.innerBox.color = new RGBAColor(c.red, c.green, c.blue, this.getAlpha());

			// Call decorated InanimateBoxes internalRender
			this.parent.innerBox.internalRender(f);
		}
	}

	/**
	 *
	 * This is the strategy for the visible time of the {@link ToggleBox} While
	 * this is the strategy of the ToggleBox it's a solid block.
	 */
	private class ToggleBoxStrategyVisible extends ToggleBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */

		ToggleBoxStrategyVisible(ToggleBox parent) {
			super(parent);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWithSolid(this.parent.getFrame(), dir);
		}

		@Override
		public int getAlpha() {
			return 220;
		}
	}

	/**
	 *
	 * This is the strategy for the invisible time of the {@link ToggleBox}
	 * While this is the strategy of the ToggleBox it isn't a solid block.
	 */
	private class ToggleBoxStrategyInvisible extends ToggleBoxStrategy {
		/**
		 * Create strategy by parent.
		 *
		 * @param parent
		 *            the parent
		 */
		ToggleBoxStrategyInvisible(ToggleBox parent) {
			super(parent);
		}

		@Override
		public int getAlpha() {
			return 80;
		}
	}

}
