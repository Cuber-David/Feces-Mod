package K.content.Keycheck;

import K.content.KUnitTypes;
import K.content.extend.Bullets.SlashBulletType;
import arc.Core;
import arc.Events;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.game.Team;
import mindustry.game.EventType;
import mindustry.content.UnitTypes;
import mindustry.type.UnitType;

public class KeybindRody {
    private static boolean initialized = false;
    private static boolean lastF2 = false;
    private static boolean lastF3 = false;
    private static boolean lastF4 = false;
    private static boolean lastF5 = false;
    private static boolean isCharging = false;
    private static float chargetime = 0f;
    private static float mxt = 180f;
    private static float mnt = 120f;
    private static float px = Vars.player.x;
    private static float py = Vars.player.y;
    private static float mx = Core.input.mouseWorldX();
    private static float my = Core.input.mouseWorldX();
    private static float urot = Vars.player.angleTo(mx,my)*Mathf.degRad;
    private static final String[] ALLOWED_UNITS = {
            "kmod-Rody"
    };
    public static void init() {
        if (initialized) return;
        Events.run(EventType.Trigger.update, () -> {
            if (!checkPlayer()) return;
            if (!isPlayerUnitAllowed()) {
                return;
            }
            boolean f2Down = Core.input.keyDown(KeyCode.f2);
            if (f2Down && !lastF2) {
                spawnBullet();
            }
            lastF2 = f2Down;
            boolean f3Down = Core.input.keyDown(KeyCode.f3);
            if (f3Down && !lastF3) {
                spawnUnitAtMouse(KUnitTypes.Ba, "Ba");
            }
            lastF3 = f3Down;
            boolean f4Down = Core.input.keyDown(KeyCode.f4);
            if (f4Down && !lastF4) {
                startCharge();
            } else if (f4Down && isCharging){
                chargetime += Time.delta;
                if (chargetime >= mxt){
                    chargetime = mxt;
                }
                drawce();
            } else if (!f4Down && isCharging) {
                release();
            }
            lastF4 = f4Down;
            boolean f5Down = Core.input.keyDown(KeyCode.f5);
            if (f5Down && !lastF5) {
                spawnUnitAtMouse(UnitTypes.scepter, "scepter");
            }
            lastF5 = f5Down;
        });
        initialized = true;
    }
    private static boolean checkPlayer() {
        if (Vars.player == null) return false;
        Unit playerUnit = Vars.player.unit();
        if (playerUnit == null) return false;
        if (playerUnit.dead()) return false;
        return true;
    }
    private static boolean isPlayerUnitAllowed() {
        Unit playerUnit = Vars.player.unit();
        if (playerUnit == null) return false;
        String unitName = playerUnit.type.name;
        for (String allowed : ALLOWED_UNITS) {
            if (unitName.equals(allowed)) {
                return true;
            }
        }
        return false;
    }
    private static String getPlayerUnitInfo() {
        if (Vars.player == null) return "玩家不存在";
        Unit unit = Vars.player.unit();
        if (unit == null) return "没有控制单位";
        return unit.type.name + " (HP: " + (int)unit.health + "/" + (int)unit.maxHealth() + ")";
    }
    private static void spawnUnitAtMouse(UnitType unitType, String name) {
        if (!checkPlayer()) {
            Log.info("玩家不存在，无法生成");
            return;
        }
        Team team = Vars.player.team();
        Unit unit = unitType.spawn(team, px+Mathf.cos(urot)*160, py+Mathf.sin(urot)*160);
    }

    private static void spawnBullet() {
        Team team = Vars.player.team();
        float rot = urot*Mathf.radDeg;
        float dst = Vars.player.dst(mx,my)/2850f;
        for (int i = 0; i < 20; i++) {
            SlashBulletType.createBullet(new SlashBulletType(),team,px,py,rot,1000,1,dst);
        }
    }

    private static void startCharge(){
        isCharging = true;
        chargetime = 0f;
    }

    private static void release(){
        if (chargetime<mnt){
            rest();
            return;
        }
        float CRatio = Mathf.clamp(chargetime/mxt);
        atk(CRatio);
        rest();
    }

    private static void atk(float c){
        if(!checkPlayer()) return;
    }

    private static void rest(){
        isCharging = false;
        chargetime = 0;
    }

    private static void drawce(){
        if(!checkPlayer()) return;

    }

    private float cx(float l){
        return Mathf.cos(urot)*l;
    }
    private float sx(float l){
        return Mathf.sin(urot)*l;
    }
}