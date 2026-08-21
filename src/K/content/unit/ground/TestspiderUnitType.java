package K.content.unit.ground;

import K.content.entities.SmallDeathblastAbility;
import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.weapons.PointDefenseWeapon;

public class TestspiderUnitType extends UnitType {
    public TestspiderUnitType(String name) {
        super(name);

        constructor = UnitTypes.arkyid.constructor;

        drag = 0.3f;
        speed = 0.32f;
        hitSize = 40f;
        health = 4200;
        armor = 6f;
        canDrown = false;

        rotateSpeed = 1.7f;

        legCount = 6;
        legMoveSpace = 1f;
        legPairOffset = 3;
        legLength = 30f;
        legExtension = -15;
        legBaseOffset = 10f;
        stepShake = 1f;
        legLengthScl = 0.96f;
        rippleScale = 2f;
        legSpeed = 0.2f;

        stepSound = Sounds.walkerStep;
        stepSoundVolume = 0.85f;
        stepSoundPitch = 1.1f;

        legSplashDamage = 32;
        legSplashRange = 30;

        hovering = true;
        shadowElevation = 0.65f;
        groundLayer = Layer.legUnit;
        deathExplosionEffect = Fx.impactReactorExplosion;

        abilities.add(new SmallDeathblastAbility(),
                      new RepairFieldAbility(100,300,64),
                      new ShieldRegenFieldAbility(300f, 500f, 600f, 60f));

        weapons.add(
        new Weapon("kmod-weapon3"){{
            y = 10f;
            x = 0f;
            mirror = false;
            shootY = 7f;
            reload = 45;
            shake = 3f;
            rotateSpeed = 2f;
            ejectEffect = Fx.casing1;
            shootSound = Sounds.shootArtillerySap;
            rotate = true;
            shadow = 8f;
            recoil = 3f;
            buildRange = 160;
            buildSpeed = 2;

            bullet = new ArtilleryBulletType(2f, 120){{
                hitEffect = Fx.blastExplosion;
                despawnSound = Sounds.explosionArtilleryShock;
                knockback = 3f;
                lifetime = 200f;
                width = height = 19f;
                collidesTiles = true;
                ammoMultiplier = 4f;
                splashDamageRadius = 60f;
                splashDamage = 160f;
                backColor = Color.valueOf("673931");
                frontColor = lightningColor = Color.valueOf("deb6af");
                lightning = 3;
                lightningLength = 10;
                lightningDamage = 30;
                smokeEffect = Fx.shootBigSmoke2;
                shake = 5f;

                status = StatusEffects.blasted;
                statusDuration = 60f * 10;
            }};
        }});

        weapons.add(new PointDefenseWeapon("kmod-fecesdefense-mount"){{
                x = -8f;
                y = -20;
                reload = 8f;
                targetInterval = 8f;
                targetSwitchInterval = 8f;
                mirror = false;

                bullet = new BulletType(){{
                    shootEffect = Fx.sparkShoot;
                    hitEffect = Fx.pointHit;
                    maxRange = 120f;
                    damage = 30f;
                }};
        }});
        weapons.add(
                new Weapon("kmod-weapon4"){{
                    shootSound = Sounds.shootLaser;
                    shootY = 14;
                    reload = 24f;
                    ejectEffect = Fx.none;
                    x = 10f;
                    y = 16f;
                    rotate = true;
                    bullet = new LaserBoltBulletType(5.2f, 30){{
                        shootEffect = Fx.none;
                        despawnEffect = Fx.none;
                        hitEffect = Fx.freezing;
                        lifetime = 55f;
                        healPercent = 9.5f;
                        collidesTeam = true;
                        backColor = Color.valueOf("51caca");
                        frontColor = Color.white;
                        layer = 60;
                    }};
                }}
        );

    }
}
