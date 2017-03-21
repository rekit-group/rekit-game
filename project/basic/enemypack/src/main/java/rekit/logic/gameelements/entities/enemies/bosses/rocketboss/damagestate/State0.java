package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate;

public class State0 extends DamageState {

	@Override
	public DamageState getNextState() {
		return new State0();
	}

	@Override
	public float getTimeFactor() {
		return 0.2E-3F;
	}
	
	@Override
	public String getHeadImgSrc() {
		return "rocketBoss/head_0.png";
	}
	
	@Override
	public float getMouthAmplitude() {
		return 0.1f;
	}
}
