package K.content.unit.God;

import arc.util.Nullable;
import mindustry.entities.units.AIController;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;

public class RodyAI extends AIController {
    protected @Nullable AIController fallback;

    @Override
    public void unit(Unit unit) {
         isLogicControllable();
    }

    @Override
    public Unit unit() {
        return null;
    }

    @Override
    public void updateUnit(){
        updateMovement();
    }

    @Override
    public void updateMovement(){
        unloadPayloads();

        if(target != null && unit.hasWeapons()){
            if(unit.type.circleTarget){
                circleAttack(unit.type.circleTargetRadius);
            }else{
                moveTo(target, unit.type.range * 0.8f);
                unit.lookAt(target);
            }
        }
    }

    @Override
    public boolean shouldShoot() {
        return false;
    }
}
