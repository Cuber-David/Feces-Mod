package K.content.extend.Bullets;

import K.content.extend.util.DrawFunc;
import K.content.sounds;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.util.Tmp;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Layer;

import static arc.graphics.g2d.Lines.circleVertices;

public class PurpleBulletType extends BulletType {
    public PurpleBulletType(){
        lifetime = 3;
        speed=0;
        damage=888888;
        collides = false;
        despawnEffect=hitEffect=new Effect(120,e -> {
            Rand rand = new Rand();
            float rad = 300f;
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
        });
    }
    @Override
    public void despawned(Bullet b) {
        Damage.damage(b.team,b.x,b.y,b.hitSize*320,b.damage);
        for (int i = 0; i < 90; i++) {
            Lightning.create(b.team,Color.valueOf("f1ccf7"),10000,b.x,b.y, Mathf.random(360),Mathf.random(1600));
        }
        b.shooter.remove();
        sounds.purple.at(b.x,b.y,1,10);
        super.despawned(b);
    }
}
