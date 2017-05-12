package rekit.logic.gameelements.entities.enemies.piston;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.piston.state.PistonState;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.operable.OpProgress;

public class PistonInner extends Enemy {

	/**
	 * The {@link OpProgress} to switch between {@link Piston#PISTON_COLOR_1}
	 * and {@link Piston#PISTON_COLOR_2}.
	 */
	private OpProgress<RGBAColor> colorProgress;

	private Piston parent;

	PistonInner(Piston parent) {
		super(new Vec(), new Vec(), new Vec());
		this.parent = parent;
		this.colorProgress = new OpProgress<RGBAColor>(Piston.PISTON_COLOR_1, Piston.PISTON_COLOR_2);

		// this sets initial position and prevents deletion
		this.innerLogicLoop();
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			if (((PistonState) this.parent.machine.getState()).getCurrentHeight() > Piston.NO_DAMAGE_TOLERANCE_HEIGHT) {
				// if piston is currently open, do damage
				element.addDamage(1);
			}
		}
	}

	@Override
	public void innerLogicLoop() {
		PistonState currentState = (PistonState) this.parent.machine.getState();

		// calculate middle pos and size
		// Note: these position Vecs are relative to the middle of the
		// Pistons Base!
		// Also: in direction UP
		Vec btmPos = new Vec(0, -Piston.BASE_HEIGHT / 2f + 0.01);

		Vec topPos = btmPos
				// Move current length up
				.addY(-currentState.getCurrentHeight() * this.parent.expansionLength)
				// Remove margin
				.addY(Piston.LOWER_MARGIN);

		topPos = topPos.setY((topPos.y > btmPos.y) ? btmPos.y : topPos.y);

		Vec middlePos = btmPos.add(topPos).scalar(0.5f);
		Vec size = new Vec(Piston.PISTON_INNER_WIDTHS[0], btmPos.y - topPos.y);

		// setting values for rendering and collision frame
		this.setPos(this.parent.getPos().add(this.parent.rotatePosToDir(middlePos)));
		this.setSize(this.parent.rotateSizeToDir(size));
	}

	@Override
	public void internalRender(GameGrid f) {

		int num = Piston.PISTON_INNER_WIDTHS.length;
		for (int i = 0; i < num; ++i) {
			RGBAColor col = this.colorProgress.getNow(i / (float) num);

			// Draw Rectangle for beam
			Vec size = this.getSize();
			Vec pos = this.getPos();
			float spikeDelta = 0.4f * (1 - (i / (float) num));
			if (this.parent.direction == Direction.LEFT || this.parent.direction == Direction.RIGHT) {
				size = size.setY(Piston.PISTON_INNER_WIDTHS[i] / 1.5f);
				size = size.addX(-spikeDelta);
				pos = pos.addX(((this.parent.direction == Direction.LEFT) ? 1 : -1) * (spikeDelta / 2f));

			} else {
				size = size.setX(Piston.PISTON_INNER_WIDTHS[i] / 1.5f);
				size = size.addY(-spikeDelta);
				pos = pos.addY(((this.parent.direction == Direction.UP) ? 1 : -1) * (spikeDelta / 2f));
			}
			f.drawRectangle(pos, size, col);
		}

	}

	@Override
	public PistonInner create(Vec startPos, String... options) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer getZHint() {
		return (int) this.team.zRange.normalize(this.team.zRange.min);
	}

}