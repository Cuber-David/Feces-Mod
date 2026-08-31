package K.content.Fx.OtherEffects;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.core.Renderer;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static K.content.extend.Math.kangles.lineVector;
import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

public class KaiEffect extends Effect {
    public KaiEffect(float t){
        super(60,e -> {
            Draw.z(200);
            color(Color.white, Pal.lightPyraFlame, Color.red, e.fin());
            randLenVectors(e.id, 1, e.finpow() * t, e.rotation+120, 3f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 1.5f * e.fout() * Math.min(t / 15,9));
                color(Color.red, Pal.redSpark, Color.red, e.fin());
                randLenVectors(e.id, 1, e.finpow() * t/8, (x1, y1) -> {
                    Fill.circle(e.x + x + x1, e.y + y + y1, 0.5f * e.fout() * Math.min(t / 15,9));
                });
            });
            color(Color.white, Pal.lightPyraFlame, Color.red, e.fin());
            randLenVectors(e.id, 1, e.finpow() * t, e.rotation-120, 3f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 1.5f * e.fout() * Math.min(t / 15,9));
                color(Color.red, Pal.redSpark, Color.red, e.fin());
                randLenVectors(e.id, 1, e.finpow() * t/8, (x1, y1) -> {
                    Fill.circle(e.x + x + x1, e.y + y + y1, 0.5f * e.fout() * Math.min(t / 15,9));
                });
            });
            color(Color.white, Pal.lightPyraFlame, Color.red, e.fin());
            lineVector(e.id,1,(2*t-20)*e.finpow(),e.rotation+177+ Mathf.random(6),(x, y) -> {
                Fill.circle(e.x + x, e.y + y,  1.5f * e.fout() * Math.min(t / 15,9));
                color(Color.red, Pal.redSpark, Color.red, e.fin());
                randLenVectors(e.id, 1, e.finpow() * t, (x1, y1) -> {
                    Fill.circle(e.x + x1, e.y + y1, 0.5f * e.fout() * Math.min(t / 15,9));
                });
            });
        });
    }
}
