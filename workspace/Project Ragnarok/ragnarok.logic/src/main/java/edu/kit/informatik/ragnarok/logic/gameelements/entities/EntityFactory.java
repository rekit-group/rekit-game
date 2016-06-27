package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.HashMap;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.Rocket;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.Slurp;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.Warper;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups.Coin;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups.Life;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class EntityFactory {

	private static Scene scene;

	public static void init(Scene scene) {
		EntityFactory.scene = scene;
	}

	private static final HashMap<Integer, Entity> prototypes = new HashMap<Integer, Entity>() {
		/**
		 * SUID
		 */
		private static final long serialVersionUID = -4277174101128620924L;

		{
			this.put(2, new RektKiller());
			this.put(3, new Rocket());
			this.put(4, new Warper());
			this.put(5, new Slurp());
			this.put(10, new Coin());
			this.put(20, new Life());
		}
	};

	public static void generate(int entityId, int x, int y) {
		Entity entity = EntityFactory.prototypes.get(entityId);
		if (entity != null) {
			// Add enemy to model
			EntityFactory.scene.addGameElement(entity.create(new Vec(x, y)));
		}

	}

}
