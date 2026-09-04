package K.content.Keycheck;

import K.content.Fx.KFx;
import K.content.KUnitTypes;
import K.content.extend.Bullets.jujutsu.DomainInf;
import K.content.extend.util.DrawPurple;
import K.content.extend.util.PlayerUtils;
import K.content.sounds;
import K.content.statuseffect;
import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.Lightning;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class KeybindGodK {
    private static boolean initialized = false;
    private static boolean lastF2 = false;
    private static boolean lastF6 = false;
    private static boolean lastF4 = false;
    private static boolean lastF5 = false;
    private static boolean isCharging = false;
    private static float chargetime = 0f;
    private static final float mxt = 180f;
    private static final float mnt = 120f;
    private static boolean ce = false;
    private static boolean dc = false;
    private static boolean w = false;
    private static Unit b = null;
    private static Unit r = null;
    private static Bullet c=null;
    private static final float l = 10;
    private static final String[] ALLOWED_UNITS = {
            "kmod-GodK"
    };
    public static void init() {
        if (initialized) return;
        Events.run(EventType.Trigger.update, () -> {
            if (!checkPlayer()) return;
            if (!isPlayerUnitAllowed()) {
                return;
            }
            boolean shiftDown = Core.input.keyDown(KeyCode.shiftLeft) || Core.input.keyDown(KeyCode.shiftRight);
            boolean f2Down = Core.input.keyDown(KeyCode.f2);
            if (f2Down) {
                if(!hasUnit(KUnitTypes.Blue,getPlayer().unit.team)){
                    b = spawnUnitAtMouse(KUnitTypes.Blue);
                } else {
                    if (b==null){
                        b = findAnyUnit(KUnitTypes.Blue,getPlayer().unit.team);
                    }
                }
                b = findAnyUnit(KUnitTypes.Blue,getPlayer().unit.team);
                if (b != null) {
                    float mx = getPlayer().mouseX;
                    float my = getPlayer().mouseY;
                    float dp = getPlayer().unit.dst(mx,my);
                    if (dp<181){
                        b.remove();
                    } else {
                        float d = b.dst(mx, my);
                        float r = b.angleTo(mx, my) * Mathf.degRad;
                        if (shiftDown) {
                            b.isShooting = true;
                        } else {
                            if (d > 10) {
                                b.x += Mathf.cos(r) * l;
                                b.y += Mathf.sin(r) * l;
                            }
                        }
                    }
                }
            }
            lastF2 = f2Down;
            boolean f3Down = Core.input.keyDown(KeyCode.f3);
            if (f3Down) {
                if(!hasUnit(KUnitTypes.Red,getPlayer().unit.team)){
                    r = spawnUnitAtMouse(KUnitTypes.Red);
                } else {
                    if (r==null){
                        r = findAnyUnit(KUnitTypes.Red,getPlayer().unit.team);
                    }
                }
                r = findAnyUnit(KUnitTypes.Red,getPlayer().unit.team);
                if (r != null) {
                    float mx = getPlayer().mouseX;
                    float my = getPlayer().mouseY;
                    float dp = getPlayer().unit.dst(mx,my);
                    if (dp<181){
                        r.remove();
                    } else {
                        float d = r.dst(mx, my);
                        float ro = r.angleTo(mx, my) * Mathf.degRad;
                        if (shiftDown) {
                            if (r.armor<100)r.armor = 100;
                        } else {
                            if (d > 10) {
                                r.x += Mathf.cos(ro) * l;
                                r.y += Mathf.sin(ro) * l;
                            }
                        }
                    }
                }
            }
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
                    c = new DomainInf().create(u, team, px - cx(160), py - sx(160), rot);
                    ce = c != null;
                    u.apply(statuseffect.domainopen,1E30f);
                } else {
                    c.remove();
                    dc = true;
                }
            }
            lastF5 = f5Down;
            boolean f6Down = Core.input.keyDown(KeyCode.f6);
            if (f6Down && !lastF6) {
                float px = getPlayer().unit.x;
                float py = getPlayer().unit.y;
                if(hasUnit(KUnitTypes.Purple,getPlayer().unit.team)){
                    if (b==null){
                        b = findAnyUnit(KUnitTypes.Purple,getPlayer().unit.team);
                    }
                }else {
                    if (w) {
                        sounds.weak.at(px, py, 1, 1);
                        w = false;
                    }
                }
                b = findAnyUnit(KUnitTypes.Purple,getPlayer().unit.team);
                if (b != null) {
                    b.mounts[0].weapon.alwaysShooting = true;
                    if(b.isShooting())b.remove();
                }
            } else w = true;
            lastF6 = f6Down;
            Events.on(EventType.UnitDamageEvent.class, e -> {
                Unit u = e.unit;
                if (u == null || u.type == null) return;
                if (KUnitTypes.Domaininf.equals(u.type)) {
                    dc = true;
                }
            });
            if((dc)){
                Unit u = getPlayer().unit;
                ce = false;
                u.clearStatuses();
                u.apply(statuseffect.jujutsufuse,600);
                dc = false;
            }
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
        return PlayerUtils.findPlayerUnit(KUnitTypes.GodK);
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
    private static Unit spawnUnitAtMouse(UnitType unitType) {
        float px = getPlayer().unit.x;
        float py = getPlayer().unit.y;
        if (!checkPlayer()) {
            Log.info("玩家不存在，无法生成");
            return null;
        }
        Team team = getPlayer().unit.team();
        return unitType.spawn(team, px+cx(160), py+sx(160));
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
            Unit u1 = KUnitTypes.Purple.spawn(u.team,px+cx(160),py+sx(160));
            u1.hitSize = chargetime/4;
            u1.vel().add(cx(160),sx(160));
            u1.mounts[0].weapon.alwaysShooting = false;
        }
    }

    private static void rest(){
        isCharging = false;
        chargetime = 0;
    }

    private static void drawce(){
        float px = getPlayer().unit.x;
        float py = getPlayer().unit.y;
        DrawPurple.drawp(px+cx(160), py+sx(160),chargetime);
        KFx.vortexWarpEffect.at(px+cx(160), py+sx(160),chargetime);
        Lightning.create(getPlayer().unit.team,Color.valueOf("f1ccf7").a(0.9f),1f,px+cx(160), py+sx(160), Mathf.random(360), (int) Mathf.random(chargetime/4));

    }

    private static float cx(float l){
        return Mathf.cos(getPlayer().unit.angleTo(getPlayer().mouseX,getPlayer().mouseY)*Mathf.degRad)*l;
    }
    private static float sx(float l){
        return Mathf.sin(getPlayer().unit.angleTo(getPlayer().mouseX,getPlayer().mouseY)*Mathf.degRad)*l;
    }
    public static boolean hasUnit(UnitType unitType, Team team) {
        if (unitType == null || team == null) return false;
        for (Unit unit : Groups.unit) {
            if (unit != null && !unit.dead() && unit.type == unitType && unit.team == team) {
                return true;
            }
        }
        return false;
    }
    public static Unit findAnyUnit(UnitType targetType, Team team) {
        if (targetType == null) return null;

        for (Unit unit : Groups.unit) {
            if (unit == null || unit.dead()) continue;
            if (unit.type != targetType) continue;
            if (team != null && unit.team != team) continue;
            return unit;
        }
        return null;
    }
}
