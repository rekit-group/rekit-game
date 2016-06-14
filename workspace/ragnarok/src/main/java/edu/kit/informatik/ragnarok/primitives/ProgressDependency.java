package edu.kit.informatik.ragnarok.primitives;

public class ProgressDependency {
	
	private float initial;
	private float end;
	
	public ProgressDependency(float initial, float end) {
		this.initial = initial;
		this.end = end;
	}
	
	public float getNow(float progress) {
		return initial + (end - initial) * progress;
	}
}
