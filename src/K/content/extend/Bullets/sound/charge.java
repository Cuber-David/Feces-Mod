package K.content.extend.Bullets.sound;

import K.content.sounds;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class charge extends BulletType {
    public charge() {
        super();
        lifetime = 1f;
        damage = 0;
        hitEffect = Fx.none;
        shootEffect = Fx.none;
        hitShake = 0;
        despawnSound = sounds.charge;
        despawnEffect = Fx.none;
        lightOpacity = 0;
        lightRadius = 0;
    }
}
