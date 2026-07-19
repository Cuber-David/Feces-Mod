package K.content.extend.Bullets.sound;

import K.content.sounds;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class alarm extends BulletType {
    public alarm() {
        super();
        lifetime = 1f;
        damage = 0;
        hitEffect = Fx.none;
        shootEffect = Fx.none;
        hitShake = 0;
        despawnSound = sounds.alarm;
        despawnEffect = Fx.none;
        lightOpacity = 0;
        lightRadius = 0;
    }
}
