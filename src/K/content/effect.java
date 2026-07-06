package K.content;

import mindustry.type.StatusEffect;

public class effect {
    public static StatusEffect
    sick;

    public static void load() {
        sick = new StatusEffect("sick"){{
            speedMultiplier = 0.8f;
            reloadMultiplier = 0.8f;
        }};
    }
}
