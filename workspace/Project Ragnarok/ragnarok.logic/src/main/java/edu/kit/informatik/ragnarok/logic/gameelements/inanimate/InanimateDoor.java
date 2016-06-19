package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;


import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateDoor extends InanimateBox {

	public InanimateDoor(Vec2D pos) {
		super(pos, new Vec2D(1, 7), new RGBColor(130, 130, 130));
	}

	@Override
	public void render(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);
		
		Vec2D start = this.getPos().add(this.getSize().multiply(-(1/2f))).add(new Vec2D(0.1f, 0.1f));
		Vec2D end = start.add(this.getSize()).add(new Vec2D(-0.2f, -0.2f));

		for (float x = start.getX(); x <= end.getX(); x+= 0.2) {
			f.drawCircle(new Vec2D(x, start.getY()), new Vec2D(0.12f), new RGBColor(80, 80, 80));
			f.drawCircle(new Vec2D(x, end.getY()), new Vec2D(0.12f), new RGBColor(80, 80, 80));	
		}
		
		for (float y = start.getY(); y <= end.getY(); y+= 0.2) {
			f.drawCircle(new Vec2D(start.getX(), y), new Vec2D(0.12f), new RGBColor(80, 80, 80));
			f.drawCircle(new Vec2D(end.getX(), y), new Vec2D(0.12f), new RGBColor(80, 80, 80));
		}
	}
	
}
