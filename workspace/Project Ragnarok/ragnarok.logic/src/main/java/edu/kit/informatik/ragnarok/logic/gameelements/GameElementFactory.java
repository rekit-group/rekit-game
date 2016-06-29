package edu.kit.informatik.ragnarok.logic.gameelements;

import java.util.HashMap;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Enemy;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Pickup;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class GameElementFactory {

	private static Scene scene;

	public static void init(Scene scene) {
		GameElementFactory.scene = scene;
	}

	private static final HashMap<Integer, GameElement> prototypes = GameElementFactory.load();

	public static void generate(int id, int x, int y) {
		GameElement element = GameElementFactory.prototypes.get(id);
		if (element != null) {
			// Add enemy to model
			GameElementFactory.scene.addGameElement(element.create(new Vec(x, y)));
		}

	}

	private final synchronized static HashMap<Integer, GameElement> load() {
		if (GameElementFactory.prototypes != null) {
			return GameElementFactory.prototypes;
		}
		HashMap<Integer, GameElement> hm = new HashMap<>();
		hm.put(1, Inanimate.getPrototype());
		for (Entity e : Enemy.getEnemyPrototypes()) {
			hm.put(e.getID(), e);
		}
		for (Entity e : Pickup.getPickupPrototypes()) {
			hm.put(e.getID(), e);
		}
		return hm;
	}

}
