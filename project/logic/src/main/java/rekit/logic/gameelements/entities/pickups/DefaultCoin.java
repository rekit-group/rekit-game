package rekit.logic.gameelements.entities.pickups;

import rekit.logic.gameelements.type.Coin;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.image.RGBColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class represents default coins.
 * 
 *
 */
@LoadMe
public class DefaultCoin extends Coin {
	/**
	 * Prototype constructor.
	 */
	public DefaultCoin() {
		super();
	}

	/**
	 * Constructor with position.
	 *
	 * @param startPos
	 *            the position.
	 */
	protected DefaultCoin(Vec startPos) {
		super(startPos);
	}

	/**
	 * The default color of the coin.
	 */
	private static RGBColor color = new RGBColor(232, 214, 16);
	/**
	 * The shadow color of the coin.
	 */
	private static RGBColor darkColor = new RGBColor(192, 174, 6);

	@Override
	protected RGBAColor getColor() {
		return DefaultCoin.color.toRGBA();
	}

	@Override
	protected RGBAColor getDarkerColor() {
		return DefaultCoin.darkColor.toRGBA();
	}

	@Override
	protected int getValue() {
		return 10;
	}

	@Override
	public Coin create(Vec startPos, String[] options) {
		return new DefaultCoin(startPos);
	}
}
