package edu.kit.informatik.ragnarok.logic.parallax;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class BackgroundElement extends GameElement {

	protected ParallaxLayer parent;

	protected int backgroundZ;

	public BackgroundElement(ParallaxLayer parent, Vec pos) {
		super(pos.setZ(parent == null ? 1 : parent.perspectiveZ), new Vec(), new Vec(1), Team.NEUTRAL);
		this.parent = parent;
		this.backgroundZ = -10000;
	}

	@Override
	public int getOrderZ() {
		// no actual scaling, but useful
		return (int) this.parent.fieldXtoLayerX(this.backgroundZ);
	}

	@Override
	public void addPoints(int points) {
		// Do nothing
	}

	@Override
	public int getPoints() {
		// Do nothing
		return 0;
	}

	@Override
	public void addDamage(int damage) {
		// Do nothing
	}

	@Override
	public int getLifes() {
		// Do nothing
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing
	}

}
