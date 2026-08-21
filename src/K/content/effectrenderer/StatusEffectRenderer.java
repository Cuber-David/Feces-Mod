package K.content.effectrenderer;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class StatusEffectRenderer {
    private float warmup = 0f;
    private StatusEffect targetEffect;
    private boolean initialized = false;

    // 线条参数
    private int numLines = 90;
    private float life = 300f;
    private Color color1 = Color.valueOf("991e3b");
    private Color color2 = Color.valueOf("2f0f62");
    private Color color3 = Color.valueOf("a87d98");

    public StatusEffectRenderer(StatusEffect effect) {
        this.targetEffect = effect;
    }

    public void update() {
        if (targetEffect == null) return;

        Unit player = Vars.player != null ? Vars.player.unit() : null;
        if (player == null) {
            warmup = Math.max(warmup - Time.delta / 30f, 0f);
            return;
        }

        boolean hasEffect = player.hasEffect(targetEffect);

        if (hasEffect) {
            warmup = Math.min(warmup + Time.delta / 30f, 1f);
        } else {
            warmup = Math.max(warmup - Time.delta / 30f, 0f);
        }
    }

    public void draw() {
        if (warmup <= 0.01f) return;
        if (!initialized) {
            Log.info("StatusEffectRenderer 开始绘制");
            initialized = true;
        }

        float width = Core.camera.width;
        float height = Core.camera.height;
        float cx = Core.camera.position.x;
        float cy = Core.camera.position.y;

        float scale = 800f / width;
        float base = Time.time / life;

        Draw.blend(Blending.additive);
        // 全屏底色
        Draw.color(Color.black);
        Draw.alpha(1f * warmup);
        Fill.quad(
                cx - width/2, cy - height/2,
                cx - width/2, cy + height/2,
                cx + width/2, cy + height/2,
                cx + width/2, cy - height/2
        );
        // 线条
        for (int i = 0; i < numLines; i++) {
            float angle = (float)i / numLines * 365f + Time.time * 0.01f;
            float progress = (Mathf.random(1f) + base) % 1f;

            float maxDist = Mathf.dst(width/2, height/2);
            float startDist = maxDist * 0.95f;
            float endDist = maxDist * (1f - progress * 0.9f);

            float colorChoice = Mathf.random(1f);
            Color lineColor;
            if (colorChoice < 0.33f) lineColor = color1;
            else if (colorChoice < 0.66f) lineColor = color2;
            else lineColor = color3;

            float alpha = (0.3f + 0.7f * (1f - progress)) * warmup * 0.8f;

            Draw.color(lineColor);
            Draw.alpha(alpha);
            Lines.stroke((2f + 4f * (1f - progress)) / scale);

            float startX = cx + Mathf.cosDeg(angle) * startDist;
            float startY = cy + Mathf.sinDeg(angle) * startDist;
            float endX = cx + Mathf.cosDeg(angle) * endDist;
            float endY = cy + Mathf.sinDeg(angle) * endDist;

            Lines.line(startX, startY, endX, endY);
        }

        Draw.blend();
        Draw.reset();
    }
}