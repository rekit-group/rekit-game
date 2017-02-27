package ragnarok.logic.level;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import ragnarok.config.GameConf;
import ragnarok.core.GameElement;
import ragnarok.logic.gameelements.GameElementFactory;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.inanimate.EndTrigger;
import ragnarok.logic.gameelements.inanimate.Inanimate;
import ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import ragnarok.logic.gameelements.type.Boss;
import ragnarok.logic.gameelements.type.Enemy;
import ragnarok.logic.level.bossstructure.BossStructure;
import ragnarok.logic.level.parser.LevelParser;
import ragnarok.primitives.geometry.Vec;

/**
 * <p>
 * {@link Configurable} class that holds and manages a collection of
 * {@link Structure}s and can build them in an Iterator-like-way one at a time
 * using the <i>next()</i> and <i>hasNext()</i> methods.
 * </p>
 * <p>
 * Due to a private constructor it can only be instantiated through the method
 * <i>load(int randomSeed, String levelName)</i> that creates a new
 * StructureManager from a file called level_<i>{levelName}</i>.dat, fills
 * itself with all specified settings (first line of file), boss settings
 * (second line of file) and {@link Structure}-templates (rest of file) using
 * the {@link LevelParser}.
 * </p>
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 * @version 1.1
 */
public class StructureManager extends Configurable implements Iterator<Structure> {

	/**
	 * Private data structure that hold all structures that can be build in this
	 * level.
	 */
	private final Map<Integer, Structure> structures;

	/**
	 * Instance of Random that is used for all randomized actions involving
	 * {@link Structure}-decisions.
	 */
	private Random rand;

	/**
	 * Keeps track of which structure number is the last that was generated or
	 * <i>-1</i> if none. Will be iterated every time <i>next()</i> is called.
	 * Is used in non-shuffeled as well as non-infinite levels.
	 */
	private int currentStructureId = -1;

	/**
	 * Instance of BossSettings used to hold all settings concerning boss
	 * spawning. See {@link BossSettings} for more.
	 */
	public final BossSettings bossSettings;

	/**
	 * Used to keep track how many columns have been built into the level since
	 * the last call if <i>init()</i>. Is used to keep track of when to spawn
	 * {@link Boss Bosses}.
	 */
	protected int unitsBuilt = 0;

	/**
	 * Used to keep track how many columns had been build into the level since
	 * the last call of <i>next()</i>. Is used to keep track of when to spawn
	 * {@link Boss}es.
	 */
	private int lastUnitsBuilt = 0;
	/**
	 * All aliases.
	 */
	private final Map<String, String> alias = new HashMap<>();

	/**
	 * Private constructor to prevent instantiation from outside.
	 *
	 * @param randomSeed
	 *            the random seed
	 */
	private StructureManager(int randomSeed) {
		super();
		this.structures = new HashMap<>();
		this.bossSettings = new BossSettings();
		this.rand = new Random(randomSeed);
	}

	/**
	 * Initializes all attributes. Must also be called upon re-initialization
	 * (dying, loading new level, ...).
	 */
	public void init() {
		this.currentStructureId = -1;
		this.unitsBuilt = 0;
	}

	/**
	 * Static factory method that creates a new StructureManager from a file
	 * called level_<i>{levelName}</i>.dat, fills it with all specified settings
	 * (first line of file), boss settings (second line of file) and
	 * {@link Structure}-templates (rest of file) using the {@link LevelParser}.
	 *
	 * @param data
	 *            the structure file
	 * @param randomSeed
	 *            the seed to used for the StructureManager that will be used
	 *            for all random-based actions.
	 * @return the newly created StructureManager.
	 * @throws IOException
	 *             iff file does not exist
	 */
	public static StructureManager load(InputStream data, int randomSeed) throws IOException {

		// create Scanner from InputStream
		Scanner scanner = new Scanner(data, Charset.defaultCharset().name());
		// don't use line-ending-wise iteration, but get the whole file at once
		// \\A = <EOF>
		scanner.useDelimiter("\\A");
		// get whole file content as String
		String input = scanner.hasNext() ? scanner.next() : "";

		// close scanner after use to prevent java-typical resource-wasting ;)
		scanner.close();

		// create new StructureManager (we gotz them constructor in here!)
		StructureManager instance = new StructureManager(randomSeed);
		// create Parser to extract all information of file-String
		LevelParser.parseLevel(input, instance);

		// return newly created instance of StructureManager
		return instance;
	}

	/**
	 * Returns the next {@link Structure} of this {@link StructureManager}.
	 * Which {@link Structure} is the next strongly depends on the settings
	 * given in the file specified in the method <i>load(int randomSeed, String
	 * levelName)</i>. Also, if specified by in the {@link BossSettings}
	 * <i>bossSettings</i>, this method may decide to return a {@link Boss}es
	 * {@link BossStructure} instead of a regular {@link Structure}.
	 */
	@Override
	public Structure next() {

		// if this is level start: build initial Structure
		if (this.unitsBuilt == 0) {
			return this.getInitialStructure();
		} else {
			// increment structureId / count
			this.currentStructureId++;

			// if end of sequence reached: Determine if to repeat or end level
			if (this.currentStructureId >= this.structures.size()) {
				if (this.isSettingSet("infinite")) {
					this.currentStructureId = 0;
				} else {
					return this.getEndStructure();
				}
			}

			// get a BossStructure or null.
			Structure bossStructureOrNull = this.bossSettings.getNextOrNull(this.lastUnitsBuilt, this.unitsBuilt);

			// keep track of last <i>unitsBuilt</i> before it is incremented.
			this.lastUnitsBuilt = this.unitsBuilt;

			// return BossStructure if we want it (no usual Structure in this
			// case!)
			if (bossStructureOrNull != null) {
				return bossStructureOrNull;
			}

			// get next Structure depending on strategy (shuffled / in order)
			if (this.isSettingSet("shuffle")) {
				return this.nextShuffeled();
			} else {
				return this.nextInOrder();
			}
		}
	}

	/**
	 * Method used by <i>next</i> if the setting <i>shuffle</i> is set (to
	 * anything other then 0). Internally, it randomly gets a {@link Structure}
	 * from the {@link StructureManager}s <i>structures</i> and returns it.
	 * However, there will be added a gap with the width that
	 * <i>nextGapWidth()</i> returns and the internal value <i>unitsBuilt</i>
	 * will be incremented accordingly to the width of the {@link Structure} and
	 * its gap.
	 *
	 * @return the selected {@link Structure} that has just been randomly
	 *         determined.
	 */
	private Structure nextShuffeled() {
		// get random next Structure
		int randId = this.rand.nextInt(this.structures.size());
		Structure selected = this.structures.get(randId);

		// determine and set gap width
		int gap = this.nextGapWidth();
		selected.setGap(gap);

		// keep track of how far we built
		this.unitsBuilt += selected.getWidth() + gap;

		return selected;
	}

	/**
	 * Method used by <i>next</i> if the setting <i>shuffle</i> is not set (or
	 * 0). Internally, it uses the value <i>currentStructureId</i> to get the
	 * next {@link Structure} from the {@link StructureManager}s
	 * <i>structures</i> and returns it. However, there will be added a gap with
	 * the width that <i>nextGapWidth()</i> returns and the internal value
	 * <i>unitsBuilt</i> will be incremented accordingly to the width of the
	 * {@link Structure} and its gap.
	 *
	 * @return the selected {@link Structure} that is next in order.
	 */
	private Structure nextInOrder() {
		// get next Structure in order
		Structure selected = this.structures.get(this.currentStructureId);

		// determine and set gap width
		int gap = this.nextGapWidth();
		selected.setGap(gap);

		// keep track of how far we built
		this.unitsBuilt += selected.getWidth() + gap;

		return selected;
	}

	/**
	 * Returns a random gapWidth between 1 and 2 if setting <i>doGaps</i> is set
	 * (to anything other then 0).
	 *
	 * @return the random gapWidth between 1 and 2.
	 */
	private int nextGapWidth() {
		return this.isSettingSet("doGaps") ? this.rand.nextInt(2) + 1 : 0;
	}

	/**
	 * Adds a {@link Structure} to the internal data structure
	 * <i>structures</i>.
	 *
	 * @param structure
	 *            the structure
	 */
	public void addStructure(Structure structure) {
		this.structures.put(this.structures.size(), structure);
	}

	/**
	 * {@link Configurable} internal class that is used to store all
	 * {@link Boss}-relevant settings. These settings are, in concrete, used to
	 * determine at which x-position (=<i>settingName</i>) which {@link Boss}
	 * (=<i>settingValue</i>) is to be spawned. To enable easy access, the class
	 * supplies the method getNextOrNull, that returns a {@link Structure} or
	 * <i>null</i> depending of there is a {@link BossStructure} to currently
	 * return or not.
	 *
	 * @author Angelo Aracri
	 * @version 1.0
	 */
	public class BossSettings extends Configurable {

		/**
		 * Uses information stored in the upper instance of
		 * {@link StructureManager} and the internal settings supplied by
		 * extending {@link Configurable} to do one of the following things:
		 * <ul>
		 * <li>Either the settings specify a {@link Boss} (=<i>settingValue</i>)
		 * to be spawned because the level generation reached the threshold
		 * (=<i>settingName</i>) and therefore returns a fully prepared
		 * {@link BossStructure} containing a {@link Boss} already.</li>
		 * <li>Or there is currently no Boss to be spawned at this time (or at
		 * all) and therefore return <i>null</i>.</li>
		 * </ul>
		 *
		 * @param fromX
		 *            the x-position in the level to start checking if a
		 *            {@link Boss} must be spawned.
		 * @param toX
		 *            the x-position in the level to end checking if a
		 *            {@link Boss} must be spawned.
		 * @return a {@link BossStructure} with a Boss attached if there must be
		 *         a {@link Boss} somewhere between the given values
		 *         <i>fromX</i> and <i>toX</i>, <i>null</i> otherwise.
		 */
		public Structure getNextOrNull(int fromX, int toX) {
			for (int i = fromX; i <= toX; i++) {
				String setting = "AT" + i;
				if (this.isSettingSet(setting)) {
					GameElement bossGameElement = GameElementFactory.getPrototype(this.getSettingValue(setting)).create(new Vec(), new String[] {});
					if (bossGameElement instanceof Boss) {
						Boss boss = (Boss) bossGameElement;
						Structure bossStructure = boss.getBossStructure();
						StructureManager.this.unitsBuilt += bossStructure.getWidth();
						return bossStructure;
					} else {
						GameConf.GAME_LOGGER.error("Error while spawning Boss: " + this.getSettingValue(setting) + " is not a BossID");
					}

				}
			}
			return null;
		}
	}

	/**
	 * <p>
	 * Used to hold and get the first {@link Structure} that will <b>always</b>
	 * be the first {@link Structure} to be returned by <i>next()</i>.
	 * </p>
	 * <p>
	 * Is thought to give the {@link Player} an easier start and to prevent him
	 * from spawning in an {@link Enemy} or even an {@link Inanimate}.
	 * </p>
	 *
	 * @return the first {@link Structure} to be returned by <i>next()</i>.
	 */
	private Structure getInitialStructure() {
		// Flat floor
		String floor = Inanimate.class.getSimpleName();
		List<String[]> lines = new ArrayList<>();
		lines.add(new String[] { floor, floor, floor, floor, floor, floor, floor, floor, floor, floor, floor, floor });
		Structure structure = new Structure(lines, new HashMap<>());

		// keep track of how far we built (only works because we know structure
		// has no gap!)
		this.unitsBuilt += structure.getWidth();

		return structure;
	}

	/**
	 * <p>
	 * Used to hold and get the last {@link Structure} that will be the last
	 * {@link Structure} to be returned by <i>next()</i> before <i>hasNext()</i>
	 * indicates that there are no more {@link Structure Structures} to be
	 * returned. <br>
	 * Since this never happens on levels that have the setting <i>infinite</i>
	 * set (to anything other then 0), this method will never be called in these
	 * levels.
	 * </p>
	 * <p>
	 * Is thought to give the {@link Player} a characteristic ending and to add
	 * a {@link InanimateTrigger} that effectively ends the level upon
	 * collision.
	 * </p>
	 *
	 * @return the (optional) last {@link Structure} to be returned by
	 *         <i>next()</i>.
	 */
	private Structure getEndStructure() {

		String verticalTrigger = EndTrigger.class.getSimpleName();
		// Vertical wall

		List<String[]> lines = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			lines.add(new String[1]);
		}
		lines.get(4)[0] = verticalTrigger;

		Structure structure = new Structure(lines, new HashMap<>());

		// keep track of how far we built
		this.unitsBuilt += structure.getWidth();

		return structure;
	}

	@Override
	public boolean hasNext() {
		return this.isSettingSet("infinite") || this.currentStructureId < this.structures.size();
	}

	/**
	 * Set an alias.
	 *
	 * @param toReplace
	 *            the source
	 * @param replaceWith
	 *            the target
	 */
	public void setAlias(String toReplace, String replaceWith) {
		if (this.alias.put(toReplace, replaceWith) != null) {
			GameConf.GAME_LOGGER.warn("Multiple definitions (alias) for " + toReplace);
		}

	}

	/**
	 * Get the alias map.
	 *
	 * @return the aloas map
	 */
	public Map<String, String> getAlias() {
		return this.alias;
	}

}
