package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state.IdleState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state.State;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

@LoadMe
@VisitInfo(res = "conf/cannon", visit = true)
public class Cannon extends Enemy implements Visitable, StateMachine {

	private static Vec SIZE;
	public static float STATE_IDLE_DURATION;
	public static float STATE_AIMING_DURATION;
	public static float STATE_CHARGING_DURATION;
	public static float STATE_SHOOTING_DURATION;
	
	public static float ANGLE_SPEED;
	
	public static RGBColor COLOR_BASE;
	public static RGBColor COLOR_CANNON;
	
	@NoVisit
	private State currentState;
	
	@NoVisit
	private float currentAngle;
	
	@NoVisit
	private Polygon polygon;
	
	/**
	 * Prototype constructor
	 */
	public Cannon() {
	}
	
	public Cannon(Vec pos) {
		super(pos.addY(-0.5f + SIZE.getY() / 2f), new Vec(), SIZE);
		
		this.currentState = new IdleState();
		this.currentState.enter(this);
		this.currentAngle = currentState.getTargetAngle();
		
		float x = 0.3f;
		float y = 0.9f;
		this.polygon = new Polygon(this.getPos(), new Vec[]{
			new Vec(x/2, 0),
			new Vec(x/2, y),
			new Vec(-x/2, y),
			new Vec(-x/2, 0),
			new Vec(0, 0)
		});
	}
	
	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new Cannon(startPos);
	}
	
	@Override
	public void internalRender(Field f) {
		
		// draw cannon base
		f.drawCircle(this.getPos(), this.getSize(), COLOR_BASE);
		f.drawRectangle(this.getPos().addY(-this.getSize().getY() / 4f), this.getSize().scalar(1, 0.5f), COLOR_BASE);
		
		// draw rotated cannon with (optional) shaking
		Vec cannonPos = this.getPos().addX(this.currentState.getCannonShake());
		f.drawCircle(cannonPos, this.getSize().scalar(0.8f), COLOR_CANNON);
		this.polygon.moveTo(cannonPos);
		f.drawPolygon(this.polygon.rotate(-this.currentAngle, this.getPos()), COLOR_CANNON, true);
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.currentState.logicLoop(deltaTime);
		
		// move angle in right direction
		this.currentAngle += Math.signum(this.currentState.getTargetAngle() - this.currentAngle) * deltaTime * ANGLE_SPEED;
		
		if (Math.abs(this.currentState.getTargetAngle() - this.currentAngle) < ANGLE_SPEED / 10) {
			this.currentAngle = this.currentState.getTargetAngle();
		}

	}

	@Override
	public void setNextState(State next) {
		currentState.leave();
		next.enter(this);
		currentState = next;
	}
	
	public void hitSomething() {
		this.currentState.hitSomething();
	}
	
}
