package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
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
		RGB innerColor = new RGB(90, 90, 90);
		RGB frontColor = new RGB(200, 30, 30);
		RGB outerColor = new RGB(50, 50, 50);
		
		// draw body
		f.drawRectangle(this.getPos(), this.getSize().multiply(0.8f, 0.6f), innerColor);
		
		// draw spike at front
		Vec2D startPt = this.getPos().addX(-this.getSize().multiply(0.5f).getX());
		Vec2D[] relPts = new Vec2D[] {
				new Vec2D(this.getSize().multiply(0.1f).getX(), -this.getSize().multiply(0.5f).getY()),
				new Vec2D(this.getSize().multiply(0.1f).getX(), this.getSize().multiply(0.5f).getY()),
				new Vec2D()
		};
		f.drawPolygon(startPt, relPts, frontColor);
		
		// draw stripes
		Vec2D stripeStart = this.getPos().addX(-this.getSize().multiply(0.4f - 0.05f - 0.025f).getX());
		for (int x = 0; x < 9; x++) {
			f.drawRectangle(
					stripeStart.addX(0.15f * x),
					this.getSize().multiply(0.05f, 0.75f), outerColor);
		}
		
		// draw drive at back
		startPt = this.getPos().addX(this.getSize().multiply(0.5f).getX()).addY(-this.getSize().multiply(0.5f).getY());
		relPts = new Vec2D[] {
				new Vec2D(0, this.getSize().getY()),
				new Vec2D(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.8f),
				new Vec2D(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.2f),
				new Vec2D()
		};
		f.drawPolygon(startPt, relPts, outerColor);
	}
	
	public Vec2D getSize() {
		return new Vec2D(1.8f, 0.5f);
	}

	
	@Override
	public void logicLoop(float deltaTime) {
		// move ahead with player max speed
		this.setPos(this.getPos().addX(-c.playerWalkMaxSpeed * deltaTime));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}
		
		if (this.isHostile(element)) {
			
			if (dir == Direction.UP) {
				element.setVel(element.getVel().setY(c.playerJumpBoost));
				
				element.addPoints(20);
				
				// Kill the rocket itself
				this.addDamage(1);
			} else {
				// Give player damage
				element.addDamage(1);

				// Kill the rocket itself
				this.addDamage(1);
			}
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
