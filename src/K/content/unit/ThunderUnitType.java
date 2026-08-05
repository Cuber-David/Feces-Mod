package K.content.unit;

import K.content.fx;
import K.content.sounds;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.TimedKillUnit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;


public class ThunderUnitType extends UnitType {
    public ThunderUnitType(String name) {
        super(name);
        constructor = TimedKillUnit::create;
        lifetime = 6f;
        range = 10000;
        useUnitCap  = false;
        drawCell = false;
        weapons.add(new Weapon(){{
            shootOnDeath = true;
            targetUnderBlocks = false;
            reload = 24f;
            shootCone = 360f;
            ejectEffect = fx.Thunder;
            shootSound = sounds.thunder;
            shootSoundVolume = 6f;
            x = shootY = 0f;
            mirror = false;
            bullet = new BulletType(){{
                collidesTiles = false;
                collides = false;
                despawnEffect = fx.Thunder;
                despawnSound = sounds.thunder;

                rangeOverride = 25f;
                hitEffect = Fx.none;
                speed = 0f;
                splashDamageRadius = 44f;
                instantDisappear = true;
                splashDamage = 80f;
                buildingDamageMultiplier = 0.68f;
                killShooter = true;
                hittable = false;
                collidesAir = true;
            }};
        }});
    }
}
