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
				inst.setDirection(Direction.values()[opt]);
			} else {
				GameConf.GAME_LOGGER.error("RektKiller was supplied invalid option " + options[0] + " at index 0 for Direction");
			}
		}

		return inst;
	}


}
