package edu.kit.informatik.ragnarok.logic.state;

import edu.kit.informatik.ragnarok.primitives.time.Timer;

public abstract class State {

	protected TimeStateMachine parent;
	protected Timer timer;

	public State() {
		this.timer = new Timer(this.getTimerTime());
	}

	public void enter(TimeStateMachine parent) {
		this.parent = parent;
	}

	public void leave() {
		// Do nothing
	}

	public void logicLoop(float deltaTime) {
		timer.removeTime(deltaTime);
		if (timer.timeUp()) {
			parent.nextState();
		}
	}

	public abstract State getNextState();

	public abstract float getTimerTime();
}
