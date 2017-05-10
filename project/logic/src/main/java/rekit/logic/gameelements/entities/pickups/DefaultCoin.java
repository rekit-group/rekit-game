package rekit.logic.gameelements.entities.pickups;

import rekit.logic.gameelements.type.Coin;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
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
	private static RGBAColor color = new RGBAColor(232, 214, 16);
	/**
	 * The shadow color of the coin.
	 */
	private static RGBAColor darkColor = new RGBAColor(192, 174, 6);

	@Override
	protected RGBAColor getColor() {
		return DefaultCoin.color;
	}

	@Override
	protected RGBAColor getDarkerColor() {
		return DefaultCoin.darkColor;
	}

	@Override
	protected int getValue() {
		return 10;
	}

	@Override
	public DefaultCoin create(Vec startPos, String... options) {
		return new DefaultCoin(startPos);
	}
}
