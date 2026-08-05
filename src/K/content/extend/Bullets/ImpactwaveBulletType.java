package K.content.extend.Bullets;

import K.content.fx;
import arc.graphics.Color;
import mindustry.entities.bullet.BulletType;
import mindustry.content.Fx;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;
import mindustry.entities.Damage;
import mindustry.entities.Units;

public class ImpactwaveBulletType extends BulletType {

    // 脉冲配置
    public float pulseInterval = 10f;
    public float pulseDamage = 20f;
    public float pulseRadius = 40f;

    // 消失爆炸配置
    public float finalDamage = 80f;
    public float finalRadius = 64f;

    public ImpactwaveBulletType() {
        super(6f, 0f);

        // 隐藏子弹
        hitSize = 0f;
        hittable = false;
        reflectable = false;
        absorbable = false;
        collidesGround = false;
        collidesAir = false;

        // 飞行参数
        lifetime = 60f;

        // 关闭特效
        hitEffect = Fx.none;
        despawnEffect = Fx.none;
        shootEffect = Fx.none;
        smokeEffect = Fx.none;
        trailLength = 0;
        trailChance = 0f;
    }

    @Override
    public void draw(Bullet b) {
        // 完全隐形
    }

    @Override
    public void update(Bullet b) {
        super.update(b);

        // 检测最终爆炸
        if (b.time >= lifetime - 1f) {
            triggerFinalExplosion(b);
            return;
        }

        // 周期性脉冲
        if ((int)b.time % (int)pulseInterval == 0 && b.time > 0) {
            triggerPulse(b);
        }

        // 飞行粒子
        if ((int)b.time % 3 == 0) {
            //fx.Feceswave.at(b.x, b.y, 0f, Pal.bulletYellow);
        }
    }

    private void triggerPulse(Bullet b) {
        // 1. 特效
        fx.Feceswave.at(b.x, b.y, 0f, Pal.bulletYellow);

        // 2. 范围伤害（主要方法）
        Damage.damage(b.team, b.x, b.y, pulseRadius, pulseDamage);

        // 3. ★ 使用正确的 Units.nearby 签名 ★
        Units.nearby(b.team, b.x, b.y, pulseRadius, unit -> {
            if (unit.team != b.team && unit.dst(b) <= pulseRadius && unit.dst(b) > 0) {
                float dist = unit.dst(b);
                // ★ 修正：使用 vel 方法 ★
                unit.vel().add(
                        (unit.x - b.x) / dist * 2f,
                        (unit.y - b.y) / dist * 2f
                );
            }
        });
    }

    private void triggerFinalExplosion(Bullet b) {
        // 1. 最终特效
        Fx.scatheExplosion.wrap(Color.brown).at(b.x, b.y, 0f, Pal.bulletYellow);

        // 2. 最终范围伤害
        Damage.damage(b.team, b.x, b.y, finalRadius, finalDamage);

        // 3. ★ 使用正确的 Units.nearby 签名 ★
        Units.nearby(b.team, b.x, b.y, finalRadius, unit -> {
            if (unit.team != b.team && unit.dst(b) <= finalRadius && unit.dst(b) > 0) {
                float dist = unit.dst(b);
                unit.vel().add(
                        (unit.x - b.x) / dist * 5f,
                        (unit.y - b.y) / dist * 5f
                );
            }
        });
    }

    @Override
    public void despawned(Bullet b) {
        super.despawned(b);
        if (b.time < lifetime - 1f) {
            triggerFinalExplosion(b);
        }
    }
}