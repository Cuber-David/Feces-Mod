package K.content.extend.Bullets;

import K.content.effects.SpriteAnimationEffect;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;

public class Explosion extends BasicBulletType {

    public Explosion() {
        // --- 基础属性 ---
        this.damage = 0f;
        this.width = 0;
        this.lifetime = 60f;
        this.speed = 0f;
        splashDamage = 1000;
        splashDamageRadius = 40;

        shootEffect = Fx.none;
        circleShooter = false;

        // --- 命中效果 ---
        this.hitEffect = Fx.none;
        this.despawnEffect = Fx.none;
    }



    @Override
    public void init(Bullet b) {
        SpriteAnimationEffect.spawnEffect(b);
        super.init(b);

        // 自定义初始化逻辑，如添加特定效果
    }
}