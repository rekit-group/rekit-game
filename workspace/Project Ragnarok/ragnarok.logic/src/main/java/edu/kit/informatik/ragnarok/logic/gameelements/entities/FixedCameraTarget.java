package edu.kit.informatik.ragnarok.logic.gameelements.entities;

public class FixedCameraTarget implements CameraTarget {

	private float cameraOffset;

	public FixedCameraTarget(float cameraOffset) {
		this.cameraOffset = cameraOffset;
	}

	@Override
	public float getCameraOffset() {
		return this.cameraOffset;
	}

	@Override
	public void resetCameraOffset() {
		return;
	}
}
