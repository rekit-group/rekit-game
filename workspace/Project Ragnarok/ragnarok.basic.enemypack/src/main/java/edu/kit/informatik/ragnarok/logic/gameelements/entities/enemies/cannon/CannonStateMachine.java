package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state.CannonState;
import edu.kit.informatik.ragnarok.logic.state.TimeStateMachine;

/**
 * <p>
 * Extension of {@link TimeStateMachine} that is specifically used for
 * {@link Cannon Cannons}.
 * </p>
 * Different from the regular {@link TimeStateMachine} as it saves the reference
 * to a {@link Cannon} supplied in the constructor and only accepts and returns
 * specialized {@link CannonState CannonStates}.
 * 
 * @author Angelo Aracri
 */
public class CannonStateMachine extends TimeStateMachine {

	/**
	 * Reference to a parenting {@link Cannon}.
	 */
	protected Cannon parentCannon;

	/**
	 * Constructor that extends the {@link TimeStateMachines}
	 * {@link TimeStateMachine#TimeStateMachine(edu.kit.informatik.ragnarok.logic.state.State)
	 * constructor} by also saving the reference to a given parenting
	 * {@link Cannon}.
	 * 
	 * @param parentCannon
	 *            the parenting {@link Cannon}.
	 * @param initialState
	 *            the initial {@link CannonState}.
	 */
	public CannonStateMachine(Cannon parentCannon, CannonState initialState) {
		super(initialState);

		// set reference to Cannon
		initialState.setCannon(parentCannon);
		this.parentCannon = parentCannon;
	}

	@Override
	public void nextState() {
		// get next State defined by current state
		CannonState nextState = (CannonState) this.currentState.getNextState();

		// set reference to Cannon
		nextState.setCannon(parentCannon);

		// call States protected nextState with the modified CannonState
		nextState(nextState);
	}

	@Override
	public CannonState getState() {
		return (CannonState) this.currentState;
	}

}
