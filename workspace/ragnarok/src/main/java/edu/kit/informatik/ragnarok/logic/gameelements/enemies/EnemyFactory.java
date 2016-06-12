package edu.kit.informatik.ragnarok.logic.gameelements.enemies;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;

public class EnemyFactory {
	
	private static GameModel model;
	
	public static void init(GameModel model) {
		EnemyFactory.model = model;
	}
	
	public static void generate(int x, int y) {
		// Create Enemy
		Entity enemy = new TryHardTriangle();
		enemy.setPos(new Vec2D(x, y));
		
		// Add enemy to model
		model.addGameElement(enemy);
	}
	
}
