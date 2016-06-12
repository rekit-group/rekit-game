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
	
	public static void generate(int x, int y) {
		Random r = new Random();
		
		// Create Enemy
		Entity enemy = new RektKiller(new Vec2D(x, y), r.nextInt(6));
		
		// Move to the very bottom to prevent initial falling
		float moveY = (1 - enemy.getSize().getY()) / 2f;
		float moveX = (1 - enemy.getSize().getX()) / 2f;
		enemy.setPos(enemy.getPos().addY(moveY).addX(moveX)); 
		
		// Add enemy to model
		model.addGameElement(enemy);
	}
	
}
