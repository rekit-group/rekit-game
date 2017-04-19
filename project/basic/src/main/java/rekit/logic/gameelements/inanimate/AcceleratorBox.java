package rekit.logic.gameelements.inanimate;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.type.DynamicInanimate;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;


@LoadMe
@SetterInfo(res = "conf/acceleratorBox")
public final class AcceleratorBox extends DynamicInanimate implements Configurable {
	
	private static Vec SIZE;

	private static RGBAColor COL_1;
	private static RGBAColor COL_2;

	private static float ANGLE_RANGE_FACTOR;

	private static float BOOST;
	
	private static float BORDER_WIDTH;
	private static float INNER_RADIUS;
	
	@NoSet
	private Polygon innerPolygon = new Polygon(new Vec(), new Vec[]{
			new Vec(-INNER_RADIUS, INNER_RADIUS), //
			new Vec(0, -INNER_RADIUS), //
			new Vec(INNER_RADIUS, INNER_RADIUS), //
			new Vec(0, 0), //
	});
	
	@NoSet
	private Direction direction;
			
	/**
	 * Prototype Constructor.
	 */
	public AcceleratorBox() {
		super();
	}

	protected AcceleratorBox(Vec startPos, Direction dir) {
		super(startPos, AcceleratorBox.SIZE, null);
		this.direction = dir;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), AcceleratorBox.COL_2);
		f.drawRectangle(this.getPos(), this.getSize().add(new Vec(-2 * AcceleratorBox.BORDER_WIDTH)), AcceleratorBox.COL_1);
		
		this.innerPolygon.moveTo(this.getPos());
		f.drawPolygon(this.innerPolygon, COL_2, true, true);
	}

	@Override
	public AcceleratorBox create(Vec startPos, String[] options) {
		return new AcceleratorBox(startPos, Direction.UP);
	}
}
