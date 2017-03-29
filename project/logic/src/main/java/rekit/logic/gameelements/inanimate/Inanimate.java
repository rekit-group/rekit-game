package rekit.logic.gameelements.inanimate;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;

/**
 *
 * This is the default implementation of an inanimate in the game.
 *
 */
public class Inanimate extends GameElement {
	/**
	 * The prototype.
	 */
	private static Inanimate instance;
	/**
	 * The color.
	 */
	protected RGBAColor color;

	/**
	 * Create an inanimate.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param team
	 *            the team
	 */
	protected Inanimate(Vec pos, Vec size, RGBAColor color, Team team) {
		super(pos, new Vec(), size, team);
		this.color = color;
	}

	/**
	 * Create an inanimate.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	protected Inanimate(Vec pos, Vec size, RGBAColor color) {
		this(pos, size, color, Team.INANIMATE);
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.95f), this.color);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		element.collidedWith(this.getCollisionFrame(), dir);
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
		if (startPos.y + 1 >= GameConf.GRID_H) {
			return InanimateFloor.staticCreate(startPos);
		} else {
			return InanimateBox.staticCreate(startPos);
		}
	}

	/**
	 * Get the inanimate-prototype.
	 *
	 * @return the prototype
	 */
	public static synchronized Inanimate getPrototype() {
		if (Inanimate.instance == null) {
			Inanimate.instance = new Inanimate(new Vec(), new Vec(1, 1), new RGBAColor(0, 0, 0, 0));
		}
		return Inanimate.instance;
	}

}
