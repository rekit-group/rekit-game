package rekit.logic;

import java.util.Map;
import java.util.function.Consumer;

import org.fuchss.tools.lambda.VoidFunction;

import rekit.config.GameConf;
import rekit.core.CameraTarget;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gui.GuiElement;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.scene.Scenes;

/**
 * This is the public interface of all Scenes.
 *
 * @author Dominik Fuchss
 *
 */
public interface IScene {
	/**
	 * Adds a GameElement to the Model. The elements will not directly be added
	 * to the internal data structure to prevent concurrency errors. Instead
	 * there is an internal list to hold all waiting GameElements that will be
	 * added in the next call of logicLoop
	 *
	 * @param e
	 *            the GameElement to add
	 */
	void addGameElement(GameElement e);

	/**
	 * Removes a GameElement from the Model The elements will not directly be
	 * removed from the internal data structure to prevent concurrency errors.
	 * Instead there is an internal list to hold all waiting GameElements that
	 * will be removed in the next call of logicLoop
	 *
	 * @param e
	 *            the GameElement to remove
	 */
	void markForRemove(GameElement e);

	/**
	 * Adds a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to add
	 */
	void addGuiElement(GuiElement e);

	/**
	 * Removes a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to remove
	 */
	void removeGuiElement(GuiElement e);

	/**
	 * Set the camera target.
	 *
	 * @param cameraTarget
	 *            the camera target
	 */
	void setCameraTarget(CameraTarget cameraTarget);

	/**
	 * Get the current camera offset.
	 *
	 * @return the current camera offset
	 */
	float getCameraOffset();

	/**
	 * Apply function on game elements (non-neutral).
	 *
	 * @param function
	 *            the function
	 * @see Team#isNeutral()
	 */
	void applyToNonNeutralGameElements(Consumer<GameElement> function);

	/**
	 * Apply function on all game elements.
	 *
	 * @param function
	 *            the function
	 */
	void applyToGameElements(Consumer<GameElement> function);

	/**
	 * Apply function on game elements.
	 *
	 * @param function
	 *            the function
	 */
	void applyToGuiElements(Consumer<GuiElement> function);

	/**
	 * Get the model of the MVC.
	 *
	 * @return the model
	 */
	Model getModel();

	/**
	 * Invoke logic.
	 *
	 */
	void logicLoop();

	/**
	 * Initialize the scene. e.g. build Level/GUI so Scene is ready to be drawn
	 * Must be called on restart.
	 */
	void init();

	/**
	 * Start the scene. Begin drawing and Player/Enemies will begin to move.
	 */
	default void start() {
	}

	/**
	 * Restart the scene.
	 */
	default void restart() {
		this.init();
		this.start();
	}

	/**
	 * Get a map of duration-time of elements.
	 *
	 * @return the duration-time of elements by class
	 */
	Map<String, Long> getGameElementDurations();

	/**
	 * Get the amount of elements in the scene.
	 *
	 * @return the amount of elements
	 */
	int getGameElementCount();

	/**
	 * Get the associated Root-MenuItem.
	 *
	 * @return the root-menuItem or {@code null} if no {@link Scenes#MAIN_MENU}
	 */
	MenuItem getMenu();

	/**
	 * Toggle pause for the scene. See {@link IScene#setPause(boolean)}.
	 *
	 * @return {@code true} iff operation is possible, {@code false} otherwise
	 */
	boolean togglePause();

	/**
	 * Set whether the game can paused
	 *
	 * @param canPause
	 *            {@code true} iff pausable
	 */
	void setCanPause(boolean canPause);

	/**
	 * Indicates whether the scene is paused.
	 *
	 * @return {@code true} if paused, {@code false} otherwise
	 */
	boolean isPaused();

	/**
	 * Set the pause state. When pause usually the logic loop
	 *
	 *
	 * Useful when resetting.
	 *
	 * @param pause
	 *            the new pause state
	 */
	void setPause(boolean pause);

	/**
	 * This method indicates whether the scene encapsulates a level.
	 *
	 * @return {@code true} if this scene encapsulates a level, {@code false}
	 *         otherwise
	 */
	default boolean isLevelScene() {
		return false;
	}

	/**
	 * Will invoked if user wants to attack something.
	 *
	 * @see GameConf#CONTINUOS_ATTACK
	 */
	default void attack() {
	}

	/**
	 * The handler of {@link #attack()}.
	 *
	 * @param handler
	 *            the handler or {@code null} to reset
	 */
	default void setAttackHandler(VoidFunction handler) {
	}
}
