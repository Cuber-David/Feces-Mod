package K.content.Fx;

import K.content.Fx.OtherEffects.FragmentExplosionEffect;
import K.content.extend.util.DrawFunc;
import K.content.extend.util.DrawPurple;
import K.content.sounds;
import K.graphics.MainRenderer;
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
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import static arc.graphics.Color.alpha;
import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.renderer;

public class KFx {
    public static final Vec2 v = new Vec2();
    public static final Rand rand = new Rand();
    public static Effect Bigcasing,shootBig,hitBulletBigger,hitLaserBigger,PulseCharge,PulseChargeBegin,PulseShoot,
            BigExplosion,collapserExplode,ReactorExplosion,Thunder,Hugebeam,Feceswave,
            endHitRedSmall,ellipsetrail,ellipsetrailblue,disorder,bp,fee,slash,exp,orbitred,
            purpleexp,purpleb,Shcokcharge,vortexWarpEffect;

    public static void load(){
        Bigcasing = new Effect(30f, e -> {
            color(Pal.lightOrange, Color.lightGray, Pal.lightishGray, e.fin());
            alpha(e.fout(0.3f));
            float rot = Math.abs(e.rotation) + 90f;
            int i = -Mathf.sign(e.rotation);

            float len = (2f + e.finpow() * 6f) * i;
            float lr = rot + e.fin() * 30f * i;
            Fill.rect(
                    e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 30f * e.fin()),
                    e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 30f * e.fin()),
                    1f, 2f, rot + e.fin() * 50f * i
            );

        }).layer(Layer.bullet);
        shootBig = new Effect(8, e -> {
            color(Pal.lighterOrange, Pal.lightOrange, e.fin());
            float w = 10f + 50 * e.fout();
            Drawf.tri(e.x, e.y, w, 150f * e.fout(), e.rotation);
            Drawf.tri(e.x, e.y, w, 30f * e.fout(), e.rotation + 90f);
        });
        hitBulletBigger = new Effect(140, e -> {
            color(Color.white, Pal.lightOrange, e.fin());

            e.scaled(140f, s -> {
                stroke(5.0f + s.fout());
                Lines.circle(e.x, e.y, s.fin() * 50f);
            });

            stroke(5.0f + e.fout());

            randLenVectors(e.id, 50, e.fin() * 15f, (x, y) -> {
                float ang = Mathf.angle(x, y);
                lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
            });

            Drawf.light(e.x, e.y, 200f, Pal.lightOrange, 0.6f * e.fout());
        });
        hitLaserBigger = new Effect(140, e -> {
            color(Color.valueOf("a1f7ab"), Color.valueOf("bdf9c4"), e.fin());

            e.scaled(140f, s -> {
                stroke(5.0f + s.fout());
                Lines.circle(e.x, e.y, s.fin() * 50f);
            });

            stroke(5.0f + e.fout());

            randLenVectors(e.id, 50, e.fin() * 15f, (x, y) -> {
                float ang = Mathf.angle(x, y);
                lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
            });

            Drawf.light(e.x, e.y, 200f, Color.valueOf("bdf9c4"), 0.6f * e.fout());
        });
        PulseCharge = new Effect(38f, e -> {
            color(Color.valueOf("673931"));

            randLenVectors(e.id, 14, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 1f);
            });
        });
        PulseChargeBegin = new Effect(60f, e -> {
            float margin = 1f - Mathf.curve(e.fin(), 0.9f);
            float fin = Math.min(margin, e.fin());

            color(Color.valueOf("673931"));
            Fill.circle(e.x, e.y, fin * 3f);

            color();
            Fill.circle(e.x, e.y, fin * 2f);
        });
        PulseShoot = new Effect(24f, e -> {
            e.scaled(10f, b -> {
                color(Color.valueOf("673931"), Color.white, b.fin());
                stroke(b.fout() * 3f + 0.2f);
                Lines.circle(b.x, b.y, b.fin() * 50f);
            });

            color(Color.valueOf("673931"));

            for(int i : Mathf.signs){
                Drawf.tri(e.x, e.y, 13f * e.fout(), 85f, e.rotation + 90f * i);
            }
        });
        BigExplosion = new Effect(30, 500f, b -> {
            float intensity = 8f;
            float baseLifetime = 25f + intensity * 15f;
            b.lifetime = 50f + intensity * 64f;

            color(Color.valueOf("878787"));
            alpha(0.8f);
            for(int i = 0; i < 5; i++){
                rand.setSeed(b.id*2 + i);
                float lenScl = rand.random(0.25f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> {
                    randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.8f * intensity), 25f * intensity, (x, y, in, out) -> {
                        float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                        float rad = fout * ((2f + intensity) * 2.35f);

                        Fill.circle(e.x + x, e.y + y, rad);
                        Drawf.light(e.x + x, e.y + y, rad * 2.6f, Color.valueOf("878787"), 0.7f);
                    });
                });
            }

            b.scaled(baseLifetime, e -> {
                Draw.color();
                e.scaled(5 + intensity * 2f, i -> {
                    stroke((3.1f + intensity/5f) * i.fout());
                    Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                    Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
                });

                color(Color.white, Pal.lighterOrange, e.fin());
                stroke((2f * e.fout()));

                Draw.z(Layer.effect + 0.001f);
                randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(8 * intensity), 30f * intensity, (x, y, in, out) -> {
                    lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (4f + intensity));
                    Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
                });
            });
        });
        collapserExplode = new Effect(300F, 1600f, e -> {
            float rad = 150f;
            rand.setSeed(e.id);

            Draw.color(Color.purple, e.color, e.fin() + 0.6f);
            float circleRad = e.fin(Interp.circleOut) * rad * 4f;
            Lines.stroke(12 * e.fout());
            Lines.circle(e.x, e.y, circleRad);
            for (int i = 0; i < 24; i++) {
                Tmp.v1.set(1, 0).setToRandomDirection(rand).scl(circleRad);
                DrawFunc.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, rand.random(circleRad / 16, circleRad / 12) * e.fout(), rand.random(circleRad / 4, circleRad / 1.5f) * (1 + e.fin()) / 2, Tmp.v1.angle() - 180);
            }

            Draw.blend(Blending.additive);
            Draw.z(Layer.effect + 0.1f);
            Fill.light(e.x, e.y, circleVertices(circleRad), circleRad, Color.clear, Tmp.c1.set(Draw.getColor()).a(e.fout(Interp.pow10Out)));
            Draw.blend();
            Draw.z(Layer.effect);

            e.scaled(120f, i -> {
                Draw.color(Color.purple, i.color, i.fin() + 0.4f);
                Fill.circle(i.x, i.y, rad * i.fout());
                Lines.stroke(18 * i.fout());
                Lines.circle(i.x, i.y, i.fin(Interp.circleOut) * rad * 1.2f);
                Angles.randLenVectors(i.id, 40, rad / 3, rad * i.fin(Interp.pow2Out), (x, y) -> {
                    lineAngle(i.x + x, i.y + y, Mathf.angle(x, y), i.fslope() * 25 + 10);
                });
                Angles.randLenVectors(i.id, (int) (rad / 4), rad / 6, rad * (1 + i.fout(Interp.circleOut)) / 1.5f, (x, y) -> {
                    float angle = Mathf.angle(x, y);
                    float width = i.foutpowdown() * rand.random(rad / 6, rad / 3);
                    float length = rand.random(rad / 2, rad * 5) * i.fout(Interp.circleOut);
                    Draw.color(i.color);
                    DrawFunc.tri(i.x + x, i.y + y, width, rad / 3 * i.fout(Interp.circleOut), angle - 180);
                    DrawFunc.tri(i.x + x, i.y + y, width, length, angle);
                    Draw.color(Color.black);
                    width *= i.fout();
                    DrawFunc.tri(i.x + x, i.y + y, width / 2, rad / 3 * i.fout(Interp.circleOut) * 0.9f * i.fout(), angle - 180);
                    DrawFunc.tri(i.x + x, i.y + y, width / 2, length / 1.5f * i.fout(), angle);
                });
                Draw.color(Color.black);
                Fill.circle(i.x, i.y, rad * i.fout() * 0.75f);
            });

            Drawf.light(e.x, e.y, rad * e.fout(Interp.circleOut) * 4f, e.color, 0.7f);
        }).layer(Layer.effect + 0.001f);
        ReactorExplosion = new Effect(30, 500f, b -> {
            float intensity = 8f;
            float baseLifetime = 25f + intensity * 30f;
            b.lifetime = 50f + intensity * 10f;

            color(Color.valueOf("878787"));
            alpha(0.8f);
            for(int i = 0; i < 100; i++){
                rand.setSeed(b.id*2 + i);
                float lenScl = rand.random(0.25f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> {
                    randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.8f * intensity), 50f * intensity, (x, y, in, out) -> {
                        float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                        float rad = fout * ((2f + intensity) * 2.35f);

                        Fill.circle(e.x + x, e.y + y, rad);
                        Drawf.light(e.x + x, e.y + y, rad * 2.6f, Color.valueOf("878787"), 0.7f);
                    });
                });
            }

            b.scaled(baseLifetime, e -> {
                Draw.color();
                e.scaled(5 + intensity * 2f, i -> {
                    stroke((2.1f + intensity/5f) * i.fout());
                    Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                    Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
                });

                color(Color.white, Pal.lighterOrange, e.fin());
                stroke((2f * e.fout()));

                Draw.z(Layer.effect + 0.001f);
                randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(8 * intensity), 30f * intensity, (x, y, in, out) -> {
                    lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (4f + intensity));
                    Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
                });
            });
        });
        Thunder = new Effect(30,e -> {
            float l = 1024;
            float r0,r10;
            r0 = rand.random(70f, 110f);
            r10 = rand.random(0, 20f) - 10;
            if(r10 < 0){ r10 = r10 - 20;}
            else {r10 = r10 + 20;}
            float r11 = r10 + r0;
            float r = r0 * Mathf.degRad;
            float r1 = r11 * Mathf.degRad;
            float x1 = (float) (e.x + Math.cos(r) * l);
            float y1 = (float) (e.y + Math.sin(r) * l);
            float x2 = (float) (x1 + Math.cos(r1) * l);
            float y2 = (float) (y1 + Math.sin(r1) * l);
            stroke(10, Color.white);
            line(x1, y1, e.x, e.y);renderer.lights.line(x1, y1, e.x, e.y, 30, Color.white, 3f);stroke(10, Color.white);
            line(x1, y1, x2, y2);renderer.lights.line(x1, y1, x2, y2, 30, Color.white, 3f);
            e.lifetime = 0;
        });
        Hugebeam = new Effect(360,e -> {
            if(e.time > 324&&e.fin()!=1){
                float e1=(360-e.time)/36;
                if(e1>1){e1=1;}
                stroke(180*e1*10,Color.white.a(e1));
                line(e.x,e.y+80*e1,e.x,e.y+1920);
                renderer.lights.line(e.x,e.y+600,e.x,e.y+1920,600*e1,Color.white, 3f);stroke(10, Color.white);
                Fill.circle(e.x,e.y,90*e1);
                stroke(10*e1,Color.white.a(0.2f*e1));
                Fill.circle(e.x,e.y,260*e1);
                Drawf.light(e.x,e.y,600*e1,Color.red,1);
            } else if (e.time<=324&e.time>=322) {
                sounds.expr.at(new Position() {
                    @Override
                    public float getX() {
                        return e.x;
                    }

                    @Override
                    public float getY() {
                        return e.y;
                    }
                },1,10);
            } else if (e.time>144&&e.time<322){
                stroke(180,Color.white);
                line(e.x,e.y+80,e.x,e.y+1920);
                renderer.lights.line(e.x,e.y+600,e.x,e.y+1920,600,Color.white,3f);stroke(10, Color.white);
                Drawf.tri(e.x+90,e.y+2000,240f,2000f,e.rotation + 270);
                Drawf.tri(e.x-90,e.y+2000,240f,2000f,e.rotation + 270);
                Fill.circle(e.x,e.y,90);
                stroke(10,Color.white.a(0.2f));
                Fill.circle(e.x,e.y,260);
                Drawf.light(e.x,e.y,600,Color.red,1);
            } else if (e.time<=144 && e.time!=0){
                float e2 = e.time/144;
                stroke(180*e2,Color.white.a(e2));
                line(e.x,e.y+80*e2,e.x,e.y + 1920);
                renderer.lights.line(e.x, e.y + 600, e.x, e.y + 1920, 600*e2, Color.white, 3f);stroke(10, Color.white);
                Fill.circle(e.x,e.y,90*e2);
                stroke(10 * e2, Color.white.a(0.2f*e2));
                Fill.circle(e.x,e.y,260*e2);
                Drawf.light(e.x,e.y,600*e2,Color.red,1);
            }
        });
        Feceswave = new Effect(60,e -> {
            if(e.fin()<0.8) {
                Lines.stroke(25,Color.valueOf("673931"));
                Fill.circle(e.x, e.y, 32 * (e.fin()));
                Lines.stroke(25,Color.black.a(1));
                Fill.circle(e.x, e.y, 24 * (e.fin()));
            }
            if(e.fin()>=0.8){
                Lines.stroke(25,Color.valueOf("673931"));
                Fill.circle(e.x, e.y, 32 * 5*(1-e.fin()));
                Lines.stroke(25,Color.black.a(1));
                Fill.circle(e.x, e.y, 24 * 5*(1-e.fin()));
            }
        });
        endHitRedSmall = new Effect(15f, e -> {
            e.scaled(e.lifetime / 2f, s -> {
                color(Color.valueOf("f53036"), Color.valueOf("ff786e"), s.fin());
                Lines.stroke(2f * s.fout());
                Lines.circle(e.x, e.y, 10f * s.fin());
            });

            color(Color.valueOf("ff786e"),Color.valueOf("f53036"), e.fin());

            Angles.randLenVectors(e.id, 7, e.fin(Interp.pow3Out) * 20f, (x, y) -> {
                float ang = Mathf.angle(x, y);
                Lines.stroke(e.fout());
                Lines.lineAngle(e.x + x, e.y + y, ang, e.fout(Interp.pow5In) * 12f);
            });
        });
        ellipsetrail = new Effect(30,e -> {
            color(Color.white);
            Lines.stroke(0.2f,Color.white.a(1-e.fin()));
            Lines.ellipse(e.x,e.y,0.2f,10,7,e.rotation + 90);
        });
        ellipsetrailblue = new Effect(30,e -> {
            color(Color.valueOf("33ffff"));
            Lines.stroke(0.2f,Color.valueOf("33ffff").a(1-e.fin()));
            Lines.ellipse(e.x,e.y,0.2f,10,7,e.rotation + 90);
        });
        disorder = new Effect(20f, e -> {
            color(Color.valueOf("#F7B080"), Color.valueOf("#915923"), e.fin());

            stroke(e.fout() * 2.4f);
            randLenVectors(e.id, 4, 7 + 50 * e.fin(), (x, y) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 5 + 1);
            });

            color(Color.gray, Color.darkGray, e.fin());
            randLenVectors(e.id, 3, 3 + 28 * e.fin(), (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4f));
        });
        bp = new Effect(120, e -> {
            e.color = Color.black;
            color(Tmp.c1.set(e.color).mul(1.1f));
            Fill.circle(e.x, e.y, 640*Math.min(1,(1-e.fout())/0.2f)*1);
        }){{
            layer = 55f;
            clip = 2500f;
        }};
        fee = new FragmentExplosionEffect();
        slash = new Effect(24, e -> {
            Draw.color(KPal.darkRed);
            Drawf.tri(e.x,e.y,8*e.fout(),128*e.fout(),e.rotation+90);
            Drawf.tri(e.x,e.y,8*e.fout(),128*e.fout(),e.rotation+270);
            Draw.color(Color.black);
            Drawf.tri(e.x,e.y,6*e.fout(),96*e.fout(),e.rotation+90);
            Drawf.tri(e.x,e.y,6*e.fout(),96*e.fout(),e.rotation+270);
            Angles.randLenVectors(e.id, 3, 16 * e.finpow(), e.rotation, 360, (x, y) -> Fill.poly(e.x + x, e.y + y, 3, 10 * e.foutpow(), Mathf.randomSeed(e.id, 360) + e.time));
        }).layer(Layer.max - 20);
        exp = new Effect(96,e -> {{
            int tf = 16;
            float pro = e.fin()/1;
            int fi = (int)(pro*tf);
            fi = Math.min(fi,tf-1);
            TextureRegion[] frames = new TextureRegion[tf];
            for (int i=0;i<tf;i++)
            {
                String path = "kmod-"+"e-" + (i+1);
                frames[i] = new TextureRegion(Core.atlas.find(path));
            }
            TextureRegion frame = frames[fi];
            if (frame != null){
                float alpha = Interp.pow2Out.apply(1-e.fout());
                float scale = 0.5f + 0.5f*alpha;
                float s = 100;
                Draw.z(Layer.effect);
                Draw.color(e.color);
                Draw.rect(frame,e.x,e.y,s,s,e.rotation);
                Draw.color();
            }
        }});
        orbitred = new Effect(60,e->{
            float p = e.fin();
            float x = e.x;
            float y = e.y;
            for (int i = 0; i < 10; i++) {
                float angle = Mathf.random() * 360f + Time.time * 0.1f;
                float radius;
                float rand = Mathf.random();
                if (rand < 0.5f) {
                    radius = 20f + Mathf.random() * 20f;
                } else if (rand < 0.8f) {
                    radius = 40f + Mathf.random() * 20f;
                } else {
                    radius = 60f + Mathf.random() * 20f;
                }
                float px = x + Angles.trnsx(angle, radius);
                float py = y + Angles.trnsy(angle, radius);
                float length = 3f + Mathf.random() * 4f;
                length *= (1f - p * 0.5f);
                float thickness = 0.5f + Mathf.random() * 0.8f;
                thickness *= (1f - p * 0.5f);
                if (length < 0.3f) continue;
                float alpha = (1f - p) * 0.8f;
                float rot = angle + 90f;
                Draw.color(Color.valueOf("87040c"), alpha);
                Draw.rect("white", px, py, length, thickness, rot);
            }
            Draw.reset();
        });
        purpleexp = new Effect(120,e -> {
            Rand rand = new Rand();
            float rad = 600f;
            rand.setSeed(e.id);
            for (int i = 0; i < 240; i++) {
                Tmp.v1.set(1, 0).setToRandomDirection(rand).scl(rad);
                DrawFunc.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, rand.random(rad / 16, rad / 12) * e.fout(), rand.random(rad*3.5f, rad*5.5f) * (1 + e.fin()) / 2, Tmp.v1.angle());
            }
            Draw.blend(Blending.additive);
            Draw.z(Layer.effect + 0.1f);
            Fill.light(e.x, e.y, circleVertices(rad), rad, Color.clear, Tmp.c1.set(Draw.getColor()).a(e.fout(Interp.pow10Out)));
            Draw.color(Color.white, Color.valueOf("f1ccf7"), e.fin() + 0.6f);
            float circleRad = e.fin(Interp.circleOut) * rad * 4f;
            Lines.stroke(12 * e.fout());
            Lines.circle(e.x, e.y, circleRad);
            Draw.blend(Blending.additive);
            Draw.z(Layer.effect + 0.1f);
            Fill.light(e.x, e.y, circleVertices(circleRad), circleRad, Color.clear, Tmp.c1.set(Draw.getColor()).a(e.fout(Interp.pow10Out)));
            Draw.blend();
            Draw.z(Layer.effect);
        }){{clip = 2000;}};
        purpleb = new Effect(120, e -> {
            float progress = e.fin();
            Color color = Color.purple;

            // 半径逐渐扩大
            float radius = 1600 * Interp.pow2Out.apply(progress);

            // 颜色保持鲜艳，最后10%淡出
            float alpha;
            if (progress < 0.9f) {
                alpha = 1f;
            } else {
                alpha = 1f - (progress - 0.9f) / 0.1f;
            }

            float rotation = Time.time / 50f; // 横向旋转速度

            Draw.blend(Blending.additive);

            // ===== 1. 发光光晕 =====
            Draw.color(color, alpha * 0.15f);
            Fill.circle(e.x, e.y, radius * 1.8f);

            // ===== 2. 球体核心（通过多个椭圆叠加形成立体球） =====
            int layers = 16;
            for (int i = 0; i < layers; i++) {
                float layerAngle = (float)i / layers * 180f + rotation * 30f;
                float layerDepth = Mathf.sinDeg(layerAngle); // -1 到 1
                float layerAlpha = 0.3f + 0.7f * (0.5f + 0.5f * layerDepth);

                // 椭圆半径：水平方向随深度变化
                float rx = radius * (0.6f + 0.4f * Mathf.cosDeg(layerAngle));
                float ry = radius * 0.5f * Mathf.sinDeg(layerAngle);
                ry = Math.abs(ry) + radius * 0.05f;

                Draw.color(color, alpha * 0.25f * layerAlpha);
                Fill.rect(e.x, e.y + ry * 0.3f, rx * 1.6f, ry * 1.2f, 0f);
            }

            // ===== 3. 球体主体（实心球） =====
            Draw.color(color, alpha * 0.7f);
            Fill.circle(e.x, e.y, radius * 0.85f);

            // ===== 4. 横向旋转条纹（球体表面的环） =====
            Draw.color(color, alpha * 0.5f);
            Lines.stroke(2.5f);

            int rings = 6;
            for (int i = 0; i < rings; i++) {
                float ringPos = (float)i / rings * 180f + rotation * 20f;
                float ringDepth = Mathf.cosDeg(ringPos); // -1 到 1

                // 环的宽度随深度变化（边缘窄，中间宽）
                float ringWidth = 0.3f + 0.7f * (0.5f + 0.5f * ringDepth);
                float ringY = e.y + radius * 0.8f * Mathf.sinDeg(ringPos);
                float ringRadius = radius * 0.8f * Mathf.cosDeg(ringPos);
                ringRadius = Math.abs(ringRadius) + radius * 0.05f;

                // 绘制环（用椭圆弧）
                float arcAngle = (float)i / rings * 180f + rotation * 30f;
                float startAngle = arcAngle;
                float endAngle = arcAngle + 180f;

                Draw.alpha(alpha * 0.4f * (0.3f + 0.7f * (0.5f + 0.5f * ringDepth)));

                // 用多个线段绘制弧
                int segments = 20;
                for (int j = 0; j < segments; j++) {
                    float segAngle = startAngle + (float)j / segments * (endAngle - startAngle);
                    float segAngleNext = startAngle + (float)(j + 1) / segments * (endAngle - startAngle);

                    float sx = e.x + Mathf.cosDeg(segAngle) * ringRadius;
                    float sy = ringY + Mathf.sinDeg(segAngle) * ringRadius * 0.3f;
                    float sxn = e.x + Mathf.cosDeg(segAngleNext) * ringRadius;
                    float syn = ringY + Mathf.sinDeg(segAngleNext) * ringRadius * 0.3f;

                    Lines.line(sx, sy, sxn, syn);
                }
            }

            // ===== 5. 经线（纵向条纹） =====
            Draw.color(color, alpha * 0.3f);
            Lines.stroke(1.5f);

            int meridianCount = 8;
            for (int i = 0; i < meridianCount; i++) {
                float angle = (float)i / meridianCount * 360f + rotation * 15f;

                float x1 = e.x + Mathf.cosDeg(angle) * radius * 0.8f;
                float y1 = e.y + Mathf.sinDeg(angle) * radius * 0.3f;
                float x2 = e.x + Mathf.cosDeg(angle + 180) * radius * 0.8f;
                float y2 = e.y + Mathf.sinDeg(angle + 180) * radius * 0.3f;

                float depthAlpha = 0.3f + 0.7f * (0.5f + 0.5f * Mathf.sinDeg(angle + 90));
                Draw.alpha(alpha * 0.25f * depthAlpha);
                Lines.line(x1, y1, x2, y2);
            }

            // ===== 6. 高光（3D光照效果） =====
            float lightAngle = rotation * 30f;
            float lx = e.x + Mathf.cosDeg(lightAngle) * radius * 0.5f;
            float ly = e.y + Mathf.sinDeg(lightAngle) * radius * 0.3f - radius * 0.15f;

            // 主高光
            Draw.color(Color.white, alpha * 0.5f);
            Fill.rect(lx, ly, radius * 0.6f, radius * 0.35f, lightAngle);

            // 次级高光
            Draw.color(Color.white, alpha * 0.2f);
            Fill.rect(lx - radius * 0.1f, ly + radius * 0.15f, radius * 0.8f, radius * 0.4f, lightAngle + 20);

            // ===== 7. 边缘光 =====
            Draw.color(color, alpha * 0.3f);
            Lines.stroke(1.5f);
            Lines.circle(e.x, e.y, radius * 0.85f);

            // ===== 8. 外发光 =====
            Draw.color(color, alpha * 0.1f);
            Lines.stroke(3f);
            Lines.circle(e.x, e.y, radius * 1.1f);

            Draw.blend();
            Draw.reset();
        });
        Shcokcharge = new Effect(60f, 100f, e -> {
            TextureRegion ch = Core.atlas.find("kmod-charge");
            float r = 2*e.time*360/e.lifetime;
            float s =1000;
            float st = e.fin()*s;
            Draw.color(Color.red.a(0.5f*(e.fout()-0.2f)));
            Lines.stroke(40);
            Lines.circle(e.x,e.y,1000*e.fout());
            Lines.circle(e.x,e.y,500*(e.fout()-0.2f));
            if(e.time<=e.lifetime/2) {
                for (int i = 0; i < 3; i++) {
                    Draw.color(Color.red.a(e.time * 2 / e.lifetime));
                    Draw.rect(ch, e.x, e.y, s, s, r+i*120);
                    Draw.rect(ch, e.x, e.y, s/8, s, (r+60+i*120)*(-1));
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    Draw.color(Color.red.a(1-((e.time-e.lifetime/2) * 2 / e.lifetime)));
                    Draw.rect(ch, e.x, e.y, s/st, s/st, r+i*120);
                    Draw.rect(ch, e.x, e.y, s/(st*8), s/st, (r+60+i*120)*(-1));
                }
            }
            Draw.reset();
        });
        vortexWarpEffect = new Effect(1f, e -> {
            float x = e.x;
            float y = e.y;
            DrawPurple.drawp(x,y,e.rotation);
        });
    }
}
