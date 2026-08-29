package K.content.extend.Bullets.jujutsu;

import K.content.Fx.KFx;
import K.content.sounds;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Rand;
import arc.util.Tmp;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;

import static arc.graphics.g2d.Lines.circleVertices;
import static arc.math.Angles.randLenVectors;

public class RedBulletType extends BulletType {
    public RedBulletType(){
        super(0,10000);
        damage = 10000;
        shootEffect = KFx.Shcokcharge;
        hitSize = 1601;
        drag = 888;
        collides =  false;
        lifetime = 50;
        pierce = true;
        despawnSound = sounds.desnukehit;
        despawnEffect = new Effect(60 ,e -> {
            Rand rand = new Rand();
            float rad = 150f;
            rand.setSeed(e.id);
            Draw.color(Color.white, Color.red, e.fin() + 0.6f);
            float circleRad = e.fin(Interp.circleOut) * rad * 4f;
            Lines.stroke(12 * e.fout());
            Lines.circle(e.x, e.y, circleRad);
            Draw.blend(Blending.additive);
            Draw.z(Layer.effect + 0.1f);
            Fill.light(e.x, e.y, circleVertices(circleRad), circleRad, Color.clear, Tmp.c1.set(Draw.getColor()).a(e.fout(Interp.pow10Out)));
            Draw.blend();
            Draw.z(Layer.effect);
            float intensity = 40f;
            Draw.color(Color.red, 0.15f);
            for(int i = 0; i < 4; i++){
                rand.setSeed(e.id* 2L + i);
                float lenScl = rand.random(0.5f, 1f);
                int fi = i;
                e.scaled(e.lifetime * lenScl, s -> {
                    randLenVectors(s.id + fi - 1, s.fin(Interp.pow10Out), (int)(2.9f * intensity), 10f * intensity, (x, y, in, out) -> {
                        float fout = s.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                        float radi = fout * ((2f + intensity) * 2.35f);

                        Fill.circle(e.x + x, e.y + y, radi);
                        Drawf.light(e.x + x, e.y + y, radi * 4.5f, s.color, 0.5f);
                    });
                });
            }
            Draw.reset();
        });
    }

    @Override
    public void draw(Bullet b) {
    }

    @Override
    public void update(Bullet b) {
        super.update(b);
        if(b.time>=b.lifetime-1) Damage.damage(b.team,b.x,b.y,b.hitSize*4,b.damage);
    }
}
