package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.RektKiller;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.DamageState;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.State3;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.type.Boss;
import rekit.logic.level.bossstructure.BossStructure;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBColor;
import rekit.util.ReflectUtils.LoadMe;
import rekit.util.state.TimeStateMachine;

@LoadMe
public class RocketBoss extends Boss {

	private TimeStateMachine machine;

	private Vec startPos;

	private float calcX;

	private Mouth mouth;

	public static Vec MOVEMENT_PERIOD = new Vec(1.6f, 0.9f);
	public static Vec MOVEMENT_RANGE = new Vec(0.3f, 0.7f);

	public static Vec HEAD_SIZE = new Vec(2, 1.6f);
	public static Vec HEAD_PADDING = new Vec(0.2f, 0.2f);
	public static RGBColor HEAD_COL = new RGBColor(100, 100, 100);

	public static Vec EYE_SIZE = new Vec(0.4f, 0.4f);
	public static Vec EYE_LEFT_POS = EYE_SIZE.scalar(0.5f).add(HEAD_PADDING).sub(HEAD_SIZE.scalar(0.5f));
	public static Vec EYE_RIGHT_POS = EYE_LEFT_POS.scalar(-1, 1);

	public static Vec MOUTH_SIZE = new Vec(1.6f, 0.4f);
	public static Vec MOUTH_POS = (MOUTH_SIZE.scalar(-0.5f).sub(HEAD_PADDING).add(HEAD_SIZE.scalar(0.5f))).setX(0);
	public static RGBColor MOUTH_BG_COL = new RGBColor(200, 200, 200);

	public static long ARM_STATE_TIME_BUILD = 100;
	public static long ARM_SEGMENT_DIST = 10;
	public static Vec ARM_SEGMENT_SIZE = new Vec(1f, 1f);

	public static RGBColor ARM_SEGMENT_COL = new RGBColor(100, 100, 100);

	/**
	 * Standard constructor
	 */
	public RocketBoss() {

	}

	public RocketBoss(Vec startPos) {
		super(startPos, new Vec(), HEAD_SIZE);
		this.startPos = startPos;
		this.machine = new TimeStateMachine(new State3());
		this.mouth = new Mouth(this, MOUTH_POS, MOUTH_SIZE, MOUTH_BG_COL);
	}

	public DamageState getState() {
		return (DamageState) this.getMachine().getState();
	}

	@Override
	public void innerLogicLoop() {
		// add deltaTime with factor to local x
		float deltaX = deltaTime * getState().getTimeFactor();
		calcX += deltaX;

		// calculate and update position
		Vec scaleVec = new Vec((float) Math.sin(MOVEMENT_PERIOD.getX() * calcX), (float) Math.cos(MOVEMENT_PERIOD.getY() * calcX));
		Vec scaledUnit = MOVEMENT_RANGE.multiply(scaleVec);
		this.setPos(startPos.add(scaledUnit));

		mouth.logicLoop(calcX, deltaX);
	}

	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), HEAD_COL);

		// Render eyes
		f.drawImage(this.getPos().add(EYE_LEFT_POS), EYE_SIZE, this.getState().getEyeImgSrc());
		f.drawImage(this.getPos().add(EYE_RIGHT_POS), EYE_SIZE, this.getState().getEyeImgSrc());

		// Render mouth
		mouth.internalRender(f);
	}

	/**
	 * @return the machine
	 */
	public TimeStateMachine getMachine() {
		return machine;
	}

	@Override
	public BossStructure getBossStructure() {
		// TODO Refactor to new Layout

		int[][][] oldStruct = new int[][][] {
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } } };

		String[][] struct = new String[oldStruct.length][];
		for (int i = 0; i < oldStruct.length; i++) {
			String[] l = new String[oldStruct[i].length];
			for (int j = 0; j < oldStruct[i].length; j++) {
				if (oldStruct[i][j][0] == 0) {
					l[j] = null;
				} else if (oldStruct[i][j][0] == 1) {
					l[j] = Inanimate.class.getName();
				} else if (oldStruct[i][j][0] == 2) {
					l[j] = RektKiller.class.getName();
				}
			}
			struct[i] = l;
		}
		BossStructure structure = new BossStructure(struct, this);
		this.setBossStructure(structure);
		return structure;
	}

	@Override
	public Vec getStartPos() {
		return new Vec(25, GameConf.GRID_H / 2);
	}

	@Override
	public String getName() {
		return "Crazy Rocket Robot";
	}

	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new RocketBoss(startPos);
	}
}
