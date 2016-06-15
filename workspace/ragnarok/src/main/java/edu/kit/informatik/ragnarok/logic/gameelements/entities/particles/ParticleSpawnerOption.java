package edu.kit.informatik.ragnarok.logic.gameelements.entities.particles;

import edu.kit.informatik.ragnarok.primitives.ProgressDependency;

public class ParticleSpawnerOption {
	
	private float startMin;
	private float startMax;
	private float deltaMin;
	private float deltaMax;
	
	public ParticleSpawnerOption(float startMin, float startMax, float deltaMin, float deltaMax) {
		this.startMin = startMin;
		this.startMax = startMax;
		this.deltaMin = deltaMin;
		this.deltaMax = deltaMax;
	}
	
	public ParticleSpawnerOption(float start, float delta) {
		this.startMin = start;
		this.startMax = start;
		this.deltaMin = delta;
		this.deltaMax = delta;
	}
	
	public ParticleSpawnerOption(float value) {
		this.startMin = value;
		this.startMax = value;
		this.deltaMin = 0;
		this.deltaMax = 0;
	}
	
	public ProgressDependency randomizeProgressDependency() {
		float start = (float) (startMin + Math.random() * (startMax - startMin));
		float delta = (float) (deltaMin + Math.random() * (deltaMax - deltaMin));
		
		return new ProgressDependency(start, start+delta);
	}
	
}
