package K.Othermod.NH;

import arc.Events;
import mindustry.game.EventType;

public class NHRegister {
    static {
    }

    public static void load() {
        Events.on(EventType.ResetEvent.class, e -> NHGroups.clear());
        Events.on(EventType.WorldLoadBeginEvent.class, e -> NHGroups.worldReset());

        Events.run(EventType.Trigger.draw, () -> {
            NHGroups.draw();
        });

        Events.on(EventType.WorldLoadEvent.class, e -> {
            NHGroups.worldInit();
        });
    }
}
