package K.content.unit.others.jujutsu;

import K.KMod;
import K.Other_mod.FM.flame_extend.EmpathyDamage;
import K.content.Fx.KFx;
import K.content.effects.SpecialDeathEffects;
import K.content.extend.Bullets.jujutsu.SlashBulletType;
import K.entities.SimpleFragments;
import K.graphics.CutBatch;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Units;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class BaU extends UnitType {
    private float c = 0;
    public BaU(String name) {
        super(name);
        hidden = true;
        hittable=targetable=false;
        constructor = BlueUnit::new;
        health = 80000;
        speed = 0;
    }

    @Override
    public void draw(Unit unit) {
    }

    @Override
    public void init() {
        c = 0;
        super.init();
    }

    @Override
    public void update(Unit unit) {
        for (int i = 0; i < 16; i++) {
            float x = unit.x+10*(int)(2*Mathf.random(-5,5));
            float y = unit.y+10*(int)(2*Mathf.random(-5,5));
            if (unit.dst(x,y)<100) {
                KFx.slash.at(x, y, unit.rotation + 90 * (int) Mathf.random(2));
            }
        }
        c++;
        Damage.damage(unit.team,unit.x,unit.y,100,1000);
        Units.nearbyEnemies(unit.team,unit.x, unit.y,100,u -> {
            if (u.health>2000) {
                u.damage(2000);
            } else {
                if (unit != null && !unit.dead()) {
                    SimpleFragments.cutUnit(unit);
                }
            }
        });
        if(c>10){
            unit.remove();
            c = 0;
        }
    }
}
