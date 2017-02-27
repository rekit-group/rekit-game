package ragnarok.logic.level;

import java.io.IOException;
import java.io.InputStream;

import ragnarok.logic.gameelements.GameElement;

/**
 * <p>
 * Manages assembling a level piece by piece for not having to build a level
 * whole at once. The method <i>generate(int max)</i> must be called at least
 * every time <i>max</i> changes to give the LevelAssembler the change to check
 * if it must generate more {@link GameElement GameElements}.
 * </p>
 * <p>
 * Internally, it uses a {@link StructureManager} to get the the next pieces (=
 * {@link Structure}) to be build.
 * </p>
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class LevelAssembler {
	/**
	 * Position of the current end of the level (x pos).
	 */
	private int generatedUntil;
	/**
	 * The structure manager.
	 */
	private StructureManager manager;

	/**
	 * Constructor that initializes attributes and instructs a new
	 * {@link StructureManager} to load a level file specified by the
	 * <i>levelName</i>. The <i>seed</i> is used for pseudo-randomizing.
	 *
	 * @param data
	 *            the structure of the level to be assembled.
	 * @param seed
	 *            the seed used for pseudo-randomizing.
	 * @throws IOException
	 *             if file cannot be read
	 */
	public LevelAssembler(InputStream data, int seed) throws IOException {
		this.manager = StructureManager.load(data, seed);
	}

	/**
	 * Initializes all attributes. Must also be called upon re-initialization
	 * (dying, loading new level, ...).
	 */
	public void init() {
		this.generatedUntil = 0;
		this.manager.init();
	}

	/**
	 * Orders the StructureManager to build {@link Structure}s if it must. This
	 * decision depends on the {@link Structure}s build so far and the parameter
	 * <i>max</i> that specifies which x position is the smallest that needs to
	 * be generated at.
	 *
	 * @param max
	 *            the lowest x position that must still be generated at.
	 */
	public void generate(int max) {
		while (this.generatedUntil < max && this.manager.hasNext()) {
			// build structure
			this.generatedUntil += this.manager.next().build(this.generatedUntil + 1, this.manager.isSettingSet("autoCoinSpawn"));
		}
	}

	/**
	 * Return if the level is infinite or not.
	 *
	 * @return true if the level is infinite, false otherwise.
	 */
	public boolean isInfinite() {
		return this.manager.isSettingSet("infinite");
	}

}
