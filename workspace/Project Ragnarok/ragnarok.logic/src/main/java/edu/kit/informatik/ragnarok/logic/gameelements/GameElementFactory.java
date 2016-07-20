package edu.kit.informatik.ragnarok.logic.gameelements;

import java.util.HashMap;
import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.EndTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public class GameElementFactory {

	private static Scene scene;

	public static void init(Scene scene) {
		GameElementFactory.scene = scene;
		GameElementFactory.load();
	}

	private static HashMap<String, GameElement[]> prototypeTypes;

	private static HashMap<String, GameElement> prototypes;

	public static GameElement getPrototype(String id) {
		GameElement element = GameElementFactory.prototypes.get(id);
		if (element != null) {
			return element;
		}

		GameElement[] elemArray = GameElementFactory.prototypeTypes.get(id);
		if (elemArray != null && elemArray.length != 0) {
			return elemArray[GameConf.PRNG.nextInt(elemArray.length)];
		}
		System.err.println("Error in GameElementFactory: Tried to get Prototype of GameElement with unknown ID " + id);
		element = GameElementFactory.prototypes.get(InanimateBox.class.getName());
		return element;
	}

	public static void generate(String id, int x, int y) {
		GameElementFactory.generate(id, x, y, new String[] {});
	}

	public static void generate(String id, int x, int y, String[] modifiers) {
		GameElement prototype = GameElementFactory.getPrototype(id);
		if (prototype != null) {
			// Add enemy to model
			GameElementFactory.scene.addGameElement(prototype.create(new Vec(x, y), modifiers));
		}
	}

	public static void generate(GameElement element) {
		if (element != null) {
			// Add GameElement to model
			GameElementFactory.scene.addGameElement(element);
		}

	}

	private final synchronized static void load() {
		if (GameElementFactory.prototypes != null) {
			return;
		}
		HashMap<String, GameElement> protos = new HashMap<>();
		HashMap<String, GameElement[]> protoTypes = new HashMap<>();

		// Put Blocks in collection
		protos.put(Inanimate.getPrototype().getClass().getName(), Inanimate.getPrototype());
		protos.put(EndTrigger.getPrototype().getClass().getName(), EndTrigger.getPrototype());

		// Put all Inanimates in collection
		Set<DynamicInanimate> inanimatePrototypes = Inanimate.getInanimatePrototypes();
		for (DynamicInanimate e : inanimatePrototypes) {
			protos.put(e.getClass().getName(), e);
		}

		// Put all Bosses in collection
		Set<Boss> bossPrototypes = Boss.getBossPrototypes();
		for (Boss e : bossPrototypes) {
			protos.put(e.getClass().getName(), e);
		}

		// Put Enemies in collection and in separate array
		Set<Enemy> enemyPrototypes = Enemy.getEnemyPrototypes();
		Enemy[] enemyCollection = new Enemy[enemyPrototypes.size()];
		int i = 0;
		for (Enemy e : enemyPrototypes) {
			protos.put(e.getClass().getName(), e);
			enemyCollection[i++] = e;
		}
		protoTypes.put(Enemy.class.getName(), enemyCollection);

		// Put Pickups in collection and in separate array
		Set<Pickup> pickupPrototypes = Pickup.getPickupPrototypes();
		Pickup[] pickupCollection = new Pickup[pickupPrototypes.size()];
		i = 0;
		for (Pickup e : pickupPrototypes) {
			protos.put(e.getClass().getName(), e);
			pickupCollection[i++] = e;
		}
		protoTypes.put(Pickup.class.getName(), pickupCollection);

		// Save local data structures in static attributes
		GameElementFactory.prototypes = protos;
		GameElementFactory.prototypeTypes = protoTypes;
	}

	public static void generateCoin(int x, int y) {
		GameElementFactory.generate("edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups.Coin", x, y);

	}

	public static void generateInanimate(int x, int y) {
		GameElementFactory.generate("edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate", x, y);
	}
}
