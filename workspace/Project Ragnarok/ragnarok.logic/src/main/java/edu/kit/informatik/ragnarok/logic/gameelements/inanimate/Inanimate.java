package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class Inanimate extends GameElement {

	protected RGBColor color;

	public Inanimate(Vec pos, Vec size, RGBColor color) {
		super(pos, new Vec(), size, Team.NEUTRAL);
		this.color = color;
	}

	@Override
	public void internalRender(Field f) {
		Vec pos = this.getPos();
		f.drawRectangle(pos, this.getSize().multiply(0.95f), this.color);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		element.collidedWith(this.getCollisionFrame(), dir);
	}

	@Override
	public void addDamage(int damage) {
		// Do nothing, blocks cannot be damaged
	}

	@Override
	public void addPoints(int points) {
		// Do nothing, blocks cannot get points
	}

	@Override
	public int getPoints() {
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, blocks cannot collide
	}

	@Override
	public int getLifes() {
		return 0;
	}

}
