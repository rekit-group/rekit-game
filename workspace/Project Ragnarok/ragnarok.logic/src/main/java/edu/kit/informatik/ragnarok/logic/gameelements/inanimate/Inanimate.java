package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

public class Inanimate extends GameElement {

	private static Inanimate instance;

	protected RGBAColor color;

	protected Inanimate(Vec pos, Vec size, RGBAColor color, Team team) {
		super(pos, new Vec(), size, team);
		this.color = color;
	}

	protected Inanimate(Vec pos, Vec size, RGBAColor color) {
		this(pos, size, color, Team.INANIMATE);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.95f), this.color);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		element.collidedWith(this.getCollisionFrame(), dir);
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
	public int getLives() {
		return 0;
	}

	@Override
	public Inanimate create(Vec startPos, String[] options) {
		if (startPos.getY() + 1 >= GameConf.GRID_H) {
			return InanimateFloor.staticCreate(startPos);
		} else {
			return InanimateBox.staticCreate(startPos);
		}
	}

	public synchronized static Inanimate getPrototype() {
		if (Inanimate.instance == null) {
			Inanimate.instance = new Inanimate(new Vec(), new Vec(1, 1), new RGBAColor(0, 0, 0, 0));
		}
		return Inanimate.instance;
	}

}
