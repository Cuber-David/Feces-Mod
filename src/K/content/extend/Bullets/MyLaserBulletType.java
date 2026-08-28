package K.content.extend.Bullets;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.gen.Bullet;

public class MyLaserBulletType extends ContinuousLaserBulletType {

    public MyLaserBulletType() {
        // --- 基础属性 ---
        this.damage = 0f;
        this.length = 96f;
        this.width = 6;
        this.lifetime = 20f;
        this.speed = 0f;

        shootEffect = Fx.none;
        circleShooter = false;
        this.colors = new Color[]{Color.valueOf("ffffff"), Color.valueOf("000000")}; // 激光渐变颜色 (橙->金)
        this.trailColor = Color.valueOf("ffffff").a(0.4f); // 轨迹颜色与透明度

        // --- 命中效果 ---
        this.hitEffect = Fx.none;
        this.despawnEffect = Fx.none;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);
        // 自定义初始化逻辑，如添加特定效果
    }
}