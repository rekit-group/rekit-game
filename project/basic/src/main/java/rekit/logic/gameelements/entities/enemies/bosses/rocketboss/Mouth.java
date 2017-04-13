package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rekit.core.GameGrid;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Timer;
import rekit.util.Math;

public class Mouth {

	private RocketBoss parent;

	private Vec pos;
	private Vec size;

	private Timer mouthCurveTimer = new Timer(50);
	private Vec mouthCurvePos;
	private List<Vec> mouthCurve = new LinkedList<>();

	public Mouth(RocketBoss parent, Vec pos, Vec size) {
		this.parent = parent;
		this.pos = pos;
		this.size = size;
	}

	public void logicLoop(float calcX, float deltaX) {
		this.mouthCurvePos = this.parent.getPos().add(this.pos).addX(0.5f * this.size.x).addX(-calcX);
		this.mouthCurveTimer.logicLoop();
		float maxDelta = this.size.y * 0.5f * this.parent.getState().getMouthAmplitude();
		while (this.mouthCurveTimer.timeUp()) {
			this.mouthCurveTimer.reset();
			Vec newVec = new Vec(calcX, (float) (Math.tan(calcX * 10) * Math.sin(calcX * 4) * Math.cos(calcX * 0.5f) * maxDelta));
			if (newVec.y > maxDelta) {
				newVec = newVec.setY(maxDelta);
			}
			if (newVec.y < -maxDelta) {
				newVec = newVec.setY(-maxDelta);
			}
			this.mouthCurve.add(newVec);
		}
		Iterator<Vec> it = this.mouthCurve.iterator();
		while (it.hasNext()) {
			if (it.next().x <= calcX - this.size.x) {
				it.remove();
			} else {
				break;
			}
		}
	}

	public void internalRender(GameGrid f) {
		f.drawPath(this.mouthCurvePos, this.mouthCurve, new RGBAColor(0, 0, 0), true);
	}
}
