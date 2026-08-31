package K.content.extend.util;

import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.gen.Groups; // 导入 Groups

public class PlayerUtils {

    public static class PlayerUnitResult {
        public final Unit unit;
        public final float mouseX, mouseY;

        public PlayerUnitResult(Unit u, float mx, float my) {
            this.unit = u;
            this.mouseX = mx;
            this.mouseY = my;
        }
    }

    /**
     * 查找第一个玩家控制的、单位类型为 targetType 的单位，并返回其鼠标坐标。
     */
    public static PlayerUnitResult findPlayerUnit(UnitType targetType) {
        // ★ 使用 Groups.player 遍历
        for (Player player : Groups.player) {
            Unit unit = player.unit();
            if (unit != null && unit.type == targetType) {
                return new PlayerUnitResult(unit, player.mouseX, player.mouseY);
            }
        }
        return null;
    }

    /**
     * 带队伍过滤的版本
     */
    public static PlayerUnitResult findPlayerUnit(UnitType targetType, Team team) {
        for (Player player : Groups.player) {
            if (player.team() != team) continue;
            Unit unit = player.unit();
            if (unit != null && unit.type == targetType) {
                return new PlayerUnitResult(unit, player.mouseX, player.mouseY);
            }
        }
        return null;
    }
}