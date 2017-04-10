package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.logic.gameelements.entities.enemies.piston.IPistonForState;
import rekit.primitives.time.Timer;
import rekit.util.state.State;

/**
 * Encapsules all timing and phase-depending information in a timeable State.
 * See {@link PistonState.getCurrentHeight} for the actual intrinsic value.
 * @author Angelo Aracri
 */
public abstract class PistonState extends State {
	
	protected IPistonForState piston;
	
	public PistonState(IPistonForState piston) {
		this.piston = piston;
		this.timer = new Timer(this.getTimerTime());
	}
	
	public void logicLoop() {
		super.logicLoop();
		System.out.println(this.getClass().getSimpleName() + "(" + this.getTimerTime() + "): " + this.timer.getProgress() +" -> " + ((PistonState)this).getCurrentHeight());
	}

	/**
	 * The intrinsic state of the State, the current height of the piston between 0 and 1.
	 * @return the height of the piston in 0 to 1.
	 */
	public abstract float getCurrentHeight();
	
}
