package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.DamageState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.State3;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.level.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.util.state.TimeStateMachine;

@LoadMe
public class RocketBoss extends Boss {

	private TimeStateMachine machine;

	private Vec startPos;

	private float calcX;

	private Mouth mouth;

	private static Vec MOVEMENT_PERIOD = new Vec(1.6f, 0.9f);
	private static Vec MOVEMENT_RANGE = new Vec(0.3f, 0.7f);

	private static Vec HEAD_SIZE = new Vec(2, 1.6f);
	private static Vec HEAD_PADDING = new Vec(0.2f, 0.2f);
	private static RGBColor HEAD_COL = new RGBColor(100, 100, 100);

	private static Vec EYE_SIZE = new Vec(0.4f, 0.4f);
	private static Vec EYE_LEFT_POS = RocketBoss.EYE_SIZE.scalar(0.5f).add(RocketBoss.HEAD_PADDING).sub(RocketBoss.HEAD_SIZE.scalar(0.5f));
	private static Vec EYE_RIGHT_POS = RocketBoss.EYE_LEFT_POS.scalar(-1, 1);

	private static Vec MOUTH_SIZE = new Vec(1.6f, 0.4f);
	private static Vec MOUTH_POS = (RocketBoss.MOUTH_SIZE.scalar(-0.5f).sub(RocketBoss.HEAD_PADDING).add(RocketBoss.HEAD_SIZE.scalar(0.5f))).setX(0);
	private static RGBColor MOUTH_BG_COL = new RGBColor(200, 200, 200);

	/**
	 * Standard constructor
	 */
	public RocketBoss() {

	}

	public RocketBoss(Vec startPos) {
		super(startPos, new Vec(), RocketBoss.HEAD_SIZE);
		this.startPos = startPos;
		this.machine = new TimeStateMachine(new State3());
		this.mouth = new Mouth(this, RocketBoss.MOUTH_POS, RocketBoss.MOUTH_SIZE, RocketBoss.MOUTH_BG_COL);
	}

	public DamageState getState() {
		return (DamageState) this.getMachine().getState();
	}

	@Override
	protected void innerLogicLoop() {

		// add deltaTime with factor to local x
		float deltaX = this.deltaTime * this.getState().getTimeFactor() * 1000;
		this.calcX += deltaX;

		// calculate and update position
		Vec scaleVec = new Vec((float) Math.sin(RocketBoss.MOVEMENT_PERIOD.getX() * this.calcX),
				(float) Math.cos(RocketBoss.MOVEMENT_PERIOD.getY() * this.calcX));
		Vec scaledUnit = RocketBoss.MOVEMENT_RANGE.multiply(scaleVec);
		this.setPos(this.startPos.add(scaledUnit));

		this.mouth.logicLoop(this.calcX, deltaX);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), RocketBoss.HEAD_COL);

		// Render eyes
		f.drawImage(this.getPos().add(RocketBoss.EYE_LEFT_POS), RocketBoss.EYE_SIZE, this.getState().getEyeImgSrc());
		f.drawImage(this.getPos().add(RocketBoss.EYE_RIGHT_POS), RocketBoss.EYE_SIZE, this.getState().getEyeImgSrc());

		// Render mouth

		this.mouth.render(f);
	}

	/**
	 * @return the machine
	 */
	public TimeStateMachine getMachine() {
		return this.machine;
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
