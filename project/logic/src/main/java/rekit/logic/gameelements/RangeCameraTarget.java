package rekit.logic.gameelements;

import rekit.core.CameraTarget;

/**
 *
 * This class can be used for set the range camera target (e.g. for BossRooms
 * etc.).
 *
 */
public final class RangeCameraTarget implements CameraTarget {

	private final CameraTarget hint;
	private final float min;
	private final float max;

	/**
	 * Create by range and hint target.
	 *
	 * @param min
	 *            the minimum value of {@link #getCameraOffset()}
	 * @param max
	 *            the maximum value of {@link #getCameraOffset()}
	 * @param hint
	 *            the target which shall focused within the range
	 */
	public RangeCameraTarget(float min, float max, CameraTarget hint) {
		this.min = min;
		this.max = max;
		this.hint = hint;
	}

	@Override
	public float getCameraOffset() {
		float hint = this.hint.getCameraOffset();
		if (hint < this.min) {
			return this.min;
		}
		if (hint > this.max) {
			return this.max;
		}
		return hint;
	}
}
