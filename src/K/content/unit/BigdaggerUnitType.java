package K.content.unit;

import K.content.fx;
import K.content.planets;
import arc.graphics.Color;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.type.Weapon;


public class BigdaggerUnitType extends UnitType{

    public BigdaggerUnitType(String name){
        super(name);

        outlineColor = Color.gray;
        outlineRadius = 10;
        constructor = UnitTypes.dagger.constructor;

        researchCostMultiplier = 0.5f;
        armor = 20;
        speed = 0.6f;
        hitSize = 80f;
        health = 6000;
        stepSoundVolume = 4f;
        alwaysUnlocked = false;

        weapons.add(new Weapon(("kmod-weapon1")) {{
            reload = 33f;
            x = 35f;
            y = 60f;
            top = false;
            ejectEffect = fx.Bigcasing;
            recoil = 10f;
            shootSound = Sounds.explosionTitan;
            shootSoundVolume = 5f;
            bullet = new BasicBulletType(4.5f, 560){{
                width = 70f;
                height = 90f;
                lifetime = 120f;
                buildingDamageMultiplier = 0.5f;
                splashDamageRadius = 50;
                splashDamage = 360;
                shootEffect = fx.shootBig;
                hitEffect = fx.hitBulletBigger;
                despawnEffect = fx.hitBulletBigger;
            }};
        }});

        mechLandShake = 120f;
        stepShake = 50f;
        mechStepParticles = true;
        canDrown = true;
        mechFrontSway = 2.2f;
        mechSideSway = 0.8f;
        legGroupSize = 10;
    }
}
