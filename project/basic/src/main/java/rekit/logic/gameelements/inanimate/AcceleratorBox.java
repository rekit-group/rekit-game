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
	
	private static RGBAColor ANGLE_BOUND_COLOR;
	private static RGBAColor ANGLE_CURRENT_COLOR;
	// px
	private static int ANGLE_LINE_WIDTH;
	// units
	private static int ANGLE_LINE_LENGTH;
			
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
	
	@NoSet
	private boolean playerCaught = false;
	
	@NoSet
	private double angleLeft;
	
	@NoSet
	private double angleRight;
	
	@NoSet
	private double angleCurrent;
	
	@NoSet
	private Vec catchPos;
			
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

		if (this.direction.getOpposite() != dir) {
			this.catchPlayer(dir);
		}
		
		super.reactToCollision(element, dir);
	}
	
	/**
	 * Method that holds a player in aiming mode, so he can aim and jump  
	 * @param dir
	 */
	private void catchPlayer(Direction dir) {
		
		this.playerCaught = true;
		
		// NOTE: Everything is for case UP and rotated in the end
		// assuming UP is 0 degrees and angles go clockwise
		
		// Calc bounds of possible angles
		this.angleLeft = (Math.PI / 4f) * AcceleratorBox.ANGLE_RANGE_FACTOR;
		this.angleRight = (2 * Math.PI) - (Math.PI / 4f) * AcceleratorBox.ANGLE_RANGE_FACTOR;
		// If hit from right, set left angle to 0
		if (this.direction.getNextClockwise() == dir) {
			this.angleLeft = 0;
		} else
		// If hit from left, set right angle to 0
		if (this.direction.getNextAntiClockwise() == dir) {
			this.angleRight = 0;
		}
				
		// Calc middle between the bounds
		this.angleCurrent = ((this.angleLeft + this.angleRight) % 2 * Math.PI) / 2f;
		double pi = Math.PI;
		double delta = ((this.angleLeft - this.angleRight + 3*pi ) % (2*pi)) - pi;
		this.angleCurrent = (2*pi + this.angleRight + (delta / 2)) % (2*pi);

		this.catchPos = this.getPos().add(dir.getVector());
	}
	
	public void logicLoop() {
		if (this.playerCaught) {
			this.getScene().getPlayer().setPos(catchPos);
			this.getScene().getPlayer().setVel(new Vec());
		}
		super.logicLoop();
	}
	
	
	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), AcceleratorBox.COL_2);
		f.drawRectangle(this.getPos(), this.getSize().add(new Vec(-2 * AcceleratorBox.BORDER_WIDTH)), AcceleratorBox.COL_1);
		
		this.innerPolygon.moveTo(this.getPos());
		f.drawPolygon(this.innerPolygon, COL_2, true, true);
		
		if (this.playerCaught) {
			this.drawAngleLineTo(f, this.angleLeft, AcceleratorBox.ANGLE_BOUND_COLOR);
			this.drawAngleLineTo(f, this.angleRight, AcceleratorBox.ANGLE_BOUND_COLOR);
			this.drawAngleLineTo(f, this.angleCurrent, AcceleratorBox.ANGLE_CURRENT_COLOR);
		}
	}
	
	/**
	 * Helper method for drawing a line to show visual feedback of the angles.
	 * Note that this method will do nothing while {@link AcceleratorBox#playerCaught} is false.
	 * @param f the {@link GameGrid} to draw on.
	 * @param angle the angle to visualize
	 * @param color the color of the angle
	 */
	private void drawAngleLineTo(GameGrid f, double angle, RGBAColor color) {
		if (!this.playerCaught) {
			return;
		}
		
		Vec unitVec = new Vec(-Math.sin(angle), -Math.cos(angle));
		Vec otherPos = this.catchPos.add(unitVec.scalar(AcceleratorBox.ANGLE_LINE_LENGTH));
		
		f.drawLine(this.catchPos, otherPos, AcceleratorBox.ANGLE_LINE_WIDTH, color, true, false);
	}

	@Override
	public AcceleratorBox create(Vec startPos, String[] options) {
		return new AcceleratorBox(startPos, Direction.UP);
	}
}
