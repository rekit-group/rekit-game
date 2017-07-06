package rekit.logic.level;

import java.util.Arrays;

import rekit.config.GameConf;
import rekit.logic.gameelements.GameElementFactory;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.SettingKey;

/**
 * <p>
 * Represents a collection if GameElements that can be build into a level.
 * </p>
 * <p>
 * It is created from a two dimensional integer array that symbolizes a two
 * dimensional fraction of a possible level. Each number in the array will be
 * mapped to another kind of GameElement upon building. The mapping is specified
 * by the {@link GameElementFactory}.
 * </p>
 * <p>
 * To build an existing Structure, it supplies the method <i>build(int levelX,
 * boolean autoCoinSpawn)</i> that will build itself into the level from the
 * given position <i>levelX</i> (inclusively). Build structures will always
 * begin at the very bottom of the level independently to the original arrays
 * dimension. <br>
 * <b>Note:</b> this behavior requires building enough floor in order to make a
 * level beatable.<br>
 * Optional coin spawning can be activated with the parameter
 * <i>autoCoinSpawn</i>.<br>
 * Internally, {@link GameElementFactory} is used to add Elements to a Scene, so
 * it must be initialized with a Scene.
 * </p>
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 */
public class Structure {

	/**
	 * Initially passed array that acts as a template for building actual
	 * GameElements. The mapping from each integer to GameElements is specified
	 * by the {@link GameElementFactory}.
	 */
	private String[][] structure;

	/**
	 * Value that specifies how many columns to right of the Structure will be
	 * added with plain floor. <i>gapWidth</i> being 0 results in the Structure
	 * being build in the exact way as the <i>structureArray</i> specifies.
	 */
	private int gapWidth;

	private LevelDefinition definition;

	/**
	 * Whether to spawn Coins on empty positions or not.
	 */
	protected boolean autoCoinSpawn = true;

	private static final float coinSpawsChance = 0.08f;

	/**
	 * Create a new Structure by level definition and structure lines.
	 *
	 * @param definition
	 *            the definition
	 * @param lines
	 *            the lines for the structure
	 */
	public Structure(LevelDefinition definition, String[][] lines) {
		this.definition = definition;
		this.autoCoinSpawn = definition.isSettingSet(SettingKey.AUTO_COIN_SPAWN);
		this.structure = new String[lines.length][];
		int i = 0;
		for (String[] line : lines) {
			this.structure[i++] = line.clone();
		}
	}

	/**
	 * <p>
	 * Adds all GameElements specified in the Structures template
	 * <i>structureArray</i> relative to a given x position. Also, it generates
	 * plain floor to the structures right if specified via <i>setGap(int
	 * gapWidth)</i>.<br>
	 * Optional coin spawning can be activated with the parameter
	 * <i>autoCoinSpawn</i>.
	 * </p>
	 * <p>
	 * Additionally, the method returns the width of the build structure
	 * including the width of the (optional) gap.
	 * </p>
	 * <p>
	 * Internally, {@link GameElementFactory} is used to add Elements to a
	 * Scene, so it must be initialized with a Scene.
	 * </p>
	 * via
	 *
	 * @param levelX
	 *            the x position in the level where to build this Structure to
	 *            (inclusively)
	 * @return the width of the build structure plus the gapWidth.
	 */
	public int build(int levelX) {
		// iterate template structureArray
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				// map: structureArray y --> actual level y
				int aY = (GameConf.GRID_H - this.structure.length) + y;

				String elemInfo = this.structure[y][x];
				if (elemInfo == null) {
					this.trySpawnCoin(levelX + x, aY);
					continue;
				}

				String[] splitted = this.applyAlias(elemInfo);

				// if id != 0 => there is something to build here:
				if (splitted[0] != null) {
					// let GameElementFactory handle the rest
					GameElementFactory.generate(splitted[0], levelX + x, aY, Arrays.copyOfRange(splitted, 1, splitted.length));
				} else {
					this.trySpawnCoin(levelX + x, aY);
				}
			}
		}

		// add gap to the block right to the structure build so far, with given
		// width.
		for (int x = 0; x < this.gapWidth; x++) {
			GameElementFactory.generateInanimate(levelX + this.structure[0].length + x, GameConf.GRID_H - 1);
		}

		// return structure width plus gapWidth
		return this.getWidth() + this.gapWidth;
	}

	private void trySpawnCoin(int x, int y) {
		if (this.autoCoinSpawn && GameConf.PRNG.nextDouble() < Structure.coinSpawsChance) {
			GameElementFactory.generateDefaultCoin(x, y);
		}
	}

	/**
	 * Get alias.
	 *
	 * @param src
	 *            the source string
	 * @return the splitted definition for
	 *         {@link GameElementFactory#generate(String, int, int, String...)}
	 */
	protected String[] applyAlias(String src) {
		String[] splitted = src.split(":");
		if (!splitted[0].matches("(-|\\+)?[0-9]+")) {
			return splitted;
		}
		String alias = this.definition.getAlias(splitted[0]);
		if (alias == null && !"0".equals(src)) {
			GameConf.GAME_LOGGER.warn("No alias found for ID " + src);
		}

		String[] newHead = alias == null ? new String[1] : alias.split(":");
		String[] res = new String[splitted.length - 1 + newHead.length];
		System.arraycopy(newHead, 0, res, 0, newHead.length);
		System.arraycopy(splitted, 1, res, newHead.length, splitted.length - 1);
		return res;
	}

	/**
	 * Returns the height of this Structures template in game units.
	 *
	 * @return the height of the Structures template.
	 */
	public int getHeight() {
		return this.structure.length;
	}

	/**
	 * Return the width of this Structures template in game units. However, this
	 * method does <b>not</b> take the gap into account that can be specified
	 * using <i>setGap(int gapWidth)</i> and that will be build right to the
	 * right of the Structure. To get the actual width of the generated
	 * Structure use the return value of <i>build(int levelX, boolean
	 * autoCoinSpawn)</i>.
	 *
	 * @return the width of the Structures template.
	 */
	public int getWidth() {
		return this.structure[0].length;
	}

	/**
	 * Specifies the width of plain floor in game units that will be generated
	 * to the right of the Structure upon calling <i>build(int levelX, boolean
	 * autoCoinSpawn)</i>.
	 *
	 * @param gapWidth
	 *            the gap's width
	 */
	public void setGap(int gapWidth) {
		this.gapWidth = gapWidth;
	}

}
