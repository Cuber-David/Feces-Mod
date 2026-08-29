package K.content.extend.Bullets.jujutsu;

import K.content.Fx.KFx;
import K.content.effects.SpecialDeathEffects;
import K.Other_mod.FM.flame_extend.EmpathyDamage;
import K.content.extend.util.Utils;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class SlashBulletType extends BulletType {
    public SlashBulletType(){
        super(100,1000);
        lifetime = 30;
        hitSize = 24;
        despawnEffect = Fx.none;
        hitEffect = KFx.slash;
        pierce = true;
        pierceCap = 4;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);
    }

    public void despawned(Bullet b){
        float randlength = 100;
        float r = Mathf.random(360);
        float x = Mathf.random(randlength)-randlength/2;
        float y = Mathf.random(randlength)-randlength/2;
        KFx.slash.at(b.x+x,b.y+y,r);
        super.despawned(b);
    }

    @Override
    public void hit(Bullet b) {
        Units.nearbyEnemies(b.team,b.x,b.y,32,unit -> {
            unit.damage(b.damage);
            EmpathyDamage.damageUnit(unit, 1000f, true, () -> {
                SpecialDeathEffects eff = SpecialDeathEffects.get(unit.type);
                float rot = unit.angleTo(b.x, b.y) + 180f;
                eff.deathUnit(unit, b.x, b.y, rot, e -> {
                    float dx = e.x - b.x, dy = e.y - b.y;
                    float dst = Mathf.dst(dx, dy);
                    float force = Math.max((1f - Mathf.clamp(dst / (range + unit.hitSize / 2f + 100f))), (1f / (1f + dst / 50f)));

                    Vec2 vec = Utils.vv.set(dx, dy).nor().setLength(force * 5f);
                    if(!vec.isNaN()){
                        e.vx = vec.x;
                        e.vy = vec.y;
                        e.vr = Mathf.range(24f) * force;
                        e.vz = Mathf.random(-0.01f, 0.1f);
                    }
                });
            });
        });
        super.hit(b);
    }

    @Override
    public void draw(Bullet b) {
    }
}
