package K.content.Keycheck;

import K.content.KUnitTypes;
import K.content.extend.util.PlayerUtils;
import mindustry.gen.Player;

import static K.content.extend.util.PlayerUtils.findPlayerUnit;

public class DrawButton {
    public void drawIconRody(){
        PlayerUtils.PlayerUnitResult result = findPlayerUnit(KUnitTypes.Rody);
        if (result!=null){
            Player p = result.unit.getPlayer();
        }
    }
}

