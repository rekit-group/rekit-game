package rekit.logic.gameelements.entities.pickups;

import rekit.logic.gameelements.type.Coin;
import rekit.logic.gameelements.type.Pickup;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class defines a simple {@link Pickup}; a <b>SuperCoin</b> which will
 * give the player more points than the {@link DefaultCoin}
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
@LoadMe
public final class SuperCoin extends Coin {
	/**
	 * The default color of the coin.
	 */
	private static RGBAColor color = new RGBAColor(232, 50, 16);
	/**
	 * The shadow color of the coin.
	 */
	private static RGBAColor darkColor = new RGBAColor(192, 25, 6);

	/**
	 * Prototype constructor.
	 */
	public SuperCoin() {
		super();
	}

	/**
	 * Constructor with position.
	 *
	 * @param startPos
	 *            the position.
	 */
	protected SuperCoin(Vec startPos) {
		super(startPos);
	}

	@Override
	protected RGBAColor getColor() {
		return SuperCoin.color;
	}

	@Override
	protected RGBAColor getDarkerColor() {
		return SuperCoin.darkColor;
	}

	@Override
	public SuperCoin create(Vec startPos, String... options) {
		return new SuperCoin(startPos);
	}

	@Override
	protected int getValue() {
		return 30;
	}

}
