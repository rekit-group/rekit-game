package edu.kit.informatik.ragnarok.logic.gameelements;

import java.util.HashMap;
import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.EndTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public final class GameElementFactory {
	/**
	 * Prevent instantiation
	 */
	private GameElementFactory() {
	}

	// Create connection to scene
	private static Scene scene;
	private static boolean loaded = false;

	public static void setScene(Scene scene) {
		GameElementFactory.scene = scene;
		if (!GameElementFactory.loaded) {
			GameElementFactory.load();
		}
	}

	private static HashMap<String, GameElement[]> groups;
	private static HashMap<String, GameElement> elements;

	public static GameElement getPrototype(String id) {
		// Element
		GameElement element = GameElementFactory.elements.get(id);
		if (element != null) {
			return element;
		}
		// Group
		GameElement[] group = GameElementFactory.groups.get(id);
		if (group != null && group.length != 0) {
			return group[GameConf.PRNG.nextInt(group.length)];
		}

		System.err.println("Error in GameElementFactory: Tried to get Prototype of GameElement with unknown ID " + id);
		// if none found --> inanimate
		return GameElementFactory.elements.get(InanimateBox.class.getName());
	}

	public static void generate(String id, int x, int y) {
		GameElementFactory.generate(id, x, y, new String[0]);
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
		if (GameElementFactory.loaded) {
			return;
		}
		GameElementFactory.loaded = true;

		GameElementFactory.elements = new HashMap<>();
		GameElementFactory.groups = new HashMap<>();

		GameElementFactory.loadElements();
		GameElementFactory.loadGroups();

	}

	private static void loadElements() {
		// Put Blocks in collection
		GameElementFactory.elements.put(Inanimate.getPrototype().getClass().getName(), Inanimate.getPrototype());

		GameElementFactory.elements.put(EndTrigger.getPrototype().getClass().getName(), EndTrigger.getPrototype());
		for (DynamicInanimate e : DynamicInanimate.getInanimatePrototypes()) {
			GameElementFactory.elements.put(e.getClass().getName(), e);
		}
		for (Boss e : Boss.getBossPrototypes()) {
			GameElementFactory.elements.put(e.getClass().getName(), e);
		}
	}

	private static void loadGroups() {
		// Put Enemies in collection and in separate array
		Set<Enemy> enemyPrototypes = Enemy.getEnemyPrototypes();
		Enemy[] enemyCollection = new Enemy[enemyPrototypes.size()];
		int i = 0;
		for (Enemy e : enemyPrototypes) {
			GameElementFactory.elements.put(e.getClass().getName(), e);
			enemyCollection[i++] = e;
		}
		GameElementFactory.groups.put(Enemy.class.getName(), enemyCollection);

		// Put Pickups in collection and in separate array
		Set<Pickup> pickupPrototypes = Pickup.getPickupPrototypes();
		Pickup[] pickupCollection = new Pickup[pickupPrototypes.size()];
		i = 0;
		for (Pickup e : pickupPrototypes) {
			GameElementFactory.elements.put(e.getClass().getName(), e);
			pickupCollection[i++] = e;
		}
		GameElementFactory.groups.put(Pickup.class.getName(), pickupCollection);

	}

	public static void generateCoin(int x, int y) {
		GameElementFactory.generate("edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups.Coin", x, y);

	}

	public static void generateInanimate(int x, int y) {
		GameElementFactory.generate(Inanimate.class.getName(), x, y);
	}
}
