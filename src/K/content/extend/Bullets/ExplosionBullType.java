package K.content.extend.Bullets;

import K.content.Fx.fx;
import K.content.sounds;
import mindustry.entities.bullet.BulletType;

public class ExplosionBullType extends BulletType {
    public ExplosionBullType(){
        super(0,1000);
        lifetime = 3;
        hitSize = 64;
        despawnEffect = hitEffect = fx.exp;
        despawnSound = hitSound = sounds.smallexp;
        pierce = true;
    }
}
