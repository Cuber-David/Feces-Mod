package K.content.extend.Bullets.jujutsu;

import K.KMod;
import K.content.Fx.KFx;
import K.content.effects.SpecialDeathEffects;
import K.Other_mod.FM.flame_extend.EmpathyDamage;
import K.content.extend.util.Utils;
import K.content.extend.weapons.KWeapon;
import K.graphics.CutBatch;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;

public class SlashBulletType extends BulletType {
    public SlashBulletType(){
        super(100,1000);
        lifetime = 30;
        hitSize = 24;
        despawnEffect = Fx.none;
        hitEffect = KFx.slash;
        pierce = true;
        pierceCap = 4;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);
    }

    public void despawned(Bullet b){
        float randlength = 100;
        float r = Mathf.random(360);
        float x = Mathf.random(randlength)-randlength/2;
        float y = Mathf.random(randlength)-randlength/2;
        KFx.slash.at(b.x+x,b.y+y,r);
        super.despawned(b);
    }

    @Override
    public void update(Bullet b){
        super.update(b);
        KWeapon.hit(b.team,b.rotation(),b.x,b.y,100,1000,1000);
        Units.nearbyEnemies(b.team,b.x,b.y,b.rotation(),unit -> {
            if (unit!=null) hit(b);
        });
    }

    @Override
    public void draw(Bullet b) {
    }
}
