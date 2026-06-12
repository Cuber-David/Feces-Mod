package K;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.*;

import K.content.blocks;
import K.content.items;

public class KMod extends Mod{

    public KMod(){

        //listen for game load event
        Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("Welcome to K's ModV1").row();
                dialog.cont.button("I konw", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
    }

    @Override
    public void loadContent() {
        items.load();
        blocks.load();
    }

}
