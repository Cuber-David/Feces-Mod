package K.content;

import K.content.Fx.KFx;
import mindustry.type.StatusEffect;

public class statuseffect {
    public static StatusEffect
    none,sick,infinitude,domainopen,jujutsufuse;

    public static void load() {
        none = new StatusEffect("non"){{}};
        sick = new StatusEffect("sick"){{
            effect = KFx.disorder;
            effectChance = 0.1f;
            reactive = false;
            speedMultiplier = 0.8f;
            reloadMultiplier = 0.8f;
        }};
        infinitude = new StatusEffect("infinitude"){{
            effect = KFx.disorder;
            effectChance = 0.1f;
            reactive = false;
            speedMultiplier = 0f;
            reloadMultiplier = 0f;
        }};
        domainopen = new StatusEffect("domainopen"){{
            init(() -> opposite(statuseffect.infinitude));
            damageMultiplier = 1.2f;
            healthMultiplier = 1.2f;
        }};
        jujutsufuse = new StatusEffect("jujutsufuse"){{
            damageMultiplier = 0.9f;
        }};
    }
}
