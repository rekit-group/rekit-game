package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.core.CameraTarget;

/**
 *
 * This class can be used for set the camera fixed (e.g. for BossRooms etc.)
 *
 */
public final class FixedCameraTarget implements CameraTarget {
	/**
	 * The camera offset.
	 */
	private float cameraOffset;

	/**
	 * Create by fixed camera offset.
	 *
	 * @param cameraOffset
	 *            the camera offset
	 */
	public FixedCameraTarget(float cameraOffset) {
		this.cameraOffset = cameraOffset;
	}

	/**
	 * Get the camera offset.
	 */
	@Override
	public float getCameraOffset() {
		return this.cameraOffset;
	}
}
