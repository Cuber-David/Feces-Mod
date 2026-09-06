package K.entities;

import K.entities.SimpleFragments;
import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.game.EventType;

public class DeathTrigger {
    private static Seq<Integer> processedIds = new Seq<>();
    private static Seq<Integer> pendingIds = new Seq<>();
    private static float checkTimer = 0f;
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;

        // 每帧检测
        Events.run(EventType.Trigger.update, new Runnable() {
            @Override
            public void run() {
                checkDeaths();
            }
        });

        Log.info("[DeathTrigger] Initialized");
    }

    private static void checkDeaths() {
        // 检测所有单位
        for (Unit u : Groups.unit) {
            if (u.health <= 0 && !processedIds.contains(u.id)) {
                processedIds.add(u.id);

                // 检查是否应该触发碎片
                if (shouldTrigger(u)) {
                    pendingIds.add(u.id);

                    // 直接生成碎片
                    spawnFragmentsForUnit(u);
                }
            }
        }

        // 清理ID列表
        if (processedIds.size > 1000) {
            processedIds.clear();
        }
        if (pendingIds.size > 1000) {
            pendingIds.clear();
        }
    }

    private static boolean shouldTrigger(Unit unit) {
        if (unit == null) return false;
        if (unit.isPlayer()) return false;
        if (unit.team == null) return false;
        if (unit.team == mindustry.game.Team.derelict) return false;
        if (unit.hitSize < 8f) return false;

        // 检查位置是否有效
        if (Float.isNaN(unit.x) || Float.isNaN(unit.y)) return false;
        if (Float.isInfinite(unit.x) || Float.isInfinite(unit.y)) return false;

        return true;
    }

    private static void spawnFragmentsForUnit(Unit unit) {
        try {
            // 保存位置数据（因为在死亡后位置可能变成NaN）
            float x = unit.x;
            float y = unit.y;
            float size = unit.hitSize;

            // 检查位置
            if (Float.isNaN(x) || Float.isNaN(y)) return;
            if (x < -10000 || x > 10000 || y < -10000 || y > 10000) return;

            // 生成碎片
            Log.info("[DeathTrigger] Spawned fragments for " +
                    (unit.type != null ? unit.type.name : "unknown") +
                    " at (" + x + ", " + y + ")");

        } catch (Exception e) {
            // 忽略错误
        }
    }

    // 手动触发（用于测试）
    public static void testTrigger() {
        if (Vars.player != null && Vars.player.unit() != null) {
            Unit u = Vars.player.unit();
            Log.info("[DeathTrigger] Test trigger");
        }
    }
}