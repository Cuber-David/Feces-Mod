package K.content.Fx;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.entities.Effect;

public class FragmentExplosionEffect extends Effect {

    private static final int FRAGMENT_COUNT = 60;

    public FragmentExplosionEffect() {
        super(40f, e -> {
            float p = e.fin();
            float x = e.x;
            float y = e.y;

            for (int i = 0; i < FRAGMENT_COUNT; i++) {
                // ✅ 用种子保证每帧一致
                float rand1 = Mathf.random(7);
                float rand2 = Mathf.random(5);
                float rand3 = Mathf.random(11);
                float rand4 = Mathf.random(100);
                float rand5 = Mathf.random(100);

                float angle = (i / (float) FRAGMENT_COUNT) * 360f + rand1 * 30f - 15f;
                float speed = 30f + rand2 * 60f;
                float dist = speed * p * 5.5f;

                float px = x + Angles.trnsx(angle, dist) + rand4;
                float py = y + Angles.trnsy(angle, dist) + rand5;

                float size = (2f + rand3 * 4f) * (1f - p * 0.8f);
                if (size < 0.1f) continue;

                Draw.color(Color.white, 1f - p);
                Fill.circle(px, py, size);
            }

            if (p < 0.2f) {
                Draw.color(Color.white, (1f - p / 0.2f) * 0.4f);
                Fill.circle(x, y, 15f * (1f - p / 0.2f));
            }

            Draw.reset();
        });
    }
}