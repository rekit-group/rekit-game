package edu.kit.informatik.ragnarok.logic.state;

public class TimeStateMachine {

	protected State currentState;

	public TimeStateMachine(State initialState) {
		this.currentState = initialState;
		initialState.enter(this);
	}

	public void nextState() {
		nextState(currentState.getNextState());
	}

	protected void nextState(State nextState) {
		this.currentState.leave();
		nextState.enter(this);
		this.currentState = nextState;
	}

	public State getState() {
		return this.currentState;
	}
}
