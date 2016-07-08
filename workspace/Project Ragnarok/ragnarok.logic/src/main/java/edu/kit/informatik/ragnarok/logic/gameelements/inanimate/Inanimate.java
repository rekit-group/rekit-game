package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class Inanimate extends GameElement {

	private static Inanimate instance;

	protected RGBColor color;

	protected Inanimate(Vec pos, Vec size, RGBColor color) {
		super(pos, new Vec(), size, Team.NEUTRAL);
		this.color = color;
	}

	public static final Set<DynamicInanimate> getInanimatePrototypes() {
		return ReflectUtils.get("edu.kit.informatik", DynamicInanimate.class);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize().multiply(0.95f), this.color);
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
	public Inanimate create(Vec pos) {
		if (pos.getY() + 1 >= GameConf.GRID_H) {
			return InanimateFloor.staticCreate(pos);
		} else {
			return InanimateBox.staticCreate(pos);
		}
	}

	public static Inanimate getPrototype() {
		if (Inanimate.instance == null) {
			Inanimate.instance = new Inanimate(new Vec(), new Vec(1, 1), new RGBColor(0, 0, 0));
		}
		return Inanimate.instance;
	}

	public int getID() {
		return 1;
	}
}
