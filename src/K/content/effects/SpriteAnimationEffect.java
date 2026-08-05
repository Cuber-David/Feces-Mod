package K.content.effects; // 替换为你的mod包名

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.util.Time;
import mindustry.entities.Effect;
import mindustry.gen.Posc;
import mindustry.graphics.Layer;

/**
 * 基于贴图序列的特效动画模板（修正版）
 */
public class SpriteAnimationEffect {

    // 1. 定义贴图序列
    private static TextureRegion[] frames;
    private static final int TOTAL_FRAMES = 16; // 总帧数
    private static final float FRAME_DURATION = 0.05f; // 每帧持续时间（秒）
    private static final float ANIMATION_DURATION = TOTAL_FRAMES * FRAME_DURATION;

    /**
     * 在mod加载时初始化贴图资源
     * 贴图需放在 assets/sprites/effects/ 目录下
     * 命名为 "e-1.png", "e-2.png", ...
     */
    public static void load() {
        frames = new TextureRegion[TOTAL_FRAMES];
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            // 修正：使用 Vars.tree 加载贴图
            String path = "effects/e-" + (i + 1);
            frames[i] = new TextureRegion(arc.Core.atlas.find(path));
            // 或者使用备选方案：
            // frames[i] = arc.Core.atlas.find(path);
        }
    }

    /**
     * 创建一个简单的贴图序列特效
     */
    public static Effect create(float x, float y, float rotation) {
        return new Effect(ANIMATION_DURATION, e -> {
            // 计算当前帧索引
            float progress = e.fin() / ANIMATION_DURATION;
            int frameIndex = (int) (progress * TOTAL_FRAMES);
            frameIndex = Math.min(frameIndex, TOTAL_FRAMES - 1);

            // 获取当前帧贴图
            TextureRegion frame = frames[frameIndex];
            if (frame == null) return;

            // 绘制设置：在特效层绘制，带透明度和缩放动画
            float alpha = Interp.pow2Out.apply(1f - e.fout());
            float scale = 0.5f + 0.5f * Interp.pow2Out.apply(1f - e.fout());

            // 绘制贴图
            Draw.z(Layer.effect);
            Draw.color(e.color);

            // 修正绘制方法
            float width = frame.width * Draw.scl * scale;
            float height = frame.height * Draw.scl * scale;
            Draw.rect(frame, e.x, e.y, width, height, e.rotation);

            Draw.color(); // 重置颜色
        });
    }

    /**
     * 带渐变效果的高级特效
     */
    public static Effect createAdvanced(float x, float y, float rotation,
                                        float sizeMultiplier) {
        return new Effect(ANIMATION_DURATION, e -> {
            float progress = e.fin() / ANIMATION_DURATION;
            int frameIndex = Math.min((int)(progress * TOTAL_FRAMES), TOTAL_FRAMES - 1);
            TextureRegion frame = frames[frameIndex];
            if (frame == null) return;

            // 渐入渐出效果
            float alpha = 1f;
            if (e.fin() < 0.2f) alpha = e.fin() / 0.2f;
            else if (e.fout() < 0.2f) alpha = e.fout() / 0.2f;

            // 缩放动画：先膨胀后收缩
            float scale = 1f + 0.3f * Interp.circleOut.apply(1f - e.fout());

            Draw.z(Layer.effect);
            Draw.alpha(alpha);

            float width = frame.width * Draw.scl * scale * sizeMultiplier;
            float height = frame.height * Draw.scl * scale * sizeMultiplier;
            Draw.rect(frame, e.x, e.y, width, height, e.rotation);

            Draw.reset(); // 重置所有绘制状态
        });
    }

    /**
     * 用法示例：在任意类中调用此方法触发特效
     */
    public static void spawnEffect(Posc entity) {
        Effect effect = create(entity.x(), entity.y(), 0f);
        effect.at(entity.x(), entity.y(), 0f);
    }
}