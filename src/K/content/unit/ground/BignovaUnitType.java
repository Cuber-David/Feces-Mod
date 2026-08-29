package K.content.unit.ground;

import K.content.Fx.KFx;
import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;


public class BignovaUnitType extends UnitType{

    public BignovaUnitType(String name){
        super(name);

        outlineColor = Color.gray;
        outlineRadius = 10;
        constructor = UnitTypes.nova.constructor;
        canBoost = true;
        boostMultiplier = 1.5f;
        researchCostMultiplier = 0.5f;
        armor = 25;
        speed = 0.6f;
        hitSize = 80f;
        health = 4500;
        stepSoundVolume = 4f;
        alwaysUnlocked = false;
        buildSpeed = 30;
        isHidden();

        abilities.add(new RepairFieldAbility(180f, 60f * 4, 700f));
        engineOffset = 50f;
        engineSize = 15f;

        weapons.add(new Weapon("kmod-Bigheal-weapon"){{
            top = false;
            shootY = 20f;
            reload = 56f;
            x = 45f;
            alternate = false;
            ejectEffect = Fx.none;
            recoil = 20f;
            shootSound = Sounds.shootCorvus;
            shootSoundVolume = 10;

            bullet = new LaserBoltBulletType(5.2f, 630){{
                width = 20;
                height = 40;
                lifetime = 80f;
                healPercent = 5f;
                collidesTeam = true;
                splashDamage = 600;
                splashDamageRadius = 15f;
                hitEffect = KFx.hitLaserBigger;
                backColor = Pal.heal;
                frontColor = Color.white;
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
