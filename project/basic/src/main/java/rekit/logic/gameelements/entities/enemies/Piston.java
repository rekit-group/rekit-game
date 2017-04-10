package rekit.logic.gameelements.entities.enemies;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Progress;
import rekit.primitives.time.Timer;
import rekit.util.ReflectUtils.LoadMe;

/**
 *
 * This enemy is a piston that periodically smashes towards direction.
 * Its extension length, open & closed times, movement speed and phase offset can be cofigured. 
 *
 */
@LoadMe
@SetterInfo(res = "conf/piston")
public final class Piston extends Enemy implements Configurable {
	
	/**
	 * The height of the non-moving base of the piston
	 */
	private static float BASE_HEIGHT;
	/**
	 * The short distance between piston tip and the actual defined {@link Piston.expansionLength}
	 */
	private static float LOWER_MARGIN;

	/**
	 * The color of the non-moving base of the piston
	 */
	private static RGBAColor BASE_COLOR;
	
	/**
	 * The color of the moving part of the piston
	 */
	private static RGBAColor PISTON_COLOR;

	/**
	 * The minimum and maximum time the piston stays still in open state in milliseconds.
	 * See how the actual time can be defined in the parameters of the {@link Piston.Piston constructor}.
	 */
	private static Progress OPEN_TIME;
	
	/**
	 * The minimum and maximum time the piston stays still in closed state in milliseconds.
	 * See how the actual time can be defined in the parameters of the {@link Piston.Piston constructor}.
	 */
	private static Progress CLOSED_TIME;

	/**
	 * The minimum and maximum movement speed while opening and closing the piston in units per second.
	 * See how the actual speed can be defined in the parameters of the {@link Piston.Piston constructor}.
	 */
	private static Progress MOVEMENT_SPEED;
	
	/**
	 * The length in units, the piston expands.
	 * The actual size varies by optical means such as BASE_HEIGHT and LOWER_MARGIN. 
	 */
	private int expansionLength;
	
	/**
	 * The direction that piston is directed to.
	 */
	private Direction direction;
	
	/**
	 * The actual time the piston stays still while being open in milliseconds 
	 */
	private long openTime;
	
	/**
	 * The actual time the piston stays still while being closed in milliseconds 
	 */
	private long closedTime;
	
	/**
	 * The movement speed while opening and closing the piston in units per second.
	 */
	private long movementSpeed;
	
	/**
	 * The id of the phase to start with.
	 */
	private int startPhase;
	
	
	/**
	 * Prototype Constructor.
	 */
	public Piston() {
		super();
	}

	
	public Piston(Vec startPos) {
		super(startPos, new Vec(), new Vec(1.8f, 0.5f));
		
	}

	@Override
	public void internalRender(GameGrid f) {
		
	}

	@Override
	protected void innerLogicLoop() {
		
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			
			// Give player damage
			element.addDamage(1);
		}
	}


	@Override
	public Entity create(Vec startPos, String[] options) {
		Piston inst = new Piston(startPos);

		// if option 0 is given: set defined direction
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-3]+")) {
			int opt = Integer.parseInt(options[0]);
			if (opt >= 0 && opt < Direction.values().length) {
				// Do sth
			} else {
				GameConf.GAME_LOGGER.error("");
			}
		}

		return inst;
	}


}
