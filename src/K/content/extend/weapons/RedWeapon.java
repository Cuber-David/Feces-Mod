package K.content.extend.weapons;

import K.content.Fx.KFx;
import K.content.extend.Bullets.jujutsu.RedBulletType;
import K.content.sounds;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Bullet;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class RedWeapon extends Weapon {
    private boolean s = false;
    public RedWeapon(){
        x=0;
        y=0;
        shootY=0;
        mirror = false;
        reload = 300;
        bullet = new RedBulletType();
        shake = 100;
        shootSound = sounds.redcharge;
        shootCone = 360;
        alwaysContinuous = true;
    }

    @Override
    public void update(Unit unit, WeaponMount mount) {
        super.update(unit, mount);
        if (unit.armor==100) {
            unit.isShooting = true;
            Teamc teamc = unit.team.core();
            KFx.Shcokcharge.at(unit.x,unit.y,0);
            new RedBulletType().create(teamc,unit.x,unit.y,0);
            unit.armor = 1000;
        }
        if(unit.isShooting()) s = true;
        if(s){
            if(unit.hitSize>1.21) {
                unit.hitSize(unit.hitSize - 0.36f);
            } else s = false;
        } else {
            if (unit.hitSize<19.8f){
                unit.hitSize(unit.hitSize + 0.12f);
            }
        }
    }
}
