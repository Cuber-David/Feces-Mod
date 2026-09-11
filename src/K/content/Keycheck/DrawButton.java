package K.content.Keycheck;

import K.content.KUnitTypes;
import K.content.extend.util.PlayerUtils;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import mindustry.gen.Player;

import static K.content.extend.util.PlayerUtils.findPlayerUnit;

public class DrawButton {
    private boolean initialized = false;
    public float time = 0;

    public void drawIconRody(){
        PlayerUtils.PlayerUnitResult result = findPlayerUnit(KUnitTypes.Rody);
        if (result!=null){
            Player p = result.unit.getPlayer();
        }

        float width = Core.camera.width;
        float height = Core.camera.height;
        float cx = Core.camera.position.x;
        float cy = Core.camera.position.y;
        time += Time.delta;
        TextureRegion jie = Core.atlas.find("kmod-jie");
        float x1 = cx - 10;
        float y1 = cy + height/2;
        Draw.z(300);
        Draw.color(Color.red);
        Fill.circle(cx,cy,100);
    }
}

