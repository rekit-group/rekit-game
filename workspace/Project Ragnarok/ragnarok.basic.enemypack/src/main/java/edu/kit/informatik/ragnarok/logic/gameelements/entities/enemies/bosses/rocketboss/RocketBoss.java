package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.DamageState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate.State3;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.level.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.logic.state.TimeStateMachine;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class RocketBoss extends Boss {

	private TimeStateMachine machine;

	private Vec startPos;

	private float calcX;

	private static Vec MOVEMENT_PERIOD = new Vec(1.2f, 0.9f);
	private static Vec MOVEMENT_RANGE = new Vec(0.3f, 0.7f);

	/**
	 * Standard constructor
	 */
	public RocketBoss() {

	}

	public RocketBoss(Vec startPos) {
		super(startPos, new Vec(), new Vec(2, 1.6f));
		this.startPos = startPos;
		this.machine = new TimeStateMachine(new State3());
	}

	public DamageState getState() {
		return (DamageState) this.getMachine().getState();
	}

	public void logicLoop(float deltaTime) {
		// add deltaTime with factor to local x
		float deltaX = deltaTime * getState().getTimeFactor();
		calcX += deltaX;

		// calculate and update position
		Vec scaleVec = new Vec((float) Math.sin(MOVEMENT_PERIOD.getX() * calcX), (float) Math.cos(MOVEMENT_PERIOD.getY() * calcX));
		Vec scaledUnit = MOVEMENT_RANGE.multiply(scaleVec);
		this.setPos(startPos.add(scaledUnit));
	}

	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), new RGBColor(100, 100, 100));
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
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 },
						{ 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } },
				{ { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 },
						{ 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } } };

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
	public String getName() {
		return "Crazy Rocket Robot";
	}

	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new RocketBoss(startPos);
	}
}
