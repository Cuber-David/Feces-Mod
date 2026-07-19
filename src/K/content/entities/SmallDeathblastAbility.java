package K.content.entities;

import K.content.sounds;
import arc.Core;
import arc.graphics.Color;
import mindustry.content.StatusEffects;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.*;

public class SmallDeathblastAbility extends Ability {

    public SmallDeathblastAbility(){
    }

    public BulletType b = new BulletType() {{
        damage = 8 * 2000;
        lifetime = 0.01f;
        splashDamage = 2000;
        splashDamageRadius = 160;
        lightningColor = Color.white;
    }};

    @Override
    public void death(Unit unit) {
        Sounds.explosionReactor2.at(unit);
        unit.apply(StatusEffects.none, 120f);
        b.create(unit, unit.x, unit.y, unit.rotation);
    }

    @Override
    public String localized(){
        return Core.bundle.format("ability.deathblast");
    }
}
