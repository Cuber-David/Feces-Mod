package K.content.extend.util;

import K.graphics.MainRenderer;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Time;

import static arc.graphics.Blending.additive;

public class DrawPurple {
    public static void drawp(float x,float y,float size){
        final float c = 100;
        Draw.z(200);
        //MainRenderer.addBlackHole(x,y,10,100);
        for(int i=0;i<c;i++){
            Draw.color(Color.valueOf("5e51a0").a(0.2f),(float)i/(80*c));
            Fill.circle(x,y,size*0.25f*((16*c)/i));
        }
        Draw.color(Color.valueOf("a071c2").a(0.2f));
        Fill.circle(x,y,size*0.35f);
        Draw.color(Color.valueOf("b853e1").a(0.8f));
        Fill.circle(x,y,size*0.25f);
        Draw.color(Color.valueOf("f9f7fc"));
        drawElectronCloud(x,y,Mathf.random(-10,10),size/4);
        drawElectronCloud(x,y,Mathf.random(-10,10),size/16);
        Draw.reset();
    }

    private static void drawElectronCloud(float x, float y, int unitId, float radius) {
        float time = Time.time + unitId * 100f;

        Draw.blend(additive);

        // ========== 1. 核心光晕 ==========
        Draw.color(Color.white, 0.15f);
        Fill.circle(x, y, radius * 0.5f);

        Draw.color(new Color(0.2f, 0.6f, 1f, 1f), 0.1f);
        Fill.circle(x, y, radius);

        // ========== 2. 电子云粒子 ==========
        float particleCount = 1000;
        for (int i = 0; i < particleCount; i++) {
            float seed = i * 3.7f + unitId * 5.1f;

            // ★ 电子云分布：随机半径（概率密度在中心附近较高）
            float distFactor = Mathf.random(1f);
            // 使用平方分布让粒子更集中在中心附近（模拟1s轨道）
            float dist = radius * (distFactor * distFactor * 0.8f + 0.2f);

            // ★ 角度：随时间缓慢旋转
            float angle = (360f / particleCount) * i + time * (0.3f + 0.2f * Mathf.sin(seed));
            // 添加随机扰动，模拟电子的不确定性
            angle += Mathf.sin(time * 0.5f + seed) * 15f;

            float px = x + Mathf.cosDeg(angle) * dist;
            float py = y + Mathf.sinDeg(angle) * dist;

            // ★ 粒子大小：随机（模拟电子云密度波动）
            float size = 0.5f + 0.5f * Mathf.random(1f);
            // 透明度：外层粒子更透明
            float alpha = 0.3f + 0.5f * (1f - dist / radius);
            alpha *= 0.5f + 0.5f * Mathf.sin(time * 0.3f + seed);

            // ★ 颜色：从蓝色到青色渐变
            Color color = Color.valueOf("f9f7fc")
                    .lerp(Color.valueOf("f9f7fc").a(0.2f), dist / radius);

            Draw.color(color, alpha);
            Fill.circle(px, py, size);
        }

        // ========== 3. 高速运动电子（亮斑） ==========
        int fastElectrons = 6;
        for (int i = 0; i < fastElectrons; i++) {
            float seed = i * 7.3f + unitId * 11.7f;

            // ★ 快速轨道运动
            float angle = time * (1.5f + i * 0.3f) + seed;
            float orbitRadius = radius * (0.3f + 0.6f * (i / (float)fastElectrons));

            float px = x + Mathf.cosDeg(angle) * orbitRadius;
            float py = y + Mathf.sinDeg(angle) * orbitRadius;

            // 亮斑大小随速度变化
            float size = 3f + 2f * Mathf.sin(time * 1.2f + seed);
            float alpha = 0.5f + 0.4f * Mathf.sin(time * 1.5f + seed);

            Draw.color(Color.white, alpha);
            Fill.circle(px, py, size);

            // ★ 拖尾效果（模拟运动轨迹）
            for (int j = 1; j <= 5; j++) {
                float trailAngle = angle - j * 8f;
                float trailDist = orbitRadius * (1f - j * 0.03f);
                float trailAlpha = alpha * 0.2f * (1f - j / 5f);
                float trailSize = size * (1f - j * 0.1f);

                float tx = x + Mathf.cosDeg(trailAngle) * trailDist;
                float ty = y + Mathf.sinDeg(trailAngle) * trailDist;
                Draw.color(Color.valueOf("f9f7fc"), trailAlpha);
                Fill.circle(tx, ty, trailSize);
            }
        }

        // ========== 4. 量子涨落粒子（随机出现和消失） ==========
        int quantumParticles = 20;
        for (int i = 0; i < quantumParticles; i++) {
            float seed = i * 13.7f + unitId * 17.3f;

            // ★ 粒子在特定时间出现和消失（模拟量子涨落）
            float life = Mathf.sin(time * 0.2f + seed) * 0.5f + 0.5f;
            if (life < 0.1f) continue;

            float angle = (360f / quantumParticles) * i + time * 0.2f + seed;
            float dist = radius * (0.1f + 0.9f * Mathf.random(1f));

            float px = x + Mathf.cosDeg(angle) * dist;
            float py = y + Mathf.sinDeg(angle) * dist;

            float size = 1f + 2f * life;
            float alpha = 0.3f * life;

            Draw.color(new Color(0.5f, 0.8f, 1f, 1f), alpha);
            Fill.circle(px, py, size);
        }

        Draw.blend();
        Draw.color();
    }
}
