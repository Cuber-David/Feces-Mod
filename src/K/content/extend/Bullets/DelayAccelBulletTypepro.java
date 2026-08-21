package K.content.extend.Bullets;

import K.content.Fx.fx;
import K.content.sounds;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;

public class DelayAccelBulletTypepro extends BulletType {

    // 延迟时间（帧），1秒 = 60帧
    public float delayTime = 15f;

    // 目标速度
    public float targetSpeed = 150f;

    // 停留特效
    public Effect hoverEffect = Fx.colorSpark;
    public Color hoverColor = Color.valueOf("33ffff");
    public float hoverEffectInterval = 5f;

    // 发射特效
    public Effect launchEffect = Fx.shockwave;
    public Color launchColor = Color.valueOf("33ffff");

    public DelayAccelBulletTypepro() {
        super(0f, 360f);

        lifetime = delayTime+4f;
        hitSize = 2f;
        hittable = true;
        reflectable = false;
        absorbable = false;
        pierce = true;
        pierceArmor = true;

        // 拖尾配置
        trailColor = Color.valueOf("33ffff");
        trailLength = 8;
        trailChance = 0.4f;
        trailEffect = fx.ellipsetrail;

        // 特效配置
        hitEffect = Fx.hitBulletBig;
        despawnEffect = Fx.explosion;
        shootEffect = Fx.shootBig;
        smokeEffect = Fx.smoke;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);

        // 存储状态：0=延迟，1=飞行
        b.data = 0f;

        // 初始速度为0
        b.vel().set(0, 0);
    }

    @Override
    public void update(Bullet b) {
        float state = (float)b.data;
        float time = b.time;

        if (b.time>=delayTime){
            if(b.time<=delayTime+2) {
                sounds.beamstart.at(b.x, b.y, 1, 1);
            }
        }
        // 阶段1：延迟（速度保持0）
        if (state == 0) {
            // 延迟特效
            if (time % hoverEffectInterval == 0) {
                if (hoverEffect != null) {
                    hoverEffect.at(b.x, b.y, 0f, hoverColor);
                }
                Fx.colorSpark.at(b.x, b.y, 0f, hoverColor);
            }

            // 延迟结束，开始飞行
            if (time >= delayTime) {

                state = 1;
                b.data = state;

                // 寻找目标（可选）
                Unit target = findTarget(b);
                if (target != null) {
                    float angleToTarget = b.angleTo(target);
                    b.rotation(angleToTarget);
                }

                // 设置速度
                float rad = b.rotation() * Mathf.degRad;
                b.vel().set(Mathf.cos(rad) * targetSpeed, Mathf.sin(rad) * targetSpeed);

                // 发射特效
                if (launchEffect != null) {
                    launchEffect.at(b.x, b.y, 0f, launchColor);
                }

                // 变为可被拦截
                hittable = true;
            }
        }

        // 阶段2：全速飞行
        if (state == 1) {
            // 保持速度和方向
            float rad = b.rotation() * Mathf.degRad;
            b.vel().set(Mathf.cos(rad) * targetSpeed, Mathf.sin(rad) * targetSpeed);

            // 追踪目标
            Unit target = findTarget(b);
            if (target != null) {
                float angleToTarget = b.angleTo(target);
                float currentAngle = b.rotation();

                // 计算角度差
                float diff = angleToTarget - currentAngle;
                while (diff > 180f) diff -= 360f;
                while (diff < -180f) diff += 360f;

                // 追踪强度
                float strength = 0.2f;
                float newAngle = currentAngle + diff * strength;
                b.rotation(newAngle);
            }
        }
    }

    private Unit findTarget(Bullet b) {
        Unit[] nearest = {null};
        float[] nearestDist = {Float.MAX_VALUE};

        Units.nearby(b.team, b.x, b.y, 200f, unit -> {
            if (unit.team != b.team && unit.isValid() && !unit.dead()) {
                float dist = unit.dst(b);
                if (dist < nearestDist[0]) {
                    nearestDist[0] = dist;
                    nearest[0] = unit;
                }
            }
        });

        return nearest[0];
    }

    @Override
    public void draw(Bullet b) {
        float state = (float)b.data;
        float time = b.time;

        if (state == 0) {
            // 延迟阶段：绘制蓄力环
            float progress = Math.min(time / delayTime, 1f);
            float ringRadius = 1f + progress * -10f;
            float alpha = 1f - progress * 0.5f;

            Draw.color(hoverColor, alpha);
            Lines.stroke(2f);
            Lines.circle(b.x, b.y, ringRadius);

            // 额外的内环
            for (int i = 1; i <= 3; i++) {
                float offset = i * 8f;
                Draw.color(hoverColor, alpha * 0.4f);
                Lines.stroke(1f);
                Lines.circle(b.x, b.y, ringRadius + offset);
            }

            // 中心光点
            Draw.color(hoverColor, 0.8f);
            Fill.circle(b.x, b.y, 4f);

            Draw.color();
            Lines.stroke(1f);
        } else {
            Draw.color(hoverColor, 255f);
            Fill.circle(b.x, b.y, hitSize);
            drawLight(b);
            Draw.color(Color.gray, 0.2f);
            Lines.stroke(1f);
            Lines.circle(b.x, b.y, hitSize + 1f);
            float r = b.rotation()*Mathf.degRad;
            fx.ellipsetrail.at(b.x,b.y,r);
            for (int i = 1;i<100;i++){
                fx.ellipsetrailblue.at(b.x+i*Mathf.cos(r),b.y+i*Mathf.sin(r),r,Color.valueOf("33ffff"));
            }

            Draw.color();
            Lines.stroke(1f);
        }
    }

    @Override
    public void despawned(Bullet b) {
        super.despawned(b);
        Fx.explosion.at(b.x, b.y, 0f, Pal.bulletYellow);
    }
}