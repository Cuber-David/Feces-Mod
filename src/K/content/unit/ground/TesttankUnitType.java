package K.content.unit.ground;

import arc.graphics.Color;
import arc.math.geom.Rect;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;

public class TesttankUnitType extends TankUnitType {
    public TesttankUnitType(String name) {
        super(name);
        constructor = UnitTypes.vanquish.constructor;
        speed = 0.8F;
        armor = 10;
        treadPullOffset = 0;
        health = 2800;
        hitSize = 30;
        crushDamage = 13f / 5f;
        floorMultiplier = 0.5f;
        crushFragile = true;

        float xo = 128, yo = 128;
        treadRects = new Rect[]{new Rect(25 - xo, 3 - yo, 62, 106),
                new Rect(25 - xo, 147 - yo, 62, 106)};

        tankMoveVolume *= 1.25f;
        tankMoveSound = Sounds.tankMoveHeavy;

        int i = 0;
        for(float f : new float[]{75f / 4f, -36f / 4f}){
            int fi = i ++;
            weapons.add(new Weapon("kmod-weapon5"){{
                reload = 22 + fi * 5;
                x = 48f / 4f;
                y = f;
                shootY = 5.5f;
                recoil = 2f;
                rotate = true;
                rotateSpeed = 2f;
                shootSound = Sounds.shootStell;

                bullet = new BasicBulletType(12f, 100f){{
                    sprite = "missile-large";
                    width = 6.5f;
                    height = 11f;
                    shrinkY = 0f;
                    shrinkX = 0.2f;
                    lifetime = 15f;
                    shootEffect = Fx.sparkShoot;
                    smokeEffect = Fx.shootBigSmoke;
                    hitColor = backColor = trailColor = Color.valueOf("feb380");
                    frontColor = Color.white;
                    trailWidth = 2.5f;
                    trailLength = 5;
                    hitEffect = Fx.blastExplosion;
                    despawnEffect = Fx.hitBulletColor;
                }};
            }});
        }
    }
}
