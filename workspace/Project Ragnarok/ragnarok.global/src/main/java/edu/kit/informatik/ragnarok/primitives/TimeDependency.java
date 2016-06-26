package edu.kit.informatik.ragnarok.primitives;

public class TimeDependency {

	private float duration;
	private float timeLeft;

	public TimeDependency(float duration) {
		this.duration = duration;
		this.timeLeft = duration;
	}

	public void removeTime(float deltaTime) {
		this.timeLeft -= deltaTime;
	}

	public boolean timeUp() {
		return this.timeLeft <= 0;
	}

	public void reset() {
		this.timeLeft = this.timeUp() ? this.duration + this.timeLeft : this.duration;
	}

	public float getProgress() {
		return this.timeUp() ? 1 : 1 - this.timeLeft / this.duration;
	}

}
