package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

public class MovingBox extends DynamicInanimate {

	private final static float SPEED = 0.3f;

	private Vec a;
	private Vec b;

	private TimeDependency timer;

	private Vec currentStart;
	private Vec relativeTarget;

	private RGBAColor darkCol;

	/**
	 * Prototype Constructor
	 */
	public MovingBox() {
		super();
	}

	protected MovingBox(Vec pos, int dist) {
		super(pos, new Vec(1, 0.3f), new RGBAColor(100, 100, 100, 255));

		// set starting and ending point
		this.a = pos.addY(-dist);
		this.b = pos.addY(dist);

		// set current starting and ending point
		this.currentStart = this.a;
		this.relativeTarget = this.b.add(this.currentStart.multiply(-1));

		// calculate accent color
		this.darkCol = this.color.darken(0.1f);

		// initialize movement timer
		this.timer = new TimeDependency(dist / (2 * MovingBox.SPEED));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (element.getVel().getY() > 0 && dir != Direction.LEFT && dir != Direction.RIGHT) {
			super.reactToCollision(element, Direction.UP);
		}
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.timer.removeTime(deltaTime);
		this.pos = this.currentStart.add(this.relativeTarget.multiply(this.timer.getProgress()));
		if (this.timer.timeUp()) {
			if (this.currentStart == this.a) {
				this.currentStart = this.b;
				this.relativeTarget = this.a.add(this.b.multiply(-1));
			} else {
				this.currentStart = this.a;
				this.relativeTarget = this.b.add(this.a.multiply(-1));
			}
			this.timer.reset();
		}
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);
	}

	@Override
	public int getID() {
		return 81;
	}

	@Override
	public MovingBox create(Vec startPos, int[] options) {
		int dist = 1;
		if (options.length >= 1) {
			dist = options[0];
		}
		MovingBox inst = new MovingBox(startPos, dist);
		return inst;
	}
}
