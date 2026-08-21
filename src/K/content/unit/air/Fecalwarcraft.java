package K.content.unit.air;

import K.content.entities.rebuildai;
import K.content.extend.Bullets.ImpactwaveBulletType;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.BuilderAI;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class Fecalwarcraft extends UnitType {
    public Fecalwarcraft(String name) {
        super(name);
        constructor = UnitTypes.beta.constructor;
        aiController = rebuildai::new;
        defaultCommand = UnitCommand.rebuildCommand;
        health = 250;
        armor = 5;
        itemCapacity = 60;
        hitSize = 2;
        speed = 10;
        range = 320;
        engineSize = 0;
        flying = true;
        buildRange = 160;
        buildSpeed = 2;
        mineFloor = true;
        mineRange = 160;
        mineTier = 2;
        mineSpeed = 12;
        abilities.add(new RepairFieldAbility(30,200,80) {});
        setEnginesMirror(
                new UnitEngine(4f, -4f, 1.5f, -15f)
        );
        weapons.add(new Weapon("kmod-weapon7"){{
            x = 0;
            y = 5;
            bullet = new ImpactwaveBulletType();
            reload = 60;
            rotate = false;
            shootCone = 360;
            rotateSpeed = 0;
            baseRotation = 0;
            mirror = false;
        }},
        new Weapon("kmod-weapon7"){{
            x = -4;
            y = 4;
            bullet = new ImpactwaveBulletType();
            reload = 30;
            rotate = false;
            shootCone = 360;
            rotateSpeed = 0;
            baseRotation = 30;
            mirror = true;
            alternate = false;
        }}
        );
    }
}
