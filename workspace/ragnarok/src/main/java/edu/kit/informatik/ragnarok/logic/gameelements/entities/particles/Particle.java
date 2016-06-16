package edu.kit.informatik.ragnarok.logic.gameelements.entities.particles;

import org.eclipse.swt.graphics.RGBA;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.ProgressDependency;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

/**
 * A simple GameElement with no collision and only with graphical effects.
 * Every Particle has a fixed lifeTime after which it removes itself.
 * <p>Sad life.</p>
 * Can be given many options as to how to draw the particle and how it changes
 * these options over time.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class Particle extends GameElement {

	private Polygon polygon;

	private ProgressDependency colorR;
	private ProgressDependency colorG;
	private ProgressDependency colorB;
	private ProgressDependency colorA;
	//private ProgressDependency size;
	private ProgressDependency speed;
	private ProgressDependency angle;

	private TimeDependency timer;

	private RGBA currentCol = new RGBA(0, 0, 0, 0);
	
	private Vec2D movementVec = null;
	
	/**
	 * Constructor that takes all ProgressDependencies required for the Particle behavior
	 * @param polygon the shape that the particle should be drawn with
	 * @param pos the initial position of the particle
	 * @param time the time in seconds for how the particle will be rendered
	 * @param size the <i>ProgressDendency</i> for the polygons size
	 * @param speed the <i>ProgressDendency</i> for the polygons movement speed
	 * @param angle the <i>ProgressDendency</i> for the polygons movement angle
	 * @param colorR the <i>ProgressDendency</i> for the polygons red color channel
	 * @param colorG the <i>ProgressDendency</i> for the polygons green color channel
	 * @param colorB the <i>ProgressDendency</i> for the polygons blue color channel
	 * @param colorA the <i>ProgressDendency</i> for the polygons alpha color channel
	 */
	public Particle(Polygon polygon, Vec2D pos, float lifeTime,
			ProgressDependency size, ProgressDependency speed,
			ProgressDependency angle, ProgressDependency colorR,
			ProgressDependency colorG, ProgressDependency colorB,
			ProgressDependency colorA) {
		super();
		
		// clone polygon so we can work with it
		this.polygon = polygon.clone();

		// set shape options
		// this.size = size;
		
		// set movement options
		this.speed = speed;
		this.angle = angle;

		// set color options
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.colorA = colorA;

		// create timer to get progress between 0 and 1 relative to time
		this.timer = new TimeDependency(lifeTime);
		
		// set position
		this.setPos(pos.clone());
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		// tell timer passed time
		this.timer.removeTime(deltaTime);

		// If time is up: kill yourself
		if (this.timer.timeUp()) {
			this.getGameModel().removeGameElement(this);
		} else {
			// get current progress of Particle
			float progress = this.timer.getProgress();
			currentCol = new RGBA(capColor(colorR.getNow(progress)),
					capColor(colorG.getNow(progress)), capColor(colorB.getNow(progress)),
					capColor(colorA.getNow(progress)));

			// get speed and angle relative to progress
			float speed = this.speed.getNow(progress);
			float angle = this.angle.getNow(progress);

			// only recalculate movement vector if speed and angle are dynamic
			if (movementVec == null || !this.speed.isStatic() || !this.angle.isStatic()) {
				// get Einheitsvector in 0degrees
				movementVec = Direction.UP.getVector();
				
				// set Amount in units/time
				movementVec = movementVec.multiply(speed * deltaTime);
				
				// set Angle
				movementVec = movementVec.rotate(angle);
			}

			// apply distance-vector to position
			this.setPos(this.getPos().add(movementVec));
		}
	}

	@Override
	public int getZ () {
		return 100;
	}
	
	@Override
	public void render(Field f) {
		//float progress = timer.getProgress();

		polygon.moveTo(this.getPos());
		
		f.drawPolygon(polygon, currentCol);
	}
	
	/**
	 * Caps a color to make sure it is never smaller than 0 or greater than 255
	 * @param col the color value to cap
	 * @return the capped color
	 */
	public int capColor(float col) {
		int intCol = (int) col;
		return intCol > 255 ? 255 : (intCol < 0 ? 0 : intCol);
	}

	@Override
	public void addPoints(int points) {
		// Do nothing
	}

	@Override
	public int getPoints() {
		// Do nothing
		return 0;
	}

	@Override
	public void addDamage(int damage) {
		// Do nothing
	}

	@Override
	public int getLifes() {
		// Do nothing
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing
	}

}