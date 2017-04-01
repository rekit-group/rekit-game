package rekit.logic.level;

import org.fuchss.configuration.Configurable;

import rekit.config.GameConf;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.type.Boss;
import rekit.persistence.level.LevelDefinition;
import rekit.primitives.geometry.Vec;

/**
 * {@link Configurable} internal class that is used to store all
 * {@link Boss}-relevant settings. These settings are, in concrete, used to
 * determine at which x-position (=<i>settingName</i>) which {@link Boss}
 * (=<i>settingValue</i>) is to be spawned. To enable easy access, the class
 * supplies the method getNextOrNull, that returns a {@link Structure} or
 * <i>null</i> depending of there is a {@link BossStructure} to currently return
 * or not.
 *
 * @author Angelo Aracri
 */
class BossSetting {
	private final Level level;

	/**
	 * Create the BossSetting-Container.
	 *
	 * @param level
	 *            the level
	 */
	BossSetting(Level level) {
		this.level = level;
	}

	/**
	 * Uses information stored in the upper instance of {@link LevelDefinition}
	 * and the internal settings supplied by extending {@link Configurable} to
	 * do one of the following things:
	 * <ul>
	 * <li>Either the settings specify a {@link Boss} (=<i>settingValue</i>) to
	 * be spawned because the level generation reached the threshold
	 * (=<i>settingName</i>) and therefore returns a fully prepared
	 * {@link BossStructure} containing a {@link Boss} already.</li>
	 * <li>Or there is currently no Boss to be spawned at this time (or at all)
	 * and therefore return <i>null</i>.</li>
	 * </ul>
	 *
	 * @param fromX
	 *            the x-position in the level to start checking if a
	 *            {@link Boss} must be spawned.
	 * @param toX
	 *            the x-position in the level to end checking if a {@link Boss}
	 *            must be spawned.
	 * @return a {@link BossStructure} with a Boss attached if there must be a
	 *         {@link Boss} somewhere between the given values <i>fromX</i> and
	 *         <i>toX</i>, <i>null</i> otherwise.
	 */
	public Structure getNextOrNull(int fromX, int toX) {
		for (int i = fromX; i <= toX; i++) {
			String setting = "AT" + i;
			if (this.level.getDefinition().getBossSetting(setting) != null) {
				GameElement bossGameElement = GameElementFactory.getPrototype(this.level.getDefinition().getBossSetting(setting)).create(new Vec(),
						new String[] {});
				if (bossGameElement instanceof Boss) {
					Boss boss = (Boss) bossGameElement;
					Structure bossStructure = boss.getBossStructure();
					this.level.addUnitsBuild(bossStructure.getWidth());
					return bossStructure;
				} else {
					GameConf.GAME_LOGGER.error("Error while spawning Boss: " + this.level.getDefinition().getBossSetting(setting) + " is not a BossID");
				}

			}
		}
		return null;
	}
}