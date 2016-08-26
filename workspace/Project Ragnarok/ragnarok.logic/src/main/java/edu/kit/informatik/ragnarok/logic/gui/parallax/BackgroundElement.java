package edu.kit.informatik.ragnarok.logic.gui.parallax;

import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

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
		return this.backgroundZ - (int) this.parent.fieldXtoLayerX(1000);
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
	public int getLives() {
		// Do nothing
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing
	}

}
