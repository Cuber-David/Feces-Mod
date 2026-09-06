package K;

import K.content.*;
import K.content.Fx.OtherFx;
import K.content.Fx.KFx;
import K.content.Keycheck.KeybindGodK;
import K.content.Keycheck.KeybindRody;
import K.Other_mod.NH.NHContent;
import K.content.campaign.*;
import K.content.effectrenderer.StatusEffectRenderer;
import K.content.effects.Severation;
import K.content.effects.SpecialDeathEffects;
import K.content.entities.EntityRegister;
import K.Other_mod.FM.flame_extend.EmpathyDamage;
import K.Other_mod.FM.flame_extend.SpecialMain;
import K.content.KUnitTypes;
import K.entities.DeathTrigger;
import K.entities.MockGroup;
import K.entities.SimpleFragments;
import K.graphics.*;
import K.Other_mod.FM.AntiCheat;
import arc.*;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.input.KeyCode;
import arc.math.Angles;
import arc.struct.Seq;
import arc.util.*;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.type.StatusEffect;
import mindustry.ui.dialogs.*;
import mindustry.world.blocks.storage.CoreBlock;

public class KMod extends Mod{
    public static Mods.LoadedMod MOD;
    public static AntiCheat antiCheat;
    public static CutBatch cutBatch;
    public static FragmentationBatch fragBatch;
    public static VaporizeBatch vaporBatch;
    public static DevastationBatch devasBatch;

    public static final String MOD_NAME = "kmod";
    public final boolean test = false;
    private StatusEffectRenderer renderer;



    public KMod(){


        Events.on(ClientLoadEvent.class, e -> {
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("Welcome to Feces Mod's First Version").row();
                dialog.cont.image(Core.atlas.find("kmod-frog")).pad(20f).row();
                dialog.cont.button("I konw", dialog::hide).size(120f, 60f);
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
                antiCheat = new AntiCheat();
                KSFX.inst.loadHeadless();
                SpecialMain.load();
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
                    OtherFx.FlameFX.desMissileHit.at(p.x, p.y, ang);
                }
            });
        }
    }

    @Override
    public void init() {
        SimpleFragments.init();

        SimpleFragments.gridCols = 5;
        SimpleFragments.gridRows = 5;
        SimpleFragments.fragmentSpeed = 0.5f;
        SimpleFragments.gap = 1f;
        SimpleFragments.randomOffset = true;

        MainRenderer.init();
        StatusEffect target = Vars.content.getByName(ContentType.status, "kmod-infinitude");
        if (target == null) {
            target = statuseffect.infinitude;
        }
        renderer = new StatusEffectRenderer(target);
        Events.run(EventType.Trigger.update, () -> {
            if (renderer != null) renderer.update();
        });

        Events.run(EventType.Trigger.draw, () -> {
            if (renderer != null) renderer.draw();
        });
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

        EntityRegister.load();
        sounds.load();
        KFx.load();
        statuseffect.load();

        SpecialDeathEffects.load();
        weathers.load();
        items.load();
        liquids.load();
        KUnitTypes.load();
        blocks.load();
        loadouts.load();
        Vars.schematics.getLoadouts().get((CoreBlock)blocks.fecescore, Seq::new).add(loadouts.basiccore,loadouts.basioncore);
        KPlanetGenerator.load();
        planets.load();
        sector.load();
        techtree.load();
        NHContent.loadLast();
        KeybindRody.init();
        KeybindGodK.init();
    }

    private void createTestCut(float x, float y) {
        try {
            // 1. 获取贴图
            TextureRegion region = Core.atlas.find("circle");
            if (region == null || !region.found()) {
                region = Core.atlas.white();
            }

            // 2. 直接创建 Severation
            Severation sev = new Severation();
            sev.region.set(region);
            sev.x = x;
            sev.y = y;
            sev.width = 50f;
            sev.height = 50f;
            sev.rotation = 0f;
            sev.z = Layer.flyingUnit;
            sev.color = Color.toFloatBits(1f, 0f, 0f, 1f); // 红色，方便看到

            // 3. 添加两个三角形组成矩形
            Severation.CutTri t1 = new Severation.CutTri();
            t1.pos[0] = -0.5f; t1.pos[1] = -0.5f;
            t1.pos[2] = 0.5f; t1.pos[3] = -0.5f;
            t1.pos[4] = -0.5f; t1.pos[5] = 0.5f;
            sev.tris.add(t1);

            Severation.CutTri t2 = new Severation.CutTri();
            t2.pos[0] = 0.5f; t2.pos[1] = -0.5f;
            t2.pos[2] = 0.5f; t2.pos[3] = 0.5f;
            t2.pos[4] = -0.5f; t2.pos[5] = 0.5f;
            sev.tris.add(t2);

            // 4. 更新边界
            sev.updateBounds();
            Log.info("Created Severation: tris=" + sev.tris.size + ", area=" + sev.area);

            // 5. 添加到世界
            sev.add();
            Log.info("Severation added to world");

            // 6. 检查是否真的添加成功
            Log.info("Is added: " + sev.isAdded());
            Log.info("CutsSeq size: " + Severation.cutsSeq.size);

        } catch (Exception e) {
            Log.err("Test cut failed: " + e);
            e.printStackTrace();
        }
    }
}
