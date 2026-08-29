package K.content.Fx.OtherEffects;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.entities.Effect;
import mindustry.graphics.Pal;

import static K.content.extend.Math.kangles.lineVector;
import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

public class KaiEffect extends Effect {
    public KaiEffect(float t){
        super(60,e -> {
            Draw.z(200);
            color(Pal.lightPyraFlame, Pal.darkPyraFlame, e.fin());

            randLenVectors(e.id, 1, e.finpow() * 70f+t, e.rotation+120, 10f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f + Math.min(t / 15,9));
            });
            randLenVectors(e.id, 1, e.finpow() * 70f+t, e.rotation-120, 10f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f + Math.min(t / 15,9));
            });
            lineVector(e.id,1,(2*t-20)*e.finpow(),e.rotation+175+ Mathf.random(10),(x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f + Math.min(t / 15,9));
            });
        });
    }
}
