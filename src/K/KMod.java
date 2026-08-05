package K;

import K.content.*;
import K.content.effects.ImpactBatch;
import K.content.effects.Severation;
import K.content.effects.SpecialDeathEffects;
import K.content.effects.SpriteAnimationEffect;
import K.content.extend.fo.EmpathyDamage;
import K.content.extend.fo.SpecialMain;
import K.content.KUnitTypes;
import K.entities.MockGroup;
import K.graphics.*;
import arc.*;
import arc.input.KeyCode;
import arc.math.Angles;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.gen.Unit;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.*;

public class KMod extends Mod{
    public static CutBatch cutBatch;
    public static FragmentationBatch fragBatch;
    public static VaporizeBatch vaporBatch;
    public static DevastationBatch devasBatch;

    public static final String MOD_NAME = "kmod";
    public final boolean test = false;

    public KMod(){

        //listen for game load event
        Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("Welcome to K's Mod FIRST TEST").row();
                dialog.cont.image(Core.atlas.find("kmod-frog")).pad(20f).row();
                dialog.cont.button("I konw", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
        MockGroup.load();
        Severation.init();
        new KSFX();
        Events.on(FileTreeInitEvent.class, e -> Core.app.post(() -> {
            if(!Vars.headless){
                FlameShaders.load();
                ImpactBatch.init();
                fragBatch = new FragmentationBatch();
                cutBatch = new CutBatch();
                vaporBatch = new VaporizeBatch();
                devasBatch = new DevastationBatch();
                //fragBatch.load();
                KSFX.inst.loadHeadless();
                SpecialMain.load();
                //SpecialDeathEffects.load();
            }
        }));
        Events.on(ClientLoadEvent.class, e -> {
            SpecialMain.loadClient();
        });
        Events.on(WorldLoadEvent.class, e -> EmpathyDamage.worldLoad());
        if(test){
            Events.run(Trigger.update, () -> {
                Unit p = Vars.player.unit();
                if(Core.input.keyTap(KeyCode.x)){
                    float ang = Angles.mouseAngle(p.x, p.y);
                    //FlameBullets.test.create(p, p.x, p.y, ang);
                    FlameFX.desMissileHit.at(p.x, p.y, ang);
                }
            });
        }
    }

    public static String name(String name) {
        return MOD_NAME + "-" + name;
    }

    public static void print(Object... args){
        print(Log.LogLevel.info, " ", args);
    }

    public static void print(Log.LogLevel level, Object... args){
        print(level, " ", args);
    }

    @Override
    public void loadContent() {
        sounds.load();
        bullets.load();
        fx.load();
        effect.load();

        SpriteAnimationEffect.load();
        SpecialDeathEffects.load();
        weathers.load();
        items.load();
        liquids.load();
        KUnitTypes.load();
        blocks.load();
        KPlanetGenerator.load();
        planets.load();
        sector.load();
        techtree.load();
    }

}
