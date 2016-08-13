package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class defines a simple {@link Pickup}; a <b>Coin</b> which will give the
 * player points
 *
 *
 */
@LoadMe
public class Coin extends Pickup {
	/**
	 * Prototype Constructor
	 */
	public Coin() {
		super();
	}

	/**
	 * The default color of the coin
	 */
	private static RGBColor color = new RGBColor(232, 214, 16);
	/**
	 * The shadow color of the coin
	 */
	private static RGBColor darkColor = new RGBColor(192, 174, 6);
	/**
	 * The X-Coordinate
	 */
	private float x = 0;
	/**
	 * Sin(X) for spinning coin
	 */
	private double sin = 0;

	/**
	 * Instantiate a Coin by position
	 *
	 * @param startPos
	 *            the start position
	 */
	public Coin(Vec startPos) {
		super(startPos, new Vec(), new Vec(0.7f, 0.7f));
	}

	@Override
	public void perform(GameElement collector) {
		collector.addPoints(10);
		this.addDamage(1);
	}

	@Override
	public void logicLoop(float renderDelta) {
		this.x += renderDelta;
		this.sin = Math.sin(this.x * 3);
		this.setSize(new Vec((float) (0.7f * this.sin), 0.7f));
	}

	@Override
	public void internalRender(Field f) {

		for (float x = -0.020f; x <= 0.020f; x += 0.005f) {
			f.drawCircle(this.getPos().addX(x), this.getSize(), Coin.color);
		}
		if (this.sin < 0) {
			f.drawCircle(this.getPos().addX(-0.03f), this.getSize(), Coin.darkColor);
		}
		if (this.sin > 0) {
			f.drawCircle(this.getPos().addX(0.03f), this.getSize(), Coin.darkColor);
		}

	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		return new Coin(startPos);
	}

}
