package K.content.unit.ground;

import K.content.entities.TurretShield;
import K.content.extend.AdaptedShootHelix;
import K.content.items;
import K.content.sounds;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.Effect;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.RailBulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class TacticalassaultmechaUnitType extends UnitType {
    public TacticalassaultmechaUnitType(String name) {
        super(name);
        hitSize = 15;
        health = 1000;
        armor = 5;
        legCount = 4;
        legSpeed = 2f;
        legBaseOffset = 8;
        legExtension = 4;
        speed =2;
        constructor = UnitTypes.anthicus.constructor;

        abilities.add(new TurretShield(){{
            radius = hitSize + 16f;
            angle = 130;
            regen = 1f;
            cooldown = 60f * 10f;
            max = 640f;
            width = 24f;
            drawWidth = 12f;
            whenShooting = true;
            chanceDeflect = -1f;
        }});

        weapons.add(new Weapon("kmod-weapon8"){{
            shoot = new ShootPattern() {{
                shots = 24;
                shotDelay = 2f;
            }};
            mirror = false;
            x = 9;
            shootSound = sounds.laser;
            reload = 120;
            bullet = new RailBulletType() {
                {
                    length = 135f;
                    damage = 10f;

                    hitColor = items.Feces.color.cpy().lerp(Color.white, 0.4f);
                    hitEffect = endEffect = Fx.hitBulletColor;
                    pierceDamageFactor = 0.4f;

                    statusDuration = 180f;

                    smokeEffect = Fx.colorSpark;

                    endEffect = new Effect(16f, e -> {
                        color(e.color);
                        Drawf.tri(e.x, e.y, e.fout() * 1.5f, 6f, e.rotation);
                    });

                    shootEffect = new Effect(16f, e -> {
                        color(e.color);
                        float w = 1.2f + 4 * e.fout();

                        Drawf.tri(e.x, e.y, w, 30f * e.fout(), e.rotation);
                        color(e.color);

                        for (int i : Mathf.signs) {
                            Drawf.tri(e.x, e.y, w * 0.9f, 22f * e.fout(), e.rotation + i * 60f);
                        }

                        Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
                    });

                    lineEffect = new Effect(25f, e -> {
                        if (!(e.data instanceof Vec2)) return;

                        Vec2 v = (Vec2) e.data;

                        color(e.color);
                        stroke(e.fout() + 0.5f);

                        Fx.rand.setSeed(e.id);
                        for (int i = 0; i < 7; i++) {
                            Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                            Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 20f * Fx.rand.random(0.5f, 1f) + 0.3f);
                        }

                        e.scaled(16f, b -> {
                            stroke(b.fout() * 1.5f);
                            color(e.color);
                            Lines.line(e.x, e.y, v.x, v.y);
                        });
                    });
                }
                public void hitTile(Bullet b, Building build, float x, float y, float initialHealth, boolean direct) {
                    super.hitTile(b, build, x, y, initialHealth, direct);

                    build.applySlowdown(0.25f, 180f);
                }
            };
        }},
                new Weapon("kmod-weapon9"){{
                    top = false;
                    shootSound = Sounds.shootFlame;
                    x = -8;
                    mirror = false;
                    shootY = 2f;
                    shoot = new ShootPattern() {{
                        shots = 5;
                        shotDelay = 6f;
                    }};
                    reload = 55f;
                    recoil = 1f;
                    ejectEffect = Fx.none;
                    bullet = new BulletType(4.2f, 30f){{
                        ammoMultiplier = 3f;
                        hitSize = 14f;
                        lifetime = 21f;
                        pierce = true;
                        pierceBuilding = true;
                        pierceCap = 5;
                        statusDuration = 60f * 5;
                        shootEffect = new Effect(33f, 80f, e -> {
                            color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());

                            randLenVectors(e.id, 30, e.finpow() * 140f, e.rotation, 10f, (x, y) -> {
                                Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f);
                            });
                        }).followParent(false);
                        hitEffect = Fx.hitFlameSmall;
                        despawnEffect = Fx.none;
                        status = StatusEffects.burning;
                        keepVelocity = false;
                        hittable = false;
                    }};
                }});
    }
}
