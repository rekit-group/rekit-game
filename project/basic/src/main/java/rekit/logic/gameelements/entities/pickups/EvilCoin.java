package rekit.logic.gameelements.entities.pickups;

import rekit.core.GameGrid;
import rekit.logic.gameelements.type.Coin;
import rekit.logic.gameelements.type.Pickup;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class defines a simple {@link Pickup}; an <b>EvilCoin</b> which will
 * give the player negative points.
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
@LoadMe
public final class EvilCoin extends Coin {

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
		return null;
	}

	@Override
	protected RGBAColor getDarkerColor() {
		return null;
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawImage(this.getPos().addY((float) (this.sin * 0.1)), new Vec(1, 1), "evilCoin.png");
	}

	@Override
	public Coin create(Vec startPos, String[] options) {
		return new EvilCoin(startPos);
	}

	@Override
	protected int getValue() {
		return -30;
	}

}
