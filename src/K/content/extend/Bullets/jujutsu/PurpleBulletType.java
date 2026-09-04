package K.content.extend.Bullets.jujutsu;

import K.content.Fx.KFx;
import K.content.extend.util.DrawFunc;
import K.content.sounds;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;

import static K.content.Fx.KFx.rand;
import static K.content.Fx.OtherEffects.SphereEffect.createDenseParticleSphereEffect;

public class PurpleBulletType extends BulletType {
    public PurpleBulletType(float h){
        hitSize = h;
        lifetime = 3;
        pierce = true;
        speed=0;
        damage=888888;
        collides = false;
        despawnEffect=hitEffect=new Effect(121,e -> {
            if (e.time<1) createDenseParticleSphereEffect(Color.purple,hitSize*10,120).at(e.x,e.y);
            if (e.time>120) {
                KFx.purpleexp.at(e.x, e.y);
                sounds.desnukehit.at(e.x,e.y,1,10);
            }
            if (e.time>110) {
                Draw.z(220);
                Draw.color(Color.black);
                Fill.circle(e.x,e.y,100000);
                Draw.color(Color.white);
                Fill.circle(e.x,e.y,1000);
                for (int i = 0; i < 240; i++) {
                    Tmp.v1.set(1, 0).setToRandomDirection(rand).scl(1000);
                    DrawFunc.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, rand.random(1000 / 16, 1000 / 12) * e.fout(), rand.random(1000*3.5f, 1000*5.5f) * (1 + e.fin()) / 2, Tmp.v1.angle());
                }
                Draw.reset();
            }
        }){{clip = 2000;}};
    }
    @Override
    public void despawned(Bullet b) {
        Damage.damage(b.team,b.x,b.y,b.hitSize*320,b.damage);
        for (int i = 0; i < 90; i++) {
            Lightning.create(b.team,Color.valueOf("f1ccf7"),10000,b.x,b.y, Mathf.random(360),Mathf.random(1600));
        }
        b.shooter.remove();
        sounds.purple.at(b.x,b.y,1,10);
        createDamageField(b.x,b.y,b.team,1000,25*hitSize,120);
        super.despawned(b);
    }

    private void createDamageField(float x, float y, Team team, float damage, float radius, int duration) {
        // 使用 Time.run 实现循环伤害
        for (int i = 0; i < duration; i += 5) { // 每5帧造成一次伤害
            int finalI = i;
            Time.run(i, () -> {
                // 对范围内敌人造成伤害
                Units.nearbyEnemies(team, x, y, radius, unit -> {
                    float dist = unit.dst(x, y);
                    if (dist <= radius) {
                        // 距离越近伤害越高
                        float damageMultiplier = 1f - (dist / radius) * 0.5f;
                        unit.damage(damage * damageMultiplier / 12f); // 每帧伤害
                    }
                });
            });
        }
    }
}
