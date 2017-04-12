package rekit.logic.gameelements.entities.enemies.piston;

import rekit.logic.gameelements.entities.enemies.piston.state.PistonState;

/**
 * The interface that a {@link PistonState} can see of a {@link Piston}
 * 
 * @author Angelo Aracri
 */
public interface IPistonForState {

	/**
	 * Get the time in milliseconds how low to keep the piston open.
	 * 
	 * @return the time in milliseconds
	 */
	long getCalcTimeOpen();

	/**
	 * Get the time in milliseconds how low to keep the piston closed.
	 * 
	 * @return the time in milliseconds
	 */
	long getCalcTimeClosed();

	/**
	 * Get the time in milliseconds how low to keep the piston opening and
	 * closing.
	 * 
	 * @return the time in milliseconds
	 */
	long getCalcTimeTransistion();
}
