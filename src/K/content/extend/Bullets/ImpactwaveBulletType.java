package K.content.extend.Bullets;

import K.content.Fx.KFx;
import arc.graphics.Color;
import mindustry.entities.bullet.BulletType;
import mindustry.content.Fx;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.entities.Units;
import mindustry.entities.Damage;
import mindustry.world.Tile;
import mindustry.Vars;

/**
 * 震荡波子弹 - 在飞行过程中周期性释放震荡波，并在消失时产生大爆炸
 * 对建筑伤害极低（0.01倍）
 */
public class ImpactwaveBulletType extends BulletType {

    // 脉冲配置
    public float pulseInterval = 20f;
    public float pulseDamage = 12f;
    public float pulseRadius = 40f;

    // 消失爆炸配置
    public float finalDamage = 30f;
    public float finalRadius = 80f;

    // 建筑伤害倍率
    public float buildingDamageMultiplier = 0.01f;

    public ImpactwaveBulletType() {
        super(5f, 0f);

        hitSize = 0f;
        hittable = false;
        reflectable = false;
        absorbable = false;

        lifetime = 100f;
        drag = 0.001f;
        collides = false;  // ★ 改为 false ★

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

        if (b.time >= lifetime - 1f) {
            triggerFinalExplosion(b);
            return;
        }

        if ((int)b.time % (int)pulseInterval == 0 && b.time > 0) {
            triggerPulse(b);
        }
    }

    private void triggerPulse(Bullet b) {
        // 1. 播放特效
        KFx.Feceswave.at(b.x, b.y, 0f, Pal.bulletYellow);
        Sounds.explosionCrawler.at(b.x,b.y,0.5f,1);

        // 2. 使用 Damage.damage 直接造成伤害
        Damage.damage(b.team, b.x, b.y, pulseRadius, pulseDamage);

        // 3. 对建筑额外处理（降低伤害）
        for (Tile tile : Vars.world.tiles) {
            Building building = tile.build;
            if (building != null && building.team != b.team) {
                float dist = building.dst(b);
                if (dist <= pulseRadius && dist > 0) {
                    float damageMultiplier = 1f - (dist / pulseRadius) * 0.5f;
                    float normalDamage = pulseDamage * damageMultiplier;
                    float buildingDamage = normalDamage * buildingDamageMultiplier;

                    // 恢复多余的伤害
                    float excessDamage = normalDamage - buildingDamage;
                    building.health = Math.min(building.health + excessDamage, building.maxHealth);
                }
            }
        }

        // 4. 单位击退效果
        Units.nearby(b.team, b.x, b.y, pulseRadius, unit -> {
            if (unit.team != b.team && unit.dst(b) <= pulseRadius && unit.dst(b) > 0) {
                float dist = unit.dst(b);
                float strength = 2f * (1f - dist / pulseRadius);
                unit.vel().add(
                        (unit.x - b.x) / dist * strength,
                        (unit.y - b.y) / dist * strength
                );
            }
        });
    }

    private void triggerFinalExplosion(Bullet b) {
        // 1. 播放最终特效
        Fx.scatheExplosion.wrap(Color.brown).at(b.x, b.y, 0f, Pal.bulletYellow);
        Sounds.explosionTitan.at(b.x,b.y,1,5);

        // 2. 使用 Damage.damage 直接造成伤害
        Damage.damage(b.team, b.x, b.y, finalRadius, finalDamage);

        // 3. 对建筑额外处理（降低伤害）
        for (Tile tile : Vars.world.tiles) {
            Building building = tile.build;
            if (building != null && building.team != b.team) {
                float dist = building.dst(b);
                if (dist <= finalRadius && dist > 0) {
                    float damageMultiplier = 1f - (dist / finalRadius) * 0.3f;
                    float normalDamage = finalDamage * damageMultiplier;
                    float buildingDamage = normalDamage * buildingDamageMultiplier;

                    float excessDamage = normalDamage - buildingDamage;
                    building.health = Math.min(building.health + excessDamage, building.maxHealth);
                }
            }
        }

        // 4. 单位击退效果
        Units.nearby(b.team, b.x, b.y, finalRadius, unit -> {
            if (unit.team != b.team && unit.dst(b) <= finalRadius && unit.dst(b) > 0) {
                float dist = unit.dst(b);
                float strength = 5f * (1f - dist / finalRadius);
                unit.vel().add(
                        (unit.x - b.x) / dist * strength,
                        (unit.y - b.y) / dist * strength
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