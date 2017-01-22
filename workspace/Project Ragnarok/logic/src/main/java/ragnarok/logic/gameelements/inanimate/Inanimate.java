package ragnarok.logic.gameelements.inanimate;

import ragnarok.config.GameConf;
import ragnarok.core.GameGrid;
import ragnarok.core.GameElement;
import ragnarok.core.Team;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Frame;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;

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

	/**
	 * Get the inanimate-prototype.
	 *
	 * @return the prototype
	 */
	public synchronized static Inanimate getPrototype() {
		if (Inanimate.instance == null) {
			Inanimate.instance = new Inanimate(new Vec(), new Vec(1, 1), new RGBAColor(0, 0, 0, 0));
		}
		return Inanimate.instance;
	}

}
