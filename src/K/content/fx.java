package K.content;

import K.util.DrawFunc;
import arc.Settings;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.util.Tmp;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static arc.graphics.Color.alpha;
import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

public class fx {
    public static final Rand rand = new Rand();
    public static Effect Bigcasing,shootBig,hitBulletBigger,hitLaserBigger,PulseCharge,PulseChargeBegin,PulseShoot,
            BigExplosion,collapserExplode,ReactorExplosion;

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

            if (true) {
                Draw.blend(Blending.additive);
                Draw.z(Layer.effect + 0.1f);

                Fill.light(e.x, e.y, circleVertices(circleRad), circleRad, Color.clear, Tmp.c1.set(Draw.getColor()).a(e.fout(Interp.pow10Out)));
                Draw.blend();
                Draw.z(Layer.effect);
            }


            e.scaled(120f, i -> {
                Draw.color(Color.purple, i.color, i.fin() + 0.4f);
                Fill.circle(i.x, i.y, rad * i.fout());
                Lines.stroke(18 * i.fout());
                Lines.circle(i.x, i.y, i.fin(Interp.circleOut) * rad * 1.2f);
                Angles.randLenVectors(i.id, 40, rad / 3, rad * i.fin(Interp.pow2Out), (x, y) -> {
                    lineAngle(i.x + x, i.y + y, Mathf.angle(x, y), i.fslope() * 25 + 10);
                });

                if (true)
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
    }
}
