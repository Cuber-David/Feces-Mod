package K.content.unit.ground;

import arc.graphics.Color;
import arc.math.geom.Rect;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class TacticalsuppressiontankUnitType extends UnitType {
    public TacticalsuppressiontankUnitType(String name) {
        super(name);
        tankMoveSound = Sounds.tankMoveHeavy;
        constructor = UnitTypes.precept.constructor;
        speed = 0.4f;
        floorMultiplier = 0.65f;
        rotateSpeed = 1.5f;
        health = 3000;
        armor = 8f;
        hitSize = 24;
        treadPullOffset = 5;
        treadRects = new Rect[]{new Rect(16 - 60f, 48 - 70f, 30, 75), new Rect(44 - 60f, 17 - 70f, 17, 60)};
        crushFragile = true;
        crashDamageMultiplier = 5;

        weapons.add(new Weapon("kmod-weapon10"){{
            shootSound = Sounds.explosionDull;
            layerOffset = 0.0001f;
            reload = 80f;
            shootY = 16f;
            recoil = 3f;
            rotate = true;
            rotateSpeed = 1.625f;
            mirror = false;
            shootCone = 2f;
            x = 0f;
            y = -1f;
            heatColor = Color.valueOf("f9350f");
            cooldownTime = 30f;
            bullet = new BasicBulletType(7f, 120){{
                sprite = "missile-large";
                width = 7.5f;
                height = 13f;
                lifetime = 28f;
                hitSize = 6f;
                pierceCap = 2;
                pierce = true;
                pierceBuilding = true;
                hitColor = backColor = trailColor = Color.valueOf("feb380");
                frontColor = Color.white;
                trailWidth = 2.8f;
                trailLength = 8;
                hitEffect = despawnEffect = Fx.missileTrailSmoke;
                shootEffect = Fx.shootTitan;
                smokeEffect = Fx.shootSmokeTitan;
                splashDamageRadius = 32f;
                splashDamage = 280f;
                despawnSound = Sounds.drillImpact;

                trailEffect = Fx.hitSquaresColor;
                trailRotation = true;
                trailInterval = 3f;

                fragBullets = 4;

                fragBullet = new BasicBulletType(5f, 35){{
                    sprite = "missile-large";
                    width = 5f;
                    height = 7f;
                    lifetime = 15f;
                    hitSize = 4f;
                    pierceCap = 3;
                    pierce = true;
                    pierceBuilding = true;
                    hitColor = backColor = trailColor = Color.valueOf("feb380");
                    frontColor = Color.white;
                    trailWidth = 1.7f;
                    trailLength = 3;
                    drag = 0.01f;
                    despawnEffect = hitEffect = Fx.hitBulletColor;
                }};
            }};
        }});
        weapons.add(new Weapon("kmod-weapon11"){{
            x = -8;
            y = 4;
            shootY = 8;
            mirror = false;
            reload = 80;
            rotate = true;
            rotateSpeed = 0.05f;
            rotationLimit = 30;
            inaccuracy = 15;
            shoot = new ShootPattern() {{
                shots = 36;
                shotDelay = 1.5f;
                firstShotDelay = 80;
            }};
            bullet = new BasicBulletType(6,6){{
                width = 5f;
                height = 7f;
                lifetime = 24f;
                hitSize = 4f;
                despawnEffect = hitEffect = Fx.hitBulletColor;
                hitColor = backColor = trailColor = Color.valueOf("feb380");
                frontColor = Color.white;
            }};
        }});
    }
}
