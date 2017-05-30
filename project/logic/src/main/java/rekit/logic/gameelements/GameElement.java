package rekit.logic.gameelements;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.core.Team.Range;
import rekit.logic.Collidable;
import rekit.logic.ILevelScene;
import rekit.logic.IScene;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;

/**
 * <p>
 * Abstract class that represents an Element that is a visual part of a
 * {@link IScene}, eg. a level.
 * </p>
 * <p>
 * Its situation in the level is defined by the attributes <i>pos</i>
 * (=position), <i>vel</i> (=velocity) and <i>size</i>.
 * </p>
 * <p>
 * To implement a GameElement, note these important steps:
 * <ul>
 * <li>Use super constructor <i>GameElement(Vec startPos, Vec vel, Vec size,
 * Team team)</i></li>
 * <li>Implement factory method <i>create(Vec startPos, String[] modifiers)</i>
 * for dynamic creation</li>
 * <li>Implement template method <i>logicLoop()</i> to add custom logic.</li>
 * <li>Implement template method <i>internalRender(Field field)</i> to add
 * custom visualization.</li>
 * </ul>
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 */
public abstract class GameElement implements Collidable {

	/**
	 * Flag that is used to signal if the {@link GameElement} is supposed to be
	 * rendered or not.
	 */
	protected boolean visible = true;

	/**
	 * Flag that is used to signal if the {@link GameElement} is supposed to be
	 * deleted or not.
	 */
	protected boolean deleteMe = false;

	/**
	 * The {@link GameElement GameElements} size that can be imagined as a box
	 * of this {@link Vec Vecs} dimensions around the {@link Vec} <i>pos</i>.
	 */
	private Vec size;

	/**
	 * The {@link GameElement GameElements} velocity that can be used to alter
	 * its position in the <i>logicLoop</i>.
	 */
	private Vec vel;

	/**
	 * <p>
	 * The {@link GameElement GameElements} absolute position in the level.
	 * </p>
	 * <p>
	 * <b>Note:</b> the position points to the center of the
	 * {@link GameElement}.
	 * </p>
	 */
	private Vec pos;

	// /**
	// * <p>
	// * The {@link GameElement GameElements} last absolute position in the
	// level.
	// * This position is saved upon every call of <i>setPos(Vec value)</i> and
	// is
	// * mainly used for collision detection.
	// * </p>
	// * <p>
	// * <b>Note:</b> the position points to the center of the
	// * {@link GameElement}.
	// * </p>
	// */
	// private Vec lastPos;

	/**
	 * The {@link Team} the {@link GameElement} is in that mainly specifies
	 * behavior upon collision.
	 */
	protected Team team;

	/**
	 * The {@link ILevelScene} the {@link GameElement} is in and manages this
	 * and all other {@link GameElement GameElements}.
	 */
	private ILevelScene scene;

	/**
	 * Prototype constructor. Use the constructor <i>GameElement(Vec startPos,
	 * Vec vel, Vec size, Team team)</i> for regular use and extending.
	 *
	 * @param team
	 *            the {@link Team} of the GameElement
	 */
	protected GameElement(Team team) {
		this.team = team;
	}

	/**
	 * Standard constructor that should be used in every extending class.
	 *
	 * @param startPos
	 *            the absolute {@link Vec} that points to the {@link GameElement
	 *            GameElements} initial position.
	 * @param vel
	 *            the {@link Vec} that represents the initial velocity of the
	 *            {@link GameElement}
	 * @param size
	 *            the {@link Vec} that represents the size of the
	 *            {@link GameElement}
	 * @param team
	 *            the {@link Team} that is used for determining if two
	 *            {@link GameElement GameElements} can damage each other, etc.
	 */
	protected GameElement(Vec startPos, Vec vel, Vec size, Team team) {
		this.team = team;
		this.vel = vel;
		this.size = size;
		this.setPos(startPos);
	}

	/**
	 * <p>
	 * Template method that will be called periodically after being added to a
	 * {@link IScene}.
	 * </p>
	 * <p>
	 * Should be overwritten in sub classes for implementing custom logic,
	 * physics emulation, time-based actions, ...
	 * </p>
	 *
	 */
	public void logicLoop() {
		// check if entity fell out of the world
		this.checkForDelete();
	}

	/**
	 * Check whether the element shall be deleted. If so, mark for delete.
	 */
	private void checkForDelete() {
		Vec realPos = this.getPos().translate2D(this.scene.getCameraOffset());
		if (realPos.y > GameConf.GRID_H + GameConf.GRID_TOLERANCE_BELOW) {
			this.destroy();
			return;
		}
		float relX = realPos.x + this.getSize().x;
		float offset = this.getScene().getCameraOffset();
		if (offset > relX + GameConf.GRID_W && !this.getScene().isOffsetWildCard()) {
			this.destroy();
			return;
		}
	}

	/**
	 * <p>
	 * Method that will be called periodically after being added to a
	 * {@link IScene}.
	 * </p>
	 * <p>
	 * Its only task is to call the template method <i>internalRender(Field
	 * f)</i> if the {@link GameElement} is supposed to be rendered.
	 * </p>
	 *
	 * @param f
	 *            the {@link GameGrid} that represents the games field and
	 *            supplies primitive drawing operations.
	 */
	public final void render(GameGrid f) {
		if (this.isVisible()) {
			this.internalRender(f);
		}
	}

	/**
	 * <p>
	 * Template method that will be called periodically after being added to a
	 * {@link IScene}. Must be as performant as possible to keep the FPS low,
	 * since it will be called in every render-loop.
	 * </p>
	 * <p>
	 * Should be overwritten in sub classes for custom visualization using the
	 * {@link GameGrid}.
	 * </p>
	 *
	 * @param f
	 *            the {@link GameGrid} that represents the games field and
	 *            supplies primitive drawing operations.
	 */
	protected void internalRender(GameGrid f) {
		// do nothing
	}

	/**
	 * <p>
	 * Stub Factory method that every {@link GameElement} must implement in
	 * order to being able to be instantiated dynamically by the level creation.
	 * </p>
	 * <p>
	 * A <i>startPos</i> must be supplied to position the new GameElement right.
	 * </p>
	 * <p>
	 * Also, an array of <i>modifiers</i> that the extending {@link GameElement}
	 * can define and use is supplied. These are used make level creation able
	 * to determine the {@link GameElement GameElements} behavior. <b>Note:</b>
	 * the <i>modifiers</i> are not checked in syntax, so it must be thoroughly
	 * checked.
	 * </p>
	 *
	 * @param startPos
	 *            the initial position of the new {@link GameElement}.
	 * @param options
	 *            optional parameters that may determine additional behavior of
	 *            the {@link GameElement}.
	 * @return the newly created {@link GameElement}.
	 */
	public GameElement create(Vec startPos, String... options) {
		throw new UnsupportedOperationException("Create not supported for " + this.getClass().getSimpleName());
	}

	/**
	 * Returns if the {@link GameElement} is currently supposed to be rendered
	 * or not.
	 *
	 * @return true if the {@link GameElement} should be rendered, false
	 *         otherwise.
	 */
	protected boolean isVisible() {
		return this.visible;
	}

	/**
	 * <p>
	 * Getter for the {@link GameElement GameElements} absolute position.
	 * </p>
	 * <p>
	 * <b>Note:</b> the position points to the center of the
	 * {@link GameElement}.
	 * </p>
	 *
	 * @return the current position of the {@link GameElement}.
	 */
	public final Vec getPos() {
		return this.pos;
	}

	/**
	 * <p>
	 * Setter for the {@link GameElement GameElements} absolute position.
	 * </p>
	 * <p>
	 * <b>Note:</b> the position points to the center of the
	 * {@link GameElement}.
	 * </p>
	 *
	 * @param value
	 *            the new position the {@link GameElement} is supposed to have.
	 */
	public final void setPos(Vec value) {
		// if (this.pos == null) {
		// this.lastPos = value;
		// } else {
		// this.lastPos = this.pos;
		// }

		this.pos = value;
	}

	// /**
	// * <p>
	// * Getter for the {@link GameElement GameElements} last absolute position.
	// * This position is saved upon every call of <i>setPos(Vec value)</i> and
	// is
	// * mainly used for collision detection.
	// * </p>
	// * <p>
	// * <b>Note:</b> the position points to the center of the
	// * {@link GameElement}.
	// * </p>
	// *
	// * @return the last position of the {@link GameElement}.
	// */
	// public final Vec getLastPos() {
	// return this.lastPos;
	// }

	// /**
	// * Set the last valid position.
	// *
	// * @param lastPos
	// * the last valid position
	// */
	// protected final void setLastPos(Vec lastPos) {
	// this.lastPos = lastPos;
	// }

	/**
	 * <p>
	 * Getter for the {@link GameElement GameElements} velocity.
	 * </p>
	 *
	 * @return the current velocity of the {@link GameElement}.
	 */
	public final Vec getVel() {
		return this.vel;
	}

	/**
	 * <p>
	 * Setter for the {@link GameElement GameElements} velocity.
	 * </p>
	 *
	 * @param newVel
	 *            the new velocity the {@link GameElement} is supposed to have.
	 */
	public final void setVel(Vec newVel) {
		this.vel = newVel;
	}

	/**
	 * <p>
	 * Getter for the {@link GameElement GameElements} size.
	 * </p>
	 *
	 * @return the current size of the {@link GameElement}.
	 */
	public final Vec getSize() {
		return this.size;
	}

	/**
	 * Set the size of the GameElement.
	 *
	 * @param size
	 *            the size
	 */
	public void setSize(Vec size) {
		this.size = size == null ? null : size.abs();
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		// Do nothing
	}

	/**
	 * Returns a {@link Frame} that represents the collision box the
	 * {@link GameElement} will be using. Is dependent of the {@link GameElement
	 * GameElements} position and size.
	 *
	 * @return the {@link GameElement GameElements} collision box
	 */
	public Frame getCollisionFrame() {
		Vec v1 = this.getPos().add(this.getSize().scalar(-0.5f));
		Vec v2 = this.getPos().add(this.getSize().scalar(0.5f));
		return new Frame(v1, v2);
	}

	/**
	 * Setter for the {@link IScene} this {@link GameElement} belongs to.
	 *
	 * @param value
	 *            the new {@link IScene} the GameElement is in.
	 */
	public final void setScene(ILevelScene value) {
		this.scene = value;
	}

	/**
	 * Get the current scene.
	 *
	 * @return the current scene
	 */
	public final ILevelScene getScene() {
		return this.scene;
	}

	/**
	 * Getter for the {@link GameElement GameElements} {@link Team} that mainly
	 * specifies behavior upon collision.
	 *
	 * @return the {@link GameElement GameElements} {@link Team}
	 */
	public final Team getTeam() {
		return this.team;
	}

	/**
	 * <p>
	 * Getter for an imaginary z-position of the {@link GameElement}.
	 * </p>
	 * <p>
	 * Is used to determine the rendering order of every {@link GameElement}.
	 * </p>
	 *
	 * @return a number that represents the {@link GameElement GameElements}
	 *         rendering-order
	 */
	public final byte getZ() {
		if (this.getZHint() == null) {
			return this.team.zRange.std;
		}
		return this.team.zRange.normalize(this.getZHint());
	}

	/**
	 * Get a hint (for {@link Range#normalize(int)} for positioning this
	 * {@link GameElement} via {@link #getZ()}.
	 *
	 * @return {@code null} if no hint, otherwise the expected position
	 */
	public Integer getZHint() {
		return null;
	}

	/**
	 * <p>
	 * Method that, upon call, signals that this {@link GameElement} is supposed
	 * to be deleted in the next <i>logicLoop</i> of the {@link IScene}.
	 * </p>
	 * <p>
	 * <b>Note:</b> The underlying {@link GameElement} might go through several
	 * collisions and be rendered multiple times before it is actually deleted.
	 * </p>
	 */
	public void destroy() {
		this.deleteMe = true;
	}

	/**
	 * Getter for the flag that specifies if the {@link GameElement} is waiting
	 * for deletion.
	 *
	 * @return true if the {@link GameElement} is supposed to be deleted, false
	 *         otherwise.
	 */
	public final boolean getDeleteMe() {
		return this.deleteMe;
	}

	/**
	 * Check for collision with another element and invoke necessary methods.
	 *
	 * @param e2
	 *            the other element
	 */
	public final void checkCollision(GameElement e2) {
		float w = 0.5F * (this.size.x + e2.size.x);
		float h = 0.5F * (this.size.y + e2.size.y);
		Vec dxy = this.pos.sub(e2.pos);
		float dx = dxy.x;
		float dy = dxy.y;
		if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
			float wy = w * dy;
			float hx = h * dx;

			if (wy > hx) {
				if (wy > -hx) {
					this.reactToCollision(e2, Direction.UP);
				} else {
					this.reactToCollision(e2, Direction.RIGHT);
				}
			} else if (wy > -hx) {
				this.reactToCollision(e2, Direction.LEFT);
			} else {
				this.reactToCollision(e2, Direction.DOWN);
			}
		}

	}

	/**
	 * Behavior after this GameElement hit an enemy
	 */
	public void killBoost() {
		// Do nothing
	}

	/**
	 * This method indicates whether this entity can be added to it's group.
	 *
	 * @return {@code true} if addable to a group, {@code false} otherwise
	 */
	public boolean isAddableToGroup() {
		return true;
	}
}
