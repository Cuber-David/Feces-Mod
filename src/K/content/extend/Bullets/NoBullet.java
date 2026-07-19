package K.content.extend.Bullets;

import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;

public class NoBullet extends BulletType {
    public NoBullet() {
        super();
        damage = 0;
        hitEffect = Fx.none;
        shootEffect = Fx.none;
        hitShake = 0;
        hitSound = Sounds.none;
    }
}
