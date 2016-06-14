package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

public class Warper extends Entity {

	private static final float warpDelta = 1;
	private float warpTimeLeft = warpDelta;
	private float progress = warpTimeLeft / warpDelta;
	
	public Warper(Vec2D startPos) {
		super(startPos);
	}

	@Override
	public void render(Field f) {

		for (float i = 1; i >= 0.2; i-=0.1) {
			RGB innerColor = new RGB((int)(250 * (1-progress) * i), (int)(250 * (progress)), (int)(150));
			
			// draw body
			f.drawCircle(this.getPos(), this.getSize().multiply(i), innerColor);
		}
		
		
	}
	
	public Vec2D getSize() {
		return new Vec2D(0.6f, 0.6f);
	}

	
	
	@Override
	public void logicLoop(float deltaTime) {
		// decrease time left
		warpTimeLeft -= deltaTime;
		
		// if time is up
		if (warpTimeLeft < 0) {
			// reset time (with overflow)
			warpTimeLeft = Warper.warpDelta + warpTimeLeft;
			
			// get target (player)
			Vec2D target = this.getGameModel().getPlayer().getPos();
			
			// determine if x or y is greater in distance  
			Vec2D dif = this.getPos().add(target.multiply(-1)); 
			if (Math.abs(dif.getX()) > Math.abs(dif.getY())) {
				this.setPos(this.getPos().addX(-Math.signum(dif.getX())));
			} else {
				this.setPos(this.getPos().addY(-Math.signum(dif.getY())));
			}
		}
		
		// calculate progress in 0 to 1
		progress = warpTimeLeft / warpDelta;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}
		
		if (this.isHostile(element)) {
			
			
			// Give player damage
			element.addDamage(1);

			// Kill the warper itself
			this.addDamage(1);
		}
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, pass right through everything
	}

	@Override
	public void addDamage(int damage) {
		this.destroy();
	}

}
