package edu.kit.informatik.ragnarok.logic.gameelements;

import java.util.HashMap;
import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.EndTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

/**
 *
 * This class will be used to load and group all GameElements.
 *
 */
public final class GameElementFactory {
	/**
	 * Prevent instantiation.
	 */
	private GameElementFactory() {
	}

	// Create connection to scene
	/**
	 * The current scene.
	 */
	private static IScene scene;
	/**
	 * Indicates whether the factory has been loaded.
	 */
	private static boolean loaded = false;

	/**
	 * Set the current scene.
	 *
	 * @param scene
	 *            the current scene.
	 */
	public static synchronized void setScene(IScene scene) {
		GameElementFactory.scene = scene;
		if (!GameElementFactory.loaded) {
			GameElementFactory.load();
		}
	}

	/**
	 * All Groups.
	 */
	private static HashMap<String, GameElement[]> groups;
	/**
	 * All Elements.
	 */
	private static HashMap<String, GameElement> elements;

	/**
	 * Get Prototype by identifier.
	 *
	 * @param id
	 *            the identifier
	 * @return the GameElement or {@link InanimateBox} of none found.
	 */
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
		return Inanimate.getPrototype();
	}

	/**
	 * Generate a new GameElement at position.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x pos
	 * @param y
	 *            the y pos
	 * @param modifiers
	 *            the optional modifiers
	 */
	public static void generate(String id, int x, int y, String... modifiers) {
		GameElement prototype = GameElementFactory.getPrototype(id);
		GameElementFactory.generate(prototype.create(new Vec(x, y), modifiers));

	}

	/**
	 * Generate a new GameElement.
	 *
	 * @param element
	 *            the element
	 */
	public static void generate(GameElement element) {
		if (element != null) {
			// Add GameElement to model
			GameElementFactory.scene.addGameElement(element);
		}

	}

	/**
	 * Load the factory.
	 */
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

	/**
	 * Load all elements.
	 */
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

	/**
	 * Load all groups.
	 */
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

	/**
	 * Generate coin at position.
	 *
	 * @param x
	 *            the x pos
	 * @param y
	 *            the y pos
	 */
	public static void generateCoin(int x, int y) {
		GameElementFactory.generate("edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups.Coin", x, y);

	}

	/**
	 * Generate inanimate at position.
	 *
	 * @param x
	 *            the x pos
	 * @param y
	 *            the y pos
	 */
	public static void generateInanimate(int x, int y) {
		GameElementFactory.generate(Inanimate.class.getName(), x, y);
	}
}
