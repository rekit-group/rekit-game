package rekit.logic.gameelements.entities.enemies.piston;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.entities.enemies.piston.state.ClosedState;
import rekit.logic.gameelements.entities.enemies.piston.state.ClosingState;
import rekit.logic.gameelements.entities.enemies.piston.state.OpenState;
import rekit.logic.gameelements.entities.enemies.piston.state.OpeningState;
import rekit.logic.gameelements.entities.enemies.piston.state.PistonState;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Progress;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.state.TimeStateMachine;

/**
 *
 * This enemy is a piston that periodically smashes towards direction.
 * Its extension length, open & closed times, movement speed and phase offset can be cofigured. 
 *
 */
@LoadMe
@SetterInfo(res = "conf/piston")
public final class Piston extends Enemy implements Configurable, IPistonForState {
	
	/**
	 * The height of the non-moving base of the piston.
	 */
	protected static float BASE_HEIGHT;
	/**
	 * The width of the moving part of the piston.
	 */
	protected static float PISTON_WIDTH;
	
	/**
	 * The short distance between piston tip and the actual defined {@link Piston.expansionLength}.
	 */
	protected static float LOWER_MARGIN;

	/**
	 * The color of the non-moving base of the piston.
	 */
	private static RGBAColor BASE_COLOR;
	
	/**
	 * The color of the moving part of the piston.
	 */
	protected static RGBAColor PISTON_COLOR;

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
	 * The reference to the inner, moving part of the piston.
	 */
	@NoSet
	private PistonInner inner;
	
	/**
	 * The length in units, the piston expands.
	 * The actual size varies by optical means such as BASE_HEIGHT and LOWER_MARGIN. 
	 */
	@NoSet
	private int expansionLength;
	
	/**
	 * The direction that piston is directed to.
	 */
	@NoSet
	private Direction direction;
	

	/**
	 * The id of the phase to start with.
	 */
	@NoSet
	private int startPhaseId;
	
	/**
	 * The internal StateMachine that handles everything time related. 
	 */
	@NoSet
	private TimeStateMachine machine;
	
	@NoSet
	private long calcTimeOpen;
	
	@NoSet
	private long calcTimeClosed;
	
	@NoSet
	private long calcTimeTransition;
	
	/**
	 * Prototype Constructor.
	 */
	public Piston() {
		super();
	}

	
	public Piston(Vec startPos, int expansionLength, Direction direction, float timeOpen, float timeClosed, float movementSpeed, float startPhaseId) {
		super(startPos, new Vec(), new Vec(Piston.BASE_HEIGHT, 1));
		
		// save trivial parameters
		this.direction = direction;
		this.expansionLength = expansionLength;
		
		// calculate base position (determined by Direction and BASE_HEIGHT)
		Vec basePos = new Vec(0, 0.5f - Piston.BASE_HEIGHT/2f); // case upwards
		basePos = basePos.rotate(direction.getAngle());
		this.setPos(startPos.add(basePos));
		
		// set size (determined by Direction and BASE_HEIGHT)
		Vec size = new Vec(1, Piston.BASE_HEIGHT);
		this.setSize(size);
		
		// calculate all durations
		this.calcTimeOpen = (long) Piston.OPEN_TIME.getNow(timeOpen);
		this.calcTimeClosed = (long) Piston.CLOSED_TIME.getNow(timeClosed);
		this.calcTimeTransition = (long) (1000 * expansionLength / Piston.MOVEMENT_SPEED.getNow(movementSpeed));

		// Create TimeStateMachine for opening/closing behavior.
		this.machine = new TimeStateMachine(new OpenState((IPistonForState)this)); 
		
		// go to the right start phase
		for (int i = 0; i < startPhaseId % 4; i++) {
			this.machine.nextState();
		}
	}

	@Override
	public void internalRender(GameGrid f) {
		// Draw base part of Piston
		f.drawRectangle(this.getPos(), this.getSize(), Piston.BASE_COLOR);
	}

	@Override
	protected void innerLogicLoop() {
		
		if (this.inner == null && this.getScene() != null) {
			this.inner = new PistonInner();
			this.getScene().addGameElement(this.inner);
		}
		
		// Let the machine work...
		this.machine.logicLoop();
	
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
		int expansionLength = 1;
		Direction direction = Direction.DOWN;
		
		float timeOpen = 0.5f;
		float timeClosed = 0.5f;
		float movementSpeed = 0.5f;
		int startPhaseId = 0;
		
		return new Piston(
				startPos,
				expansionLength,
				direction,
				timeOpen,
				timeClosed,
				movementSpeed,
				startPhaseId);
		/*
		// if option 0 is given: set defined direction
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-3]+")) {
			int opt = Integer.parseInt(options[0]);
			if (opt >= 0 && opt < Direction.values().length) {
				// Do sth
			} else {
				GameConf.GAME_LOGGER.error("");
			}
		}
		*/
	}
	
	public class PistonInner extends Enemy {
		
		PistonInner() {
			super(new Vec(), new Vec(), new Vec());
		}
		
		public void innerLogicLoop() {
			PistonState currentState = (PistonState) Piston.this.machine.getState();
			
			// calculate middle pos and size
			Vec btmPos = new Vec(0, 0.5f - Piston.BASE_HEIGHT);
			Vec topPos = btmPos.add(new Vec(0, -currentState.getCurrentHeight() * Piston.this.expansionLength));
			Vec middlePos = btmPos.add(topPos).scalar(0.5f);
			Vec size = new Vec(Piston.PISTON_WIDTH, btmPos.y - topPos.y);
			
			// setting values for rendering and collision frame
			this.setPos(Piston.this.getPos().add(middlePos.rotate(Piston.this.direction.getAngle())));
			this.setSize(size.rotate(Piston.this.direction.getAngle()));
		}
		
		@Override
		public void internalRender(GameGrid f) {
			
			
			
			
			
			f.drawRectangle(this.getPos(), this.getSize(), Piston.PISTON_COLOR);
			
			//Piston.this.getPos()
		}
		
		@Override
		public GameElement create(Vec startPos, String[] options) {
			return null;
		}

	}

	@Override
	public long getCalcTimeOpen() {
		return this.calcTimeOpen;
	}


	@Override
	public long getCalcTimeClosed() {
		return this.calcTimeClosed;
	}


	@Override
	public long getCalcTimeTransistion() {
		return this.calcTimeTransition;
	}


}
