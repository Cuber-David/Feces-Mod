package K.content.unit;

import K.content.extend.Bullets.HealingConeBulletType;
import mindustry.ai.UnitCommand;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static K.KMod.name;

public class HelperdroneUnitType extends UnitType {
    public HelperdroneUnitType(String name) {
        super(name);

        constructor = UnitTypes.poly.constructor;
        defaultCommand = UnitCommand.rebuildCommand;

        health = 400;
        hitSize = 8;
        flying = true;
        drag = 0.06f;
        accel = 0.12f;
        speed = 3.5f;
        engineSize = 0f;
        range = 100f;
        isEnemy = false;
        controlSelectGlobal = false;
        wreckSoundVolume = deathSoundVolume = 0.7f;
        buildBeamOffset = 3f;
        buildRange = 160f;
        buildSpeed = 3f;
        mineSpeed = 4;
        mineRange = 40;
        mineTier = 3;
        mineFloor = true;

        setEnginesMirror(
                new UnitEngine(3f, -5.5f, 1.5f, -90f),
                new UnitEngine(7f, -3f, 1.5f, -30f)
        );

        abilities.add(new RepairFieldAbility(100,300,64));

        weapons.add(new Weapon(name("fixweapon")){{

            x = 0f;
            y = 0f;
            shootY = 3f;
            reload = 260f;
            shootSound = Sounds.loopBuild;

            continuous = rotate = true;

            mirror = false;
            rotateSpeed = 1.5f;

            bullet = new HealingConeBulletType(3f){{
                length = 120;
                healPercent = 6f;
                allyStatus = StatusEffects.overclock;
                allyStatusDuration = 9f * 60f;
                status = StatusEffects.sapped;
                statusDuration = 40f;
                lifetime = 6f * 60f;
            }};
        }});
    }
}
