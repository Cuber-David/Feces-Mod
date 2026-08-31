package K.content.extend.Bullets.jujutsu;

import K.KMod;
import K.KSFX;
import K.Other_mod.FM.flame_extend.EmpathyDamage;
import K.content.Fx.OtherEffects.KaiEffect;
import K.content.Fx.OtherFx;
import K.content.effects.SpecialDeathEffects;
import K.content.extend.util.Utils;
import K.content.sounds;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Fires;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.defense.Wall;

import static K.content.extend.Math.kangles.lineVector;
import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Draw.yscl;
import static arc.math.Angles.randLenVectors;

public class KaiBulletType extends BulletType {
    public KaiBulletType(float hs){
        super(100,10000);
        lifetime = hs/4f;
        hitSize = hs;
        drawSize = 1000;
        despawnShake = hitShake = 100;
        despawnHit = true;
        splashDamage = 100000;
        splashDamageRadius = 50000;
        hitEffect = despawnEffect = new Effect(30, e -> {
            Draw.z(200);
            color(Pal.lightPyraFlame.a(e.fout()*e.fout()), Pal.redLight.a(e.fout()*e.fout()), e.fin());
            randLenVectors(e.id,(int)hitSize*24,hitSize*16*e.fin(),(x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.35f*hitSize*e.fin() + e.fout() * 1.6f);
                Tile t = Vars.world.tileWorld(e.x + x,e.y + y);
                if (t!=null) Fires.create(t);
                color(Color.white.a(e.fout()*e.fout()), Pal.redSpark.a(e.fout()*e.fout()), e.fin());
                Fill.circle(e.x + x + Mathf.random(-50,50), e.y + y + Mathf.random(-50,50), 0.21f*hitSize*e.fin() + e.fout() * 1.6f);
            });
        }){{clip = 2000;}};
    }

    @Override
    public void draw(Bullet b) {
        new Effect(120 ,e -> {
            color(Pal.lightPyraFlame, Pal.redSpark, Color.white, e.fin());
            randLenVectors(e.id,1,hitSize/2, b.rotation(), (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.15f*hitSize + e.fout() * 1.6f);
            });
            lineVector(e.id,2,hitSize,b.rotation(),(x,y) -> {
                Fill.circle(e.x + x, e.y + y, 0.15f*hitSize + e.fout() * 1.6f);
            });
        }).at(b.x,b.y,b.rotation());
        new KaiEffect(hitSize).at(b.x,b.y,b.rotation());
        Drawf.light(b.x,b.y,5*hitSize,Color.red,100);
    }

    @Override
    public void hit(Bullet b) {
        hit(b,b.x,b.y);
    }

    @Override
    public void hit(Bullet b, float x, float y){
        super.hit(b, x, y);
        float bx = b.x, by = b.y;
        Team team = b.team;

        int sid1 = sounds.desnukehit.at(bx, by, 1f, 4f);
        Core.audio.protect(sid1, true);
        float fall = Mathf.pow(Mathf.clamp(1f - sounds.desnukehit.calcFalloff(bx, by) * 1.1f), 1.5f);
        int sid2 = sounds.desnukehitfar.play(fall * 2f, 1f, sounds.desnukehit.calcPan(bx, by));
        Core.audio.protect(sid2, true);

        float[] arr = new float[360 * 3];
        Utils.rayCastCircle(b.x, b.y, 2400f, t -> (t.block().isStatic() || t.block() instanceof Wall) && !Mathf.within(b.x, b.y, t.worldx(), t.worldy(), 150f), t -> {
            float dst = 1f - Mathf.clamp(Mathf.dst(bx, by, t.x * Vars.tilesize, t.y * Vars.tilesize) / 480f);
            if(Mathf.chance(Mathf.pow(dst, 2f) * 0.75f)) Fires.create(t);
        }, t -> {
            float nx = t.x * Vars.tilesize, ny = t.y * Vars.tilesize;
            float ang = Angles.angle(bx, by, nx, ny);

            OtherFx.FlameFX.desNukeShockSmoke.at(nx, ny, ang);
        }, bl -> {
            //float d = lethal ? 12000f + bl.maxHealth / 20f : bl.health / 1.5f;
            float d = 21000f + bl.maxHealth / 5f;

            EmpathyDamage.damageBuildingRaw(bl, d, true, () -> {
                SpecialDeathEffects eff = SpecialDeathEffects.get(bl.block);

                bl.block.destroySound.at(bl);

                if(eff.explosionEffect == Fx.none){
                    Fx.dynamicExplosion.at(bl.x, bl.y, (Vars.tilesize * bl.block.size) / 2f / 8f);
                }else{
                    eff.explosionEffect.at(bl.x, bl.y, bl.hitSize() / 2f);
                }

                float shake = bl.hitSize() / 3f;
                Effect.shake(shake, shake, bl);

                if(bl.block.createRubble && !bl.floor().solid && !bl.floor().isLiquid){
                    Effect.rubble(bl.x, bl.y, bl.block.size);
                }

                float healthBase = (bl.block.size * Vars.tilesize);
                KMod.devasBatch.baseZ = Layer.block;
                KMod.devasBatch.switchBatch(bl::draw, dev -> {
                    dev.lifetime = Mathf.random(1f, 2f) * 60f;
                    dev.health = (Math.min(dev.width, dev.height) / healthBase) * bl.maxHealth * 1.5f;
                    dev.team = team;
                    dev.explosion = eff.explosionEffect != Fx.none ? eff.explosionEffect : OtherFx.FlameFX.fragmentExplosion;
                    dev.collides = true;
                    dev.contagiousChance = 0.85f;
                    dev.slowDownAmount = 0.5f;

                    float dx = dev.x - x;
                    float dy = dev.y - y;
                    float len = Mathf.sqrt(dx * dx + dy * dy);
                    //float force = Mathf.clamp(1f - (len / (range + bl.hitSize() / 2f + 8f)));
                    float force = 1f / (1f + (len - 150f) / 500f);

                    Vec2 v = Utils.vv.set(dx, dy).nor().setLength(force * 18f);
                    if(!v.isNaN()){
                        dev.vx = v.x;
                        dev.vy = v.y;
                        dev.vr = Mathf.range(25f * force);
                    }
                });
            });
        }, arr);

        Utils.scanEnemies(b.team, b.x, b.y, 4800f, true, true, t -> {
            if(t instanceof Unit u){
                //float damageScl = 1f;
                //if(u.isGrounded()) damageScl = FUtils.inRayCastCircle(bx, by, arr, u);
                float damageScl = Utils.inRayCastCircle(bx, by, arr, u);

                if(damageScl > 0){
                    Tmp.v2.trns(Angles.angle(bx, by, u.x, u.y), (16f + 5f / u.mass()) * damageScl);
                    u.vel.add(Tmp.v2);

                    EmpathyDamage.damageUnit(u, (u.maxHealth / 100f + 10000f) * damageScl, true, () -> {
                        KMod.vaporBatch.discon = null;
                        KMod.vaporBatch.switchBatch(u::draw, null, (d, w) -> {
                            float with = Utils.inRayCastCircle(bx, by, arr, d);
                            if(with > 0.5f){
                                d.disintegrating = true;
                                float dx = d.x - bx, dy = d.y - by;
                                float len = Mathf.sqrt(dx * dx + dy * dy);
                                float force = (6f / (1f + len / 90f) + (len / 480f) * 1.01f);

                                Vec2 v = Tmp.v1.set(dx, dy).nor().setLength(force * Mathf.random(0.9f, 1f));

                                d.lifetime = Mathf.random(60f, 90f) * Mathf.lerp(1f, 0.5f, Mathf.clamp(len / 480f));
                                d.drag = -0.015f;

                                d.vx = v.x;
                                d.vy = v.y;
                                d.vr = Mathf.range((force / 3f) * 5f);
                                d.zOverride = Layer.flyingUnit;
                            }
                        });
                    });
                }
            }else if(t instanceof Building bl){
                float damageScl = Utils.inRayCastCircle(bx, by, arr, bl);
                if(damageScl > 0){
                    Runnable death = t.within(bx, by, 150f + bl.hitSize() / 2f) ? () -> {
                        KMod.vaporBatch.discon = null;
                        KMod.vaporBatch.switchBatch(bl::draw, null, (d, w) -> {
                            d.disintegrating = true;
                            float dx = d.x - bx, dy = d.y - by;
                            float len = Mathf.sqrt(dx * dx + dy * dy);
                            //float force = Math.max(10f / (1f + len / 50f), (len / 150f) * 3f);
                            float force = (3f / (1f + len / 50f) + (len / 150f) * 0.9f);
                            //float force = (len / 150f) * 15f;

                            Vec2 v = Tmp.v1.set(dx, dy).nor().setLength(force * Mathf.random(0.9f, 1f));

                            d.lifetime = Mathf.random(60f, 90f) * Mathf.lerp(1f, 0.5f, Mathf.clamp(len / 150f));
                            d.drag = -0.03f;

                            d.vx = v.x;
                            d.vy = v.y;
                            d.vr = Mathf.range((force / 3f) * 5f);
                            d.zOverride = Layer.turret + 1f;
                        });
                        //FlameFX.desNukeVaporize.at(u.x, u.y, u.angleTo(bx, by) + 180f, u.hitSize / 2f);
                        OtherFx.FlameFX.desNukeVaporize.at(bl.x, bl.y, bl.angleTo(bx, by) + 180f, bl.hitSize() / 2f);
                    } : () -> {
                        SpecialDeathEffects eff = SpecialDeathEffects.get(bl.block);

                        bl.block.destroySound.at(bl);

                        if(eff.explosionEffect == Fx.none){
                            Fx.dynamicExplosion.at(bl.x, bl.y, (Vars.tilesize * bl.block.size) / 2f / 8f);
                        }else{
                            eff.explosionEffect.at(bl.x, bl.y, bl.hitSize() / 2f);
                        }

                        float shake = bl.hitSize() * 3f;
                        Effect.shake(shake, shake, bl);

                        if(bl.block.createRubble && !bl.floor().solid && !bl.floor().isLiquid){
                            Effect.rubble(bl.x, bl.y, bl.block.size);
                        }

                        float healthBase = (bl.block.size * Vars.tilesize);
                        KMod.devasBatch.baseZ = Layer.block;
                        KMod.devasBatch.switchBatch(bl::draw, dev -> {
                            dev.lifetime = Mathf.random(1f, 2f) * 60f;
                            dev.health = (Math.min(dev.width, dev.height) / healthBase) * bl.maxHealth * 1.5f;
                            dev.team = team;
                            dev.explosion = eff.explosionEffect != Fx.none ? eff.explosionEffect : OtherFx.FlameFX.fragmentExplosion;
                            dev.collides = true;
                            dev.contagiousChance = 0.85f;
                            dev.slowDownAmount = 0.5f;

                            float dx = dev.x - x;
                            float dy = dev.y - y;
                            float len = Mathf.sqrt(dx * dx + dy * dy);
                            //float force = Mathf.clamp(1f - (len / (range + bl.hitSize() / 2f + 8f)));
                            float force = 1f / (1f + (len - 150f) / 500f);

                            Vec2 v = Utils.vv.set(dx, dy).nor().setLength(force * 15f);
                            if(!v.isNaN()){
                                dev.vx = v.x;
                                dev.vy = v.y;
                                dev.vr = Mathf.range(25f * force);
                            }
                        });
                    };

                    EmpathyDamage.damageBuildingRaw(bl, (bl.maxHealth / 10f + 10000f) * damageScl, true, death);
                }
            }
        });

        Effect.shake(60f, 120f, b.x, b.y);
        OtherFx.FlameFX.desNukeShockwave.at(b.x, b.y, 480f);
        OtherFx.FlameFX.desNuke.at(b.x, b.y, 960f, arr);

        KSFX.inst.impactFrames(bx, by, b.rotation(), 23f, false, () -> {
            for(int i = 0; i < arr.length; i++){
                float len1 = arr[i], len2 = arr[(i + 1) % arr.length];
                float ang1 = (i / (float)arr.length) * 360f;
                float ang2 = ((i + 1f) / arr.length) * 360f;

                float x1 = Mathf.cosDeg(ang1) * len1, y1 = Mathf.sinDeg(ang1) * len1;
                float x2 = Mathf.cosDeg(ang2) * len2, y2 = Mathf.sinDeg(ang2) * len2;

                Fill.tri(bx, by, bx + x1, by + y1, bx + x2, by + y2);
            }
        });
    }

    public void despawned(Bullet b){
        hit(b, b.x, b.y);

        despawnEffect.at(b.x, b.y, b.rotation(), hitColor);
        despawnSound.at(b, 1f + Mathf.range(hitSoundPitchRange));
        randLenVectors(b.id,500,1000,(x,y) -> {
            OtherFx.FlameFX.desGroundMelt.at(b.x+x,b.y+y);
        });
        Effect.shake(despawnShake, despawnShake, b);
    }
}
