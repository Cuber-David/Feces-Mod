package K.content.extend.Bullets;

import K.content.sounds;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class Soundb extends BulletType {
    public Soundb() {
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
        collideFloor = false;
        collidesAir = false;
        collidesGround = false;
    }
}
