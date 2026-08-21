package K.content.extend.Bullets;

import K.content.Fx.fx;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;

public class SlashBulletType extends BulletType {
    public SlashBulletType(){
        super(0,1000);
        lifetime = 3;
        despawnEffect = fx.slash;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);
    }

    @Override
    public void draw(Bullet b) {

    }
}
