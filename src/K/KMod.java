package K;

import K.content.*;
import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.*;

public class KMod extends Mod{

    public KMod(){

        //listen for game load event
        Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("Welcome to K's ModV2").row();
                dialog.cont.image(Core.atlas.find("kmod-frog")).pad(20f).row();
                dialog.cont.button("I konw", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
    }

    @Override
    public void loadContent() {
        items.load();
        blocks.load();
        KPlanetGenerator.load();
        planets.load();
        sector.load();
        techtree.load();
    }

}
