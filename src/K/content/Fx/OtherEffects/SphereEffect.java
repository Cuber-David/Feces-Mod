package K.content.Fx.OtherEffects;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.Effect;

public class SphereEffect {

    /**
     * 密集粒子旋转球体 - 粒子覆盖整个球面
     */
    public static Effect createDenseParticleSphereEffect(Color color, float maxRadius, float duration) {
        return new Effect(duration, e -> {
            float progress = e.fin();
            float radius = maxRadius * Interp.pow2Out.apply(progress);

            // 颜色保持鲜艳，最后10%淡出
            float alpha;
            if (progress < 0.9f) {
                alpha = 1f;
            } else {
                alpha = 1f - (progress - 0.9f) / 0.1f;
            }

            float rotX = Time.time / 40f;
            float rotY = Time.time / 55f;

            Draw.blend(Blending.additive);

            // ===== 发光光晕 =====
            for(int i=0;i<100;i++){
                Draw.color(color.a(0.2f),(float)i/(80*100));
                Fill.circle(e.x,e.y,radius*0.25f*((float) (16 * 100) /i));
            }

            // ===== 粒子数量：覆盖整个球体 =====
            int latSteps = 20;   // 纬度步数
            int lonSteps = 30;   // 经度步数
            int totalParticles = latSteps * lonSteps; // 600个粒子

            float particleBaseSize = 2.5f + radius * 0.04f;

            for (int lat = 0; lat < latSteps; lat++) {
                for (int lon = 0; lon < lonSteps; lon++) {
                    // ===== 球面坐标 =====
                    float theta = (float)lat / latSteps * 180f + rotY * 15f;
                    float phi = (float)lon / lonSteps * 360f + rotX * 30f;

                    // ===== 3D球面位置 =====
                    float sinTheta = Mathf.sinDeg(theta);
                    float cosTheta = Mathf.cosDeg(theta);
                    float sinPhi = Mathf.sinDeg(phi);
                    float cosPhi = Mathf.cosDeg(phi);

                    float px = radius * sinTheta * cosPhi;
                    float py = radius * cosTheta;
                    float pz = radius * sinTheta * sinPhi;

                    // ===== 投影到2D =====
                    float tiltAngle = 25f;
                    float cosTilt = Mathf.cosDeg(tiltAngle);
                    float sinTilt = Mathf.sinDeg(tiltAngle);

                    float projX = px;
                    float projY = py * cosTilt - pz * sinTilt;
                    float depth = py * sinTilt + pz * cosTilt;

                    // 透视投影
                    float perspective = 1f + depth / (radius * 2.5f);
                    float screenX = e.x + projX * perspective;
                    float screenY = e.y + projY * perspective;

                    // ===== 深度因子 =====
                    float depthFactor = 0.4f + 0.6f * (0.5f + 0.5f * (depth / radius));
                    float distanceFromCenter = Mathf.dst(projX, projY);
                    float edgeFactor = 1f - (distanceFromCenter / radius) * 0.2f;

                    // ===== 粒子大小 =====
                    float sizeVar = 0.7f + 0.6f * (0.5f + 0.5f * (depth / radius));
                    float particleSize = particleBaseSize * sizeVar * (1f + (1f - distanceFromCenter / radius) * 0.3f);

                    // ===== 颜色 =====
                    float brightness = 0.8f + 0.2f * (depth / radius);
                    float finalAlpha = alpha * 0.7f * depthFactor * edgeFactor;

                    Draw.color(
                            color.r * brightness,
                            color.g * brightness,
                            color.b * brightness,
                            finalAlpha
                    );

                    // ===== 绘制粒子 =====
                    Fill.square(screenX, screenY, particleSize, 45f);

                    // 粒子发光
                    Draw.color(color, alpha * 0.15f * depthFactor * edgeFactor);
                    Fill.square(screenX, screenY, particleSize * 2.5f, 45f);
                }
            }

            // ===== 额外随机粒子（填补空隙） =====
            int extraParticles = 150;
            for (int i = 0; i < extraParticles; i++) {
                long seed = i * 9999L + (long)(Time.time / 80f) * 1000L;

                float theta = Mathf.random(0f, 180f) + rotY * 20f;
                float phi = Mathf.random(0f, 360f) + rotX * 35f;

                float sinTheta = Mathf.sinDeg(theta);
                float cosTheta = Mathf.cosDeg(theta);
                float sinPhi = Mathf.sinDeg(phi);
                float cosPhi = Mathf.cosDeg(phi);

                float px = radius * sinTheta * cosPhi;
                float py = radius * cosTheta;
                float pz = radius * sinTheta * sinPhi;

                float tiltAngle = 25f;
                float cosTilt = Mathf.cosDeg(tiltAngle);
                float sinTilt = Mathf.sinDeg(tiltAngle);

                float projX = px;
                float projY = py * cosTilt - pz * sinTilt;
                float depth = py * sinTilt + pz * cosTilt;

                float perspective = 1f + depth / (radius * 2.5f);
                float screenX = e.x + projX * perspective;
                float screenY = e.y + projY * perspective;

                float depthFactor = 0.4f + 0.6f * (0.5f + 0.5f * (depth / radius));
                float distanceFromCenter = Mathf.dst(projX, projY);
                float edgeFactor = 1f - (distanceFromCenter / radius) * 0.15f;

                float particleSize = particleBaseSize * (0.5f + Mathf.random(0f, 0.8f));

                Draw.color(color, alpha * 0.3f * depthFactor * edgeFactor);
                Fill.square(screenX, screenY, particleSize, 45f);
            }

            // ===== 核心亮点 =====
            Draw.color(Color.white, alpha * 0.25f);
            Fill.circle(e.x, e.y, radius * 0.12f);

            // ===== 高光 =====
            float lightAngle = rotX * 30f + 50f;
            float lx = e.x + Mathf.cosDeg(lightAngle) * radius * 0.4f;
            float ly = e.y - radius * 0.25f + Mathf.sinDeg(lightAngle) * radius * 0.12f;

            Draw.color(Color.white, alpha * 0.2f);
            Fill.rect(lx, ly, radius * 0.3f, radius * 0.15f, lightAngle);

            Draw.blend();
            Draw.reset();
        }){{clip = 2000;}};
    }
}