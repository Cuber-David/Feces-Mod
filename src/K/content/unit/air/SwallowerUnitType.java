package K.content.unit.air;

import mindustry.ai.types.HugAI;
import mindustry.type.UnitType;
import mindustry.type.unit.NeoplasmUnitType;

public class SwallowerUnitType extends UnitType {
    public SwallowerUnitType(String name) {
        super(name);
        omniMovement = false;
        rotateSpeed = 1.7f;
        flying = true;
        segments = 4;
        drawBody = true;
        drawCell = false;
        crushDamage = 2f;
        aiController = HugAI::new;
        segmentScl = 4f;
        segmentPhase = 50f;
        speed = 10f;
    }
}
