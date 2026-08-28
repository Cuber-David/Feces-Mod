package K.content.extend.Bullets;

import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;

public class NoBullet extends BulletType {
    public NoBullet() {
        super(0,0);
        damage = 0;
        hitEffect = despawnEffect = Fx.none;
        shootEffect = Fx.none;
        hitShake = 0;
        hitSound = Sounds.none;
    }

    @Override
    public void draw(Bullet b) {
    }
}
