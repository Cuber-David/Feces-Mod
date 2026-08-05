package K.content.extend.blocks;

import mindustry.gen.Building;
import mindustry.world.blocks.defense.turrets.PowerTurret;

public class PowerTurret1 extends PowerTurret {
    public PowerTurret1(String name) {
        super(name);
        obstructsLight = false;
    }
    @Override
    public void init(){
        //assign to update clipSize internally
        lightRadius = 30f + 20f * size;
        fogRadius = Math.max(fogRadius, (int)(lightRadius / 8f * 3f) + 13);
        emitLight = true;

        super.init();
    }
}
