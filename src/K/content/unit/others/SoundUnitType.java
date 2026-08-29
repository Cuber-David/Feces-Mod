package K.content.unit.others;

import K.content.extend.Bullets.*;
import K.content.extend.Bullets.Soundb;
import K.content.Fx.KFx;
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
        hidden = true;
        //#0
        weapons.add(
                new Weapon("1"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new Soundb(){{
                        despawnSound = sounds.alarm;
                    }};
                }}
        );
        //#1
        weapons.add(
                new Weapon("2"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new Soundb();
                }}
        );
        //#2
        weapons.add(
                new Weapon("3"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new MyLaserBulletType();
                }}
        );
        //#3
        weapons.add(
                new Weapon("4"){{
                    mirror = false;
                    reload = 60f;
                    bullet = new Soundb(){{
                        despawnSound = sounds.dogshit;
                    }};
                }}
        );
        //#4
        weapons.add(
                new Weapon("5"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new Soundb(){{
                        despawnSound = sounds.watching;
                    }};
                }}
        );
        //#5
        weapons.add(
                new Weapon("6"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new Soundb(){{
                        despawnEffect = KFx.Hugebeam;
                        despawnSound = sounds.beamlarge;
                    }};
                }}
        );
        //#6
        weapons.add(
                new Weapon("7"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new BlackHoleBullet(){{
                    }};
                }}
        );
        //#7
        weapons.add(
                new Weapon("8"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new DomainInf() {{
                    }};
                }}
        );
        //#8
        weapons.add(
                new Weapon("9"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new DomainCook() {{
                    }};
                }}
        );
        //#9
        weapons.add(
                new Weapon("10"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new ExplosionBullType();
                }}
        );
        //#10
        weapons.add(
                new Weapon("11"){{
                    mirror = false;
                    reload = 60f;
                    shootSound = Sounds.none;
                    bullet = new SlashBulletType(){{
                        hitSize = 100;
                    }};
                }}
        );
    }
}
