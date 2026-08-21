package K.content.NH;

import K.graphics.NHRenderer;
import K.graphics.ScreenShaderDrawer;
import arc.Core;
import mindustry.Vars;

public class NHVars {
    public static NHWorldData worldData;
    public static NHRenderer renderer;

    public static CutsceneControl cutscene;
    public static CutsceneUI cutsceneUI;

    public static void init() {

        worldData = new NHWorldData();

        UpdateProxy.init();

        cutscene = new CutsceneControl();
        cutsceneUI = new CutsceneUI();


        if (Vars.headless) return;
        initHeadless();
    }

    public static void initHeadless() {
        renderer = new NHRenderer();
        ScreenShaderDrawer.init();
    }
}
