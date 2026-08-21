package K.content.unit.others;

import K.content.extend.Bullets.BlackHoleBullet;
import K.content.extend.Bullets.DomainBulletType;
import K.content.extend.Bullets.MyLaserBulletType;
import K.content.extend.Bullets.SlashBulletType;
import K.content.extend.Bullets.sound.alarm;
import K.content.extend.Bullets.sound.charge;
import K.content.Fx.fx;
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
        //#0
        weapons.add(
                new Weapon("1"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new alarm();
                }}
        );
        //#1
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new charge();
                }}
        );
        //#2
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new MyLaserBulletType();
                }}
        );
        //#3
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    bullet = new charge(){{
                        despawnSound = sounds.dogshit;
                    }};
                }}
        );
        //#4
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new charge(){{
                        despawnSound = sounds.watching;
                    }};
                }}
        );
        //#5
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new charge(){{
                        despawnEffect = fx.Hugebeam;
                        despawnSound = sounds.beamlarge;
                    }};
                }}
        );
        //#6
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new BlackHoleBullet(){{
                    }};
                }}
        );
        //#7
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new DomainBulletType() {{
                    }};
                }}
        );
        //#8
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new SlashBulletType() {{
                    }};
                }}
        );
    }
}
