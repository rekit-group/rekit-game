package edu.kit.informatik.ragnarok.logic.gameelements.entities;

public class FixedCameraTarget implements CameraTarget {

	private float cameraOffset;
	
	public FixedCameraTarget(float cameraOffset) {
		this.cameraOffset = cameraOffset;
	}
	
	public float getCameraOffset() {
		return this.cameraOffset; 
	}
}
