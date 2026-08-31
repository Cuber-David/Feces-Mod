package K.content.Fx;

import K.content.KUnitTypes;
import K.content.extend.util.DrawFunc;
import K.content.extend.util.EUGet;
import K.content.extend.util.Utils;
import K.graphics.CutBatch;
import K.graphics.GraphicUtils;
import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.Sized;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;
import mindustry.ui.Fonts;
import mindustry.world.Block;

import static K.KMod.name;
import static K.content.extend.util.EUGet.*;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.math.Angles.*;
import static arc.math.Interp.*;
import static mindustry.Vars.*;
import static mindustry.content.Fx.rand;
import static mindustry.content.Fx.v;

public class OtherFx {
    public static class VEFX{
        public static Effect shield = new Effect(30f, e -> {
            blend(Blending.additive);
            color(Tmp.c1.set(KPal.primary).a(Mathf.absin(e.fin(pow2Out), 1f / 50f, 1f) * 0.5f * e.fout()));

            Fill.polyBegin();
            for(int i = 0; i < 6; i++){
                float ang = i * (360f / 6f);
                Tmp.v1.trns(ang, 30f);
                Tmp.v1.y *= 0.333f;

                Vec2 v = Tmp.v2.trns(e.rotation + 90f, Tmp.v1.x, Tmp.v1.y).add(e.x, e.y);
                Fill.polyPoint(v.x, v.y);
            }
            Fill.polyEnd();

            blend();
        }),

        aoeExplosion2 = new Effect(80f, 500f, e -> {
            float z = z();
            z(z - 0.001f);

            Rand r = Utils.rand;
            r.setSeed(e.id * 31L);

            color(Color.gray);
            alpha(0.9f);
            for(int i = 0; i < 3; i++){
                float lenScl = r.random(0.4f, 1f);
                float time = Mathf.clamp(e.time / (e.lifetime * lenScl));

                float l = pow10Out.apply(time) * 100f;

                for(int j = 0; j < 4; j++){
                    float len = r.random(0.4f, 1f) * l;
                    float ang = r.random(360f);
                    float fout = Interp.pow5Out.apply(1 - time) * r.random(0.5f, 1f);

                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    //Fill.circle(e.x + x, e.y + y, fout * ((2f + intensity) * 1.8f));
                    Fill.circle(v.x, v.y, fout * 60f);
                }
            }

            //color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
            //stroke((1.7f * e.fout()) * (1f + (intensity - 1f) / 2f));
            z(z);
            color(KPal.primary, Pal.lightOrange, Color.gray, e.fin());
            Lines.stroke(2.72f * e.fout());
            for(int i = 0; i < 8; i++){
                //float c = r.random(0.2f);
                float l = r.random(20f, 150f) * e.finpow() + 0.1f;
                float a = r.random(360f);
                Vec2 v = Tmp.v1.trns(a, l);
                //lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (3f + intensity));
                Lines.lineAngle(v.x + e.x, v.y + e.y, Mathf.angle(v.x, v.y), 1f + e.fout() * 12f);
                //Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
                Drawf.light(e.x + v.x, e.y + v.y, 11f * e.fout(), Draw.getColor(), 0.8f);
            }

            color(Color.white);
            if(e.time < 3f){
                Fill.circle(e.x, e.y, e.rotation);
                Drawf.light(e.x, e.y, e.rotation * 2.5f, Color.white, 0.9f);
            }
        }),

        apathyCrit = new Effect(80f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id * 31L);
            for(int i = 0; i < 45; i++){
                //
                float offd = 0.4f;

                float ra = pow3Out.apply(r.random(1f)) / 2f + 0.5f;

                float in = (i / 45f) * ra * (1 - offd);

                //float of = r.random(1f - offd);
                //float time = Mathf.curve(e.fin(), of, of + offd);
                float time = Mathf.curve(e.fin(), in, in + offd);
                float angle = r.random(360f);
                float length = r.random(15f, 135f);
                float size = r.random(12f, 25f);

                if(time <= 0 || time >= 1) continue;

                Vec2 v = Tmp.v1.trns(angle, length * pow2In.apply(time)).add(e.x, e.y);
                color(KPal.primary, KPal.blood, pow2.apply(time));
                Fill.circle(v.x, v.y, size * pow2Out.apply(slope.apply(pow2In.apply(time))));
            }

        }).layer(Layer.flyingUnit + 0.01f),
        apathyBleed = new Effect(15f, e -> {
            color(KPal.blood);
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float minRange = e.color.r;
            float maxRange = e.color.g;

            for(int i = 0; i < 6; i++){
                float angle = e.rotation + pow2In.apply(r.nextFloat()) * (r.chance(0.5f) ? -1f : 1f) * 15f;
                float len = r.random(minRange, maxRange) * e.fin(pow2Out);
                float s = r.random(6f, 10f) * pow3Out.apply(e.fout());

                Tmp.v1.trns(angle, len).add(e.x, e.y);
                Fill.circle(Tmp.v1.x, Tmp.v1.y, s);
            }
        }).rotWithParent(true).layer(Layer.flyingUnit + 0.01f),
        apathyDeath = new Effect(30f, e -> {
            color(KPal.blood);
            Rand r = Utils.rand;
            r.setSeed(e.id);

            Fill.circle(e.x, e.y, (1f - Mathf.curve(e.fin(), 0f, 0.4f)) * e.rotation * 2f);

            for(int i = 0; i < 70; i++){
                float fin = Mathf.curve(e.fin(), r.random(0.1f), 1 - r.random(0.5f));
                float angle = r.random(360f);
                float length = r.random(220f, 460f);
                float size = r.random(9f, 15f) * pow2Out.apply(Utils.biasSlope(fin, 0.1f));
                float offset = r.random(e.rotation);

                if(fin > 0f && fin < 1f){
                    Tmp.v1.trns(angle, offset + length * pow3Out.apply(fin)).add(e.x, e.y);
                    GraphicUtils.tri(Tmp.v1.x, Tmp.v1.y, e.x, e.y, size, angle);
                    Drawf.tri(Tmp.v1.x, Tmp.v1.y, size, size * 2f, angle);
                }
            }
        }),

        bigLaserCharge = new Effect(120f, e -> {
            color();
            float scl = (1f + Mathf.absin(e.fin(pow2In), 1f / 100f, 1f)) * 180f * e.fin();

            for(int i = 0; i < 4; i++){
                float a = (360 / 4f) * i + 45f;

                Drawf.tri(e.x, e.y, (scl + 5) / 8f, scl, a);
            }
        }).layer(Layer.flyingUnit + 0.01f),
        bigLaserFlash = new Effect(8f, e -> {
            //
            color();
            float scl = 180f + 280f * e.finpow();

            for(int i = 0; i < 4; i++){
                float a = (360 / 4f) * i + 45f;

                Drawf.tri(e.x, e.y, 40 * pow3Out.apply(e.fout()), scl, a);
            }
        }).layer(Layer.flyingUnit + 0.01f),
        bigLaserHitSpark = new Effect(15f, e -> {
            color(Color.white, KPal.primary, e.fin());
            Lines.stroke(e.fout() * 1.2f + 0.5f);

            Angles.randLenVectors(e.id, 8, 87f * e.fin(), e.rotation, 45f, (x, y) -> {
                Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 9f + 0.5f);
            });

            Rand r = Utils.rand;
            r.setSeed(e.id + 642);
            float c = 0.4f;
            for(int i = 0; i < 6; i++){
                float id = i / 5f;
                float f = Mathf.curve(e.fin(), c * id, c * id + (1 - c));
                float ang = e.rotation + r.range(60f);
                float len = r.random(57f, 92f) * pow2Out.apply(f);
                float size = r.random(5f, 9f) * (1 - f);
                if(f > 0.001f){
                    color(Color.white, KPal.primary, f);
                    Vec2 v = Tmp.v1.trns(ang, len);

                    Fill.poly(e.x + v.x / 2, e.y + v.y / 2, 4, size / 2);
                    Fill.poly(e.x + v.x, e.y + v.y, 4, size);
                }
            }
        }),
        bigLaserHit = new Effect(30f, e -> {
            color(Color.white, KPal.primary, Color.gray, pow2Out.apply(e.fin()));

            //float size = e.data instanceof Float ? ((float)e.data) / 2f : 50f;
            float size = (e.data instanceof Float ? ((float)e.data) : (e.data instanceof Sized s ? s.hitSize() : 50f)) * 1.25f;

            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float w = r.range(size);
                float l = r.random(180f, 310f);
                float s = r.random(8f, 30f);

                float ic = i / 15f;
                float c = 0.3f;
                float f = Mathf.curve(e.fin(), ic * c, (ic * c) + (1 - c));

                if(f >= 0.0001f && f < 1f){
                    Vec2 v = Tmp.v1.trns(e.rotation, l * pow3In.apply(f), w * circleOut.apply(pow3In.apply(f))).add(e.x, e.y);
                    Fill.circle(v.x, v.y, s * (1 - (f * f)));
                }
            }
        }),


        shootShockWave = new Effect(35f, 600f, e -> {
            //GraphicUtils.drawShockWave(e.x, e.y, 75f, 0f, -e.rotation - 90f, 200f, 4f, 12);
            color(Color.white);
            alpha(0.666f * e.fout());

            float size = e.data instanceof Float ? (float)e.data : 200f;
            float nsize = size - 10f;

            GraphicUtils.drawShockWave(e.x, e.y, -75f, 0f, -e.rotation - 90f, nsize * e.finpow() + 10, 16f * e.finpow() + 4f, 16, 1f);
        }).layer((Layer.bullet + Layer.effect) / 2),

        fragmentGroundImpact = new Effect(40f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(e.color);

            float size = e.rotation;
            int iter = ((int)(size / 8f)) + 6;
            for(int i = 0; i < iter; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size) + (r.random(0.5f, 1f) * size * 0.5f + 20f) * e.finpow()).add(e.x, e.y);
                Fill.circle(v.x, v.y, r.random(5f, 16f) * e.fout());
            }
        }).layer(Layer.debris),
        fragmentExplosion = new Effect(40f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float size = e.rotation;
            e.lifetime = size / 1.5f + 10f;

            int iter = ((int)(size / 7f)) + 12;
            int iter3 = ((int)(size / 14.5f)) + 12;
            color(Color.gray);
            //alpha(0.9f);
            for(int i = 0; i < iter3; i++){
                //
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size / 2f) * e.finpow());
                float s = r.random(size / 2.75f, size / 2f) * e.fout();
                Fill.circle(v.x + e.x, v.y + e.y, s);
            }
            for(int i = 0; i < iter; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size) + (r.random(0.25f, 2f) * size) * e.finpow());
                float s = r.random(size / 3.5f, size / 2.5f) * e.fout();
                Fill.circle(v.x + e.x, v.y + e.y, s);
                Fill.circle(v.x / 2 + e.x, v.y / 2 + e.y, s * 0.5f);
            }

            float sfin = Mathf.curve(e.fin(), 0f, 0.65f);
            if(sfin < 1f){
                int iter2 = ((int)(size / 10f)) + 4;
                float sfout = 1f - sfin;

                color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
                Lines.stroke((1.7f * sfout) * (1f + size / 60f));

                Draw.z(Layer.effect + 0.001f);

                for(int i = 0; i < iter2; i++){
                    Vec2 v = Tmp.v1.trns(r.random(360f), r.random(0.001f, size / 2f) + (r.random(0.4f, 2.2f) * size) * pow2Out.apply(sfin));
                    Lines.lineAngle(e.x + v.x, e.y + v.y, Mathf.angle(v.x, v.y), 1f + sfout * 3 * (1f + size / 50f));
                }
            }
        }),

        fragmentExplosionSpark = new Effect(26f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float size = e.rotation;
            e.lifetime = size / 1.5f + 10f;

            float sfin = e.fin();

            int iter2 = ((int)(size / 12f)) + 3;
            float sfout = 1f - sfin;

            color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
            Lines.stroke((1.7f * sfout) * (1f + size / 60f));

            Draw.z(Layer.effect + 0.001f);

            for(int i = 0; i < iter2; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(0.001f, size / 2f) + (r.random(0.4f, 2.2f) * size) * pow2Out.apply(sfin));
                Lines.lineAngle(e.x + v.x, e.y + v.y, Mathf.angle(v.x, v.y), 1f + sfout * 3 * (1f + size / 50f));
            }
        }),

        destroySparks = new Effect(40f, 1200f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id + 64331);
            float size = (float)e.data;
            int isize = (int)(size * 1.75f) + 12;
            int isize2 = (int)(size * 1.5f) + 9;

            float fin1 = Mathf.clamp(e.time / 20f);
            float fin2 = Mathf.clamp(e.time / 10f);

            Lines.stroke(Math.max(2f, Mathf.sqrt(size) / 8f));
            for(int i = 0; i < isize2; i++){
                float f = Mathf.curve(fin1, 0f, r.random(0.8f, 1f));
                Vec2 v = Tmp.v1.trns(r.random(360f), 1f + (size * r.nextFloat() + 10f) * 1.5f * pow3Out.apply(f));
                float rsize = r.random(0.5f, 1.5f);
                if(f < 1){
                    color(KPal.paleYellow, Pal.lightOrange, Color.gray, f);
                    Lines.lineAngle(v.x + e.x, v.y + e.y, v.angle(), (size / 5f) * rsize * (1 - f));
                }
            }
            for(int i = 0; i < isize; i++){
                float f = Mathf.curve(e.fin(), 0f, r.random(0.5f, 1f));
                float re = Mathf.pow(r.nextFloat(), 1.5f);
                float ang = re * 90f * (r.nextFloat() > 0.5f ? 1 : -1);
                //float dst = (1f - Math.abs(ang / 90f) / 1.5f) * (50f + size * 3f * r.nextFloat()) * pow3Out.apply(f);
                float dst = (50f + ((size * 3f) / (1f + re / 5f)) * Mathf.pow(r.nextFloat(), (1f + re / 2f))) * Interp.pow3Out.apply(f);
                Vec2 v = Tmp.v1.trns(e.rotation + ang, 1f + dst);
                float rsize = r.random(0.75f, 1.5f);

                if(f < 1){
                    color(KPal.paleYellow, Pal.lightOrange, Color.gray, pow2In.apply(f));
                    Lines.lineAngle(v.x + e.x, v.y + e.y, v.angle(), (size / 3f) * rsize * (1 - f));
                }
            }

            color(KPal.paleYellow);
            for(int i = 0; i < 4; i++){
                float rot = i * 90f;
                Drawf.tri(e.x, e.y, (size / 2.5f) * (1 - fin2), size + size * fin2 * 1f, rot);
            }
        }).layer(Layer.effect + 0.005f),
        debrisSmoke = new Effect(40f, e -> {
            color(Color.gray);
            float fin = Utils.biasSlope(e.fin(), 0.075f);
            Fill.circle(e.x, e.y, e.rotation * fin);
        }),
        heavyDebris = new Effect(4f * 60f, 1200f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id + 644331);
            float size = (float)e.data;
            float sizeTime = (size) + 15f;
            int isize = (int)(size * 1.75f) + 12;

            float fin = Mathf.clamp(e.time / sizeTime);
            float fout = Mathf.clamp((e.lifetime - e.time) / 60f);
            Lines.stroke(3f);
            for(int i = 0; i < isize; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), Mathf.sqrt(r.nextFloat()) * size * 0.75f).add(e.x, e.y);
                float f = Mathf.curve(fin, 0f, r.random(0.5f, 1f));
                float angle = Mathf.pow(r.nextFloat(), 1.25f) * (r.random(1f) < 0.5f ? -1f : 1f) * 60f;
                //float angle = r.range(35f);
                float dst = r.random((220f + size * 4.5f) * pow3Out.apply(f)) * (1 - Math.abs(angle / 60f) / 1.5f);
                float s = r.chance(0.25f) ? (size / 3f) * r.random(0.5f, 1f) : Math.min(r.random(5f, 9f), size / 4f);
                float rrot = r.random(360f);
                int sides = r.random(3, 6);
                Vec2 v2 = Tmp.v2.trns(angle + e.rotation, dst);

                Draw.color(Tmp.c1.set(e.color).mul(r.random(0.9f, 1.2f)).a(fout));

                if(r.chance(0.75f)){
                    Fill.poly(v.x + v2.x, v.y + v2.y, sides, s, rrot);
                }else{
                    Lines.poly(v.x + v2.x, v.y + v2.y, sides, s, rrot);
                }
            }

        }).layer(Layer.debris - 0.01f),
        simpleFragmentation = new Effect(30f, e -> {
            if(!(e.data instanceof TextureRegion region)) return;
            float bounds = Math.min(region.width, region.height);
            float b2 = bounds / 4f;
            float bw = b2 / region.texture.width;
            float bh = b2 / region.texture.height;
            float bscl = bounds * scl;
            int ib = (int)(bscl * 1.5f) + 8;
            Rand r = Utils.rand;
            r.setSeed(e.id + 46241);

            Draw.color(e.color);
            for(int i = 0; i < ib; i++){
                float u = Mathf.lerp(region.u, (region.u2 - bw), r.nextFloat());
                float v = Mathf.lerp(region.v, (region.v2 - bh), r.nextFloat());
                float u2 = u + bw;
                float v2 = v + bh;

                TextureRegion tr = Tmp.tr1;
                tr.texture = region.texture;
                tr.set(u, v, u2, v2);

                float f = Mathf.curve(e.fin(), 0f, r.random(0.8f, 1f));

                Vec2 base = Tmp.v1.trns(r.random(360f), bscl / 2f).add(e.x, e.y);
                Vec2 off = Tmp.v2.trns(e.rotation + r.range(30f), 120f * r.nextFloat() * pow2Out.apply(f));

                float rrot = r.random(360f) + r.range(180f) * f;

                if(f < 1){
                    Draw.alpha(1f - Mathf.curve(f, 0.8f, 1f));
                    Draw.rect(tr, base.x + off.x, base.y + off.y, rrot);
                }
            }
        }).layer(Layer.flyingUnitLow),


        empathyDecoyDestroy = new Effect(90f, 700f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(KPal.empathy);
            Lines.stroke(12f * e.fout());
            Lines.circle(e.x, e.y, 6f + 160f * e.fin());

            Lines.stroke(2f * e.fout());
            for(int i = 0; i < 10; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(45f, 230f) * e.finpow()).add(e.x, e.y);

                Lines.line(e.x, e.y, v.x, v.y, false);
                Fill.poly(v.x, v.y, 4, 3f * e.fout());
            }
        }),

        chordonDecoyDestroy = new Effect(90f, 700f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(KPal.chordon);
            Lines.stroke(12f * e.fout());
            Lines.circle(e.x, e.y, 6f + 160f * e.fin());

            Lines.stroke(2f * e.fout());
            for(int i = 0; i < 10; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(45f, 230f) * e.finpow()).add(e.x, e.y);

                Lines.line(e.x, e.y, v.x, v.y, false);
                Fill.poly(v.x, v.y, 4, 3f * e.fout());
            }
        }),

        empathyParry = new Effect(8f, e -> {
            color();
            float scl = 20f + 30f * e.finpow();

            for(int i = 0; i < 4; i++){
                float a = (360 / 4f) * i;

                Drawf.tri(e.x, e.y, 8 * pow3Out.apply(e.fout()), scl, a);
            }
        }).layer(Layer.flyingUnit + 0.01f),
        empathyParryExplosion = new Effect(40f, e -> {
            color(KPal.empathyDark, e.fout());
            blend(Blending.additive);
            float r = pow3Out.apply(Mathf.clamp(e.time / 6f)) * e.rotation + e.finpow() * 10f;
            Fill.circle(e.x, e.y, r);
            blend();
        }).layer(Layer.flyingUnitLow + 1f),

        empathyPrimeStrike = new Effect(40f, 300f, e -> {
            Rand rand = Utils.rand;
            rand.setSeed(e.id + 45245);
            float rrot = 90f + (rand.random(15f, 180f - 15f) * (rand.nextFloat() >= 0.5f ? 1 : -1));
            float exLength = rand.random(8f, 25f);

            Tmp.c1.set(KPal.empathyAdd).a(Mathf.clamp((e.lifetime - e.time) / 30f));
            color(Tmp.c1);
            blend(Blending.additive);

            float fin = Mathf.clamp(e.time / 5f);
            GraphicUtils.draw3D(e.x, e.y, rand.range(40f), rrot, -e.rotation + 90f, fs -> {
                for(int i = 0; i < 16; i++){
                    float f1 = (i / 16f);
                    float f2 = ((i + 1) / 16f);

                    float rot = f1 * 180f * fin;
                    float nrot = f2 * 180f * fin;
                    float width1 = f1 * 17f;
                    float width2 = f2 * 17f;

                    //float ex1 = Mathf.slope(f1) * 10f;
                    //float ex2 = Mathf.slope(f2) * 10f;

                    for(int j = 0; j < 2; j++){
                        float r = j == 0 ? rot : nrot;
                        float w = j == 0 ? width1 : width2;
                        float ex = pow2Out.apply(Mathf.slope(j == 0 ? f1 : f2)) * exLength;
                        for(int k = 0; k < 2; k++){
                            int sign = j == 0 ? k : 1 - k;
                            Vec2 v = Tmp.v1.trns(r, 30f + w * -sign).add(0, ex);
                            fs.add(v.x, v.y);
                        }
                    }
                }
            });
            blend();
        }),
        empathyDashShockwave = new Effect(10f, 300f, e -> {
            color(Color.white);
            alpha(0.666f * e.fout());

            float size = 60f;
            float nsize = size - 15f;

            GraphicUtils.drawShockWave(e.x, e.y, -75f, 0f, -e.rotation - 90f, nsize * e.finpow() + 15, 30f * e.finpow() + 4f, 16, 1f);
        }),
        empathyDashDust = new Effect(3f * 60f, 150, e -> {
            float fin = Mathf.clamp(e.time / 25f);
            float fout = Mathf.clamp((e.lifetime - e.time) / 60f);

            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(e.color);
            for(int i = 0; i < 3; i++){
                int sign = Mathf.sign(r.nextBoolean());
                float size = r.random(3f, 7f);
                //Vec2 v = Tmp.v1.trns(e.rotation + 90f * sign + r.range(25f), r.random(45f) * pow2Out.apply(fin)).add(e.x, e.y);
                Vec2 v = Tmp.v1.trns(e.rotation + 90f * sign + r.range(25f), Mathf.pow(r.nextFloat(), 1.75f) * 45f * pow2Out.apply(fin)).add(e.x, e.y);
                Fill.circle(v.x, v.y, size * fout);
            }

        }).layer(Layer.scorch + 5f),
        empathyPrimeShockwave = new Effect(40f, 450f, e -> {
            color(Color.white);
            alpha(0.666f * e.fout());
            Rand r = Utils.rand;
            r.setSeed(e.id);

            float size = 200f;
            float nsize = size - 15f;

            GraphicUtils.drawShockWave(e.x, e.y, 90f - r.random(5f, 15f), 0f, -e.rotation - 90f, nsize * e.finpow() + 15, 30f * e.finpow() + 5f, 16, 1f);
        }).layer(Layer.flyingUnit),
        empathyPrimeHit = new Effect(12f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id + 3511);
            float fin = pow10Out.apply(e.fin());
            float fout = pow2Out.apply(e.fout());

            color(KPal.empathy);

            for(int i = 0; i < 8; i++){
                float angle = Mathf.pow(r.nextFloat(), 1.5f) * 100f * (r.nextFloat() > 0.5f ? -1 : 1);
                float dst = Mathf.pow(1f - Math.abs(angle / 100f) * 0.8f, 2f);

                Tmp.v1.trns(e.rotation + angle, 5f + r.range(5f)).add(e.x, e.y);

                float len = r.random(50f, 110f);
                float wid = r.random(14f, 20.5f);
                //GraphicUtils.diamond(Tmp.v1.x, Tmp.v1.y, r.random(10f, 14.5f) * dst * fout, r.random(40f, 60f) * dst * fin, e.rotation + angle);
                Drawf.tri(Tmp.v1.x, Tmp.v1.y, wid * fout, 2f * len * dst * fin, e.rotation + angle);
                Drawf.tri(Tmp.v1.x, Tmp.v1.y, wid * fout, 0.25f * len * dst * fin, e.rotation + angle + 180f);
            }
            float randrot = r.random(360f);
            for(int i = 0; i < 7; i++){
                float ang = ((360f / 7f) * i) + r.range((180f / 7f) / 1.5f) + randrot;
                float len = r.random(40f, 90f);
                float wid = r.random(10f, 15f);
                Drawf.tri(e.x, e.y, wid * fout, len * fin, e.rotation + ang);
            }
            Lines.stroke(2f * fout);
            for(int i = 0; i < 12; i++){
                float angle = Mathf.pow(r.nextFloat(), 1.5f) * 90f * (r.nextFloat() > 0.5f ? -1 : 1);
                float dst = Mathf.pow(1f - Math.abs(angle / 90f) * 0.8f, 2f);

                Tmp.v1.trns(e.rotation + angle, 30f + r.range(5f)).add(e.x, e.y);
                float len = r.random(180f, 320f) * dst;
                /*
                float wid = r.random(4f, 5f);
                Drawf.tri(Tmp.v1.x, Tmp.v1.y, wid * fout, len, e.rotation + angle);
                Drawf.tri(Tmp.v1.x, Tmp.v1.y, wid * fout, 5f, e.rotation + angle + 180f);
                */
                //Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, len * fin, angle);
                Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, e.rotation + angle, len * fin);
            }

            color();

            Drawf.tri(e.x, e.y, 8f * fout, 60f + 90f * fin, e.rotation + 90f);
            Drawf.tri(e.x, e.y, 8f * fout, 60f + 90f * fin, e.rotation - 90f);

            for(int i = 0; i < 4; i++){
                float angle = r.range(75f);

                float width = (1f - Math.abs(angle / 75f) * 0.45f) * 90f * r.random(0.75f, 1.25f);
                float rwid = r.random(0.9f, 1.1f);
                int iwid = (int)(width / 9f) + 5;

                for(int j = 0; j < 3; j++){
                    float offset = r.range(width / 2f);
                    float ff = 1f - (Math.abs(offset) / (width / 2f)) * 0.75f;
                    float dst = (1f - Math.abs((angle + offset) / (75f + width / 2))) + 0.5f;

                    Tmp.v1.trns(e.rotation + angle + offset, 30f).add(e.x, e.y);
                    GraphicUtils.diamond(Tmp.v1.x, Tmp.v1.y, ff * r.random(3f, 4.5f) * fout, ff * r.random(15f, 23f) * dst * fin, e.rotation + angle + offset);
                    //Drawf.tri(Tmp.v1.x, Tmp.v1.y, size, size * 2f, angle);
                    //float len = r.random(15f, 23f);
                    //float wid = r.random(3f, 4.5f);
                    //Drawf.tri(Tmp.v1.x, Tmp.v1.y, ff * wid * fout, ff * len * dst * fin, e.rotation + angle + offset);
                    //Drawf.tri(Tmp.v1.x, Tmp.v1.y, ff * wid * fout, ff * 0.25f * len * dst * fin, e.rotation + angle + offset + 180f);
                }

                for(int j = 0; j < iwid; j++){
                    /*
                    float ww = j / (iwid - 1f);
                    float w = Mathf.slope(ww) * width * 0.2f * rwid;
                    int side = j % 2;
                    float w1 = side == 0 ? w : 0f;
                    float w2 = side == 1 ? w : 0f;

                    Tmp.v1.trns(e.rotation + (angle - width / 2f) + width * ww, 30f - w1).add(e.x, e.y);
                    Tmp.v2.trns(e.rotation + (angle - width / 2f) + width * ww, 30f - w2).add(e.x, e.y);

                    Fill.polyPoint(Tmp.v1.x, Tmp.v1.y);
                    Fill.polyPoint(Tmp.v2.x, Tmp.v2.y);
                    */

                    Fill.polyBegin();
                    for(int k = 0; k < 2; k++){
                        float ww = (j + k) / (float)iwid;
                        float w = pow2Out.apply(Mathf.slope(ww)) * width * 0.035f * rwid * fin * fout;
                        int side = k % 2;
                        float w1 = side == 0 ? w : 0f;
                        float w2 = side == 1 ? w : 0f;

                        Tmp.v1.trns(e.rotation + (angle - width / 2f) + width * ww, 30f - w1).add(e.x, e.y);
                        Tmp.v2.trns(e.rotation + (angle - width / 2f) + width * ww, 30f - w2).add(e.x, e.y);

                        Fill.polyPoint(Tmp.v1.x, Tmp.v1.y);
                        Fill.polyPoint(Tmp.v2.x, Tmp.v2.y);
                    }
                    Fill.polyEnd();
                }
            }
        }).layer(Layer.flyingUnit + 0.01f),

        empathyShotgun = new Effect(6, 1200f, e -> {
            if(!(e.data instanceof Float)) return;
            Draw.color(KPal.empathy);
            float l = (float)e.data;
            e.lifetime = Math.max(l / (500f / 6f), 2f);
            Tmp.v1.trns(e.rotation, l).add(e.x, e.y);
            Lines.stroke(2f);
            Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, e.rotation + 180f, l * e.fout());
        }),

        empathyRico = new Effect(30f, 4000f, e -> {
            if(!(e.data instanceof Float)) return;
            Draw.color(KPal.empathy);
            float l = (float)e.data;
            Lines.stroke(4f * e.fout());
            Lines.lineAngle(e.x, e.y, e.rotation, l);
        }),

        empathyLightningHit = new Effect(14f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(Color.white, KPal.empathy, e.fin());
            Lines.stroke(0.5f + e.fout());
            for(int i = 0; i < 7; i++){
                float rot = e.rotation + r.range(35f);
                float len = r.random(20f) * e.fin();
                Tmp.v1.trns(rot, len).add(e.x, e.y);
                Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, rot, 4.5f * e.fout() + 1f);
            }
        }),

        empathyRendHit = new Effect(20f, 150f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(Color.white, KPal.empathy, e.fin());
            Lines.stroke(4f * e.fout());
            for(int i = 0; i < 8; i++){
                float rot = e.rotation + r.range(5f);
                float len = r.random(140f) * e.finpow();
                float ll = r.random(10f, 25f) * e.finpow();

                Tmp.v1.trns(rot, len).add(e.x, e.y);
                Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, rot, ll);
            }
        }),
        empathyRend = new Effect(60f, 600f, e -> {
            //Draw.color(Color.white, e.fout());
            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 8; i++){
                float f = i / 7f;
                float a = 0.5f;
                float scl = r.random(0.5f, 1.3f);

                float fin = pow4Out.apply(Mathf.curve(e.fin(), a * f, (1f - a) + (a * f)));
                float x = r.random(360f), y = r.random(360f), z = r.random(360f);

                if(fin <= 0.001f || fin >= 0.999f) continue;
                Draw.color(Color.white, (1f - fin) * 0.5f);
                GraphicUtils.drawShockWave(e.x, e.y, x, y, z, 180f * scl * fin + 10, 16f * fin + 8f, 16, 1f);
            }
        }).layer(Layer.flyingUnit + 0.01f),

        empathyBlast = new Effect(60f, 900f, e -> {
            Draw.color(Color.white, KPal.empathyAdd, pow2Out.apply(e.fin()));
            Draw.alpha(pow2In.apply(e.fout()));
            Draw.blend(Blending.additive);

            float size = e.rotation;
            Fill.circle(e.x, e.y, (size * pow10Out.apply(e.fin())) + (size * 0.1f * e.fin()));

            Draw.blend();
        }),

        empathySquareDespawn = new Effect(60f, 280f, e -> {
            float size = 120f;

            Draw.color(KPal.empathy);
            Lines.stroke(4f * e.fout());
            Lines.poly(e.x, e.y, 4, size, e.rotation + 45f);

            Fill.poly(e.x, e.y, 4, size * Mathf.curve(e.fout(), 0.85f, 1f), e.rotation + 45f);
            Draw.color();
        }).layer(Layer.flyingUnit),

        empathyDualDespawn = new Effect(15f, e -> {
            Draw.color(e.color);
            Angles.randLenVectors(e.id, 7, 17f * e.finpow(), (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4f * e.rotation));
            Fill.circle(e.x, e.y, e.fout() * 16f * e.rotation);
        }),

        empathyBigLaserHit = new Effect(30f, e -> {
            color(Color.white, KPal.empathy, Color.gray, pow2Out.apply(e.fin()));

            //float size = e.data instanceof Float ? ((float)e.data) / 2f : 50f;
            float size = (e.data instanceof Float ? ((float)e.data) : (e.data instanceof Sized s ? s.hitSize() : 50f)) * 1.25f;

            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float w = r.range(size);
                float l = r.random(180f, 310f);
                float s = r.random(8f, 30f);

                float ic = i / 15f;
                float c = 0.3f;
                float f = Mathf.curve(e.fin(), ic * c, (ic * c) + (1 - c));

                if(f >= 0.0001f && f < 1f){
                    Vec2 v = Tmp.v1.trns(e.rotation, l * pow3In.apply(f), w * circleOut.apply(pow3In.apply(f))).add(e.x, e.y);
                    Fill.circle(v.x, v.y, s * (1 - (f * f)));
                }
            }
        }),

        empathyDepowered = new Effect(40f, 1200f, e -> {
            float size = e.rotation;
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(KPal.red, KPal.empathyAdd, e.fin());
            blend(Blending.additive);
            Lines.stroke(2.4f * e.fout());
            Lines.circle(e.x, e.y, size + size * pow2Out.apply(e.fin()));
            for(int i = 0; i < 4; i++){
                //TODO effect
                float ang = i * 90f + 45f;
                float sscl = size / 25f;
                TextureRegion region = GraphicUtils.getChain();
                float len = region.width * scl * sscl;

                for(int j = 0; j < 16; j++){
                    Tmp.v2.trns(ang + r.random(35f), (r.random(70f) + len * j) * pow3Out.apply(e.fin()));
                    Vec2 tr = Tmp.v1.trns(ang, size + len * j).add(e.x, e.y).add(Tmp.v2);
                    Draw.rect(region, tr.x, tr.y, region.width * scl * sscl * e.fout(), region.height * scl * sscl * e.fout(), ang + r.range(180f) * pow2Out.apply(e.fin()));
                }
            }
            blend();
        }),

        empathyRainbowHit = new Effect(30f, e -> {
            //float size = e.data instanceof Float ? ((float)e.data) / 2f : 50f;
            float size = (e.data instanceof Float ? ((float)e.data) : (e.data instanceof Sized s ? s.hitSize() : 50f)) * 1.25f;

            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float w = r.range(size);
                float l = r.random(180f, 310f);
                float s = r.random(8f, 15f);

                float ic = i / 15f;
                float c = 0.3f;
                float f = Mathf.curve(e.fin(), ic * c, (ic * c) + (1 - c));

                float time = f * 40f + Time.time;
                Draw.color(Tmp.c1.set(Color.red).shiftHue(time * 5f));

                if(f >= 0.0001f && f < 1f){
                    Vec2 v = Tmp.v1.trns(e.rotation, l * pow3In.apply(f), w * circleOut.apply(pow3In.apply(f))).add(e.x, e.y);
                    //Fill.circle(v.x, v.y, s * (1 - (f * f)));
                    Fill.poly(v.x, v.y, 4, s * (1 - (f * f)));
                }
            }
        }).layer(Layer.flyingUnit + 0.1f),

        endFlash = new Effect(15f, e -> {
            float f = pow2In.apply(Mathf.curve(e.fin(), 0f, 0.1f));
            float fo = Mathf.curve(e.fout(), 0.4f, 1f);
            float f2 = pow2Out.apply(Mathf.curve(e.fin(), 0.1f, 0.75f));
            float scl = e.rotation;

            Draw.color();
            for(int i = 0; i < 4; i++){
                float r = i * 90f;
                Drawf.tri(e.x, e.y, 5f * fo * scl, (5f + 120f * f) * fo * scl, r);
            }
            for(int i = 0; i < 2; i++){
                float r = i * 180f;
                Drawf.tri(e.x, e.y, 7f * e.fout() * scl, (7f + 310f * f2) * scl, r);
            }
        }).layer(Layer.flyingUnit + 0.1f),

        endDeath = new Effect(50f, 1000f, e -> {
            float fin1 = Mathf.curve(e.fin(), 0f, 0.65f);
            float size = e.rotation;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            e.lifetime = 50f + r.range(4f);

            int base = (int)((size * size) / 34f) + 2;
            int base2 = (int)((size * size) / 16f) + 4;

            Draw.color(KPal.darkRed, KPal.empathy, Mathf.curve(pow2Out.apply(fin1), 0f, 0.5f));

            for(int i = 0; i < base; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), Mathf.sqrt(r.nextFloat()) * size + ((20f + size * 4f) * pow2Out.apply(fin1) * r.nextFloat()));
                float s = r.random(0.5f, 1.1f) * (size * 0.4f + 8f) * (1f - fin1);
                if(fin1 < 1f) Fill.circle(v.x + e.x, v.y + e.y, s);
            }
            Draw.color(KPal.darkRed, KPal.empathy, Mathf.curve(pow2Out.apply(e.fin()), 0f, 0.5f));
            for(int i = 0; i < base2; i++){
                float sin = Mathf.sin(r.random(7f, 11f), r.random(size * 2f)) * e.fin();
                Vec2 v = Tmp.v1.trns(r.random(360f), Mathf.sqrt(r.nextFloat()) * size + ((40f + size * 8f) * pow2In.apply(e.fin()) * r.nextFloat()), sin);
                float s = r.random(0.5f, 1.1f) * (size * 0.25f + 3f) * (1f - pow4In.apply(e.fin()));
                Fill.circle(v.x + e.x, v.y + e.y, s);
            }
        }),

        endSplash = new Effect(35f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            e.lifetime = 50f + r.range(16f);

            Draw.color(KPal.darkRed);
            int am = r.random(5, 9);
            for(int i = 0; i < am; i++){
                float of = 0.3f / (am - 1f);
                float c = Mathf.curve(e.fin(), of * i, (1 - 0.3f) + (of * i));
                float ang = r.range(40f) + e.rotation;
                float scl = r.random(0.6f, 1.4f) * 200f;
                float len = r.random(350f, 900f);

                if(c > 0.0001f && c < 0.9999f){
                    Tmp.v1.trns(ang, len *  pow2Out.apply(c)).add(e.x, e.y);
                    GraphicUtils.diamond(Tmp.v1.x, Tmp.v1.y, scl * 0.22f * (1f - pow3In.apply(c)), scl * pow3Out.apply(Mathf.curve(c, 0f, 0.5f)) + scl * 0.5f, ang);
                }
            }
        }).layer(Layer.darkness + 1f),

        coloredHit = new Effect(15f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(Color.white, KPal.red, e.fin());
            Lines.stroke(0.5f + e.fout());

            for(int i = 0; i < 8; i++){
                float ang = r.range(12f) + e.rotation;
                float len = r.random(40f) * e.fin();
                Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);

                Lines.lineAngle(v.x, v.y, ang, e.fout() * 8f + 1f);
            }
        }),

        desGroundHit = new Effect(30f, 250f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            int amount = r.random(4, 12);
            int amount2 = r.random(7, 14);
            float c = r.random(0.1f, 0.6f);
            float c2 = r.random(0.1f, 0.3f);

            z(Layer.groundUnit);
            color(Color.gray);
            for(int i = 0; i < amount2; i++){
                float l = (i / (amount2 - 1f)) * c2;
                float f = Mathf.curve(e.fin(), l, (1f - c2) + l);
                float ang = r.random(360f);
                float len = r.random(80f) * e.rotation;
                float scl = r.random(8.5f, 19f) * e.rotation;
                if(f > 0f && f < 1f){
                    float f2 = pow2Out.apply(f) * 0.6f + f * 0.4f;
                    Vec2 v = Tmp.v1.trns(ang, len * f2).add(e.x, e.y);
                    Fill.circle(v.x, v.y, scl * (1f - f));
                }
            }

            z(Layer.groundUnit + 0.02f);
            color(KPal.melt, e.color, pow3Out.apply(e.fin()));
            for(int i = 0; i < amount; i++){
                float l = (i / (amount - 1f)) * c;
                float f = Mathf.curve(e.fin(), l, (1f - c) + l);
                float ang = r.random(360f);
                float len = r.random(100f) * e.rotation;
                float scl = r.random(3f, 13f) * e.rotation;
                if(f > 0f && f < 1f){
                    float f2 = pow2Out.apply(f) * 0.4f + f * 0.6f;
                    Vec2 v = Tmp.v1.trns(ang, len * f2).add(e.x, e.y);
                    Fill.circle(v.x, v.y, scl * (1f - f));
                }
            }
        }).layer(Layer.groundUnit),

        desGroundHitMain = new Effect(90f, 900f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            float arange = 25f;
            float scl = 1f;
            float range = 300f;

            color(Color.gray, 0.8f);
            for(int i = 0; i < 4; i++){
                int count = r.random(15, 23);
                for(int k = 0; k < count; k++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rr = r.range(arange) + e.rotation;
                    float len = r.random(range) * pow4Out.apply(e.fin());
                    float sscl = r.random(21f, 43f) * scl * pow2.apply(1f - f) * Mathf.clamp(e.time / 8f);

                    if(f < 1){
                        Vec2 v = Tmp.v1.trns(rr, len).add(e.x, e.y);
                        Fill.circle(v.x, v.y, sscl);
                    }
                }

                arange *= 2f;
                scl *= 1.12f;
                range *= 0.6f;
            }
            float fin2 = Mathf.clamp(e.time / 18f);

            if(fin2 < 1){
                int count = 20;
                color(Pal.lighterOrange);
                for(int i = 0; i < count; i++){
                    float f = Mathf.curve(fin2, 0f, 1f - r.random(0.2f));
                    float ang = r.range(40f) + e.rotation;
                    float off = r.random(70f) + r.random(15f) * f;
                    float len = r.random(190f, 450f);

                    if(f < 1){
                        Vec2 v = Tmp.v1.trns(ang, off).add(e.x, e.y);
                        Lines.stroke(0.5f + (1f - f) * 3f);
                        Lines.lineAngle(v.x, v.y, ang, len * f, false);
                    }
                }
            }
        }),

        desCreepHit = new Effect(20f, e -> {
            float angr = 90f;
            float len = 1f;
            Rand r = Utils.rand;
            r.setSeed(e.id);

            Draw.color(KPal.red);
            Lines.stroke(1.75f);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 10; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float tlen = r.random(32f) * len * f + r.random(15f);
                    float rot = r.range(angr) + e.rotation;
                    float slope = pow2Out.apply(Mathf.slope(f)) * 24f * len;
                    Vec2 v = Tmp.v1.trns(rot, tlen).add(e.x, e.y);
                    Lines.lineAngle(v.x, v.y, rot, slope, false);
                }

                angr *= 0.7f;
                len *= 1.7f;
            }
            Draw.reset();
        }),

        desCreepHeavyHit = new Effect(300f, 1200f, e -> {
            float sizeScl = e.data instanceof Float ? (float)e.data : 1f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            float scl = Mathf.clamp(e.time / 8f);
            float range = 32f;
            float countScl = 1f;
            float z = z();
            Tmp.c2.set(Color.gray).a(0.8f);
            for(int i = 0; i < 5; i++){
                color(Pal.lightOrange, Tmp.c2, i / 4f);
                float arange = 180f;
                float range2 = 1f;
                for(int j = 0; j < 5; j++){
                    int count = (int)(r.random(12, 15) * countScl);
                    for(int k = 0; k < count; k++){
                        float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                        float ang = r.range(arange) + e.rotation;
                        float len = r.random(range * range2) * sizeScl * 0.5f;
                        float size = r.random(10f, 24f) * scl * sizeScl * 0.5f;

                        z(z - r.random(0.002f));
                        if(f < 1f){
                            Vec2 v = Tmp.v1.trns(ang, len * pow5Out.apply(f)).add(e.x, e.y);
                            Fill.circle(v.x, v.y, size * (1f - pow10In.apply(f)));
                        }
                    }

                    arange *= 0.6f;
                    range2 *= 1.75f;
                }
                scl *= 1.5f;
                range *= 1.6f;
                countScl *= 1.4f;
            }
            z(z);

            float shock = 230f * sizeScl * (1f + e.fin() * 2f) + (e.fin() * 50f);
            color(Pal.lighterOrange);
            if(e.time < 5f){
                Fill.circle(e.x, e.y, shock);
            }

            Lines.stroke(3f * e.fout());
            Lines.circle(e.x, e.y, shock);

            for(int i = 0; i < 16; i++){
                float ang = r.random(360f);
                Vec2 v = Tmp.v1.trns(ang, shock).add(e.x, e.y);
                Drawf.tri(v.x, v.y, 8f * e.fout() * sizeScl, (70f + 25f * e.fin()) * sizeScl, ang + 180f);
            }

            color(Pal.lighterOrange, Pal.lightOrange, e.fin());
            float arange = 180f;
            float range2 = 1f;
            Lines.stroke(3f);
            for(int i = 0; i < 6; i++){
                int count = r.random(8, 12);
                for(int k = 0; k < count; k++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                    float f2 = pow5Out.apply(f);
                    float rot = e.rotation + r.range(arange);
                    float len = range2 * r.random(120f) * sizeScl * f2 + r.random(50f * sizeScl);
                    float str = r.random(34f, 60f) * range2 * sizeScl * pow2Out.apply(Mathf.slope(f2));
                    if(f < 1f){
                        Vec2 v = Tmp.v1.trns(rot, len).add(e.x, e.y);
                        Lines.lineAngle(v.x, v.y, rot, str);
                    }
                }

                arange *= 0.65f;
                range2 *= 1.6f;
            }
        }),

        desGroundMelt = new Effect(15f * 60, e -> {
            z(Layer.debris);
            color(Color.red);
            //Draw.blend(Blending.additive);
            float fout = Mathf.curve(e.fout(), 0f, 0.333f);

            Fill.circle(e.x, e.y, e.rotation * Mathf.clamp(e.time / 6f) * fout);

            //Draw.blend();
            z(Layer.debris + 0.05f);

            color(KPal.melt);
            blend(Blending.additive);
            Fill.circle(e.x, e.y, e.rotation * Mathf.clamp(e.time / 6f) * fout);
            blend();
        }).layer(Layer.debris),

        desRailHit = new Effect(80f, 900f, e -> {
            float sizeScl = e.data instanceof Float ? (float)e.data : 1f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            float ang = 180f;
            float rscl = 0.7f * sizeScl;
            Draw.color(KPal.red);
            for(int i = 0; i < 5; i++){
                int count = (int)(10 * rscl);
                for(int j = 0; j < count; j++){
                    float fin = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rot = r.range(ang) + e.rotation;
                    float off = r.random(22f * rscl) + r.random(50f * Mathf.pow(rscl, 1.5f)) * pow4Out.apply(fin);
                    float sscl = r.random(0.7f, 1.2f);

                    float wid = 12f * sscl * rscl * (1f - pow4In.apply(fin));
                    float hei = 52f * sscl * Mathf.pow(rscl, 1.5f) * pow5Out.apply(fin);

                    Vec2 v = Tmp.v1.trns(rot, off).add(e.x, e.y);
                    Drawf.tri(v.x, v.y, wid, hei, rot);
                    Drawf.tri(v.x, v.y, wid, wid * 2.2f, rot + 180f);
                }

                ang *= 0.6f;
                rscl *= 1.5f;
            }

            ang = 180f;
            rscl = 0.5f * sizeScl;
            Draw.color(KPal.red, Color.white, e.fin());
            Lines.stroke(3f);
            for(int i = 0; i < 7; i++){
                int count = 12;
                for(int j = 0; j < count; j++){
                    float fin = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rot = r.range(ang) + e.rotation;
                    float off = r.random(30f * rscl) + r.random(40f * Mathf.pow(rscl, 1.6f)) * pow5Out.apply(fin);

                    float len = r.random(20f, 40f) * Mathf.pow(rscl, 1.6f) * sineOut.apply(Mathf.slope(pow5Out.apply(fin)));

                    Vec2 v = Tmp.v1.trns(rot, off).add(e.x, e.y);
                    Lines.lineAngle(v.x, v.y, rot, len, false);
                }

                ang *= 0.5f;
                rscl *= 1.5f;
            }

            if(sizeScl < 0.75f) return;
            Draw.color(Color.white, 0.666f * e.fout());

            GraphicUtils.drawShockWave(e.x, e.y, -105f, 0f, -e.rotation - 90f, 400f * sizeScl * pow2Out.apply(e.fin()) + 70f, 30f * Mathf.pow(sizeScl, 1f / 1.5f) * pow2Out.apply(e.fin()) + 4f, 16, 0.015f);
        }),

        desNukeShockwave = new Effect(190f, 1900f * 2f, e -> {
            float size = e.rotation;

            Draw.color(Color.white, 0.333f * e.fout());
            Lines.stroke((size / 15f) + (size / 5f) * e.fin());
            Lines.circle(e.x, e.y, size / 3f + size * pow2Out.apply(e.fin()) * 2f);
        }).layer(Layer.groundUnit + 1f),

        desNuke = new Effect(80f, 500f * 2, e -> {
            if(!(e.data instanceof float[] arr)) return;
            float size = e.rotation;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            float scl = 1f;
            Tmp.c2.set(Color.gray).a(0.8f);
            for(int k = 0; k < 6; k++){
                float cf = k / 5f;
                color(Tmp.c2, Pal.lightOrange, cf);
                for(int i = 0; i < 40; i++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float len = r.random(size * scl * 0.75f) * pow5Out.apply(f) + r.random(size / 5f);
                    float ang = r.random(360f);
                    float psize = size / 5f;
                    float rad = r.random(psize * (scl * 0.5f + 0.5f) * 0.87f, psize) * scl * (1f - pow5In.apply(f));
                    if(f < 1f){
                        Tmp.v1.trns(ang, len).add(e.x, e.y);
                        Fill.circle(Tmp.v1.x, Tmp.v1.y, rad);
                    }
                }
                scl *= 0.75f;
            }
            scl = 1f;
            color(Pal.lighterOrange);
            Lines.stroke(3f);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 20; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float ang = r.random(360f);
                    float len = r.random(size * scl * 0.5f) * pow5Out.apply(f) + r.random(size / 5f);
                    float line = r.random(22f, 45f) * Mathf.pow(scl, 1.1f) * pow2Out.apply(Mathf.slope(pow5Out.apply(f)));

                    if(f < 1f){
                        Tmp.v1.trns(ang, len).add(e.x, e.y);
                        Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, ang, line, false);
                    }
                }
                scl *= 1.4f;
            }

            float fin = Mathf.clamp(e.time / 10f);
            if(fin < 1){
                Tmp.c2.set(Pal.lightOrange).a(0f);
                color(Pal.lighterOrange, Tmp.c2, fin);
                for(int i = 0; i < arr.length; i++){
                    float len1 = arr[i], len2 = arr[(i + 1) % arr.length];
                    float ang1 = (i / (float)arr.length) * 360f;
                    float ang2 = ((i + 1f) / arr.length) * 360f;

                    if(len1 >= size){
                        len1 += (size / 1.5f) * fin;
                    }
                    if(len2 >= size){
                        len2 += (size / 1.5f) * fin;
                    }

                    float x1 = Mathf.cosDeg(ang1) * len1, y1 = Mathf.sinDeg(ang1) * len1;
                    float x2 = Mathf.cosDeg(ang2) * len2, y2 = Mathf.sinDeg(ang2) * len2;

                    Fill.tri(e.x, e.y, e.x + x1, e.y + y1, e.x + x2, e.y + y2);
                }
            }
        }),

        desNukeShoot = new Effect(35f, e -> {
            float ang = 90f, len = 1f;
            Rand r = Utils.rand;
            r.setSeed(e.id);

            //Draw.color(ProximaPal.red, Color.white, e.fin());
            Lines.stroke(2f);
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rot = e.rotation + r.range(ang);
                    Draw.color(KPal.red, Color.white, f);
                    Vec2 v = Tmp.v1.trns(rot, r.random(40f) * pow2Out.apply(f) * len).add(e.x, e.y);
                    Lines.lineAngle(v.x, v.y, rot, f * 40f * r.random(0.75f, 1f) * len * pow2Out.apply(Mathf.slope(f)), false);
                }
                ang *= 0.5f;
                len *= 1.4f;
            }
        }),

        desNukeVaporize = new Effect(40f, 1200f, e -> {
            float size = e.data instanceof Float ? (float)e.data : 10f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            int count = 20 + (int)(size * size * 0.5f);
            float c = 0.25f;
            for(int i = 0; i < count; i++){
                float l = r.nextFloat() * c;
                float f = Mathf.curve(e.fin(), l, ((1f - c) + l) * r.random(0.8f, 1f));
                float len = r.random(0.5f, 1f) * (80f + size * 10f) * pow2In.apply(f);
                float off = Mathf.sqrt(r.nextFloat()) * size, ang = r.random(360f), rng = r.range(10f);
                float scl = (size / 2f) * r.random(0.9f, 1.1f) * Utils.biasSlope(f, 0.1f);

                if(f > 0 && f < 1){
                    Vec2 v1 = Tmp.v1.trns(ang, off).add(e.x, e.y).add(Tmp.v2.trns(e.rotation + rng, len));
                    Draw.color(Pal.lightOrange, Pal.rubble, pow3Out.apply(f));
                    Fill.circle(v1.x, v1.y, scl);
                }
            }
        }).layer(Layer.flyingUnit),

        desNukeShockSmoke = new Effect(40f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            int count = 10;
            float c = 0.4f;
            for(int i = 0; i < count; i++){
                float l = r.nextFloat() * c;
                float f = Mathf.curve(e.fin(), l, ((1f - c) + l) * r.random(0.8f, 1f));
                float len = r.random(0.75f, 1f) * 160f * pow2In.apply(f);
                float off = Mathf.sqrt(r.nextFloat()) * Vars.tilesize / 2f, ang = r.random(360f), rng = r.range(10f);
                float scl = r.random(4f, 6f) * (1f - pow2In.apply(f));

                if(f > 0 && f < 1){
                    Vec2 v1 = Tmp.v1.trns(ang, off).add(e.x, e.y).add(Tmp.v2.trns(e.rotation + rng, len));
                    color(Pal.rubble, Color.gray, f);
                    Fill.circle(v1.x, v1.y, scl);
                }
            }
        }),

        desMissileHit = new Effect(50f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            Tmp.c2.set(Color.gray).a(0.8f);
            //Tmp.c3.set(KPal.red).mul(2f);
            float scl1 = Mathf.clamp(e.time / 3f);
            float scl3 = 1.1f;
            float angScl = 0.6f;
            for(int i = 0; i < 4; i++){
                float scl2 = 1f;
                float len = 1f;
                float ang = 180f;

                //Draw.color(Tmp.c2, Pal.lightOrange, i / 3f);
                color(Tmp.c2, KPal.red, i / 3f);
                for(int j = 0; j < 5; j++){
                    for(int k = 0; k < 9; k++){
                        float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                        float rot = e.rotation + r.range(ang);
                        //float ll = r.random(45f) * len * pow10Out.apply(f) * scl1;
                        float ll = r.random(45f) * len * pow5Out.apply(f);
                        float scl = r.random(0.666f, 1f) * scl2 * scl1 * 18f * (1f - pow10In.apply(f));

                        Vec2 v = Tmp.v1.trns(rot, ll).add(e.x, e.y);
                        Fill.circle(v.x, v.y, scl);
                    }

                    ang *= angScl;
                    len *= 1.5f;
                    scl2 *= scl3;
                }
                scl1 *= 0.9f;
                angScl *= 0.8f;
                scl3 *= 0.9f;
            }
            color(KPal.red);
            scl1 = 1f;
            scl3 = 1f;
            angScl = 180f;
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 6; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                    float rot = e.rotation + r.range(angScl);
                    float ll = r.random(20f) * scl3 * pow2Out.apply(f);
                    float size = r.random(5f, 10f);
                    float wid = size * scl1 * Utils.biasSlope(f, 0.2f);
                    float len = wid * 3f + size * 7f * Mathf.pow(scl1, 1.2f) * pow5Out.apply(f);

                    Vec2 v = Tmp.v1.trns(rot, wid * 2f + ll).add(e.x, e.y);
                    Drawf.tri(v.x, v.y, wid, len, rot);
                    Drawf.tri(v.x, v.y, wid, wid * 3f, rot + 180f);
                }

                scl1 *= 1.2f;
                scl3 *= 1.5f;
                angScl *= 0.5f;
            }

            Draw.reset();
        });
        public static Effect nuclearcloud = new Effect(120f, 1500f, e -> {
            float size = e.rotation;
            if(size <= 0f) size = 200f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            // 主核云效果
            Draw.color(Pal.lightOrange, Pal.lighterOrange, Color.gray, e.fin());
            Draw.blend(Blending.additive);

            // 核心爆炸云
            float coreSize = size * (0.5f + e.fin() * 1.2f);
            Fill.circle(e.x, e.y, coreSize);

            Draw.blend();

            // 烟雾层
            Draw.color(Color.gray, Pal.rubble, e.fin());
            for(int i = 0; i < 60; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                float ang = r.random(360f);
                float len = r.random(size * 0.8f) * pow2Out.apply(f);
                float rad = r.random(15f, 45f) * (1f - pow3In.apply(f));

                if(f < 1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }

            // 热浪/火焰层
            Draw.color(Pal.lightOrange, Pal.lighterOrange, e.fin());
            for(int i = 0; i < 35; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.4f));
                float ang = r.random(360f);
                float len = r.random(size * 1.2f) * pow3Out.apply(f);
                float rad = r.random(8f, 28f) * (1f - pow2In.apply(f));

                if(f < 1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }

            // 冲击波环
            Draw.color(Color.white, 0.5f * e.fout());
            Lines.stroke(Math.max(2f, size / 25f) * e.fout());
            Lines.circle(e.x, e.y, size * (0.5f + pow2Out.apply(e.fin()) * 1.5f));

            // 外缘光芒
            Draw.color(Pal.lighterOrange, 0.6f * e.fout());
            Lines.stroke(Math.max(1f, size / 30f) * e.fout());
            Lines.circle(e.x, e.y, size * (0.6f + pow2Out.apply(e.fin()) * 1.8f));

            // 辐射粒子效果
            Draw.color(KPal.melt, Color.gray, e.fout());
            for(int i = 0; i < 80; i++){
                float f = Mathf.curve(e.fin(), 0f, r.random(0.6f, 1f));
                float ang = r.random(360f);
                float len = r.random(size * 1.5f) * pow4Out.apply(f);
                float rad = r.random(3f, 12f) * (1f - pow4In.apply(f));

                if(f < 1f && f > 0.01f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }

            // 上升烟雾柱
            Draw.color(Color.gray, Pal.rubble, e.fout());
            for(int i = 0; i < 40; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                float ang = e.rotation + r.range(30f);
                float len = r.random(size * 2f) * pow2Out.apply(f);
                float rad = r.random(10f, 35f) * (1f - pow2In.apply(f));

                if(f < 1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }
        }).layer(Layer.effect);
        // 原子弹爆炸效果
        public static Effect atomicBomb = new Effect(150f, 2000f, e -> {
            float size = e.rotation;
            if(size <= 0f) size = 300f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            // 超亮闪光
            Draw.color(Color.white);
            Draw.blend(Blending.additive);
            Fill.circle(e.x, e.y, size * 0.8f * e.fin());
            Draw.blend();

            // 蘑菇云核心
            Draw.color(Pal.lightOrange, Pal.lighterOrange, e.fin());
            float coreSize = size * (0.3f + e.fin() * 1.5f);
            Fill.circle(e.x, e.y, coreSize);

            // 多层爆炸环
            for(int layer = 0; layer < 3; layer++){
                float layerOffset = layer * 0.2f;
                float alpha = 0.6f * (1f - layerOffset) * e.fout();
                Draw.color(Pal.lightOrange, alpha);
                Lines.stroke(Math.max(2f, size / 20f) * e.fout());
                Lines.circle(e.x, e.y, size * (0.4f + layer * 0.3f + pow2Out.apply(e.fin()) * 2f));
            }

            // 冲击波
            Draw.color(Color.white, 0.4f * e.fout());
            Lines.stroke(size / 15f * e.fout());
            Lines.circle(e.x, e.y, size * (0.6f + pow3Out.apply(e.fin()) * 3f));

            // 碎片粒子
            for(int i = 0; i < 150; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                float ang = r.random(360f);
                float len = r.random(size * 2.5f) * pow2Out.apply(f);
                float rad = r.random(5f, 20f) * (1f - pow3In.apply(f));

                if(f < 1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Draw.color(Pal.lightOrange, Color.gray, f);
                    Fill.circle(v.x, v.y, rad);
                }
            }
        }).layer(Layer.effect);

        // 辐射云效果
        public static Effect radiationCloud = new Effect(180f, 800f, e -> {
            float size = e.rotation;
            if(size <= 0f) size = 150f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            Draw.color(Color.green, 0.4f * e.fout());
            Draw.blend(Blending.additive);

            // 辐射云主体
            for(int i = 0; i < 50; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.4f));
                float ang = r.random(360f);
                float len = r.random(size) * pow3Out.apply(f);
                float rad = r.random(8f, 25f) * (1f - pow2In.apply(f));

                if(f < 1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }

            Draw.blend();

            // 辐射粒子和火花
            Draw.color(Color.yellow, Color.green, e.fin());
            for(int i = 0; i < 60; i++){
                float f = Mathf.curve(e.fin(), 0f, r.random(0.7f, 1f));
                float ang = r.random(360f);
                float len = r.random(size * 1.2f) * pow4Out.apply(f);
                float rad = r.random(2f, 8f) * (1f - pow4In.apply(f));

                if(f < 1f && f > 0.05f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }
        }).layer(Layer.effect);

        // 放射性污染效果
        public static Effect radioactiveContamination = new Effect(240f, 600f, e -> {
            float size = e.rotation;
            if(size <= 0f) size = 100f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            Draw.color(Color.yellow, Color.green, 0.3f * e.fout());

            // 污染粒子云
            for(int i = 0; i < 80; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.5f));
                float ang = r.random(360f);
                float len = r.random(size * 1.5f) * pow2Out.apply(f);
                float rad = r.random(3f, 15f) * (1f - pow2In.apply(f));

                if(f < 1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }

            // 放射性闪烁粒子
            Draw.color(Color.yellow, 0.6f * (1f - e.fin()));
            for(int i = 0; i < 40; i++){
                float f = Mathf.curve(e.fin(), 0f, r.random(0.6f, 1f));
                float ang = r.random(360f);
                float len = r.random(size) * pow3Out.apply(f);
                float rad = r.random(1f, 5f) * (1f - pow3In.apply(f));

                if(f < 1f && f > 0.1f){
                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(v.x, v.y, rad);
                }
            }
        }).layer(Layer.debris);
    }

    public static class FlameFX{
        public static Effect shield = new Effect(30f, e -> {
            blend(Blending.additive);
            color(Tmp.c1.set(KPal.primary).a(Mathf.absin(e.fin(pow2Out), 1f / 50f, 1f) * 0.5f * e.fout()));

            Fill.polyBegin();
            for(int i = 0; i < 6; i++){
                float ang = i * (360f / 6f);
                Tmp.v1.trns(ang, 30f);
                Tmp.v1.y *= 0.333f;

                Vec2 v = Tmp.v2.trns(e.rotation + 90f, Tmp.v1.x, Tmp.v1.y).add(e.x, e.y);
                Fill.polyPoint(v.x, v.y);
            }
            Fill.polyEnd();

            blend();
        }),

        aoeExplosion2 = new Effect(80f, 500f, e -> {
            float z = z();
            z(z - 0.001f);

            Rand r = Utils.rand;
            r.setSeed(e.id * 31L);

            color(Color.gray);
            alpha(0.9f);
            for(int i = 0; i < 3; i++){
                float lenScl = r.random(0.4f, 1f);
                float time = Mathf.clamp(e.time / (e.lifetime * lenScl));

                float l = pow10Out.apply(time) * 100f;

                for(int j = 0; j < 4; j++){
                    float len = r.random(0.4f, 1f) * l;
                    float ang = r.random(360f);
                    float fout = Interp.pow5Out.apply(1 - time) * r.random(0.5f, 1f);

                    Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);
                    //Fill.circle(e.x + x, e.y + y, fout * ((2f + intensity) * 1.8f));
                    Fill.circle(v.x, v.y, fout * 60f);
                }
            }

            //color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
            //stroke((1.7f * e.fout()) * (1f + (intensity - 1f) / 2f));
            z(z);
            color(KPal.primary, Pal.lightOrange, Color.gray, e.fin());
            Lines.stroke(2.72f * e.fout());
            for(int i = 0; i < 8; i++){
                //float c = r.random(0.2f);
                float l = r.random(20f, 150f) * e.finpow() + 0.1f;
                float a = r.random(360f);
                Vec2 v = Tmp.v1.trns(a, l);
                //lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (3f + intensity));
                Lines.lineAngle(v.x + e.x, v.y + e.y, Mathf.angle(v.x, v.y), 1f + e.fout() * 12f);
                //Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
                Drawf.light(e.x + v.x, e.y + v.y, 11f * e.fout(), Draw.getColor(), 0.8f);
            }

            color(Color.white);
            if(e.time < 3f){
                Fill.circle(e.x, e.y, e.rotation);
                Drawf.light(e.x, e.y, e.rotation * 2.5f, Color.white, 0.9f);
            }
        }),

        apathyCrit = new Effect(80f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id * 31L);
            for(int i = 0; i < 45; i++){
                //
                float offd = 0.4f;

                float ra = pow3Out.apply(r.random(1f)) / 2f + 0.5f;

                float in = (i / 45f) * ra * (1 - offd);

                //float of = r.random(1f - offd);
                //float time = Mathf.curve(e.fin(), of, of + offd);
                float time = Mathf.curve(e.fin(), in, in + offd);
                float angle = r.random(360f);
                float length = r.random(15f, 135f);
                float size = r.random(12f, 25f);

                if(time <= 0 || time >= 1) continue;

                Vec2 v = Tmp.v1.trns(angle, length * pow2In.apply(time)).add(e.x, e.y);
                color(KPal.primary, KPal.blood, pow2.apply(time));
                Fill.circle(v.x, v.y, size * pow2Out.apply(slope.apply(pow2In.apply(time))));
            }

        }).layer(Layer.flyingUnit + 0.01f),
        apathyBleed = new Effect(15f, e -> {
            //Draw.color(FlamePalettes.primary, FlamePalettes.blood, pow2Out.apply(e.fin()));
            color(KPal.blood);
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float minRange = e.color.r;
            float maxRange = e.color.g;

            for(int i = 0; i < 6; i++){
                float angle = e.rotation + pow2In.apply(r.nextFloat()) * (r.chance(0.5f) ? -1f : 1f) * 15f;
                float len = r.random(minRange, maxRange) * e.fin(pow2Out);
                float s = r.random(6f, 10f) * pow3Out.apply(e.fout());

                Tmp.v1.trns(angle, len).add(e.x, e.y);
                Fill.circle(Tmp.v1.x, Tmp.v1.y, s);
            }
        }).rotWithParent(true).layer(Layer.flyingUnit + 0.01f),
        apathyDeath = new Effect(30f, e -> {
            color(KPal.blood);
            Rand r = Utils.rand;
            r.setSeed(e.id);

            Fill.circle(e.x, e.y, (1f - Mathf.curve(e.fin(), 0f, 0.4f)) * e.rotation * 2f);

            for(int i = 0; i < 70; i++){
                float fin = Mathf.curve(e.fin(), r.random(0.1f), 1 - r.random(0.5f));
                float angle = r.random(360f);
                float length = r.random(220f, 460f);
                float size = r.random(9f, 15f) * pow2Out.apply(Utils.biasSlope(fin, 0.1f));
                float offset = r.random(e.rotation);

                if(fin > 0f && fin < 1f){
                    Tmp.v1.trns(angle, offset + length * pow3Out.apply(fin)).add(e.x, e.y);
                    GraphicUtils.tri(Tmp.v1.x, Tmp.v1.y, e.x, e.y, size, angle);
                    Drawf.tri(Tmp.v1.x, Tmp.v1.y, size, size * 2f, angle);
                }
            }
        }),

        bigLaserCharge = new Effect(120f, e -> {
            color();
            float scl = (1f + Mathf.absin(e.fin(pow2In), 1f / 100f, 1f)) * 180f * e.fin();

            for(int i = 0; i < 4; i++){
                float a = (360 / 4f) * i + 45f;

                Drawf.tri(e.x, e.y, (scl + 5) / 8f, scl, a);
            }
        }).layer(Layer.flyingUnit + 0.01f),
        bigLaserFlash = new Effect(8f, e -> {
            //
            color();
            float scl = 180f + 280f * e.finpow();

            for(int i = 0; i < 4; i++){
                float a = (360 / 4f) * i + 45f;

                Drawf.tri(e.x, e.y, 40 * pow3Out.apply(e.fout()), scl, a);
            }
        }).layer(Layer.flyingUnit + 0.01f),
        bigLaserHitSpark = new Effect(15f, e -> {
            color(Color.white, KPal.primary, e.fin());
            Lines.stroke(e.fout() * 1.2f + 0.5f);

            Angles.randLenVectors(e.id, 8, 87f * e.fin(), e.rotation, 45f, (x, y) -> {
                Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 9f + 0.5f);
            });

            Rand r = Utils.rand;
            r.setSeed(e.id + 642);
            float c = 0.4f;
            for(int i = 0; i < 6; i++){
                float id = i / 5f;
                float f = Mathf.curve(e.fin(), c * id, c * id + (1 - c));
                float ang = e.rotation + r.range(60f);
                float len = r.random(57f, 92f) * pow2Out.apply(f);
                float size = r.random(5f, 9f) * (1 - f);
                if(f > 0.001f){
                    color(Color.white, KPal.primary, f);
                    Vec2 v = Tmp.v1.trns(ang, len);

                    Fill.poly(e.x + v.x / 2, e.y + v.y / 2, 4, size / 2);
                    Fill.poly(e.x + v.x, e.y + v.y, 4, size);
                }
            }
        }),
        bigLaserHit = new Effect(30f, e -> {
            color(Color.white, KPal.primary, Color.gray, pow2Out.apply(e.fin()));

            //float size = e.data instanceof Float ? ((float)e.data) / 2f : 50f;
            float size = (e.data instanceof Float ? ((float)e.data) : (e.data instanceof Sized s ? s.hitSize() : 50f)) * 1.25f;

            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float w = r.range(size);
                float l = r.random(180f, 310f);
                float s = r.random(8f, 30f);

                float ic = i / 15f;
                float c = 0.3f;
                float f = Mathf.curve(e.fin(), ic * c, (ic * c) + (1 - c));

                if(f >= 0.0001f && f < 1f){
                    Vec2 v = Tmp.v1.trns(e.rotation, l * pow3In.apply(f), w * circleOut.apply(pow3In.apply(f))).add(e.x, e.y);
                    Fill.circle(v.x, v.y, s * (1 - (f * f)));
                }
            }
        }),

        rejectedRegion = new Effect(15f, 600f, e -> {
            if(!(e.data instanceof CutBatch.RejectedRegion r)) return;
            float z = Draw.z();
            Draw.z(r.z);
            Draw.color(e.color, e.fout() * e.color.a);
            Draw.blend(r.blend);

            Draw.rect(r.region, e.x, e.y, r.width, r.height, e.rotation);

            Draw.blend();
            Draw.z(z);
        }),

        shootShockWave = new Effect(35f, 600f, e -> {
            //GraphicUtils.drawShockWave(e.x, e.y, 75f, 0f, -e.rotation - 90f, 200f, 4f, 12);
            color(Color.white);
            alpha(0.666f * e.fout());

            float size = e.data instanceof Float ? (float)e.data : 200f;
            float nsize = size - 10f;

            GraphicUtils.drawShockWave(e.x, e.y, -75f, 0f, -e.rotation - 90f, nsize * e.finpow() + 10, 16f * e.finpow() + 4f, 16, 1f);
        }).layer((Layer.bullet + Layer.effect) / 2),

        fragmentGroundImpact = new Effect(40f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(e.color);

            float size = e.rotation;
            int iter = ((int)(size / 8f)) + 6;
            for(int i = 0; i < iter; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size) + (r.random(0.5f, 1f) * size * 0.5f + 20f) * e.finpow()).add(e.x, e.y);
                Fill.circle(v.x, v.y, r.random(5f, 16f) * e.fout());
            }
        }).layer(Layer.debris),
        fragmentExplosion = new Effect(40f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float size = e.rotation;
            e.lifetime = size / 1.5f + 10f;

            int iter = ((int)(size / 7f)) + 12;
            int iter3 = ((int)(size / 14.5f)) + 12;
            color(Color.gray);
            //alpha(0.9f);
            for(int i = 0; i < iter3; i++){
                //
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size / 2f) * e.finpow());
                float s = r.random(size / 2.75f, size / 2f) * e.fout();
                Fill.circle(v.x + e.x, v.y + e.y, s);
            }
            for(int i = 0; i < iter; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size) + (r.random(0.25f, 2f) * size) * e.finpow());
                float s = r.random(size / 3.5f, size / 2.5f) * e.fout();
                Fill.circle(v.x + e.x, v.y + e.y, s);
                Fill.circle(v.x / 2 + e.x, v.y / 2 + e.y, s * 0.5f);
            }

            float sfin = Mathf.curve(e.fin(), 0f, 0.65f);
            if(sfin < 1f){
                int iter2 = ((int)(size / 10f)) + 4;
                float sfout = 1f - sfin;

                color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
                Lines.stroke((1.7f * sfout) * (1f + size / 60f));

                Draw.z(Layer.effect + 0.001f);

                for(int i = 0; i < iter2; i++){
                    Vec2 v = Tmp.v1.trns(r.random(360f), r.random(0.001f, size / 2f) + (r.random(0.4f, 2.2f) * size) * pow2Out.apply(sfin));
                    Lines.lineAngle(e.x + v.x, e.y + v.y, Mathf.angle(v.x, v.y), 1f + sfout * 3 * (1f + size / 50f));
                }
            }
        }),

        fragmentExplosionSmoke = new Effect(40f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float size = e.rotation;

            e.lifetime = size / 1.5f + 10f;

            int iter = ((int)(size / 7f)) + 12;
            int iter3 = ((int)(size / 14.5f)) + 12;
            color(Color.gray);
            for(int i = 0; i < iter3; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size / 2f) * e.finpow());
                float s = r.random(size / 2.75f, size / 2f) * e.fout();
                Fill.circle(v.x + e.x, v.y + e.y, s);
            }
            for(int i = 0; i < iter; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(size) + (r.random(0.25f, 2f) * size) * e.finpow());
                float s = r.random(size / 3.5f, size / 2.5f) * e.fout();
                Fill.circle(v.x + e.x, v.y + e.y, s);
                Fill.circle(v.x / 2 + e.x, v.y / 2 + e.y, s * 0.5f);
            }
        }),

        fragmentExplosionSpark = new Effect(26f, 300f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            float size = e.rotation;
            e.lifetime = size / 1.5f + 10f;

            float sfin = e.fin();

            int iter2 = ((int)(size / 12f)) + 3;
            float sfout = 1f - sfin;

            color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
            Lines.stroke((1.7f * sfout) * (1f + size / 60f));

            Draw.z(Layer.effect + 0.001f);

            for(int i = 0; i < iter2; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(0.001f, size / 2f) + (r.random(0.4f, 2.2f) * size) * pow2Out.apply(sfin));
                Lines.lineAngle(e.x + v.x, e.y + v.y, Mathf.angle(v.x, v.y), 1f + sfout * 3 * (1f + size / 50f));
            }
        }),

        destroySparks = new Effect(40f, 1200f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id + 64331);
            float size = (float)e.data;
            int isize = (int)(size * 1.75f) + 12;
            int isize2 = (int)(size * 1.5f) + 9;

            float fin1 = Mathf.clamp(e.time / 20f);
            float fin2 = Mathf.clamp(e.time / 10f);

            Lines.stroke(Math.max(2f, Mathf.sqrt(size) / 8f));
            for(int i = 0; i < isize2; i++){
                float f = Mathf.curve(fin1, 0f, r.random(0.8f, 1f));
                Vec2 v = Tmp.v1.trns(r.random(360f), 1f + (size * r.nextFloat() + 10f) * 1.5f * pow3Out.apply(f));
                float rsize = r.random(0.5f, 1.5f);
                if(f < 1){
                    color(KPal.paleYellow, Pal.lightOrange, Color.gray, f);
                    Lines.lineAngle(v.x + e.x, v.y + e.y, v.angle(), (size / 5f) * rsize * (1 - f));
                }
            }
            for(int i = 0; i < isize; i++){
                float f = Mathf.curve(e.fin(), 0f, r.random(0.5f, 1f));
                float re = Mathf.pow(r.nextFloat(), 1.5f);
                float ang = re * 90f * (r.nextFloat() > 0.5f ? 1 : -1);
                //float dst = (1f - Math.abs(ang / 90f) / 1.5f) * (50f + size * 3f * r.nextFloat()) * pow3Out.apply(f);
                float dst = (50f + ((size * 3f) / (1f + re / 5f)) * Mathf.pow(r.nextFloat(), (1f + re / 2f))) * Interp.pow3Out.apply(f);
                Vec2 v = Tmp.v1.trns(e.rotation + ang, 1f + dst);
                float rsize = r.random(0.75f, 1.5f);

                if(f < 1){
                    color(KPal.paleYellow, Pal.lightOrange, Color.gray, pow2In.apply(f));
                    Lines.lineAngle(v.x + e.x, v.y + e.y, v.angle(), (size / 3f) * rsize * (1 - f));
                }
            }

            color(KPal.paleYellow);
            for(int i = 0; i < 4; i++){
                float rot = i * 90f;
                Drawf.tri(e.x, e.y, (size / 2.5f) * (1 - fin2), size + size * fin2 * 1f, rot);
            }
        }).layer(Layer.effect + 0.005f),
        debrisSmoke = new Effect(40f, e -> {
            color(Color.gray);
            float fin = Utils.biasSlope(e.fin(), 0.075f);
            Fill.circle(e.x, e.y, e.rotation * fin);
        }),
        heavyDebris = new Effect(4f * 60f, 1200f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id + 644331);
            float size = (float)e.data;
            float sizeTime = (size) + 15f;
            int isize = (int)(size * 1.75f) + 12;

            float fin = Mathf.clamp(e.time / sizeTime);
            float fout = Mathf.clamp((e.lifetime - e.time) / 60f);
            Lines.stroke(3f);
            for(int i = 0; i < isize; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), Mathf.sqrt(r.nextFloat()) * size * 0.75f).add(e.x, e.y);
                float f = Mathf.curve(fin, 0f, r.random(0.5f, 1f));
                float angle = Mathf.pow(r.nextFloat(), 1.25f) * (r.random(1f) < 0.5f ? -1f : 1f) * 60f;
                //float angle = r.range(35f);
                float dst = r.random((220f + size * 4.5f) * pow3Out.apply(f)) * (1 - Math.abs(angle / 60f) / 1.5f);
                float s = r.chance(0.25f) ? (size / 3f) * r.random(0.5f, 1f) : Math.min(r.random(5f, 9f), size / 4f);
                float rrot = r.random(360f);
                int sides = r.random(3, 6);
                Vec2 v2 = Tmp.v2.trns(angle + e.rotation, dst);

                Draw.color(Tmp.c1.set(e.color).mul(r.random(0.9f, 1.2f)).a(fout));

                if(r.chance(0.75f)){
                    Fill.poly(v.x + v2.x, v.y + v2.y, sides, s, rrot);
                }else{
                    Lines.poly(v.x + v2.x, v.y + v2.y, sides, s, rrot);
                }
            }

        }).layer(Layer.debris - 0.01f),
        simpleFragmentation = new Effect(30f, e -> {
            if(!(e.data instanceof TextureRegion region)) return;
            float bounds = Math.min(region.width, region.height);
            float b2 = bounds / 4f;
            float bw = b2 / region.texture.width;
            float bh = b2 / region.texture.height;
            float bscl = bounds * scl;
            int ib = (int)(bscl * 1.5f) + 8;
            Rand r = Utils.rand;
            r.setSeed(e.id + 46241);

            Draw.color(e.color);
            for(int i = 0; i < ib; i++){
                float u = Mathf.lerp(region.u, (region.u2 - bw), r.nextFloat());
                float v = Mathf.lerp(region.v, (region.v2 - bh), r.nextFloat());
                float u2 = u + bw;
                float v2 = v + bh;

                TextureRegion tr = Tmp.tr1;
                tr.texture = region.texture;
                tr.set(u, v, u2, v2);

                float f = Mathf.curve(e.fin(), 0f, r.random(0.8f, 1f));

                Vec2 base = Tmp.v1.trns(r.random(360f), bscl / 2f).add(e.x, e.y);
                Vec2 off = Tmp.v2.trns(e.rotation + r.range(30f), 120f * r.nextFloat() * pow2Out.apply(f));

                float rrot = r.random(360f) + r.range(180f) * f;

                if(f < 1){
                    Draw.alpha(1f - Mathf.curve(f, 0.8f, 1f));
                    Draw.rect(tr, base.x + off.x, base.y + off.y, rrot);
                }
            }
        }).layer(Layer.flyingUnitLow),

        empathyTrail = new Effect(20f, e -> {
            TextureRegion r = KUnitTypes.Rody.region;
            color(Color.white);
            alpha(0.75f * e.fout());
            mixcol(KPal.empathyAdd, 1f);
            blend(Blending.additive);
            Draw.rect(r, e.x, e.y, e.rotation - 90f);
            blend();

            e.lifetime = 20f + e.color.r;
        }),

        empathyDecoyDestroy = new Effect(90f, 700f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(KPal.empathy);
            Lines.stroke(12f * e.fout());
            Lines.circle(e.x, e.y, 6f + 160f * e.fin());

            Lines.stroke(2f * e.fout());
            for(int i = 0; i < 10; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), r.random(45f, 230f) * e.finpow()).add(e.x, e.y);

                Lines.line(e.x, e.y, v.x, v.y, false);
                Fill.poly(v.x, v.y, 4, 3f * e.fout());
            }
        }),

        empathyParry = new Effect(8f, e -> {
            color();
            float scl = 20f + 30f * e.finpow();

            for(int i = 0; i < 4; i++){
                float a = (360 / 4f) * i;

                Drawf.tri(e.x, e.y, 8 * pow3Out.apply(e.fout()), scl, a);
            }
        }).layer(Layer.flyingUnit + 0.01f),
        empathyParryExplosion = new Effect(40f, e -> {
            color(KPal.empathyDark, e.fout());
            blend(Blending.additive);
            float r = pow3Out.apply(Mathf.clamp(e.time / 6f)) * e.rotation + e.finpow() * 10f;
            Fill.circle(e.x, e.y, r);
            blend();
        }).layer(Layer.flyingUnitLow + 1f),

        empathyPrimeStrike = new Effect(40f, 300f, e -> {
            Rand rand = Utils.rand;
            rand.setSeed(e.id + 45245);
            float rrot = 90f + (rand.random(15f, 180f - 15f) * (rand.nextFloat() >= 0.5f ? 1 : -1));
            float exLength = rand.random(8f, 25f);

            Tmp.c1.set(KPal.empathyAdd).a(Mathf.clamp((e.lifetime - e.time) / 30f));
            color(Tmp.c1);
            blend(Blending.additive);

            float fin = Mathf.clamp(e.time / 5f);
            GraphicUtils.draw3D(e.x, e.y, rand.range(40f), rrot, -e.rotation + 90f, fs -> {
                for(int i = 0; i < 16; i++){
                    float f1 = (i / 16f);
                    float f2 = ((i + 1) / 16f);

                    float rot = f1 * 180f * fin;
                    float nrot = f2 * 180f * fin;
                    float width1 = f1 * 17f;
                    float width2 = f2 * 17f;

                    //float ex1 = Mathf.slope(f1) * 10f;
                    //float ex2 = Mathf.slope(f2) * 10f;

                    for(int j = 0; j < 2; j++){
                        float r = j == 0 ? rot : nrot;
                        float w = j == 0 ? width1 : width2;
                        float ex = pow2Out.apply(Mathf.slope(j == 0 ? f1 : f2)) * exLength;
                        for(int k = 0; k < 2; k++){
                            int sign = j == 0 ? k : 1 - k;
                            Vec2 v = Tmp.v1.trns(r, 30f + w * -sign).add(0, ex);
                            fs.add(v.x, v.y);
                        }
                    }
                }
            });
            blend();
        }),
        empathyDashShockwave = new Effect(10f, 300f, e -> {
            color(Color.white);
            alpha(0.666f * e.fout());

            float size = 60f;
            float nsize = size - 15f;

            GraphicUtils.drawShockWave(e.x, e.y, -75f, 0f, -e.rotation - 90f, nsize * e.finpow() + 15, 30f * e.finpow() + 4f, 16, 1f);
        }),
        empathyDashDust = new Effect(3f * 60f, 150, e -> {
            float fin = Mathf.clamp(e.time / 25f);
            float fout = Mathf.clamp((e.lifetime - e.time) / 60f);

            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(e.color);
            for(int i = 0; i < 3; i++){
                int sign = Mathf.sign(r.nextBoolean());
                float size = r.random(3f, 7f);
                //Vec2 v = Tmp.v1.trns(e.rotation + 90f * sign + r.range(25f), r.random(45f) * pow2Out.apply(fin)).add(e.x, e.y);
                Vec2 v = Tmp.v1.trns(e.rotation + 90f * sign + r.range(25f), Mathf.pow(r.nextFloat(), 1.75f) * 45f * pow2Out.apply(fin)).add(e.x, e.y);
                Fill.circle(v.x, v.y, size * fout);
            }

        }).layer(Layer.scorch + 5f),
        empathyPrimeShockwave = new Effect(40f, 450f, e -> {
            color(Color.white);
            alpha(0.666f * e.fout());
            Rand r = Utils.rand;
            r.setSeed(e.id);

            float size = 200f;
            float nsize = size - 15f;

            GraphicUtils.drawShockWave(e.x, e.y, 90f - r.random(5f, 15f), 0f, -e.rotation - 90f, nsize * e.finpow() + 15, 30f * e.finpow() + 5f, 16, 1f);
        }).layer(Layer.flyingUnit),

        empathyShotgun = new Effect(6, 1200f, e -> {
            if(!(e.data instanceof Float)) return;
            Draw.color(KPal.empathy);
            float l = (float)e.data;
            e.lifetime = Math.max(l / (500f / 6f), 2f);
            Tmp.v1.trns(e.rotation, l).add(e.x, e.y);
            Lines.stroke(2f);
            Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, e.rotation + 180f, l * e.fout());
        }),

        empathyRico = new Effect(30f, 4000f, e -> {
            if(!(e.data instanceof Float)) return;
            Draw.color(KPal.empathy);
            float l = (float)e.data;
            Lines.stroke(4f * e.fout());
            Lines.lineAngle(e.x, e.y, e.rotation, l);
        }),

        empathyLightningHit = new Effect(14f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(Color.white, KPal.empathy, e.fin());
            Lines.stroke(0.5f + e.fout());
            for(int i = 0; i < 7; i++){
                float rot = e.rotation + r.range(35f);
                float len = r.random(20f) * e.fin();
                Tmp.v1.trns(rot, len).add(e.x, e.y);
                Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, rot, 4.5f * e.fout() + 1f);
            }
        }),

        empathyRendHit = new Effect(20f, 150f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(Color.white, KPal.empathy, e.fin());
            Lines.stroke(4f * e.fout());
            for(int i = 0; i < 8; i++){
                float rot = e.rotation + r.range(5f);
                float len = r.random(140f) * e.finpow();
                float ll = r.random(10f, 25f) * e.finpow();

                Tmp.v1.trns(rot, len).add(e.x, e.y);
                Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, rot, ll);
            }
        }),
        empathyRend = new Effect(60f, 600f, e -> {
            //Draw.color(Color.white, e.fout());
            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 8; i++){
                float f = i / 7f;
                float a = 0.5f;
                float scl = r.random(0.5f, 1.3f);

                float fin = pow4Out.apply(Mathf.curve(e.fin(), a * f, (1f - a) + (a * f)));
                float x = r.random(360f), y = r.random(360f), z = r.random(360f);

                if(fin <= 0.001f || fin >= 0.999f) continue;
                Draw.color(Color.white, (1f - fin) * 0.5f);
                GraphicUtils.drawShockWave(e.x, e.y, x, y, z, 180f * scl * fin + 10, 16f * fin + 8f, 16, 1f);
            }
        }).layer(Layer.flyingUnit + 0.01f),

        empathyBlast = new Effect(60f, 900f, e -> {
            Draw.color(Color.white, KPal.empathyAdd, pow2Out.apply(e.fin()));
            Draw.alpha(pow2In.apply(e.fout()));
            //Draw.color(KPal.empathyAdd, e.fout());
            Draw.blend(Blending.additive);

            float size = e.rotation;
            Fill.circle(e.x, e.y, (size * pow10Out.apply(e.fin())) + (size * 0.1f * e.fin()));

            Draw.blend();
        }),

        empathySquareDespawn = new Effect(60f, 280f, e -> {
            float size = 120f;

            Draw.color(KPal.empathy);
            Lines.stroke(4f * e.fout());
            Lines.poly(e.x, e.y, 4, size, e.rotation + 45f);

            Fill.poly(e.x, e.y, 4, size * Mathf.curve(e.fout(), 0.85f, 1f), e.rotation + 45f);
            Draw.color();
        }).layer(Layer.flyingUnit),

        empathyDualDespawn = new Effect(15f, e -> {
            Draw.color(e.color);
            Angles.randLenVectors(e.id, 7, 17f * e.finpow(), (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4f * e.rotation));
            Fill.circle(e.x, e.y, e.fout() * 16f * e.rotation);
        }),

        empathyBigLaserHit = new Effect(30f, e -> {
            color(Color.white, KPal.empathy, Color.gray, pow2Out.apply(e.fin()));

            //float size = e.data instanceof Float ? ((float)e.data) / 2f : 50f;
            float size = (e.data instanceof Float ? ((float)e.data) : (e.data instanceof Sized s ? s.hitSize() : 50f)) * 1.25f;

            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float w = r.range(size);
                float l = r.random(180f, 310f);
                float s = r.random(8f, 30f);

                float ic = i / 15f;
                float c = 0.3f;
                float f = Mathf.curve(e.fin(), ic * c, (ic * c) + (1 - c));

                if(f >= 0.0001f && f < 1f){
                    Vec2 v = Tmp.v1.trns(e.rotation, l * pow3In.apply(f), w * circleOut.apply(pow3In.apply(f))).add(e.x, e.y);
                    Fill.circle(v.x, v.y, s * (1 - (f * f)));
                }
            }
        }),

        empathyDepowered = new Effect(40f, 1200f, e -> {
            float size = e.rotation;
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(KPal.red, KPal.empathyAdd, e.fin());
            blend(Blending.additive);
            Lines.stroke(2.4f * e.fout());
            Lines.circle(e.x, e.y, size + size * pow2Out.apply(e.fin()));
            for(int i = 0; i < 4; i++){
                //TODO statuseffect
                float ang = i * 90f + 45f;
                float sscl = size / 25f;
                TextureRegion region = GraphicUtils.getChain();
                float len = region.width * scl * sscl;

                for(int j = 0; j < 16; j++){
                    Tmp.v2.trns(ang + r.random(35f), (r.random(70f) + len * j) * pow3Out.apply(e.fin()));
                    Vec2 tr = Tmp.v1.trns(ang, size + len * j).add(e.x, e.y).add(Tmp.v2);
                    Draw.rect(region, tr.x, tr.y, region.width * scl * sscl * e.fout(), region.height * scl * sscl * e.fout(), ang + r.range(180f) * pow2Out.apply(e.fin()));
                }
            }
            blend();
        }),

        empathyRainbowHit = new Effect(30f, e -> {
            //float size = e.data instanceof Float ? ((float)e.data) / 2f : 50f;
            float size = (e.data instanceof Float ? ((float)e.data) : (e.data instanceof Sized s ? s.hitSize() : 50f)) * 1.25f;

            Rand r = Utils.rand;
            r.setSeed(e.id);
            for(int i = 0; i < 16; i++){
                float w = r.range(size);
                float l = r.random(180f, 310f);
                float s = r.random(8f, 15f);

                float ic = i / 15f;
                float c = 0.3f;
                float f = Mathf.curve(e.fin(), ic * c, (ic * c) + (1 - c));

                float time = f * 40f + Time.time;
                Draw.color(Tmp.c1.set(Color.red).shiftHue(time * 5f));

                if(f >= 0.0001f && f < 1f){
                    Vec2 v = Tmp.v1.trns(e.rotation, l * pow3In.apply(f), w * circleOut.apply(pow3In.apply(f))).add(e.x, e.y);
                    //Fill.circle(v.x, v.y, s * (1 - (f * f)));
                    Fill.poly(v.x, v.y, 4, s * (1 - (f * f)));
                }
            }
        }).layer(Layer.flyingUnit + 0.1f),

        endFlash = new Effect(15f, e -> {
            float f = pow2In.apply(Mathf.curve(e.fin(), 0f, 0.1f));
            float fo = Mathf.curve(e.fout(), 0.4f, 1f);
            float f2 = pow2Out.apply(Mathf.curve(e.fin(), 0.1f, 0.75f));
            float scl = e.rotation;

            Draw.color();
            for(int i = 0; i < 4; i++){
                float r = i * 90f;
                Drawf.tri(e.x, e.y, 5f * fo * scl, (5f + 120f * f) * fo * scl, r);
            }
            for(int i = 0; i < 2; i++){
                float r = i * 180f;
                Drawf.tri(e.x, e.y, 7f * e.fout() * scl, (7f + 310f * f2) * scl, r);
            }
        }).layer(Layer.flyingUnit + 0.1f),

        endDeath = new Effect(50f, 1000f, e -> {
            float fin1 = Mathf.curve(e.fin(), 0f, 0.65f);
            float size = e.rotation;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            e.lifetime = 50f + r.range(4f);

            int base = (int)((size * size) / 34f) + 2;
            int base2 = (int)((size * size) / 16f) + 4;

            //Draw.color(KPal.empathy);
            Draw.color(KPal.darkRed, KPal.empathy, Mathf.curve(pow2Out.apply(fin1), 0f, 0.5f));

            for(int i = 0; i < base; i++){
                Vec2 v = Tmp.v1.trns(r.random(360f), Mathf.sqrt(r.nextFloat()) * size + ((20f + size * 4f) * pow2Out.apply(fin1) * r.nextFloat()));
                float s = r.random(0.5f, 1.1f) * (size * 0.4f + 8f) * (1f - fin1);
                if(fin1 < 1f) Fill.circle(v.x + e.x, v.y + e.y, s);
            }
            Draw.color(KPal.darkRed, KPal.empathy, Mathf.curve(pow2Out.apply(e.fin()), 0f, 0.5f));
            for(int i = 0; i < base2; i++){
                float sin = Mathf.sin(r.random(7f, 11f), r.random(size * 2f)) * e.fin();
                Vec2 v = Tmp.v1.trns(r.random(360f), Mathf.sqrt(r.nextFloat()) * size + ((40f + size * 8f) * pow2In.apply(e.fin()) * r.nextFloat()), sin);
                float s = r.random(0.5f, 1.1f) * (size * 0.25f + 3f) * (1f - pow4In.apply(e.fin()));
                Fill.circle(v.x + e.x, v.y + e.y, s);
            }
        }),

        endSplash = new Effect(35f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);
            e.lifetime = 50f + r.range(16f);

            Draw.color(KPal.darkRed);
            int am = r.random(5, 9);
            for(int i = 0; i < am; i++){
                float of = 0.3f / (am - 1f);
                float c = Mathf.curve(e.fin(), of * i, (1 - 0.3f) + (of * i));
                float ang = r.range(40f) + e.rotation;
                float scl = r.random(0.6f, 1.4f) * 200f;
                float len = r.random(350f, 900f);

                if(c > 0.0001f && c < 0.9999f){
                    Tmp.v1.trns(ang, len *  pow2Out.apply(c)).add(e.x, e.y);
                    GraphicUtils.diamond(Tmp.v1.x, Tmp.v1.y, scl * 0.22f * (1f - pow3In.apply(c)), scl * pow3Out.apply(Mathf.curve(c, 0f, 0.5f)) + scl * 0.5f, ang);
                }
            }
        }).layer(Layer.darkness + 1f),

        coloredHit = new Effect(15f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            color(Color.white, KPal.red, e.fin());
            Lines.stroke(0.5f + e.fout());

            for(int i = 0; i < 8; i++){
                float ang = r.range(12f) + e.rotation;
                float len = r.random(40f) * e.fin();
                Vec2 v = Tmp.v1.trns(ang, len).add(e.x, e.y);

                Lines.lineAngle(v.x, v.y, ang, e.fout() * 8f + 1f);
            }
        }),

        desGroundHit = new Effect(30f, 250f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            int amount = r.random(4, 12);
            int amount2 = r.random(7, 14);
            float c = r.random(0.1f, 0.6f);
            float c2 = r.random(0.1f, 0.3f);

            z(Layer.groundUnit);
            color(Color.gray);
            for(int i = 0; i < amount2; i++){
                float l = (i / (amount2 - 1f)) * c2;
                float f = Mathf.curve(e.fin(), l, (1f - c2) + l);
                float ang = r.random(360f);
                float len = r.random(80f) * e.rotation;
                float scl = r.random(8.5f, 19f) * e.rotation;
                if(f > 0f && f < 1f){
                    float f2 = pow2Out.apply(f) * 0.6f + f * 0.4f;
                    Vec2 v = Tmp.v1.trns(ang, len * f2).add(e.x, e.y);
                    Fill.circle(v.x, v.y, scl * (1f - f));
                }
            }

            z(Layer.groundUnit + 0.02f);
            color(KPal.melt, e.color, pow3Out.apply(e.fin()));
            for(int i = 0; i < amount; i++){
                float l = (i / (amount - 1f)) * c;
                float f = Mathf.curve(e.fin(), l, (1f - c) + l);
                float ang = r.random(360f);
                float len = r.random(100f) * e.rotation;
                float scl = r.random(3f, 13f) * e.rotation;
                if(f > 0f && f < 1f){
                    float f2 = pow2Out.apply(f) * 0.4f + f * 0.6f;
                    Vec2 v = Tmp.v1.trns(ang, len * f2).add(e.x, e.y);
                    Fill.circle(v.x, v.y, scl * (1f - f));
                }
            }
        }).layer(Layer.groundUnit),

        desGroundHitMain = new Effect(90f, 900f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            float arange = 25f;
            float scl = 1f;
            float range = 300f;

            color(Color.gray, 0.8f);
            for(int i = 0; i < 4; i++){
                int count = r.random(15, 23);
                for(int k = 0; k < count; k++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rr = r.range(arange) + e.rotation;
                    float len = r.random(range) * pow4Out.apply(e.fin());
                    float sscl = r.random(21f, 43f) * scl * pow2.apply(1f - f) * Mathf.clamp(e.time / 8f);

                    if(f < 1){
                        Vec2 v = Tmp.v1.trns(rr, len).add(e.x, e.y);
                        Fill.circle(v.x, v.y, sscl);
                    }
                }

                arange *= 2f;
                scl *= 1.12f;
                range *= 0.6f;
            }
            float fin2 = Mathf.clamp(e.time / 18f);

            if(fin2 < 1){
                int count = 20;
                color(Pal.lighterOrange);
                for(int i = 0; i < count; i++){
                    float f = Mathf.curve(fin2, 0f, 1f - r.random(0.2f));
                    float ang = r.range(40f) + e.rotation;
                    float off = r.random(70f) + r.random(15f) * f;
                    float len = r.random(190f, 450f);

                    if(f < 1){
                        Vec2 v = Tmp.v1.trns(ang, off).add(e.x, e.y);
                        Lines.stroke(0.5f + (1f - f) * 3f);
                        Lines.lineAngle(v.x, v.y, ang, len * f, false);
                    }
                }
            }
        }),

        desCreepHit = new Effect(20f, e -> {
            float angr = 90f;
            float len = 1f;
            Rand r = Utils.rand;
            r.setSeed(e.id);

            Draw.color(KPal.red);
            Lines.stroke(1.75f);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 10; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float tlen = r.random(32f) * len * f + r.random(15f);
                    float rot = r.range(angr) + e.rotation;
                    float slope = pow2Out.apply(Mathf.slope(f)) * 24f * len;
                    Vec2 v = Tmp.v1.trns(rot, tlen).add(e.x, e.y);
                    Lines.lineAngle(v.x, v.y, rot, slope, false);
                }

                angr *= 0.7f;
                len *= 1.7f;
            }
            Draw.reset();
        }),

        desCreepHeavyHit = new Effect(300f, 1200f, e -> {
            float sizeScl = e.data instanceof Float ? (float)e.data : 1f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            float scl = Mathf.clamp(e.time / 8f);
            float range = 32f;
            float countScl = 1f;
            float z = z();
            Tmp.c2.set(Color.gray).a(0.8f);
            for(int i = 0; i < 5; i++){
                color(Pal.lightOrange, Tmp.c2, i / 4f);
                float arange = 180f;
                float range2 = 1f;
                for(int j = 0; j < 5; j++){
                    int count = (int)(r.random(12, 15) * countScl);
                    for(int k = 0; k < count; k++){
                        float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                        float ang = r.range(arange) + e.rotation;
                        float len = r.random(range * range2) * sizeScl * 0.5f;
                        float size = r.random(10f, 24f) * scl * sizeScl * 0.5f;

                        z(z - r.random(0.002f));
                        if(f < 1f){
                            Vec2 v = Tmp.v1.trns(ang, len * pow5Out.apply(f)).add(e.x, e.y);
                            Fill.circle(v.x, v.y, size * (1f - pow10In.apply(f)));
                        }
                    }

                    arange *= 0.6f;
                    range2 *= 1.75f;
                }
                scl *= 1.5f;
                range *= 1.6f;
                countScl *= 1.4f;
            }
            z(z);

            float shock = 230f * sizeScl * (1f + e.fin() * 2f) + (e.fin() * 50f);
            color(Pal.lighterOrange);
            if(e.time < 5f){
                Fill.circle(e.x, e.y, shock);
            }

            Lines.stroke(3f * e.fout());
            Lines.circle(e.x, e.y, shock);

            for(int i = 0; i < 16; i++){
                float ang = r.random(360f);
                Vec2 v = Tmp.v1.trns(ang, shock).add(e.x, e.y);
                Drawf.tri(v.x, v.y, 8f * e.fout() * sizeScl, (70f + 25f * e.fin()) * sizeScl, ang + 180f);
            }

            color(Pal.lighterOrange, Pal.lightOrange, e.fin());
            float arange = 180f;
            float range2 = 1f;
            Lines.stroke(3f);
            for(int i = 0; i < 6; i++){
                int count = r.random(8, 12);
                for(int k = 0; k < count; k++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                    float f2 = pow5Out.apply(f);
                    float rot = e.rotation + r.range(arange);
                    float len = range2 * r.random(120f) * sizeScl * f2 + r.random(50f * sizeScl);
                    float str = r.random(34f, 60f) * range2 * sizeScl * pow2Out.apply(Mathf.slope(f2));
                    if(f < 1f){
                        Vec2 v = Tmp.v1.trns(rot, len).add(e.x, e.y);
                        Lines.lineAngle(v.x, v.y, rot, str);
                    }
                }

                arange *= 0.65f;
                range2 *= 1.6f;
            }
        }),

        desGroundMelt = new Effect(15f * 60, e -> {
            z(Layer.debris);
            color(Color.red);
            //Draw.blend(Blending.additive);
            float fout = Mathf.curve(e.fout(), 0f, 0.333f);

            Fill.circle(e.x, e.y, e.rotation * Mathf.clamp(e.time / 6f) * fout);

            //Draw.blend();
            z(Layer.debris + 0.05f);

            color(KPal.blood);
            blend(Blending.additive);
            Fill.circle(e.x, e.y, e.rotation * Mathf.clamp(e.time / 6f) * fout);
            Drawf.light(e.x, e.y, 90f, KPal.blood, 8f * e.time);
            blend();
        }).layer(Layer.debris),

        desRailHit = new Effect(80f, 900f, e -> {
            float sizeScl = e.data instanceof Float ? (float)e.data : 1f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            float ang = 180f;
            float rscl = 0.7f * sizeScl;
            Draw.color(KPal.red);
            for(int i = 0; i < 5; i++){
                int count = (int)(10 * rscl);
                for(int j = 0; j < count; j++){
                    float fin = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rot = r.range(ang) + e.rotation;
                    float off = r.random(22f * rscl) + r.random(50f * Mathf.pow(rscl, 1.5f)) * pow4Out.apply(fin);
                    float sscl = r.random(0.7f, 1.2f);

                    float wid = 12f * sscl * rscl * (1f - pow4In.apply(fin));
                    float hei = 52f * sscl * Mathf.pow(rscl, 1.5f) * pow5Out.apply(fin);

                    Vec2 v = Tmp.v1.trns(rot, off).add(e.x, e.y);
                    Drawf.tri(v.x, v.y, wid, hei, rot);
                    Drawf.tri(v.x, v.y, wid, wid * 2.2f, rot + 180f);
                }

                ang *= 0.6f;
                rscl *= 1.5f;
            }

            ang = 180f;
            rscl = 0.5f * sizeScl;
            Draw.color(KPal.red, Color.white, e.fin());
            Lines.stroke(3f);
            for(int i = 0; i < 7; i++){
                int count = 12;
                for(int j = 0; j < count; j++){
                    float fin = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rot = r.range(ang) + e.rotation;
                    float off = r.random(30f * rscl) + r.random(40f * Mathf.pow(rscl, 1.6f)) * pow5Out.apply(fin);

                    float len = r.random(20f, 40f) * Mathf.pow(rscl, 1.6f) * sineOut.apply(Mathf.slope(pow5Out.apply(fin)));

                    Vec2 v = Tmp.v1.trns(rot, off).add(e.x, e.y);
                    Lines.lineAngle(v.x, v.y, rot, len, false);
                }

                ang *= 0.5f;
                rscl *= 1.5f;
            }

            if(sizeScl < 0.75f) return;
            Draw.color(Color.white, 0.666f * e.fout());

            GraphicUtils.drawShockWave(e.x, e.y, -105f, 0f, -e.rotation - 90f, 400f * sizeScl * pow2Out.apply(e.fin()) + 70f, 30f * Mathf.pow(sizeScl, 1f / 1.5f) * pow2Out.apply(e.fin()) + 4f, 16, 0.015f);
        }),

        desNukeShockwave = new Effect(190f, 1900f * 2f, e -> {
            float size = e.rotation*5;

            Draw.color(Color.white, 0.333f * e.fout());
            Lines.stroke((size / 15f) + (size / 5f) * e.fin());
            Lines.circle(e.x, e.y, size / 3f + size * pow2Out.apply(e.fin()) * 2f);
        }).layer(Layer.groundUnit + 1f),

        desNuke = new Effect(80f, 800f * 2, e -> {
            if(!(e.data instanceof float[] arr)) return;
            float size = e.rotation;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            float scl = 1f;
            Tmp.c2.set(Color.gray).a(0.8f);
            for(int k = 0; k < 6; k++){
                float cf = k / 5f;
                color(Tmp.c2, Pal.lightOrange, cf);
                for(int i = 0; i < 40; i++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float len = r.random(size * scl * 0.75f) * pow5Out.apply(f) + r.random(size / 5f);
                    float ang = r.random(360f);
                    float psize = size / 5f;
                    float rad = r.random(psize * (scl * 0.5f + 0.5f) * 0.87f, psize) * scl * (1f - pow5In.apply(f));
                    if(f < 1f){
                        Tmp.v1.trns(ang, len).add(e.x, e.y);
                        Fill.circle(Tmp.v1.x, Tmp.v1.y, rad);
                    }
                }
                scl *= 0.75f;
            }
            scl = 1f;
            color(Pal.lighterOrange);
            Lines.stroke(3f);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 20; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float ang = r.random(560f);
                    float len = r.random(size * scl * 0.9f) * pow5Out.apply(f) + r.random(size / 5f);
                    float line = r.random(22f, 85f) * Mathf.pow(scl, 1.1f) * pow2Out.apply(Mathf.slope(pow5Out.apply(f)));

                    if(f < 1f){
                        Tmp.v1.trns(ang, len).add(e.x, e.y);
                        Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, ang, line, false);
                    }
                }
                scl *= 1.4f;
            }

            float fin = Mathf.clamp(e.time / 10f);
            if(fin < 1){
                Tmp.c2.set(Pal.lightOrange).a(0f);
                color(Pal.lighterOrange, Tmp.c2, fin);
                for(int i = 0; i < arr.length; i++){
                    float len1 = arr[i], len2 = arr[(i + 1) % arr.length];
                    float ang1 = (i / (float)arr.length) * 360f;
                    float ang2 = ((i + 1f) / arr.length) * 360f;

                    if(len1 >= size){
                        len1 += (size / 1.5f) * fin;
                    }
                    if(len2 >= size){
                        len2 += (size / 1.5f) * fin;
                    }

                    float x1 = Mathf.cosDeg(ang1) * len1, y1 = Mathf.sinDeg(ang1) * len1;
                    float x2 = Mathf.cosDeg(ang2) * len2, y2 = Mathf.sinDeg(ang2) * len2;

                    Fill.tri(e.x, e.y, e.x + x1, e.y + y1, e.x + x2, e.y + y2);
                }
            }
        }),

        desNukeShoot = new Effect(35f, e -> {
            float ang = 90f, len = 1f;
            Rand r = Utils.rand;
            r.setSeed(e.id);

            //Draw.color(KPal.red, Color.white, e.fin());
            Lines.stroke(2f);
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                    float rot = e.rotation + r.range(ang);
                    Draw.color(KPal.red, Color.white, f);
                    Vec2 v = Tmp.v1.trns(rot, r.random(40f) * pow2Out.apply(f) * len).add(e.x, e.y);
                    Lines.lineAngle(v.x, v.y, rot, f * 40f * r.random(0.75f, 1f) * len * pow2Out.apply(Mathf.slope(f)), false);
                }
                ang *= 0.5f;
                len *= 1.4f;
            }
        }),

        desNukeVaporize = new Effect(40f, 1200f, e -> {
            float size = e.data instanceof Float ? (float)e.data : 10f;

            Rand r = Utils.rand;
            r.setSeed(e.id);

            int count = 20 + (int)(size * size * 0.5f);
            float c = 0.25f;
            for(int i = 0; i < count; i++){
                float l = r.nextFloat() * c;
                float f = Mathf.curve(e.fin(), l, ((1f - c) + l) * r.random(0.8f, 1f));
                float len = r.random(0.5f, 1f) * (80f + size * 10f) * pow2In.apply(f);
                float off = Mathf.sqrt(r.nextFloat()) * size, ang = r.random(360f), rng = r.range(10f);
                float scl = (size / 2f) * r.random(0.9f, 1.1f) * Utils.biasSlope(f, 0.1f);

                if(f > 0 && f < 1){
                    Vec2 v1 = Tmp.v1.trns(ang, off).add(e.x, e.y).add(Tmp.v2.trns(e.rotation + rng, len));
                    Draw.color(Pal.lightOrange, Pal.rubble, pow3Out.apply(f));
                    Fill.circle(v1.x, v1.y, scl);
                }
            }
        }).layer(Layer.flyingUnit),

        desNukeShockSmoke = new Effect(40f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            int count = 10;
            float c = 0.4f;
            for(int i = 0; i < count; i++){
                float l = r.nextFloat() * c;
                float f = Mathf.curve(e.fin(), l, ((1f - c) + l) * r.random(0.8f, 1f));
                float len = r.random(0.75f, 1f) * 160f * pow2In.apply(f);
                float off = Mathf.sqrt(r.nextFloat()) * Vars.tilesize / 2f, ang = r.random(360f), rng = r.range(10f);
                float scl = r.random(4f, 6f) * (1f - pow2In.apply(f));

                if(f > 0 && f < 1){
                    Vec2 v1 = Tmp.v1.trns(ang, off).add(e.x, e.y).add(Tmp.v2.trns(e.rotation + rng, len));
                    color(Pal.rubble, Color.gray, f);
                    Fill.circle(v1.x, v1.y, scl);
                }
            }
        }),

        desMissileHit = new Effect(50f, 800f, e -> {
            Rand r = Utils.rand;
            r.setSeed(e.id);

            Tmp.c2.set(Color.gray).a(0.8f);
            //Tmp.c3.set(KPal.red).mul(2f);
            float scl1 = Mathf.clamp(e.time / 3f);
            float scl3 = 1.1f;
            float angScl = 0.6f;
            for(int i = 0; i < 4; i++){
                float scl2 = 1f;
                float len = 1f;
                float ang = 180f;

                //Draw.color(Tmp.c2, Pal.lightOrange, i / 3f);
                color(Tmp.c2, KPal.red, i / 3f);
                for(int j = 0; j < 5; j++){
                    for(int k = 0; k < 9; k++){
                        float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                        float rot = e.rotation + r.range(ang);
                        //float ll = r.random(45f) * len * pow10Out.apply(f) * scl1;
                        float ll = r.random(45f) * len * pow5Out.apply(f);
                        float scl = r.random(0.666f, 1f) * scl2 * scl1 * 18f * (1f - pow10In.apply(f));

                        Vec2 v = Tmp.v1.trns(rot, ll).add(e.x, e.y);
                        Fill.circle(v.x, v.y, scl);
                    }

                    ang *= angScl;
                    len *= 1.5f;
                    scl2 *= scl3;
                }
                scl1 *= 0.9f;
                angScl *= 0.8f;
                scl3 *= 0.9f;
            }
            color(KPal.red);
            scl1 = 1f;
            scl3 = 1f;
            angScl = 180f;
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 6; j++){
                    float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.3f));
                    float rot = e.rotation + r.range(angScl);
                    float ll = r.random(20f) * scl3 * pow2Out.apply(f);
                    float size = r.random(5f, 10f);
                    float wid = size * scl1 * Utils.biasSlope(f, 0.2f);
                    float len = wid * 3f + size * 7f * Mathf.pow(scl1, 1.2f) * pow5Out.apply(f);

                    Vec2 v = Tmp.v1.trns(rot, wid * 2f + ll).add(e.x, e.y);
                    Drawf.tri(v.x, v.y, wid, len, rot);
                    Drawf.tri(v.x, v.y, wid, wid * 3f, rot + 180f);
                }

                scl1 *= 1.2f;
                scl3 *= 1.5f;
                angScl *= 0.5f;
            }

            Draw.reset();
        });
    }

    public static class EUFx {
        public static Effect StormExp(Color cor, Color liC) {
            return new Effect(72, e -> {
                Draw.color(liC, cor, e.fin());
                Fill.circle(e.x, e.y, e.fout() * 40);
                Lines.stroke(e.fout() * 4.5f);
                Lines.circle(e.x, e.y, e.fin() * 60);
                Lines.stroke(e.fout() * 2.75f);
                Lines.circle(e.x, e.y, e.fin() * 30);
                randLenVectors(e.id, 45, 1 + 65 * e.fin(), e.rotation, 360, (x, y) -> {
                    Lines.stroke(e.fout() * 2);
                    Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 12 + 1);
                });
                randLenVectors(e.id, 85, 1 + 160 * e.fin(),  Time.time * 4, 360, (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 10));
            });
        }

        public static Effect flameShoot(Color colorBegin, Color colorTo, Color colorEnd, float length, float cone, int number, float lifetime){
            return new Effect(lifetime, 80, e -> {
                    Draw.color(colorBegin, colorTo, colorEnd, e.fin());
                randLenVectors(e.id, number, e.finpow() * length, e.rotation, cone, (x, y) -> Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.5f));
            });
        }

        public static Effect elDsp(Color cor, Color cor2) {
            return new Effect(20, e -> {
                Draw.color(cor,cor2,e.fin());
                Lines.stroke(e.fout() * 3);
                Lines.circle(e.x, e.y, e.fin() * 60);
                Lines.stroke(e.fout() * 1.75f);
                Lines.circle(e.x, e.y, e.fin() * 45);
                Draw.color(cor);
                Fill.circle(e.x, e.y, e.fout() * 20);
                Draw.color(cor,cor2,e.fin());
                Fill.circle(e.x, e.y, e.fout() * 14);
            });
        }

        public static Effect absorbEffect = new Effect(38, e -> {
            Draw.color(Items.sand.color);
            Angles.randLenVectors(e.id, 2, 1 + 20 * e.fout(), e.rotation, 120, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 3 + 1);
                Fill.circle(e.x + x / 2, e.y + y / 2, e.fout() * 2);
            });
        });

        public static Effect absorbEffect2 = new Effect(50, e -> {
            Draw.color(Items.pyratite.color.cpy().a(0.7f));
            Angles.randLenVectors(e.id, 2, 1 + 5 * e.fout(), e.rotation, e.rotation + 120, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 3 + 1);
                Fill.circle(e.x + x / 2, e.y + y / 2, e.fout() * 2);
            });
        });

        public static Effect missileTrailSmokeSmall = new Effect(90f, 90f, b -> {
            float intensity = 1.1f;

            color(b.color, 0.7f);
            for(int i = 0; i < 3; i++){
                rand.setSeed(b.id* 2L + i);
                float lenScl = rand.random(0.5f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> randLenVectors(e.id + fi - 1, e.fin(pow10Out), (int)(2f * intensity), 9f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 1.2f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 1.2f, b.color, 0.5f);
                }));
            }
        }).layer(Layer.bullet - 1f);

        public static Effect unitDesGone = new Effect(12, e -> {
                Draw.color(e.color);
                Lines.stroke(2 * e.fout());
                Lines.circle(e.x, e.y, e.rotation * e.fout());
        });

        public static Effect gone(Color color, float r, float t){
            return new Effect(12, e -> {
                Draw.color(color);
                Lines.stroke(t * e.fout());
                Lines.circle(e.x, e.y, r * e.fout());
            });
        }
        public static Effect gone(Color color){
            return gone(color, 5, 2);
        }

        public static Effect rainbowShoot = new Effect(16, e -> {
            Draw.blend(Blending.additive);
            Draw.color(EC6.set(rainBowRed).shiftHue(Time.time * 2.0f));
            Lines.stroke(e.fout() * 1.5f);
            Angles.randLenVectors(e.id, 1, e.finpow() * 70f, e.rotation, 80f, (x, y) -> {
                float ang = Mathf.angle(x, y);
                Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 8f + 1.5f);
            });
            Draw.blend();
            Draw.reset();
        });

        public static Effect lancerLaserCharge(Color color){
            return new Effect(38f, e -> {
                color(color);
                randLenVectors(e.id, 14, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 1f));
            });
        }

        public static Effect chargeBeginEffect(float chargeTime, Color color){
            return new Effect(chargeTime * 1.5f, e -> {
                color(Color.white, color, e.fin());
                Fill.circle(e.x, e.y, e.fin() * 8);
                color();
                Fill.circle(e.x, e.y, e.fin() * 5);
            });
        }

        public static Effect stingerShoot(Color color){
            return new Effect(10, e -> {
                color(Color.white, color, e.fin());
                Lines.stroke(e.fout() * 2f + 0.2f);
                Lines.circle(e.x, e.y, e.fin() * 28);
            });
        }

        public static Effect trail(Color color, float width, float length){
            return new Effect(12, e -> {
                Draw.color(color);
                Drawf.tri(e.x, e.y, width * e.fout(), length, e.rotation);
            });
        }

        public static Effect prismHit = new Effect(16, e -> {
            Draw.blend(Blending.additive);
            Draw.color(EC7.set(rainBowRed).shiftHue(Time.time * 2f));
            Lines.stroke(e.fout() * 1.5f);
            randLenVectors(e.id, 1, e.finpow() * 70f, e.rotation, 80f, (x, y) -> {
                float ang = Mathf.angle(x, y);
                Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 8f + 1.5f);
            });
            Draw.blend();
            Draw.reset();
        });

        public static Effect LACraft = new Effect(60, e ->{
            Draw.color(Pal.surge, Color.valueOf("FFD37F"), e.fin());
            Lines.stroke(e.fout() * 5);
            Lines.circle(e.x, e.y, 20* e.fin());
        });

        public static Effect Start = new Effect(30, e -> {
            Draw.color(Color.valueOf("FFD37F"));
            Lines.stroke(3 * e.fout());
            if(e.data instanceof Float){
                float range = (float) e.data;
                Lines.circle(e.x, e.y, range * e.fout());
            }
        });

        public static Effect shieldDefense = new Effect(20, e -> {
            Draw.color(e.color);
            Lines.stroke(e.fslope() * 2.5f);
            Lines.poly(e.x, e.y, 6, 3 * e.fout() + 9);
            Angles.randLenVectors(e.id, 2, 32 * e.fin(), 0, 360,(x, y) -> Lines.poly(e.x + x, e.y + y, 6, 2 * e.fout() + 2));
        });

        public static Effect casingContinue(float lifetime, int shots){
            return new Effect(lifetime, e->{
                Draw.z(Layer.bullet);
                for(int a = 0; a < shots; a++) {
                    float time = lifetime / shots;
                    e.scaled(time * a, b -> {
                        Draw.color(Pal.lightOrange, Pal.lightishGray, Pal.lightishGray, b.fin());
                        Draw.alpha(b.fout(0.5f));
                        float rot = Math.abs(e.rotation) + 90;
                        int i = -Mathf.sign(e.rotation);
                        float len = (4 + b.finpow() * 9) * i;
                        float lr = rot + Mathf.randomSeedRange(e.id + i + 6, 20 * b.fin()) * i;
                        Draw.rect(Core.atlas.find("casing"),
                                e.x + Angles.trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3 * b.fin()),
                                e.y + Angles.trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3 * b.fin()),
                                3, 6,
                                rot + e.fin() * 50 * i
                        );
                    });
                }
            });
        }

        public static Effect ellipse(float startRad, int num, float lifetime, Color color){
            return ellipse(startRad, 2, num, lifetime, color);
        }
        //divide into two parts for easy adjustment of layers, but ... after writing it, I realized that seem useless : (
        public static Effect ellipse(float startRad, float rad, int num, float lifetime, Color color){
            return new Effect(lifetime, e ->{
                float length = startRad * e.fin();
                float width = length/2;

                Draw.color(color);

                //half
                for(int i = 0; i <= num; i++){
                    float rot = -90f + 180f * i / (float)num;
                    Tmp.v1.trnsExact(rot, width);

                    point(
                            (Tmp.v1.x) / width * length, //convert to 0..1, then multiply by desired length and offset relative to previous segment
                            Tmp.v1.y, //Y axis remains unchanged
                            e.x, e.y,
                            e.rotation + 90,
                            rad * e.fout()
                    );
                }

                //the other half
                for(int i = 0; i <= num; i++){
                    float rot = 90f + 180f * i / (float)num;
                    Tmp.v1.trnsExact(rot, width);

                    point(
                            (Tmp.v1.x) / width * length,
                            Tmp.v1.y,
                            e.x, e.y,
                            e.rotation + 90,
                            rad * e.fout()
                    );
                }
            });
        }
        private static void point(float x, float y, float baseX, float baseY, float rotation, float rad){
            Tmp.v1.set(x, y).rotateRadExact(rotation * Mathf.degRad);
            Fill.circle(Tmp.v1.x + baseX, Tmp.v1.y + baseY, rad);
        }

        public static Effect Mk2Shoot = new Effect(30, e ->{
            if(!(e.data instanceof Float rotation)) return;
            Draw.z(Layer.effect - 0.1f);
            Draw.color(EUGet.EC8.set(rainBowRed).shiftHue(Time.time * 2.0f));
            Angles.randLenVectors(e.id, 1, e.fin() * 20f, e.rotation + rotation, 0, (x, y) -> Fill.circle(e.x + x, e.y + y, 2 * e.fout()));
            Angles.randLenVectors(e.id, 1, e.fin() * 20f, e.rotation - rotation, 0, (x, y) -> Fill.circle(e.x + x, e.y + y, 2 * e.fout()));
            Draw.blend();
            Draw.reset();
        });
        public static Effect Mk2Shoot(float r){
            return new Effect(30, e -> {
                Draw.z(Layer.effect - 0.1f);
                Draw.color(EUGet.EC9.set(rainBowRed).shiftHue(Time.time * 2.0f));
                Angles.randLenVectors(e.id, 1, e.fin() * 20f, e.rotation + r, 0, (x, y) -> Fill.circle(e.x + x, e.y + y, 2 * e.fout()));
                Angles.randLenVectors(e.id, 1, e.fin() * 20f, e.rotation - r, 0, (x, y) -> Fill.circle(e.x + x, e.y + y, 2 * e.fout()));
                Draw.blend();
                Draw.reset();
            });
        }

        public static Effect digTile(Color color){
            return new Effect(30, e -> {
                mixcol(color, 1);
                alpha(e.fout());
                Fill.square(e.x, e.y, Vars.tilesize/2f);
            });
        }

        public static Effect expDillEffect(int size, Color color){
            return new Effect(15, e -> {
                Lines.stroke(3 * e.fout(), color);
                Lines.square(e.x, e.y, size * Vars.tilesize/2f * e.fin(), 180 * e.fout());
            });
        }

        public static Effect colorBall(Color color, float range){
            return new Effect(80, e -> {
                Draw.color(color.cpy().a(1/range));
                for(int i = 0; i < range; i++){
                    Fill.circle(e.x, e.y, range * i/range * e.fout());
                }
            });
        }

        public static Effect aimEffect(float lifetime, Color color, float width, float length, float spacing){
            return new Effect(lifetime, length, e -> {
                Draw.color(color);
                TextureRegion region = Core.atlas.find(name("aim-shoot"));
                float track = Mathf.curve(e.fin(pow2Out), 0, 0.25f) * Mathf.curve(e.fout(pow4Out), 0, 0.3f) * e.fin();
                for(int i = 0; i <= length / spacing; i++){
                    Tmp.v1.trns(e.rotation, i * spacing);
                    float f = pow3Out.apply(Mathf.clamp((e.fin() * length - i * spacing) / spacing)) * (0.6f + track * 0.4f);
                    Draw.rect(region, e.x + Tmp.v1.x, e.y + Tmp.v1.y, 155 * Draw.scl * f, 155 * Draw.scl * f, e.rotation - 90);
                }
                Tmp.v1.trns(e.rotation, 0, (2 - track) * Vars.tilesize * width);
                Lines.stroke(track * 2);
                for(int i : Mathf.signs){
                    Lines.lineAngle(e.x + Tmp.v1.x * i, e.y + Tmp.v1.y * i, e.rotation, length * (0.75f + track / 4) * Mathf.curve(e.fout(pow5Out), 0, 0.1f));
                }
            });
        }

        public static Effect expFtEffect(int amount, float size, float len, float lifetime, float startDelay){
            return new Effect(lifetime, e -> {
                float length = len + e.finpow() * 20f;
                rand.setSeed(e.id);
                for(int i = 0; i < amount; i++){
                    v.trns(rand.random(360f), rand.random(length));
                    float sizer = rand.random(size/2, size);

                    e.scaled(e.lifetime * rand.random(0.5f, 1f), b -> {
                        color(Pal.darkerGray, b.fslope() * 0.93f);

                        Fill.circle(e.x + v.x, e.y + v.y, sizer + b.fslope() * 1.2f);
                    });
                }
            }).startDelay(startDelay);
        }

        public static Effect wind = new Effect(30, e -> {
            Draw.z(Layer.debris);
            Draw.color(e.color);
            Fill.circle(e.x, e.y, e.fout() * 6 + 0.3f);
        });

        public static Effect coneFade(float findRange, float findAngle){
            return new Effect(15, e -> {
                float range = findRange * e.fout();
                Draw.color(Pal.heal);
                Draw.z(Layer.buildBeam);
                Draw.alpha(0.8f);
                for(float i = e.rotation - findAngle /2; i < e.rotation + findAngle /2; i+=2){
                    float px1 = posx(e.x, range, i);
                    float py1 = posy(e.y, range, i);
                    float px2 = posx(e.x, range, i+2);
                    float py2 = posy(e.y, range, i+2);
                    Fill.tri(e.x, e.y, px1, py1, px2, py2);
                }
            });
        }

        public static Effect chainLightningFade = chainLightningFade(45, 2.5f);

        public static Effect chainLightningFade(float lifetime) {
            return chainLightningFade(lifetime, 2.5f);
        }

        public static Effect chainLightningFade(float lifetime, float stroke) {
            return chainLightningFade(lifetime, stroke, -1);
        }

        public static Effect chainLightningFadeOverride(float rangeOverride) {
            return chainLightningFade(45, 2.5f, rangeOverride);
        }

        public static Effect chainLightningFade(float lifetime, float stroke, float rangeOverride){
            return new Effect(lifetime, 500f, e -> {
                float tx, ty;
                if (e.data instanceof Position p){
                    tx = p.getX();
                    ty = p.getY();
                } else if(e.data instanceof Float f){
                    tx = EUGet.pointAngleX(e.x, e.rotation, f);
                    ty = EUGet.pointAngleY(e.y, e.rotation, f);
                } else return;
                float dst = Mathf.dst(e.x, e.y, tx, ty);
                Tmp.v1.set(tx, ty).sub(e.x, e.y).nor();

                float normx = Tmp.v1.x, normy = Tmp.v1.y;
    //            float range = rangeOverride > 0 ? rangeOverride : e.rotation;
                float range = rangeOverride > 0 ? rangeOverride : 6;
                int links = Mathf.ceil(dst / range);
                float spacing = dst / links;

                rand.setSeed(e.id);

                FloatSeq seq = Pools.obtain(FloatSeq.class, FloatSeq::new);

                seq.add(e.x, e.y);
                for (int i = 0; i < links; i++) {
                    float nx, ny;
                    if (i == links - 1) {
                        nx = tx;
                        ny = ty;
                    } else {
                        float len = (i + 1) * spacing;
                        Tmp.v1.setToRandomDirection(rand).scl(range / 2f);
                        nx = e.x + normx * len + Tmp.v1.x;
                        ny = e.y + normy * len + Tmp.v1.y;
                    }
                    seq.add(nx, ny);
                }

                Lines.stroke(stroke * Mathf.curve(e.fout(), 0, 0.7f));
                Draw.color(Color.white, e.color, e.fin());

                Fill.circle(e.x, e.y, Lines.getStroke() / 2);


                rand.setSeed(e.id);
                float fin = Mathf.curve(e.fin(), 0, 0.5f);
                for (int j = 0; j < (seq.size - 2) * fin; j += 2) {
                    float ox = seq.get(j);
                    float oy = seq.get(j + 1);
                    float nx = seq.get(j + 2);
                    float ny = seq.get(j + 3);
                    Lines.line(ox, oy, nx, ny);
                }

                seq.clear();
                Pools.free(seq);
                Draw.reset();
            }).followParent(false);
        }

        public static Effect chainLightning = new Effect(20f, 300f, e -> {
            float tx, ty;
            if (e.data instanceof Position p){
                tx = p.getX();
                ty = p.getY();
            } else if(e.data instanceof Float f){
                tx = EUGet.pointAngleX(e.x, e.rotation, f);
                ty = EUGet.pointAngleY(e.y, e.rotation, f);
            } else return;
            float dst = Mathf.dst(e.x, e.y, tx, ty);
            Tmp.v1.set(tx, ty).sub(e.x, e.y).nor();

            float normx = Tmp.v1.x, normy = Tmp.v1.y;
            float range = 6f;
            int links = Mathf.ceil(dst / range);
            float spacing = dst / links;

            Lines.stroke(2.5f * e.fout());
            Draw.color(Color.white, e.color, e.fin());

            Lines.beginLine();

            Lines.linePoint(e.x, e.y);

            rand.setSeed(e.id);

            for(int i = 0; i < links; i++){
                float nx, ny;
                if(i == links - 1){
                    nx = tx;
                    ny = ty;
                }else{
                    float len = (i + 1) * spacing;
                    Tmp.v1.setToRandomDirection(rand).scl(range/2f);
                    nx = e.x + normx * len + Tmp.v1.x;
                    ny = e.y + normy * len + Tmp.v1.y;
                }

                Lines.linePoint(nx, ny);
            }

            Lines.endLine();
        }).followParent(false).rotWithParent(false);

        public static Effect waitChainHit18 = new Effect(27, e -> {
            if(e.time > 9){
                Draw.color(e.color);
                float out = (e.lifetime - e.time)/18;
                float in = 1 - out;
                Angles.randLenVectors(e.id, 5, 21 * fastSlow.apply(in), e.rotation, 45, (x, y) -> Fill.square(e.x + x, e.y + y, 5 * out, Mathf.randomSeed(e.id, 180)));
            }
        });

        public static Effect ElectricExp(float lifetime, float sw, float r){
            return new Effect(lifetime, e -> {
                if(e.time < sw) {
                    float fin = e.time/sw, fout = 1 - fin;
                    Lines.stroke(r/12 * fout, Pal.heal);
                    Lines.circle(e.x, e.y, r * fout);
                } else {
                    float fin = (e.time - sw) / (e.lifetime - sw), fout = 1 - fin;
                    float fbig = Math.min(fin * 10, 1);
                    Lines.stroke(r/2 * fout, Pal.heal);
                    Lines.circle(e.x, e.y, r * fbig);
                    for(int i = 0; i < 2; i++){
                        float angle = i * 180 + 60;
                        Drawf.tri(e.x + Angles.trnsx(angle, r * fbig), e.y + Angles.trnsy(angle, r * fbig), 40 * fout, r/1.5f, angle);
                    }
                    Draw.z(Layer.effect + 0.001f);
                    Lines.stroke(r/18 * fout, Pal.heal);
                    randLenVectors(e.id + 1, fin * fin + 0.001f, 20, r * 2, (x, y, in, out) -> {
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * r/4);
                        Drawf.light(e.x + x, e.y + y, out * r, Draw.getColor(), 0.8f);
                    });

                    if(!state.isPaused()) Effect.shake(3, 3, e.x, e.y);
                }
            });
        }

        public static Effect diffuse = new Effect(30, e -> {
            if(!(e.data instanceof Integer)) return;
            int size = (int) e.data;
            float f = e.fout();
            float r = Math.max(0f, Mathf.clamp(2f - f * 2f) * size * tilesize / 2f - f - 0.2f), w = Mathf.clamp(0.5f - f) * size * tilesize;
            Lines.stroke(3f * f, e.color);
            Lines.beginLine();
            for(int i = 0; i < 4; i++){
                Lines.linePoint(e.x + Geometry.d4(i).x * r + Geometry.d4(i).y * w, e.y + Geometry.d4(i).y * r - Geometry.d4(i).x * w);
                if(f < 0.5f) Lines.linePoint(e.x + Geometry.d4(i).x * r - Geometry.d4(i).y * w, e.y + Geometry.d4(i).y * r + Geometry.d4(i).x * w);
            }
            Lines.endLine(true);
        });

        public static Effect diffuse(int size, Color color, float life) {
            return new Effect(life, e -> {
                float f = e.fout();
                if(f < 1e-4f) return;
                float r = Math.max(0f, Mathf.clamp(2f - f * 2f) * size * tilesize / 2f - f - 0.2f), w = Mathf.clamp(0.5f - f) * size * tilesize;
                Lines.stroke(3f * f, color);
                Lines.beginLine();
                for (int i = 0; i < 4; i++) {
                    Lines.linePoint(e.x + Geometry.d4(i).x * r + Geometry.d4(i).y * w, e.y + Geometry.d4(i).y * r - Geometry.d4(i).x * w);
                    if (f < 0.5f)
                        Lines.linePoint(e.x + Geometry.d4(i).x * r - Geometry.d4(i).y * w, e.y + Geometry.d4(i).y * r + Geometry.d4(i).x * w);
                }
                Lines.endLine(true);
            });
        }

        public static Effect fiammettaExp(float r){
            return new Effect(30, e -> {
                float fin = Math.min(e.time/10, 1), fout = 1 - ((e.time - 10)/(e.lifetime - 10));
                Draw.color(Color.valueOf("FFD37F").cpy().a(e.time > 10 ? 0.3f * fout : 0.3f));
                Fill.circle(e.x, e.y, r * fin);
                float ww = r * 2f * fin, hh = r * 2f * fin;
                Draw.color(Color.valueOf("FFD37F").cpy().a(e.time > 10 ? fout : 1));
                Draw.rect(Core.atlas.find(name("firebird-light")), e.x, e.y, ww, hh);
            });
        }

        public static Effect normalTrail = new Effect(90, e -> {
            Draw.color(e.color);
            float r = e.rotation;
            Fill.circle(e.x, e.y, r * e.foutpow());
        }).layer(Layer.bullet - 1f);

        public static Effect normalIceTrail = new Effect(90, e -> DrawFunc.drawSnow(e.x, e.y, e.rotation * e.foutpow(), e.fin() * 180f, e.color));

        public static Effect diffHit = new Effect(30, e -> {
            if(!(e.data instanceof Healthc)) return;
            if(e.data instanceof Building b){
                if(b.block == null) return;
                Draw.mixcol(e.color, 1);
                Draw.alpha(e.fout());
                Draw.rect(b.block.fullIcon, e.x, e.y);
            }
            if(e.data instanceof Unit u){
                if(u.type == null) return;
                Draw.mixcol(e.color, 1);
                Draw.alpha(e.fout());
                Draw.rect(u.type.fullIcon, e.x, e.y, u.rotation - 90);
            }
        });

        public static Effect edessp(float lifetime){
            return new Effect(lifetime, e -> {
                if(!(e.data instanceof Object[] objects) || objects.length < 4) return;
                if(!(objects[0] instanceof TextureRegion region)) return;
                if(!(objects[1] instanceof Float range)) return;
                if(!(objects[2] instanceof Float rot)) return;
                if(!(objects[3] instanceof Float rRot)) return;

                float ex = e.x + Angles.trnsx(e.rotation + rRot * e.fin(), range * e.fout()),
                        ey = e.y + Angles.trnsy(e.rotation + rRot * e.fin(), range * e.fout());
                Draw.rect(region, ex, ey, region.width/3f * e.fin(), region.height/3f * e.fin(), rot);
            }).followParent(true);
        }

        public static Effect EUUtSp  = new Effect(80, e -> {
            if(!(e.data instanceof UnitType type)) return;

            Draw.color(Pal.accent);
            Drawf.tri(e.x, e.y, 16 * e.fout(), type.hitSize * 8 * e.fin(), e.rotation - 90);

            Draw.alpha(e.fout());
            Draw.mixcol(Pal.accent, e.fout());
            Draw.rect(type.fullIcon, e.x, e.y, e.rotation);
        }).layer(Layer.flyingUnit + 5f);

        public static Effect PlanetaryArray(float lifetime, int sp, float spl, Color color, float cr, float st, float over){
            return new Effect(lifetime, e -> {
                if(sp == 0) return;
                float fin = Mathf.curve(e.fin(), 0, over);
                float fout = Mathf.curve(e.fout(), 0, 1 - over);
                //Seq<Float> angles = new Seq<>();
                Float[] angles = Pools.obtain(Float[].class, () -> new Float[sp]);
                rand.setSeed(e.id);
                for(int i = 0; i < sp; i++){
                    //angles.add(rand.random(45f, 135f));
                    angles[i] = rand.random(45f, 135f);
                }
                float nx = e.x, ny = e.y;
                for(int i = 0; i < sp * fin; i++){
                    float it = i * (e.lifetime/sp);
                    float ef = Math.min(1, ((e.time - it) / (e.lifetime - it)) * (1 / over));

                    //float angle = e.rotation + angles.get(i) - 90;
                    float angle = e.rotation + angles[i] - 90;
                    Lines.stroke(e.fin() < over ? st * ef : st * fout, color);
                    if(cr > 0) Fill.circle(nx, ny, cr * (e.fin() < over ? ef : fout));
                    if(i == sp - 1) break;
                    Lines.lineAngle(nx, ny, angle, spl * Math.min(1, Math.max(0, ef) * 1/(1 - over)));
                    nx = EUGet.dx(nx, spl, angle);
                    ny = EUGet.dy(ny, spl, angle);
                }
            }).followParent(true);
        }

        public static Effect diffEffect(float lifetime, float st, float r, int amt, float len, float rndLen, float width, Color color, float shake){
            return new Effect(lifetime, e -> {
                rand.setSeed(e.id);
                float pin = (1 - e.foutpow());
                if(color != null) Lines.stroke(st * e.foutpow(), color);
                else Lines.stroke(st * e.foutpow(), e.color);
                Lines.circle(e.x, e.y, r * pin);
                for(int i = 0; i < amt/2; i++){
                    float a = rand.random(180);
                    float lx = EUGet.dx(e.x, r * pin, a);
                    float ly = EUGet.dy(e.y, r * pin, a);
                    Drawf.tri(lx, ly, width * e.foutpow(), (len + rand.random(-rndLen, rndLen)) * e.foutpow(), a + 180);
                }
                for(int i = 0; i < amt/2; i++){
                    float a = 180 + rand.random(180);
                    float lx = EUGet.dx(e.x, r * pin, a);
                    float ly = EUGet.dy(e.y, r * pin, a);
                    Drawf.tri(lx, ly, width * e.foutpow(), (len + rand.random(-rndLen, rndLen)) * e.foutpow(), a + 180);
                }

                if(!Vars.state.isPaused() && shake > 0) Effect.shake(shake, shake, e.x, e.y);
            });
        }

        public static Effect easyExp = new Effect(20, e -> {
            rand.setSeed(e.id);
            float baseRd = e.rotation;
            float randRd = baseRd/6f;
            float pin = (1 - e.foutpow());
            Lines.stroke(2 * e.foutpow(), e.color);
            Lines.circle(e.x, e.y, baseRd * pin);
            for(int i = 0; i < 12; i++){
                float a = rand.random(360);
                float lx = EUGet.dx(e.x, baseRd * pin, a);
                float ly = EUGet.dy(e.y, baseRd * pin, a);
                Drawf.tri(lx, ly, baseRd/6f * e.foutpow(), (baseRd/2f + rand.random(-randRd, randRd)) * e.foutpow(), a + 180);
            }
        });

        public static Effect AccretionDiskEffect = new Effect(60, e -> {
            if(headless || !(e.data instanceof ateData data) || data.owner == null) return;

            float fin = data.out ? e.finpow() : e.foutpow();
            float fout = data.out ? e.foutpow() : e.finpow();
            //float fout = 1 - fin;

            float start = Mathf.randomSeed(e.id, 360f);
            var b = data.owner;

            float ioRad = data.outRad - (data.outRad - data.inRad) * fin;
            float rad = data.speed * e.time * 6;
            float dx = dx(b.x, ioRad, start - rad),
                    dy = dy(b.y, ioRad, start - rad);

            if(data.trail == null) data.trail = new Trail(data.length);
            float dzin = data.out && e.time > e.lifetime - 10 ? pow2Out.apply((e.lifetime - e.time)/10) : fin;
            data.trail.length = data.length;
            //data.trail.length = (int) (data.length * dzin);

            if(!state.isPaused()) data.trail.update(dx, dy, 1);

            float z = Draw.z();
            Draw.z(Layer.effect - 19 * fout);
            //Draw.z(Layer.max - 1);
            data.trail.draw(Tmp.c3.set(e.color).shiftValue(-e.color.value() * fout), data.width * dzin);
            //data.trail.draw(e.color, data.width);
            Draw.z(z);
        });

        public static class ateData implements Pool.Poolable {
            public float width;
            public int length;
            public float inRad, outRad, speed;

            public transient Trail trail;

            public Bullet owner;

            public boolean out = false;

            public static ateData create(){
                return Pools.obtain(ateData.class, EUFx.ateData::new);
            }

            @Override
            public void reset() {
                width = 0;
                length = 0;
                inRad = outRad = speed = 0;

                trail = null;
                owner = null;

                out = false;
            }
        }

        public static Effect airAsh(float lifetime, float range, float pin, Color color, float width, int amount){
            return airAsh(lifetime, range, 0, pin, color, width, amount);
        }

        public static Effect airAsh(float lifetime, float range, float start, float pin, Color color, float width, int amount) {
            return new MultiEffect(
                    new Effect(lifetime, e -> {
                        Color c = color == null ? e.color : color;
                        float fee = e.time < e.lifetime/2 ? e.fin() * 2 : e.fout() * 2;
                        for(int a : Mathf.signs) {
                            for (int i = 0; i < amount; i++) {
                                float dx = EUGet.dx(e.x, range * e.fin() + start, (e.time * 8 + i) * a + Mathf.randomSeed(e.id, -10, 10)),
                                        dy = EUGet.dy(e.y, range * e.fin() + start, (e.time * 8 + i) * a + Mathf.randomSeed(e.id, -10, 10));
                                Draw.color(c);
                                Fill.circle(dx, dy, (width * i / amount + 0.2f) * fee);
                            }
                        }
                    }),
                    new Effect(lifetime, e -> {
                        Color c = color == null ? e.color : color;
                        float fee = e.time < e.lifetime/2 ? e.fin() * 2 : e.fout() * 2;
                        for(int a : Mathf.signs) {
                            for (int i = 0; i < amount; i++) {
                                float dx = EUGet.dx(e.x, (range - pin) * e.fin() + start, (e.time * 8 + i) * a + Mathf.randomSeed(e.id, -10, 10) + 120),
                                        dy = EUGet.dy(e.y, (range - pin) * e.fin() + start, (e.time * 8 + i) * a + Mathf.randomSeed(e.id, -10, 10) + 120);
                                Draw.color(c);
                                Fill.circle(dx, dy, (width * i / amount + 0.2f) * fee);
                            }
                        }
                    }),
                    new Effect(lifetime, e -> {
                        Color c = color == null ? e.color : color;
                        float fee = e.time < e.lifetime/2 ? e.fin() * 2 : e.fout() * 2;
                        for(int a : Mathf.signs) {
                            for (int i = 0; i < amount; i++) {
                                float dx = EUGet.dx(e.x, (range - pin * 2) * e.fin() + start, (e.time * 8 + i) * a + Mathf.randomSeed(e.id, -10, 10) + 240),
                                        dy = EUGet.dy(e.y, (range - pin * 2) * e.fin() + start, (e.time * 8 + i) * a + Mathf.randomSeed(e.id, -10, 10) + 240);
                                Draw.color(c);
                                Fill.circle(dx, dy, (width * i / amount + 0.2f) * fee);
                            }
                        }
                    })
            );
        }

        public static Effect hitOut = new Effect(60, e -> {
            if(e.data instanceof Unit u) {
                UnitType type = u.type;
                if(type != null) {
                    TextureRegion rg = type.fullIcon;
                    float w = rg.width * rg.scl() * xscl;
                    float h = rg.height * rg.scl() * yscl;
                    float dx = EUGet.dx(e.x, Math.max(w, h) * 0.3f * e.finpow(), e.rotation),
                            dy = EUGet.dy(e.y, Math.max(w, h) * 0.3f * e.finpow(), e.rotation);
                    float z = Draw.z();
                    Draw.z(Layer.effect + 10);
                    Draw.alpha(e.foutpow());
                    Draw.rect(rg, dx, dy, w * 1.2f * e.finpow(), h * 1.2f * e.finpow(), u.rotation - 90);
                    Draw.z(z);
                }
            }

            if(e.data instanceof Building b) {
                Block type = b.block;
                if(type != null) {
                    TextureRegion rg = type.fullIcon;
                    float w = rg.width * rg.scl() * xscl;
                    float h = rg.height * rg.scl() * yscl;
                    float dx = EUGet.dx(e.x, h * 0.2f * e.finpow(), e.rotation),
                            dy = EUGet.dy(e.y, h * 0.2f * e.finpow(), e.rotation);
                    float z = Draw.z();
                    Draw.z(Layer.effect + 10);
                    Draw.alpha(e.foutpow());
                    Draw.rect(rg, dx, dy, w * 1.2f * e.finpow(), h * 1.2f * e.finpow());
                    Draw.z(z);
                }
            }
        });

        public static Effect audioEffect = new Effect(30, e -> {
            Draw.color(e.color);
    //        float z = Draw.z();
    //        Draw.z(Layer.flyingUnitLow);
            Draw.alpha(2 * e.foutpow());
            Angles.randLenVectors(e.id, 1, e.fin() * 20f, Mathf.randomSeed(e.id, 360), 0, (x, y) -> Fill.poly(e.x + x, e.y + y, 3, 5 * e.fout(), Mathf.randomSeed(e.id, 360)));
            //Draw.z(z);
        });

        public static Effect layerCircle(float life, float r, Color color){
            return new Effect(life, e -> {
                for(int i = 0; i < r; i++){
                    Lines.stroke(1, Tmp.c1.set(color).a(i/r * 0.8f * e.foutpow()));
                    Lines.circle(e.x, e.y, i * e.finpow());
                }
            });
        }

        public static Effect layerCircle(float life, float r){
            return new Effect(life, e -> {
                for(int i = 0; i < r; i++){
                    Lines.stroke(1, Tmp.c1.set(e.color).a(i/r * 0.8f * e.foutpow()));
                    Lines.circle(e.x, e.y, i * e.finpow());
                }
            });
        }

        public static Effect layerCircle = new Effect(90, e -> {
            float r = e.rotation;
            float pin = 1 - e.foutpow();
            for(int i = 0; i < r; i++){
                Lines.stroke(1, Tmp.c1.set(e.color).a(i/r * 0.8f * e.foutpow()));
                Lines.circle(e.x, e.y, i * pin);
            }
        });

        public static Effect casing4Double = new Effect(40f, e -> {
            color(Pal.lightOrange, Pal.lightishGray, Pal.lightishGray, e.fin());
            alpha(e.fout(0.5f));
            float rot = Math.abs(e.rotation) + 90f;

            for(int i : Mathf.signs){
                float len = (7f + e.finpow() * 11f) * i;
                float lr = rot + Mathf.randomSeedRange(e.id + i + 6, 20f * e.fin()) * i;

                rect(Core.atlas.find("casing"),
                        e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                        e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                        3f, 6f,
                        rot + e.fin() * 50f * i
                );
            }

        }).layer(Layer.bullet);

        public static Effect numberJump = new Effect(60, e -> {
            if(!(e.data instanceof String s)) return;
            Draw.color(e.color);
            float len = Mathf.randomSeed(e.id, 30f, 40f);
            float in2x = Math.min(1, e.fin() * 2);
            in2x = fastSlow.apply(in2x);
            float dx = EUGet.dx(e.x, len * in2x, 90),
                    dy =  EUGet.dy(e.y, len * in2x, 90);

            float tin = e.time < 30 ? Math.min(1, e.fin() * 4) : Math.min(1, e.fout() * 4);
            tin = fastSlow.apply(tin);
            Fonts.outline.draw(s, dx, dy, e.color, Math.max(0.001f, 0.8f * tin), false, 1);
        });

        public static Effect rgX = new Effect(60, (e) -> {
            Object data = e.data;
            if (data instanceof Float size) {
                float fin = Math.min(1, e.finpow() * 5);

                for(int i = 0; i < 4; ++i) {
                    float a = (float)(45 + 90 * i);
                    float z = Draw.z();
                    Draw.z(58);
                    Draw.color(EUGet.MIKU);
                    Drawf.tri(e.x, e.y, 2 * size * e.foutpow(), 8 * size * fin, a);
                    Draw.z(59);
                    Draw.color(Color.black);
                    Drawf.tri(e.x, e.y, 2 * size * e.foutpow(), 7.5f * size * fin, a);
                    Draw.z(z);
                }
            }

        });
    }
}
