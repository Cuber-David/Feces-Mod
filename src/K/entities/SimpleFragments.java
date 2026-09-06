package K.entities;

import K.content.effects.Severation;
import K.graphics.CutBatch;
import arc.*;
import arc.func.Boolf;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.game.EventType;

public class SimpleFragments {
    private static Seq<Fragment> fragments = new Seq<>();
    private static Seq<Integer> deadUnits = new Seq<>();
    private static Boolf<Unit> unitFilter = unit -> true;  // 默认全部触发

    // 配置参数
    public static int gridCols = 4;
    public static int gridRows = 4;
    public static float fragmentLife = 180f;
    public static float fragmentSpeed = 3f;
    public static float gap = -0.5f;
    public static boolean randomOffset = false;
    public static boolean flipU = true;
    public static boolean flipV = false;

    static class Fragment {
        float x, y, vx, vy;
        float width, height;
        float life;
        float maxLife;
        float rotation, vr;
        TextureRegion region;

        Fragment(float x, float y, float w, float h, TextureRegion parent, float u1, float v1, float u2, float v2) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;

            if (parent != null && parent.found()) {
                this.region = new TextureRegion(parent.texture, u1, v1, u2, v2);
            } else {
                this.region = Core.atlas.white();
            }

            float angle = Mathf.random(360f);
            float speed = Mathf.random(1f, fragmentSpeed);
            this.vx = Mathf.cosDeg(angle) * speed;
            this.vy = Mathf.sinDeg(angle) * speed;
            this.rotation = 0f;  // will be overwritten
            this.vr = Mathf.random(-0.5f, 0.5f);
            this.maxLife = fragmentLife + Mathf.random(-30f, 30f);
            this.life = this.maxLife;
        }
    }

    public static void init() {
        Log.info("[Fragments] Initializing...");
        Events.run(EventType.Trigger.update, () -> updateFragments());
        Events.run(EventType.Trigger.draw, () -> drawFragments());
        Log.info("[Fragments] Initialized");
    }

    /**
     * 设置单位过滤器，只有符合条件的单位死亡时才会触发网格切割。
     * @param filter 过滤条件，返回 true 表示触发切割
     */
    public static void setFilter(Boolf<Unit> filter) {
        if (filter != null) unitFilter = filter;
    }

    private static void updateFragments() {
        // 检测死亡单位
        for (Unit u : Groups.unit) {
            if (u.health <= 0 && !deadUnits.contains(u.id)) {
                deadUnits.add(u.id);
                // 应用过滤器
                if (unitFilter.get(u) && u != null && !u.isPlayer() && u.hitSize > 8f) {
                    if (!Float.isNaN(u.x) && !Float.isNaN(u.y)) {
                        spawnGridFromUnit(u);
                    }
                }
            }
        }
        if (deadUnits.size > 1000) deadUnits.clear();

        // 更新碎片物理
        for (int i = 0; i < fragments.size; i++) {
            Fragment f = fragments.get(i);
            f.x += f.vx * Time.delta;
            f.y += f.vy * Time.delta;
            f.rotation += f.vr * Time.delta;
            f.vx *= 0.98f;
            f.vy *= 0.98f;
            f.vr *= 0.99f;
            f.life -= Time.delta;
        }
        for (int i = fragments.size - 1; i >= 0; i--) {
            if (fragments.get(i).life <= 0) fragments.remove(i);
        }
        if (fragments.size > 500) {
            for (int i = 0; i < fragments.size / 2; i++) {
                if (i < fragments.size) fragments.remove(0);
            }
        }
    }

    private static void drawFragments() {
        if (fragments.isEmpty()) return;
        float oz = Draw.z();
        Draw.z(Layer.overlayUI + 100);
        for (Fragment f : fragments) {
            if (f.life <= 0) continue;
            float alpha = Mathf.clamp(f.life / 30f);
            if (alpha < 0.05f) continue;
            Draw.color(1f, 1f, 1f, alpha);
            if (f.region != null && f.region.found()) {
                Draw.rect(f.region, f.x, f.y, f.width, f.height, f.rotation);
            } else {
                Fill.rect(f.x, f.y, f.width, f.height);
            }
        }
        Draw.color();
        Draw.z(oz);
    }

    private static void spawnGridFromUnit(Unit unit) {
        try {
            CutBatch cutBatch = new CutBatch();
            cutBatch.switchBatch(() -> unit.draw());
            Seq<Severation> sevs = CutBatch.returnEntities;
            if (!sevs.isEmpty()) {
                Severation sev = sevs.first();
                if (sev != null && sev.area >= 5f) {
                    spawnGridFragments(sev.x, sev.y, sev.width, sev.height, unit.rotation+90, sev.region);
                }
                // 移除所有自动生成的 Severation（防止多余贴图）
                for (Severation s : sevs) {
                    if (s != null && s.isAdded()) s.remove();
                }
            }
            CutBatch.returnEntities.clear();

            // 彻底移除单位
            unit.remove();
            Groups.draw.remove(unit);
            Groups.unit.remove(unit);
            Groups.all.remove(unit);
            unit.x = Float.POSITIVE_INFINITY;
            unit.y = Float.POSITIVE_INFINITY;

        } catch (Exception e) {
            Log.err("[Fragments] Error: " + e);
        }
    }

    private static void spawnGridFragments(float x, float y, float width, float height, float baseRotation, TextureRegion region) {
        if (Float.isNaN(x) || Float.isNaN(y)) return;
        if (width < 5f || height < 5f) return;
        if (region == null || !region.found()) region = Core.atlas.white();

        float cellW = width / gridCols;
        float cellH = height / gridRows;
        float drawW = Math.max(cellW - gap, 1f);
        float drawH = Math.max(cellH - gap, 1f);

        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                float localX = (c + 0.5f) * cellW - width / 2f;
                float localY = (r + 0.5f) * cellH - height / 2f;

                if (randomOffset) {
                    localX += Mathf.random(-cellW * 0.15f, cellW * 0.15f);
                    localY += Mathf.random(-cellH * 0.15f, cellH * 0.15f);
                }

                float cos = Mathf.cosDeg(baseRotation);
                float sin = Mathf.sinDeg(baseRotation);
                float worldX = x + (localX * cos - localY * sin);
                float worldY = y + (localX * sin + localY * cos);

                float u1 = region.u + (region.u2 - region.u) * (c / (float)gridCols);
                float u2 = region.u + (region.u2 - region.u) * ((c + 1) / (float)gridCols);
                float v1 = region.v + (region.v2 - region.v) * (r / (float)gridRows);
                float v2 = region.v + (region.v2 - region.v) * ((r + 1) / (float)gridRows);

                // 翻转控制
                if (flipU) {
                    float tmp = u1; u1 = u2; u2 = tmp;
                }
                if (flipV) {
                    float tmp = v1; v1 = v2; v2 = tmp;
                }

                Fragment f = new Fragment(worldX, worldY, drawW, drawH, region, u1, v1, u2, v2);

                // 碎片角度 = 单位角度 + 小偏移（±2°）
                f.rotation = baseRotation + 180 + Mathf.random(-2f, 2f);
                // 角速度已经很小

                // 飞散速度（向外扩散）
                float dist = Mathf.len(localX, localY);
                float angle = Mathf.angle(localX, localY);
                float speed = fragmentSpeed * (0.3f + (dist / Math.max(width, height)) * 0.7f);
                speed *= Mathf.random(0.8f, 1.2f);
                float velAngle = angle + baseRotation;
                f.vx = Mathf.cosDeg(velAngle) * speed;
                f.vy = Mathf.sinDeg(velAngle) * speed;

                fragments.add(f);
            }
        }
        Log.info("[Fragments] Grid cut: " + gridCols + "x" + gridRows + " = " + (gridCols * gridRows) + " fragments");
    }

    public static void testGridCut() {
        if (Vars.player != null && Vars.player.unit() != null) {
            Unit u = Vars.player.unit();
            spawnGridFromUnit(u);
        }
    }

    public static void clearAll() {
        fragments.clear();
        deadUnits.clear();
    }

    public static int getFragmentCount() {
        return fragments.size;
    }

    public static void cutUnit(Unit unit) {
        if (unit == null || unit.isPlayer()) return;
        spawnGridFromUnit(unit);
    }
}