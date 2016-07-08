package edu.kit.informatik.ragnarok.logic.gameelements;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.EndTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateFloor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

/**
 * ID Dictionary:
 *
 * <ul>
 * <li>-2 Random pickup</li>
 * <li>-1 Random enemy</li>
 *
 * <li>1 {@link Inanimate} (handles {@link InanimateBox} and
 * {@link InanimateFloor} itself)</li>
 * <li>2 {@link RektKiller}</li>
 * <li>3 {@link Rocket}</li>
 * <li>4 {@link Warper}</li>
 * <li>5 {@link Slurp}</li>
 *
 * <li>10 {@link Coin}</li>
 * <li>20 {@link Life}</li>
 *
 * <li>50 {@link InanimateBox}</li>
 * <li>51 {@link InanimateFloor}</li>
 * <li>60 {@link InanimateDoor}</li>
 * <li>70 {@link InanimateTrigger}</li>
 * <li>71 {@link EndTrigger}</li>
 *
 * <li>80 {@link ToggleBox}</li>
 *
 * <li>100 {@link RektSmasher}
 * </ul>
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class GameElementFactory {

	private static Scene scene;

	public static void init(Scene scene) {
		GameElementFactory.scene = scene;
		GameElementFactory.load();
		GameElementFactory.RNG = new Random();
	}

	/**
	 * A HashMap containing every spawnable GameElement sorted by its type. -1
	 * is reserved for enemies, -2 is reserved for pickups.
	 */
	private static HashMap<Integer, GameElement[]> prototypeTypes;

	/**
	 * A HashMap containing every spawnable GameElement mapped from their
	 * respective getId().
	 */
	private static HashMap<Integer, GameElement> prototypes;

	private static Random RNG;

	public static GameElement getPrototype(int id) {
		GameElement element;
		if (id > 0) {
			element = GameElementFactory.prototypes.get(id);
		} else {
			// if this type of GameElement is not defined
			if (!GameElementFactory.prototypeTypes.containsKey(id)) {
				return null;
			}
			GameElement[] elemArray = GameElementFactory.prototypeTypes.get(id);
			element = elemArray[GameElementFactory.RNG.nextInt(elemArray.length)];
		}
		return element;
	}

	public static void generate(int id, int x, int y) {
		GameElement prototype = GameElementFactory.getPrototype(id);
		if (prototype != null) {
			// Add enemy to model
			GameElementFactory.scene.addGameElement(prototype.create(new Vec(x, y)));
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
		HashMap<Integer, GameElement> protos = new HashMap<>();
		HashMap<Integer, GameElement[]> protoTypes = new HashMap<>();

		// Put Blocks in collection
		protos.put(Inanimate.getPrototype().getID(), Inanimate.getPrototype());
		protos.put(EndTrigger.getPrototype().getID(), EndTrigger.getPrototype());

		// Put all Inanimates in collection
		Set<DynamicInanimate> inanimatePrototypes = Inanimate.getInanimatePrototypes();
		for (Inanimate e : inanimatePrototypes) {
			protos.put(e.getID(), e);
		}

		// Put all Bosses in collection
		Set<Boss> bossPrototypes = Boss.getBossPrototypes();
		for (Boss e : bossPrototypes) {
			protos.put(e.getID(), e);
		}

		// Put Enemies in collection and in separate array
		Set<Enemy> enemyPrototypes = Enemy.getEnemyPrototypes();
		Enemy[] enemyCollection = new Enemy[enemyPrototypes.size()];
		int i = 0;
		for (Enemy e : enemyPrototypes) {
			protos.put(e.getID(), e);
			enemyCollection[i++] = e;
		}
		protoTypes.put(-1, enemyCollection);

		// Put Pickups in collection and in separate array
		Set<Pickup> pickupPrototypes = Pickup.getPickupPrototypes();
		Pickup[] pickupCollection = new Pickup[pickupPrototypes.size()];
		i = 0;
		for (Pickup e : pickupPrototypes) {
			protos.put(e.getID(), e);
			pickupCollection[i++] = e;
		}
		protoTypes.put(-2, pickupCollection);

		// Save local data structures in static attributes
		GameElementFactory.prototypes = protos;
		GameElementFactory.prototypeTypes = protoTypes;
	}
}
