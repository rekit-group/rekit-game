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

public class Particle extends GameElement {

	public Polygon polygon;

	private ProgressDependency colorR;
	private ProgressDependency colorG;
	private ProgressDependency colorB;
	private ProgressDependency colorA;
	private ProgressDependency size;
	private ProgressDependency speed;
	private ProgressDependency angle;

	private TimeDependency timer;

	private RGBA currentCol = new RGBA(0, 0, 0, 0);

	public Particle(Polygon polygon, Vec2D pos, float time,
			ProgressDependency size, ProgressDependency speed,
			ProgressDependency angle, ProgressDependency colorR,
			ProgressDependency colorG, ProgressDependency colorB,
			ProgressDependency colorA) {
		super();
		this.polygon = polygon.clone();

		this.size = size;// new ProgressDependency(sizeStart, sizeEnd);
		this.speed = speed;// new ProgressDependency(speedStart, speedEnd);
		this.angle = angle;// new ProgressDependency(angleStart, angleEnd);

		this.colorR = colorR;// new ProgressDependency(colorRStart, colorREnd);
		this.colorG = colorG;// new ProgressDependency(colorGStart, colorGEnd);
		this.colorB = colorB;// new ProgressDependency(colorBStart, colorBEnd);
		this.colorA = colorA;// new ProgressDependency(colorAStart, colorAEnd);

		this.timer = new TimeDependency(time);
		this.setPos(pos.clone());
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

//			System.out.println((int)colorR.getNow(progress) + " " + 
//					(int)colorG.getNow(progress) + " " + (int)colorB.getNow(progress) + " " +
//					(int)colorA.getNow(progress));
			// calculate current color
			currentCol = new RGBA((int)colorR.getNow(progress),
					(int)colorG.getNow(progress), (int)colorB.getNow(progress),
					(int)colorA.getNow(progress));

			// get speed and angle relative to progress
			float speed = this.speed.getNow(progress);
			float angle = this.angle.getNow(progress);

			// get Einheitsvektor
			Vec2D vec = Direction.UP.getVector();
			
			// set Amount in units/time
			vec = vec.multiply(speed * deltaTime);
			
			// set Angle
			vec = vec.rotate(angle);

			// apply distance-vector to position
			this.setPos(this.getPos().add(vec));
		}
	}

	@Override
	public int getZ () {
		return 100;
	}
	
	@Override
	public void render(Field f) {
		float progress = timer.getProgress();

		polygon.moveTo(this.getPos());
		
		f.drawPolygon(polygon, currentCol);
	}

}
