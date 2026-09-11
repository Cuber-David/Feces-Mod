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

        public PlayerUnitResult(Unit u, Player player, float mx, float my) {
            this.unit = u;
            this.mouseX = mx;
            this.mouseY = my;
        }
    }

    public static PlayerUnitResult findPlayerUnit(UnitType targetType) {
        for (Player player : Groups.player) {
            Unit unit = player.unit();
            if (unit != null && unit.type == targetType) {
                return new PlayerUnitResult(unit, player, player.mouseX, player.mouseY);
            }
        }
        return null;
    }

    public static PlayerUnitResult findPlayerUnit(UnitType targetType, Team team) {
        for (Player player : Groups.player) {
            if (player.team() != team) continue;
            Unit unit = player.unit();
            if (unit != null && unit.type == targetType) {
                return new PlayerUnitResult(unit, player, player.mouseX, player.mouseY);
            }
        }
        return null;
    }
}