package K.content.extend.Math;

import arc.func.Floatc2;
import arc.math.Rand;
import arc.math.geom.Vec2;

public class kangles {
    static Rand rand = new Rand();
    static Vec2 rv = new Vec2();
    public static void lineVector(long seed, float amount, float length, float angle, Floatc2 cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(angle, rand.random(length));
            cons.get(rv.x, rv.y);
        }
    }
}
