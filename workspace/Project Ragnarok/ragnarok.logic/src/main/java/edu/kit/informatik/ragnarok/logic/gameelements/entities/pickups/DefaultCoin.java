package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.type.Coin;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class represents default coins.
 * 
 * @author Dominik Fuchß
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
