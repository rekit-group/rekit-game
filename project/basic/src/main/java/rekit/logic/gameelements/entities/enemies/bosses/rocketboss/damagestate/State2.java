package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate;

public class State2 extends DamageState {

	@Override
	public DamageState getNextState() {
		return new State1();
	}

	@Override
	public float getTimeFactor() {
		return 2;
	}
	
	@Override
	public String getHeadImgSrc() {
		return "rocketBoss/head_2.png";
	}
	
	@Override
	public float getMouthAmplitude() {
		return 1.1f;
	}
}
