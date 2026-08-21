package K.content.unit.Rody;

import arc.util.Nullable;
import mindustry.ai.types.GroundAI;
import mindustry.entities.units.AIController;
import mindustry.entities.units.UnitController;
import mindustry.gen.Unit;

import static mindustry.Vars.state;

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
}
