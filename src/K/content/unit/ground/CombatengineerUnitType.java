package K.content.unit.ground;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class CombatengineerUnitType extends UnitType {
    public CombatengineerUnitType(String name) {
        super(name);
        constructor = UnitTypes.poly.constructor;
        flying = false;
        canDrown = false;
        health = 250;
        buildSpeed = 8;
        buildRange = 170;
        speed = 1.2f;
        weapons.add(
                new Weapon(""){{
                    shootSound = Sounds.shootLaser;
                    x = 0;
                    mirror = false;
                    shootY = 0;
                    reload = 48f;
                    ejectEffect = Fx.none;
                    rotate = false;
                    bullet = new LaserBoltBulletType(5.2f, 50){{
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
