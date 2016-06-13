package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.controller.commands.InputCommand;
import edu.kit.informatik.ragnarok.controller.commands.WalkCommand;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class Rocket extends Entity {

	public Rocket(Vec2D startPos) {
		super(startPos);
	}

	@Override
	public void render(Field f) {
		RGB color = new RGB(30, 30, 200);
		
		f.drawRectangle(this.getPos(), this.getSize(), color);
	}
	
	public Vec2D getSize() {
		return new Vec2D(1.8f, 1.2f);
	}

	
	@Override
	public void logicLoop(float deltaTime) {
		// Walk left
		InputCommand cmd = new WalkCommand(Direction.LEFT);
		cmd.setEntity(this);
		cmd.apply();
		
		this.setPos(this.getPos().add(this.getVel().multiply(deltaTime)));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}
		
		if (this.isHostile(element)) {
			// Give player damage
			element.addDamage(1);

			// Kill the enemy itself
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
