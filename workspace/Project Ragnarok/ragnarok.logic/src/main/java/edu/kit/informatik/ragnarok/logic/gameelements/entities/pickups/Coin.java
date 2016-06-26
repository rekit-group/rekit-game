package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class Coin extends Entity {
	public Coin() {
		super(Team.PICKUP, null);
		this.size = Vec2D.create(0.7f, 0.7f);
	}

	public Coin(Vec2D startPos) {
		super(Team.PICKUP, startPos);
		this.size = Vec2D.create(0.7f, 0.7f);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			element.addPoints(10);
			this.addDamage(1);
		}
	}

	@Override
	public void internRender(Field f) {
		RGBColor color = new RGBColor(232, 214, 16);
		RGBColor darkColor = new RGBColor(192, 174, 6);

		double sin = Math.sin((System.currentTimeMillis() / 300.0));
		Vec2D size = this.size.multiply((float) sin, 1);

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
	public void logicLoop(float deltaTime) {
		// no logic
	}

	@Override
	public Entity create(Vec2D startPos) {
		return new Coin(startPos);
	}

}
