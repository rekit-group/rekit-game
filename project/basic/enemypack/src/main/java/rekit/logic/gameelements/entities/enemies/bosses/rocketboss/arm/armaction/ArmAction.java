package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.RocketBossChild;
import rekit.primitives.geometry.Vec;


/**
 * This abstract ArmAction represents what the {@link RocketBoss} can hold in its hand at the end
 * of its {@link Arm}.
 * The ArmAction is supplied with:
 * <ul>
 * 	<li>A {@link #perform()} method that will be called once at a specific time</li>
 * 	<li>A periodic {@link #logicLoop(float calcX, float deltaX)}-call</li>
 * 	<li>A periodic {@link #innerLogic()}-call</li>
 * 	<li>A {@link #tearDown()} method that will be called upon the ArmActions end.</li>
 * </ul>
 * 
 * @author Angelo Aracri
 */
public abstract class ArmAction extends RocketBossChild {
	
	/**
	 * A list of all ArmActions there should be.
	 * Holds their corresponding probability as putting an ArmAction
	 * in this list twice, doubles its occurrences.
	 */
	private static ArmAction[] possibleArmActions = new ArmAction[]{
		new ArmActionRocketLauncher(),
		new ArmActionRocketLauncher(),
		new ArmActionRocketLauncher(),
		new ArmActionCannon()
	};
	
	/**
	 * Standard Constructor used to fulfill the prototype pattern
	 */
	public ArmAction() {
		super();
	}
	
	/**
	 * Constructor that saves a parenting {@link RocketBoss} and the relative position of this ArmAction to the parent.
	 * @param parent
	 * @param relPos
	 */
	public ArmAction(RocketBoss parent, Vec relPos) {
		super(parent, relPos);
	}
	
	/**
	 * An optional action the ArmAction can define that will be called once
	 */
	public abstract void perform();
	
	
	/**
	 * Static getter for a random ArmAction as specified in the list {@link possibleArmActions}
	 * @return a random ArmAction
	 */
	public static ArmAction getRandomArmAction() {
		return possibleArmActions[GameConf.PRNG.nextInt(possibleArmActions.length)];
	}
	
	@Override
	public void logicLoop(float calcX, float deltaX) {
		
	}
	
	@Override
	public void internalRender(GameGrid f) {
		
	}

	/**
	 * Optional functionality that will be called upon destroying the ArmAction after it is done
	 */
	public void tearDown() {
		
	}
	
	
}
