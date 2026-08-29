package K.content.extend.Bullets.jujutsu;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.graphics.Pal;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

public class KaiBulletType extends BulletType {
    public KaiBulletType(){
        super(100,10000);
        hitSize = 20;
        despawnShake = hitShake = 100;
        despawnHit = true;
        splashDamage = 10000;
        splashDamageRadius = 1000;
        hitEffect = despawnEffect = new Effect(120, e -> {
            Draw.z(200);
            color(Pal.lightPyraFlame, Pal.darkPyraFlame, e.fin());
            randLenVectors(e.id,1000,500,(x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f);
            });
        });
    }
}
