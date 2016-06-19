package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.Random;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups.*;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class EnemyFactory {

	private static GameModel model;

	public static void init(GameModel model) {
		EnemyFactory.model = model;
	}

	public static void generate(int enemyId, int x, int y) {
		Random r = new Random();

		// Create Enemy
		Entity enemy = null;
		switch (enemyId) {
		case 2:
			enemy = new RektKiller(new Vec2D(x, y), r.nextInt(16));
			break;
		case 3:
			enemy = new Rocket(new Vec2D(x, y));
			break;
		case 4:
			enemy = new Warper(new Vec2D(x, y));
			break;
		case 5:
			enemy = new Slurp(new Vec2D(x, y));
			break;

		case 10:
			enemy = new Coin(new Vec2D(x, y));
			break;
		case 20:
			enemy = new Life(new Vec2D(x, y));
			break;
		default:
			enemy = null;
		}

		if (enemy != null) {
			// Add enemy to model
			EnemyFactory.model.addGameElement(enemy);
		}

	}

}
