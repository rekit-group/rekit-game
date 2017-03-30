package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate;

public class State1 extends DamageState {

	@Override
	public DamageState getNextState() {
		return new State0();
	}

	@Override
	public float getTimeFactor() {
		return 3;
	}
	
	@Override
	public String getHeadImgSrc() {
		return "rocketBoss/head_1.png";
	}
	
	@Override
	public float getMouthAmplitude() {
		return 1.2f;
	}
}
