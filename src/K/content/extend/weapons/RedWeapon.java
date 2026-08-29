package K.content.extend.weapons;

import K.content.extend.Bullets.jujutsu.RedBulletType;
import K.content.sounds;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class RedWeapon extends Weapon {
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
    }

    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation) {
        super.shoot(unit, mount, shootX, shootY, rotation);
    }

    @Override
    public void update(Unit unit, WeaponMount mount) {
        super.update(unit, mount);
        if(unit.isShooting()){
            if(unit.hitSize>1.21) {
                unit.hitSize(unit.hitSize - 0.12f);
            }
        } else {
            if (unit.hitSize<19.8f){
                unit.hitSize(unit.hitSize + 0.12f);
            }
        }
    }
}
