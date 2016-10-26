package edu.kit.informatik.ragnarok.logic.level;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;

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
 * @version 1.1
 */
public class Structure extends Configurable {

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
	/**
	 * A mapping of all aliases.
	 */
	private Map<String, String> alias;

	/**
	 * Constructor that requires a not null two dimensional array that acts as a
	 * template for building actual GameElements. It will be saved locally. The
	 * mapping from each integer to GameElements is specified by the
	 * {@link GameElementFactory}.
	 *
	 * @param lines
	 *            the two dimensional template of the GameElements of this
	 *            Structure.
	 *
	 * @param alias
	 *            the aliases
	 */
	public Structure(List<String[]> lines, Map<String, String> alias) {
		this.structure = new String[lines.size()][];
		int i = 0;
		for (String[] line : lines) {
			this.structure[i++] = line.clone();
		}

		this.alias = alias;
	}

	/**
	 * Constructor that requires a not null two dimensional array that acts as a
	 * template for building actual GameElements. It will be saved locally. The
	 * mapping from each integer to GameElements is specified by the
	 * {@link GameElementFactory}.
	 *
	 * @param lines
	 *            the two dimensional template of the GameElements of this
	 *            Structure.
	 * @param alias
	 *            the aliases
	 */
	public Structure(String[][] lines, Map<String, String> alias) {
		this.structure = new String[lines.length][];
		int i = 0;
		for (String[] line : lines) {
			this.structure[i++] = line.clone();
		}
		this.alias = alias;
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
	 * @param autoCoinSpawn
	 *            option to add automated coin spawning into the structure.
	 * @return the width of the build structure plus the gapWidth.
	 */
	public int build(int levelX, boolean autoCoinSpawn) {
		// iterate template structureArray
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				// map: structureArray y --> actual level y
				int aY = (GameConf.GRID_H - this.structure.length) + y;

				String elemInfo = this.structure[y][x];
				if (elemInfo == null) {
					// Nothing set here:
					// check if we must generate random coins
					if (autoCoinSpawn && GameConf.PRNG.nextDouble() > 0.95f) {
						GameElementFactory.generateCoin(levelX + x, aY);
					}
					continue;
				}

				String[] splitted = elemInfo.split(":");
				if (splitted[0].matches("(-|\\+)?[0-9]+")) {
					splitted[0] = this.alias(splitted[0]);
				}
				// if id != 0 => there is something to build here:
				if (splitted[0] != null) {
					// let GameElementFactory handle the rest
					GameElementFactory.generate(splitted[0], levelX + x, aY, Arrays.copyOfRange(splitted, 1, splitted.length));
				} else {
					// otherwise check if we must generate random coins
					if (autoCoinSpawn && GameConf.PRNG.nextDouble() > 0.95f) {
						GameElementFactory.generateCoin(levelX + x, aY);
					}
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

	/**
	 * Get alias.
	 *
	 * @param string
	 *            the source string
	 * @return the alias or {@code null} if none found
	 */
	private String alias(String string) {
		String alias = this.alias.get(string);
		if (alias == null && !"0".equals(string)) {
			System.err.println("Waring: No alias found for ID " + string);
		}
		return alias;
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
