package K.content.unit;

import mindustry.entities.units.UnitController;
import mindustry.gen.Unit;

public class RodyAI implements UnitController {
    @Override
    public void unit(Unit unit) {

    }

    @Override
    public Unit unit() {
        return null;
    }
    @Override
    public void updateUnit() {


        UnitController.super.updateUnit();
    }
}
