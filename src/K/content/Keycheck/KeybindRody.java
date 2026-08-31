package K.content.Keycheck;

import K.content.Fx.OtherEffects.KaiEffect;
import K.content.KUnitTypes;
import K.content.extend.Bullets.jujutsu.DomainCook;
import K.content.extend.Bullets.jujutsu.KaiBulletType;
import K.content.extend.Bullets.jujutsu.SlashBulletType;
import K.content.extend.util.PlayerUtils;
import K.content.statuseffect;
import arc.Core;
import arc.Events;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.game.Team;
import mindustry.game.EventType;
import mindustry.type.UnitType;

public class KeybindRody {
    private static boolean initialized = false;
    private static boolean lastF2 = false;
    private static boolean lastF3 = false;
    private static boolean lastF4 = false;
    private static boolean lastF5 = false;
    private static boolean isCharging = false;
    private static float chargetime = 0f;
    private static final float mxt = 180f;
    private static final float mnt = 120f;
    private static boolean ce = false;
    private static Bullet c=null;
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
                spawnUnitAtMouse(KUnitTypes.Ba);
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
                Unit u = getPlayer().unit;
                float mx = getPlayer().mouseX;
                float my = getPlayer().mouseY;
                float px = getPlayer().unit.x;
                float py = getPlayer().unit.y;
                Team team = getPlayer().unit.team();
                float rot = getPlayer().unit.angleTo(mx,my);
                if (!ce) {
                    c = new DomainCook().create(u, team, px - cx(160), py - sx(160), rot);
                    ce = c != null;
                    u.apply(statuseffect.domainopen,3600);
                } else {
                    c.remove();
                    ce = false;
                    u.clearStatuses();
                    u.apply(statuseffect.jujutsufuse,600);
                }
            }
            lastF5 = f5Down;
        });
        initialized = true;
    }
    private static boolean checkPlayer() {
        if (Vars.player == null) {
            return false;
        } else {
            if (getPlayer()!=null) {
                Unit playerUnit = getPlayer().unit;
                return !playerUnit.hasEffect(statuseffect.jujutsufuse);
            } else return false;
        }
    }

    private static PlayerUtils.PlayerUnitResult getPlayer(){
        return PlayerUtils.findPlayerUnit(KUnitTypes.Rody);
    }

    private static boolean isPlayerUnitAllowed() {
        if(Vars.player == null) return false;
        Unit playerUnit = getPlayer().unit;
        if (playerUnit == null) return false;
        String unitName = playerUnit.type.name;
        for (String allowed : ALLOWED_UNITS) {
            if (unitName.equals(allowed)) {
                return true;
            }
        }
        return false;
    }
    private static void spawnUnitAtMouse(UnitType unitType) {
        float px = getPlayer().unit.x;
        float py = getPlayer().unit.y;
        if (!checkPlayer()) {
            Log.info("玩家不存在，无法生成");
            return;
        }
        Team team = getPlayer().unit.team();
        Unit unit = unitType.spawn(team, px+cx(160), py+sx(160));
    }

    private static void spawnBullet() {
        float mx = getPlayer().mouseX;
        float my = getPlayer().mouseY;
        float px = getPlayer().unit.x;
        float py = getPlayer().unit.y;
        Team team = getPlayer().unit.team();
        float rot = getPlayer().unit.angleTo(mx,my);
        float dst = getPlayer().unit.dst(mx,my)/2850f;
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
        if(checkPlayer()){
            float mx = getPlayer().mouseX;
            float my = getPlayer().mouseY;
            float px = getPlayer().unit.x;
            float py = getPlayer().unit.y;
            float rot = getPlayer().unit.angleTo(mx,my);
            Unit u = getPlayer().unit;
            new KaiBulletType(chargetime).create(u,u.team,px+cx(chargetime),py+sx(chargetime),rot);
        }
    }

    private static void rest(){
        isCharging = false;
        chargetime = 0;
    }

    private static void drawce(){
        float mx = getPlayer().mouseX;
        float my = getPlayer().mouseY;
        float px = getPlayer().unit.x;
        float py = getPlayer().unit.y;
        float rot = getPlayer().unit.angleTo(mx,my);
        if(checkPlayer()){
            new KaiEffect(chargetime).at(px+cx(180),py+sx(180),rot);
        }
    }

    private static float cx(float l){
        return Mathf.cos(getPlayer().unit.angleTo(getPlayer().mouseX,getPlayer().mouseY)*Mathf.degRad)*l;
    }
    private static float sx(float l){
        return Mathf.sin(getPlayer().unit.angleTo(getPlayer().mouseX,getPlayer().mouseY)*Mathf.degRad)*l;
    }
}