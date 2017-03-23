package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate;

public class State3 extends DamageState {

	@Override
	public DamageState getNextState() {
		return new State2();
	}

	@Override
	public float getTimeFactor() {
		return 1;
	}

	@Override
	public String getHeadImgSrc() {
		return "rocketBoss/head_3.png";
	}

	@Override
	public float getMouthAmplitude() {
		return 1;
	}
}
