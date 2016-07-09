package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class ToggleBox extends DynamicInanimate {

	protected InanimateBox innerBox;

	protected static final long PERIOD = 4000;

	protected long offset = 0;

	private Map<Boolean, ToggleBoxStrategy> strategies;

	private ToggleBoxStrategy currentStrategy;

	/**
	 * Prototype Constructor
	 */
	public ToggleBox() {
		super();
	}

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
	public void logicLoop(float deltaTime) {
		// Get new strategy from strategy map
		this.currentStrategy = this.strategies.get((this.scene.getTime() + this.offset) % ToggleBox.PERIOD < ToggleBox.PERIOD / 2);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.currentStrategy.reactToCollision(element, dir);
	}

	@Override
	public void internalRender(Field f) {
		this.currentStrategy.internalRender(f);
	}

	@Override
	public int getID() {
		return 80;
	}

	@Override
	public ToggleBox create(Vec startPos, int[] options) {
		ToggleBox inst = new ToggleBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255));
		if (options.length >= 1) {
			if (options[0] == 1) {
				inst.offset = ToggleBox.PERIOD / 2;
			}
		}
		return inst;
	}

	private abstract class ToggleBoxStrategy {
		protected ToggleBox parent;

		ToggleBoxStrategy(ToggleBox parent) {
			this.parent = parent;
		}

		public void reactToCollision(GameElement element, Direction dir) {
			// Do nothing
		}

		public abstract int getAlpha();

		public void internalRender(Field f) {
			// Fill alpha of color with value given of
			RGBAColor c = this.parent.color;
			this.parent.innerBox.color = new RGBAColor(c.red, c.green, c.blue, this.getAlpha());

			// Call decorated InanimateBoxes internalRender
			this.parent.innerBox.internalRender(f);
		}
	}

	private class ToggleBoxStrategyVisible extends ToggleBoxStrategy {

		ToggleBoxStrategyVisible(ToggleBox parent) {
			super(parent);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
		}

		@Override
		public int getAlpha() {
			return 220;
		}
	}

	private class ToggleBoxStrategyInvisible extends ToggleBoxStrategy {
		ToggleBoxStrategyInvisible(ToggleBox parent) {
			super(parent);
		}

		@Override
		public int getAlpha() {
			return 80;
		}
	}

}
