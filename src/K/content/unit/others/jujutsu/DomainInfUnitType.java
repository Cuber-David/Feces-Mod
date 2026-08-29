package K.content.unit.others.jujutsu;

import K.content.extend.Bullets.jujutsu.DomainInf;
import mindustry.content.Fx;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class DomainInfUnitType extends UnitType {
    public DomainInfUnitType(String name) {
        super(name);
        constructor = DomainInfUnit::new;
        health = 8000;
        hitSize = 1050;
        flying = true;
        deathExplosionEffect = Fx.none;
        aiController = DomainAI::new;
        drawCell = false;
        engineSize = 0;
        crashDamageMultiplier = 0;
        deathShake = 0;
        deathSoundVolume = 0;
        hidden = true;
    }
    public void draw(Unit b){
    }

    public void update(Unit u) {
        boolean e = false;
        for (Bullet bullet : Groups.bullet) {
            if (bullet.type instanceof DomainInf) {
                e = true;
                break;
            }
        }
        if (!e) {
            u.remove();
        }
        super.update(u);
    }
}
