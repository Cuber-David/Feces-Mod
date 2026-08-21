package K.content.unit.air;

import K.content.sounds;
import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class ConceptualhovertankUnitType extends UnitType {
    public ConceptualhovertankUnitType(String name) {
        super(name);
        constructor = UnitTypes.antumbra.constructor;
        flying = true;
        health = 2400;
        armor = 5;
        speed = 1;
        abilities.add(new ForceFieldAbility(45f, 1f, 800f, 60f * 16, 80, 0f){{
            breakSound = Sounds.shieldBreak;
        }});
        weapons.add(
                new Weapon("kmod-Laserweapon"){{
                    x = 0;
                    hitSize = 50;
                    shootSound = sounds.elaser;
                    cooldownTime = 20;
                    mirror = false;
                    rotate = true;
                    shootY = 16;
                    continuous = true;
                    rotateSpeed = 3.5f;
                    recoil = 0;
                    bullet = new ContinuousLaserBulletType(){{
                        layer = 160;
                        cooldownTime = 120;
                        rotate = true;
                        damage = 40f;
                        length = 180f;
                        hitEffect = Fx.freezing;
                        drawSize = 180f;
                        width = 1;
                        lifetime = 120f;
                        shake = 0.1f;
                        despawnEffect = Fx.none;
                        smokeEffect = Fx.none;
                        chargeEffect = Fx.none;
                        colors = new Color[]{Color.valueOf("51caca").cpy().a(.2f), Color.valueOf("51caca").cpy().a(.5f), Color.valueOf("51caca").cpy().mul(1.2f), Color.white};
                    }};
                }}
        );
    }
}
