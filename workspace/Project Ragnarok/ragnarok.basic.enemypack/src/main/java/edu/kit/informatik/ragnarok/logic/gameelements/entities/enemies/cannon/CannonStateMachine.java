package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state.CannonState;
import edu.kit.informatik.ragnarok.logic.state.TimeStateMachine;

public class CannonStateMachine extends TimeStateMachine {

	protected Cannon parentCannon;

	public CannonStateMachine(Cannon parentCannon, CannonState initialState) {
		super(initialState);

		// set reference to Cannon
		initialState.setCannon(parentCannon);
		this.parentCannon = parentCannon;
	}

	public void nextState() {
		// get next State defined by current state
		CannonState nextState = (CannonState) this.currentState.getNextState();

		// set reference to Cannon
		nextState.setCannon(parentCannon);

		// call States protected nextState with the modified CannonState
		nextState(nextState);
	}

	public CannonState getState() {
		return (CannonState) this.currentState;
	}

}
