package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.Random;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;

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
		}
		
		if (enemy != null) {
			// Add enemy to model
			model.addGameElement(enemy);	
		}
		
	}
	
}
