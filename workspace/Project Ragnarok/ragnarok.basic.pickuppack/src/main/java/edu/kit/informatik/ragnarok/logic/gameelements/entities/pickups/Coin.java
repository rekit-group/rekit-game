package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Pickup;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

/**
 * This class defines a simple {@link Pickup}; a <b>Coin</b> which will give the
 * player points
 *
 *
 */
public class Coin extends Pickup {
	/**
	 * Prototype Constructor
	 */
	public Coin() {
		super();
	}

	private static RGBColor color = new RGBColor(232, 214, 16);
	private static RGBColor darkColor = new RGBColor(192, 174, 6);
	private float x = 0;
	private double sin = 0;
	private Vec size = new Vec();

	public Coin(Vec startPos) {
		super(startPos, new Vec(), new Vec(0.7f, 0.7f));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.team.isHostile(element.getTeam())) {
			element.addPoints(10);
			this.addDamage(1);
		}
	}

	@Override
	public void logicLoop(float renderDelta) {
		this.x += renderDelta;
		this.sin = Math.sin(this.x * 3);
		this.size = new Vec((float) (0.7f * this.sin), 0.7f);
	}

	@Override
	public void internalRender(Field f) {

		for (float x = -0.020f; x <= 0.020f; x += 0.005f) {
			f.drawCircle(this.getPos().addX(x), this.size, Coin.color);
		}
		if (this.sin < 0) {
			f.drawCircle(this.getPos().addX(-0.03f), this.size, Coin.darkColor);
		}
		if (this.sin > 0) {
			f.drawCircle(this.getPos().addX(0.03f), this.size, Coin.darkColor);
		}

	}

	@Override
	public Entity create(Vec startPos) {
		return new Coin(startPos);
	}

	@Override
	public int getID() {
		return 10;
	}

}
