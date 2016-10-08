package edu.kit.informatik.ragnarok.core;

import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

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
 * </p>
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 */
public abstract class GameElement implements Collidable, Comparable<GameElement> {

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

	/**
	 * <p>
	 * The {@link GameElement GameElements} last absolute position in the level.
	 * This position is saved upon every call of <i>setPos(Vec value)</i> and is
	 * mainly used for collision detection.
	 * </p>
	 * <p>
	 * <b>Note:</b> the position points to the center of the
	 * {@link GameElement}.
	 * </p>
	 */
	private Vec lastPos;

	/**
	 * The {@link Team} the {@link GameElement} is in that mainly specifies
	 * behavior upon collision.
	 */
	protected Team team;

	/**
	 * The {@link IScene} the {@link GameElement} is in and manages this and all
	 * other {@link GameElement GameElements}.
	 */
	private IScene scene;

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
		// Do nothing
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
	 *            the {@link Field} that represents the games field and supplies
	 *            primitive drawing operations.
	 */
	public final void render(Field f) {
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
	 * {@link Field}.
	 * </p>
	 *
	 * @param f
	 *            the {@link Field} that represents the games field and supplies
	 *            primitive drawing operations.
	 */
	protected void internalRender(Field f) {
		// do nothing
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
		if (this.pos == null) {
			this.lastPos = value;
		} else {
			this.lastPos = this.pos;
		}

		this.pos = value;
	}

	/**
	 * <p>
	 * Getter for the {@link GameElement GameElements} last absolute position.
	 * This position is saved upon every call of <i>setPos(Vec value)</i> and is
	 * mainly used for collision detection.
	 * </p>
	 * <p>
	 * <b>Note:</b> the position points to the center of the
	 * {@link GameElement}.
	 * </p>
	 *
	 * @return the last position of the {@link GameElement}.
	 */
	public final Vec getLastPos() {
		return this.lastPos;
	}

	/**
	 * Set the last valid position.
	 *
	 * @param lastPos
	 *            the last valid position
	 */
	protected final void setLastPos(Vec lastPos) {
		this.lastPos = lastPos;
	}

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
		this.size = size;
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
	public final void setScene(IScene value) {
		this.scene = value;
	}

	/**
	 * Get the current scene.
	 *
	 * @return the current scene
	 */
	public final IScene getScene() {
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
	protected int getOrderZ() {
		return 0;
	}

	@Override
	public final int compareTo(GameElement other) {
		return this.getOrderZ() - other.getOrderZ();
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
	 * @param modifiers
	 *            optional parameters that may determine additional behavior of
	 *            the {@link GameElement}.
	 * @return the newly created {@link GameElement}.
	 */
	public GameElement create(Vec startPos, String[] modifiers) {
		throw new UnsupportedOperationException("Create not supported for " + this.getClass().getSimpleName());
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
		this.checkCollision(this, e2, this.getLastPos(), e2.getLastPos());
	}

	/**
	 * Check for collision between two elements and invoke necessary methods.
	 *
	 * @param e1
	 *            the first element
	 * @param e2
	 *            the second element
	 * @param e1lastPos
	 *            the last pos of e1
	 * @param e2lastPos
	 *            the last pos of e2
	 */
	private final void checkCollision(GameElement e1, GameElement e2, Vec e1lastPos, Vec e2lastPos) {
		// Return if one of the elements is about to be deleted.
		if (e1.getDeleteMe() || e2.getDeleteMe()) {
			return;
		}

		// Return if there is no collision
		if (!e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
			return;
		}

		// Simulate CollisionFrame with last Y position
		Vec e1lastYVec = new Vec(e1.getPos().getX(), e1lastPos.getY());
		Frame e1lastYFrame = new Frame(e1lastYVec.add(e1.getSize().scalar(-0.5f)), e1lastYVec.add(e1.getSize().scalar(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec e1lastXVec = new Vec(e1lastPos.getX(), e1.getPos().getY());
		Frame e1lastXFrame = new Frame(e1lastXVec.add(e1.getSize().scalar(-0.5f)), e1lastXVec.add(e1.getSize().scalar(0.5f)));

		// Simulate CollisionFrame with last Y position
		Vec e2lastYVec = new Vec(e2.getPos().getX(), e2lastPos.getY());
		Frame e2lastYFrame = new Frame(e2lastYVec.add(e2.getSize().scalar(-0.5f)), e2lastYVec.add(e2.getSize().scalar(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec e2lastXVec = new Vec(e2lastPos.getX(), e2.getPos().getY());
		Frame e2lastXFrame = new Frame(e2lastXVec.add(e2.getSize().scalar(-0.5f)), e2lastXVec.add(e2.getSize().scalar(0.5f)));

		this.simulateCollision(e1lastXFrame, e2lastXFrame, e1, e2, e1lastPos, e2lastPos, e1lastYFrame, e2lastYFrame);
	}

	/**
	 * Simulate collision between e1 and e2.
	 *
	 * @param e1lastXFrame
	 *            e1 last x-frame
	 * @param e2lastXFrame
	 *            e2 last x-frame
	 * @param e1
	 *            the object e1
	 * @param e2
	 *            the object e2
	 * @param e1lastPos
	 *            the last pos of e1
	 * @param e2lastPos
	 *            the last pos of e2
	 * @param e1lastYFrame
	 *            e1 last y-frame
	 * @param e2lastYFrame
	 *            e2 last y-frame
	 */
	private final void simulateCollision(Frame e1lastXFrame, Frame e2lastXFrame, GameElement e1, GameElement e2, Vec e1lastPos, Vec e2lastPos,
			Frame e1lastYFrame, Frame e2lastYFrame) {
		// If they still collide with the old x positions:
		// it must be because of the y position
		if (e1lastXFrame.collidesWith(e2lastXFrame)) {
			// If relative movement is in positive y direction (down)
			float relMovement = (e2.getPos().getY() - e2lastPos.getY()) - (e1.getPos().getY() - e1lastPos.getY());
			if (relMovement > 0) {
				e1.reactToCollision(e2, Direction.UP);
			} else
			// If relative movement is in negative y direction (up)
			if (relMovement < 0) {
				e1.reactToCollision(e2, Direction.DOWN);
			} else {
				return;
			}
			// check if he is still colliding even with last x position
			this.checkCollision(e1, e2, new Vec(e1lastPos.getX(), e1.getPos().getY()), new Vec(e2lastPos.getX(), e2.getPos().getY()));
		} else
		// If they still collide with the old y positions:
		// it must be because of the x position
		if (e1lastYFrame.collidesWith(e2lastYFrame)) {
			// If he moved in positive x direction (right)
			float relMovement = (e2.getPos().getX() - e2lastPos.getX()) - (e1.getPos().getX() - e1lastPos.getX());
			if (relMovement > 0) {
				e1.reactToCollision(e2, Direction.LEFT);
			} else
			// If he moved in negative x direction (left)
			if (relMovement < 0) {
				e1.reactToCollision(e2, Direction.RIGHT);
			} else {
				return;
			}
			// check if he is still colliding even with last x position
			this.checkCollision(e1, e2, new Vec(e1.getPos().getX(), e1lastPos.getY()), new Vec(e2.getPos().getX(), e2lastPos.getY()));
		}

	}

}
