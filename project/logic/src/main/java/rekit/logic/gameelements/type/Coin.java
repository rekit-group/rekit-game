package rekit.logic.gameelements.type;

import java.util.Set;

import net.jafama.FastMath;
import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.persistence.ModManager;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils;

/**
 * This class defines a simple {@link Pickup}; a <b>Coin</b> which will give the
 * player points.
 *
 *
 */
@Group
public abstract class Coin extends Pickup {
	/**
	 * Get a set of coin prototypes.
	 *
	 * @return a set of prototypes
	 */
	public static final Set<? extends GameElement> getPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, ModManager.SYSLOADER, Coin.class);
	}

	/**
	 * Prototype Constructor.
	 */
	protected Coin() {
		super();
	}

	/**
	 * The X-Coordinate.
	 */
	private float x = 0;
	/**
	 * Sin(X) for spinning coin.
	 */
	protected double sin = 0;

	/**
	 * Instantiate a Coin by position.
	 *
	 * @param startPos
	 *            the start position
	 */
	protected Coin(Vec startPos) {
		super(startPos, new Vec(), new Vec(0.7f, 0.7f));
	}

	@Override
	public void perform(GameElement collector) {
		this.getScene().getPlayer().addPoints(this.getValue());
		this.addDamage(1);
	}

	@Override
	protected void innerLogicLoop() {
		this.x += this.deltaTime / 1000F;
		this.sin = FastMath.sinQuick(this.x * 3);
		this.setSize(new Vec((float) (0.7f * this.sin), 0.7f));
	}

	@Override
	public void internalRender(GameGrid f) {

		for (float x = -0.020f; x <= 0.020f; x += 0.005f) {
			f.drawCircle(this.getPos().addX(x), this.getSize(), this.getColor());
		}
		if (this.sin < 0) {
			f.drawCircle(this.getPos().addX(-0.03f), this.getSize(), this.getDarkerColor());
		}
		if (this.sin > 0) {
			f.drawCircle(this.getPos().addX(0.03f), this.getSize(), this.getDarkerColor());
		}

	}

	/**
	 * Get the main color of the coin.
	 *
	 * @return the main color
	 */
	protected abstract RGBAColor getColor();

	/**
	 * Get a darker version of the color {@link #getColor()} for the edge of the
	 * coin.
	 *
	 * @return the darker color
	 */
	protected abstract RGBAColor getDarkerColor();

	/**
	 * Get the value of the coin.
	 *
	 * @return the value
	 */
	protected abstract int getValue();

	@Override
	public abstract Coin create(Vec startPos, String... options);

}
