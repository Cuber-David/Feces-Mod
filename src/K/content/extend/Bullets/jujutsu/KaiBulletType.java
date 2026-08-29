package K.content.extend.Bullets.jujutsu;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;

import static K.content.extend.Math.kangles.lineVector;
import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

public class KaiBulletType extends BulletType {
    public KaiBulletType(float hs){
        super(100,10000);
        lifetime = hs/4f;
        hitSize = hs;
        despawnShake = hitShake = 100;
        despawnHit = true;
        splashDamage = 10000;
        splashDamageRadius = 1000;
        hitEffect = despawnEffect = new Effect(120, e -> {
            Draw.z(200);
            color(Pal.lightPyraFlame, Pal.darkPyraFlame, e.fin());
            randLenVectors(e.id,(int)hitSize*6,hitSize*3,(x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.15f*hitSize + e.fout() * 1.6f);
            });
        });
    }

    @Override
    public void draw(Bullet b) {
        new Effect(120 ,e -> {
            color(Pal.lightPyraFlame, Pal.darkPyraFlame, e.fin());
            randLenVectors(e.id,1,hitSize/2, b.rotation(), (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.15f*hitSize + e.fout() * 1.6f);
            });
            lineVector(e.id,2,hitSize,b.rotation(),(x,y) -> {
                Fill.circle(e.x + x, e.y + y, 0.15f*hitSize + e.fout() * 1.6f);
            });
        }).at(b.x,b.y,b.rotation());
    }
}
