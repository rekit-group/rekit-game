package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Pickup;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class Coin extends Pickup {
	public Coin() {
		super();
	}

	public Coin(Vec startPos) {
		super(startPos);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHostile(element)) {
			element.addPoints(10);
			this.addDamage(1);
		}
	}

	@Override
	public Vec getSize() {
		return new Vec(0.7f, 0.7f);
	}

	@Override
	public void internalRender(Field f) {
		RGBColor color = new RGBColor(232, 214, 16);
		RGBColor darkColor = new RGBColor(192, 174, 6);

		double sin = Math.sin((System.currentTimeMillis() / 300.0));
		Vec size = this.getSize().multiply((float) sin, 1);

		for (float x = -0.025f; x <= 0.025f; x += 0.005f) {
			f.drawCircle(this.getPos().addX(x), size, color);
		}
		if (sin < 0) {
			f.drawCircle(this.getPos().addX(-0.03f), size, darkColor);
		}
		if (sin > 0) {
			f.drawCircle(this.getPos().addX(0.03f), size, darkColor);
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
