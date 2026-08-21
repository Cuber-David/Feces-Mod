package K.content.effectrenderer;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Time;

public class MyRenderer {
    private float warmup = 1f;

    public void draw() {
        float width = Core.camera.width;
        float height = Core.camera.height;
        float cx = Core.camera.position.x;
        float cy = Core.camera.position.y;

        // 计算缩放：用默认视图大小除以当前视图大小
        float defaultWidth = 800f;  // Mindustry 默认宽度
        float scale = defaultWidth / width;

        Color color1 = Color.valueOf("991e3b");
        Color color2 = Color.valueOf("2f0f62");
        Color color3 = Color.valueOf("a87d98");

        int numLines = Mathf.random(80,130);
        float life = 300f;
        float base = Time.time / life;

        Draw.z(220);
        Draw.blend(Blending.additive);

        Draw.color(Color.black);
        Draw.alpha(1f * warmup);
        Fill.quad(
                cx - width/2, cy - height/2,
                cx - width/2, cy + height/2,
                cx + width/2, cy + height/2,
                cx + width/2, cy - height/2
        );

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

            // ===== 固定像素宽度：除以缩放 =====
            float pixelWidth = (2f + 4f * (1f - progress)) / scale;
            Lines.stroke(pixelWidth);

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