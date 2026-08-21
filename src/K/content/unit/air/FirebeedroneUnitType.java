package K.content.unit.air;

import K.content.extend.AdaptedShootHelix;
import arc.graphics.Color;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.entities.bullet.SapBulletType;
import mindustry.entities.part.HaloPart;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class FirebeedroneUnitType extends UnitType {
    public FirebeedroneUnitType(String name) {
        super(name);
        constructor = UnitTypes.zenith.constructor;
        health = 1000;
        flying = true;
        hitSize = 20;

        parts.add(new HaloPart(){{
            color = Color.valueOf("6e7080");
            haloRotateSpeed = 40;
            haloRotation = 0;
            shapes = 4;
            haloRadius = 1.2f;
            radius = 2;
            triLength = 1;
            x = 6.5f;
            y = 1;
        }});
        parts.add(new HaloPart(){{
            color = Color.valueOf("6e7080");
            haloRotateSpeed = 40;
            haloRotation = 0;
            shapes = 4;
            haloRadius = 1.2f;
            radius = 2;
            triLength = 1;
            x = -6.5f;
            y = 1;
        }});

        weapons.add(new Weapon("kmod-weapon6"){{
            reload = 40f;
            x = 3f;
            y = 7;
            rotate = true;
            shootSound = Sounds.shootMalign;
            rotationLimit = 90;
            bullet = new SapBulletType(){{
                sapStrength = 0.2f;
                length = 120f;
                damage = 36;
                shootEffect = Fx.shootSmall;
                hitColor = color = Color.red;
                despawnEffect = Fx.none;
                width = 0.54f;
                lifetime = 15f;
            }};
        }}
        );
        weapons.add(new Weapon(""){{
            reload = 360f;
            x = 5f;
            y = 7;
            rotate = true;
            shake = 1f;
            shoot = new AdaptedShootHelix() {{
                flip = true;
                shots = 10;
                mag = 1.65f;
                scl = 6f;
                shotDelay = 6f;
                offset = 9.75f * Mathf.PI2;
                rotSpeedOffset = 0.015f;
                rotSpeedBegin = 0.925f;
                targetGround = true;
            }};
            inaccuracy = 5f;
            velocityRnd = 0.2f;
            shootSound = Sounds.shootMissileLong;

            bullet = new MissileBulletType(3f, 14){{
                width = 8f;
                height = 8f;
                shrinkY = 0f;
                drag = -0.003f;
                homingRange = 60f;
                scaleKeepVelocity = true;
                splashDamageRadius = 25f;
                splashDamage = 15f;
                lifetime = 50f;
                trailColor = Color.valueOf("989aa4");
                backColor = Pal.unitBack;
                frontColor = Pal.unitFront;
                hitEffect = Fx.blastExplosion;
                despawnEffect = Fx.blastExplosion;
                weaveScale = 6f;
                weaveMag = 1f;
            }};
        }});
    }

}
