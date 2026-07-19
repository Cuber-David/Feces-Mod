package K.content.unit;

import K.content.extend.Bullets.NoBullet;
import K.content.extend.Bullets.sound.alarm;
import K.content.extend.Bullets.sound.charge;
import K.content.sounds;
import mindustry.content.Fx;
import mindustry.gen.Sounds;
import mindustry.gen.TimedKillUnit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class SoundUnitType extends UnitType {

    public SoundUnitType(String name) {
        super(name);
        constructor = TimedKillUnit::create;
        drawCell = false;
        lifetime = 1;
        deathSound = Sounds.none;
        deathExplosionEffect = Fx.none;
        deathShake = 100;
        weapons.add(
                new Weapon("1"){{
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new alarm();
                }}
        );
        weapons.add(
                new Weapon("2"){{
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new charge();
                }}
        );
    }
}
