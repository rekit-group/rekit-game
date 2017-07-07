package rekit.tests;

import java.util.Random;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import net.jafama.FastMath;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * Sample Enemy that has rudimentary functionality. It bounces on the floor and
 * damages if hit from below or the sides but is vulnerable from the top.
 *
 * See the configuration file in src/main/resources/conf/pizza.properties
 *
 * @author Angelo Aracri
 */
@SetterInfo(res = "conf/pizza")
@LoadMe
public final class Pizza extends Enemy implements Configurable {

	/**
	 * The score the {@link Player} receives upon killing this {@link Enemy}. Is
	 * configurable in src/main/resources/conf/pizza.properties
	 */
	public static int POINTS;

	/**
	 * The {@link RGBAColor} of the {@link Enemy Enemies} crust. Is configurable
	 * in src/main/resources/conf/pizza.properties
	 */
	public static RGBAColor COLOR_OUTER;

	/**
	 * The {@link RGBAColor} of the {@link Enemy Enemies} inner dough. Is
	 * configurable in src/main/resources/conf/pizza.properties
	 */
	public static RGBAColor COLOR_INNER;

	/**
	 * The {@link RGBAColor} of the {@link Enemy Enemies} salami. Is
	 * configurable in src/main/resources/conf/pizza.properties
	 */
	public static RGBAColor COLOR_SALAMI;

	/**
	 * The size of the {@link Enemy} as a two dimensional {@link Vec}. Is
	 * configurable in src/main/resources/conf/pizza.properties
	 */
	public static Vec SIZE_OUTER;

	/**
	 * The size of the {@link Enemy Enemies} inner circle as a two dimensional
	 * {@link Vec}. Is configurable in src/main/resources/conf/pizza.properties
	 */
	public static Vec SIZE_INNER;

	/**
	 * The size of the {@link Enemy Enemies} salamis. Is configurable in
	 * src/main/resources/conf/pizza.properties
	 */
	public static Vec SIZE_SALAMI;

	/**
	 * The amount of salamis that a pizza needs to be delicious. Is configurable
	 * in src/main/resources/conf/pizza.properties
	 */
	private static int NUM_SALAMI = 6;

	/**
	 * The rotation speed in radians per ms. Is configurable in
	 * src/main/resources/conf/pizza.properties
	 */
	private static float ANGLE_SPEED;

	/**
	 * The velocity as a two dimensional {@link Vec} that will be applied to the
	 * {@link Enemy} upon touching the floor.
	 * src/main/resources/conf/pizza.properties
	 */
	private static Vec JUMP_VELOCITY;

	@NoSet
	private static Random RNG = new Random();

	@NoSet
	private float[] radius;

	@NoSet
	private float currentAngle;

	public Pizza() {
		super();
	}

	/**
	 * Standard Constructor that saves the initial position.
	 *
	 * @param startPos
	 *            the initial position of the Enemy.
	 */
	public Pizza(Vec startPos) {
		super(startPos, new Vec(), Pizza.SIZE_OUTER);

		this.radius = new float[Pizza.NUM_SALAMI];
		for (int i = 0; i < Pizza.NUM_SALAMI; i++) {
			this.radius[i] = 0.1f + Pizza.RNG.nextFloat() * 0.15f;
		}
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawCircle(this.getPos(), this.getSize(), Pizza.COLOR_OUTER);
		f.drawCircle(this.getPos(), Pizza.SIZE_INNER, Pizza.COLOR_INNER);

		float phi = this.currentAngle;
		for (int i = 0; i < Pizza.NUM_SALAMI; i++) {
			// get polar coordinates
			phi += 2 * Math.PI / Pizza.NUM_SALAMI;
			float rad = this.radius[i];

			// convert to x, y
			Vec pos = new Vec(FastMath.sinQuick(phi) * rad, FastMath.cosQuick(phi) * rad);

			// SALAMI
			f.drawCircle(this.getPos().add(pos), Pizza.SIZE_SALAMI, Pizza.COLOR_SALAMI);

		}

		// f.drawCircle(pos, size, color);
		// f.drawRectangle(pos, size, color);
		// f.drawRoundRectangle(pos, size, color, arcWidth, arcHeight);
		// f.drawLine(a, b, ingame, usefilter);
		// f.drawPath(startPos, pts, color, usefilter);
		// f.drawPolygon(polygon, color, fill);
		// f.drawText(arg0, arg1, arg2, arg3);
	}

	@Override
	protected void innerLogicLoop() {
		// Do usual entity logic (applying velocity, ...)
		super.innerLogicLoop();

		// newAngle = time * angleSpeed
		// deltaTime is time in ms since last call of innerLogicLoop
		this.currentAngle += this.deltaTime * Pizza.ANGLE_SPEED;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		// Only continue if the element is hostile to the enemy
		// (meaning element is Player)
		if (!this.getTeam().isHostile(element.getTeam())) {
			return;
		}

		// If hit from above:
		if (dir == Direction.UP) {
			// give the player points
			this.getScene().getPlayer().addPoints(Pizza.POINTS);
			// Let the player jump jump
			element.killBoost();
			// kill the enemy
			this.addDamage(1);
		} else {
			// Touched dangerous side
			// Give player damage
			element.addDamage(1);
			// Kill the enemy itself
			this.addDamage(1);
		}
	}

	@Override
	public void collidedWithSolid(Frame collision, Direction dir) {
		// standard behavior, that prevents clipping into other blocks
		super.collidedWithSolid(collision, dir);

		// upward push
		this.setVel(Pizza.JUMP_VELOCITY);
	}

	@Override
	public Pizza create(Vec startPos, String... options) {
		return new Pizza(startPos);
	}
}
