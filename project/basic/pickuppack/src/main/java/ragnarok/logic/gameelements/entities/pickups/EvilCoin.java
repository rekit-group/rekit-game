package ragnarok.logic.gameelements.entities.pickups;

import ragnarok.logic.gameelements.type.Coin;
import ragnarok.logic.gameelements.type.Pickup;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.primitives.image.RGBColor;
import ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class defines a simple {@link Pickup}; a <b>EvilCoin</b> which will give
 * the player negative points.
 * 
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class EvilCoin extends Coin {
	/**
	 * The default color of the coin.
	 */
	private static RGBColor color = new RGBColor(232, 50, 16);
	/**
	 * The shadow color of the coin.
	 */
	private static RGBColor darkColor = new RGBColor(192, 25, 6);

	/**
	 * Prototype constructor.
	 */
	public EvilCoin() {
		super();
	}

	/**
	 * Constructor with position.
	 *
	 * @param startPos
	 *            the position.
	 */
	protected EvilCoin(Vec startPos) {
		super(startPos);
	}

	@Override
	protected RGBAColor getColor() {
		return EvilCoin.color.toRGBA();
	}

	@Override
	protected RGBAColor getDarkerColor() {
		return EvilCoin.darkColor.toRGBA();
	}

	@Override
	public Coin create(Vec startPos, String[] options) {
		return new EvilCoin(startPos);
	}

	@Override
	protected int getValue() {
		return -25;
	}

}
