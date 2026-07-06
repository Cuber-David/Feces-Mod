package K.content.unit;

import mindustry.ai.UnitCommand;
import mindustry.content.UnitTypes;
import mindustry.type.UnitType;

public class PlasmadrillUnitType extends UnitType {
    public PlasmadrillUnitType(String name) {
        super(name);

        constructor = UnitTypes.mono.constructor;
        defaultCommand = UnitCommand.mineCommand;

        flying = true;
        drag = 0.06f;
        accel = 0.12f;
        speed = 2.5f;
        health = 200;
        engineSize = 1.8f;
        engineOffset = 5.7f;
        range = 50f;
        isEnemy = false;
        controlSelectGlobal = false;
        wreckSoundVolume = deathSoundVolume = 0.7f;

        mineTier = 1;
        mineSpeed = 2.5f;
    }
}
