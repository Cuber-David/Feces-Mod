package K.content.unit.God;

import K.content.extend.weapons.KWeapon;
import mindustry.ai.UnitCommand;
import mindustry.type.UnitType;

public class GodKUnitType extends UnitType {
    public GodKUnitType(String name) {
        super(name);
        hidden = true;
        health = armor = 1E30f;
        hitSize = 90;
        constructor = RodyUnit::new;
        aiController = RodyAI::new;
        defaultCommand = UnitCommand.mineCommand;
        mineFloor = true;
        mineSpeed = 99;
        drawMineBeam = false;
        mineRange = 8;
        mineTier = 99;
        buildRange = 800;
        buildSpeed = 99;
        drawBuildBeam = false;
        flying = true;
        drawCell = false;
        range = 8;
        speed = 10;
        rotateSpeed = 9;
        autoFindTarget = false;
        circleTargetRadius = 0;
        targetBuildingsMobile = false;
        circleTarget = false;
        targetAir = false;
        targetGround = false;
        weapons.add(new KWeapon());
    }
}
