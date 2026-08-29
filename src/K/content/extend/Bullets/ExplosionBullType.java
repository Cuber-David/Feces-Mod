package K.content.extend.Bullets;

import K.content.Fx.KFx;
import K.content.sounds;
import mindustry.entities.bullet.BulletType;

public class ExplosionBullType extends BulletType {
    public ExplosionBullType(){
        super(0,1000);
        lifetime = 3;
        hitSize = 64;
        despawnEffect = hitEffect = KFx.exp;
        despawnSound = hitSound = sounds.smallexp;
        pierce = true;
    }
}
