package K.content.unit;

import K.content.effect;
import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.SapBulletType;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class FecaldroneUnitType extends UnitType {

    public FecaldroneUnitType(String name) {
        super(name);

        constructor = UnitTypes.alpha.constructor;
        speed = 10f;
        hitSize = 8f;
        health = 200;
        flying = true;
        mineFloor = true;
        mineSpeed = 8.5f;
        mineTier = 1;
        buildSpeed = 0.75f;
        rotateSpeed = 15f;
        accel = 0.1f;
        itemCapacity = 40;
        engineOffset = 6f;
        alwaysUnlocked = false;
        wreckSoundVolume = 0.8f;
        deathSoundVolume = 0.7f;

        weapons.add(new Weapon("kmod-weapon2"){{
            shootY = 4f;
            reload = 7f;
            ejectEffect = Fx.none;
            recoil = -10f;
            rotate = true;
            shootSound = Sounds.shootSap;

            x = 3.5f;
            y = -1.5f;

            bullet = new SapBulletType(){{
                sapStrength = 0.2f;
                length = 180f;
                damage = 13;
                shootEffect = Fx.shootSmall;
                hitColor = color = Color.valueOf("673931");
                despawnEffect = Fx.none;
                status = effect.sick;
                width = 0.54f;
                lifetime = 15f;
                knockback = -4.24f;
            }};
        }});
    }
}
