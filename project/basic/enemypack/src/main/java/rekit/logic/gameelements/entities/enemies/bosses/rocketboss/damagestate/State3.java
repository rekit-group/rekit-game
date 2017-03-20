package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.damagestate;

public class State3 extends DamageState {

	@Override
	public DamageState getNextState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getTimeFactor() {
		// TODO Auto-generated method stub
		return 1E-3F;
	}

	@Override
	public float getArmNum() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getBrainImgSrc() {
		return "rocketBoss/brain_3.png";
	}

	@Override
	public String getEyeImgSrc() {
		return "rocketBoss/eye_3.png";
	}
	
	@Override
	public String getHeadImgSrc() {
		return "rocketBoss/head_3.png";
	}

	@Override
	public float getMouthMovement() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMouthPhaseFactor() {
		// TODO Auto-generated method stub
		return 0;
	}
}
