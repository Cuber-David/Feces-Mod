package K.content.unit.others;

import mindustry.content.Fx;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class DomainUnitType extends UnitType {
    public DomainUnitType(String name) {
        super(name);
        constructor = DomainUnit::new;
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
    }

    @Override
    public void draw(Unit unit) {
    }
}
