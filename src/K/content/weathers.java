package K.content;

import K.content.extend.StormWeather;
import arc.graphics.*;
import arc.util.*;
import mindustry.content.StatusEffects;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;

public class weathers {
    public static Weather
    storm;

    public static void load(){
        storm = new StormWeather("storm"){{
            attrs.set(Attribute.light, -0.2f);
            attrs.set(Attribute.water, 0.2f);
            status = StatusEffects.wet;
            sound = Sounds.rain;
            soundVol = 1.5f;
        }};
    }
}
