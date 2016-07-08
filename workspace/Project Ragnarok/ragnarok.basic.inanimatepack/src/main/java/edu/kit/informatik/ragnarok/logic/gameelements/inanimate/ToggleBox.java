package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class ToggleBox extends DynamicInanimate {

	protected InanimateBox innerBox;

	protected static final long PERIOD = 3000;

	protected long offset = 0;

	private Map<Boolean, ToggleBoxStrategy> strategies;

	private ToggleBoxStrategy currentStrategy;

	/**
	 * Prototype Constructor
	 */
	public ToggleBox() {
		super();
	}

	protected ToggleBox(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);
		// and set color
		this.innerBox.color = new RGBColor(80, 80, 255);

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
	public ToggleBox create(Vec startPos) {
		return new ToggleBox(startPos, new Vec(1), new RGBColor(80, 80, 255));
	}

	private abstract class ToggleBoxStrategy {
		protected ToggleBox parent;

		ToggleBoxStrategy(ToggleBox parent) {
			this.parent = parent;
		}

		public void reactToCollision(GameElement element, Direction dir) {
			// Do nothing
		}

		public void internalRender(Field f) {
			// Do nothing
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
		public void internalRender(Field f) {
			this.parent.innerBox.internalRender(f);
		}
	}

	private class ToggleBoxStrategyInvisible extends ToggleBoxStrategy {
		ToggleBoxStrategyInvisible(ToggleBox parent) {
			super(parent);
		}
	}

}
