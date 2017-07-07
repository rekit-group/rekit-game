package rekit.logic.gameelements.inanimate;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.type.DynamicInanimate;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils;

import java.awt.*;

/**
 * Created by Norbert on 07.07.2017.
 */
@ReflectUtils.LoadMe
public class TimeTravelBox extends DynamicInanimate {

    private int passedTime = 0;

    private boolean state_activated = false;

    private GameElement toTeleport;

    private int count = 5;

    private int time;

    private int bufferTime = 0;

    public TimeTravelBox() {
        super();
    }

    protected TimeTravelBox(Vec pos, Vec size, RGBAColor color, int time) {
        super(pos, size, color);
        this.count = time;
        this.time = time;
    }

    @Override
    public void reactToCollision(GameElement element, Direction dir) {
        if(!state_activated) {
            state_activated = true;
            toTeleport = element;
            getScene().setOffsetWildCard(true);
        }
        super.reactToCollision(element, dir);

    }

    @Override
    public DynamicInanimate create(Vec startPos, String... options) {
        return new TimeTravelBox(startPos, new Vec(1), new RGBAColor(Color.RED.getRGB()), Integer.valueOf(options[0]));
    }

    @Override
    public void logicLoop() {
        super.logicLoop();
        if(state_activated) {
            passedTime += this.deltaTime;
            bufferTime += this.deltaTime;
            if(bufferTime >= 1000) {
                count --;
                bufferTime = 0;
            }
            if(passedTime >= 1000 * time) {
                passedTime = 0;
                state_activated = false;
               // toTeleport.setPos(oldPos);
                //Animate to the old position
                new Thread(() -> {
                    if(toTeleport == null) return;
                    Vec dirVec = (getPos().add(new Vec(0).addX(1.5f))).sub(toTeleport.getPos());
                    Vec step = dirVec.scalar(0.01f);
                    Vec tmpPos = toTeleport.getPos();
                    for(int i = 0; i < 100; i++) {
                        tmpPos = tmpPos.add(step);
                        toTeleport.setPos(tmpPos);
                        try {
                            Thread.sleep(1l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    toTeleport = null;
                    getScene().setOffsetWildCard(false);
                    count = time;
                    }
                ).start();
            //    toTeleport = null;
            }
        }

    }

    @Override
    public void internalRender(GameGrid f) {
        super.internalRender(f);
        if(state_activated) {
            f.drawText(getPos().addY(-1f), String.valueOf(count),
                    new TextOptions(new Vec(0, -0.5), 40, new RGBAColor(Color.BLUE.getRGB()),
                            GameConf.GAME_TEXT_FONT, Font.BOLD), true);
        }

    }

}
