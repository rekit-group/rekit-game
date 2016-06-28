package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.HashMap;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class EntityFactory {

	private static Scene scene;

	public static void init(Scene scene) {
		EntityFactory.scene = scene;
	}

	private static final HashMap<Integer, Entity> prototypes = EntityFactory.load();

	public static void generate(int entityId, int x, int y) {
		Entity entity = EntityFactory.prototypes.get(entityId);
		if (entity != null) {
			// Add enemy to model
			EntityFactory.scene.addGameElement(entity.create(new Vec(x, y)));
		}

	}

	private final synchronized static HashMap<Integer, Entity> load() {
		if (EntityFactory.prototypes != null) {
			return EntityFactory.prototypes;
		}
		HashMap<Integer, Entity> hm = new HashMap<>();
		for (Entity e : Enemy.getBossPrototypes()) {
			hm.put(e.getID(), e);
		}
		for (Entity e : Pickup.getPickupPrototypes()) {
			hm.put(e.getID(), e);
		}
		return hm;
	}

}
