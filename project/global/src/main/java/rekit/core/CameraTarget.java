package rekit.core;

/**
 *
 * This interface has to be implemented by Objects which can set or calculate
 * the current CameraOffset.
 *
 */
public interface CameraTarget {
	/**
	 * Get the current camera offset.
	 *
	 * @return the current camera offset
	 */
	float getCameraOffset();
}
