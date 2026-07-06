package K.content.entities;

import K.content.sounds;
import arc.Core;
import arc.graphics.Color;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.StatusEffects;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.*;

public class DeathblastAbility extends Ability {

    public BulletType b = new BulletType() {{
        damage = 50000f;
        lifetime = 0.01f;
        splashDamage = 8000f;
        splashDamageRadius = 560;
        lightningColor = Pal.spore;
    }};

    @Override
    public void death(Unit unit) {
        sounds.hugeBlast.at(unit);
        unit.apply(StatusEffects.sapped, 120f);
        b.create(unit, unit.x, unit.y, unit.rotation);
    }

    @Override
    public String localized(){
        return Core.bundle.format("ability.deathblast");
    }
}
