package K.content.extend.weapons;

import K.KMod;
import K.Other_mod.FM.flame_extend.EmpathyDamage;
import K.content.effects.SpecialDeathEffects;
import K.content.extend.Bullets.NoBullet;
import K.content.sounds;
import K.graphics.CutBatch;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class KWeapon extends Weapon {
    public KWeapon(){
        reload = 10;
        recoil = 0;
        shootCone = 9000;
        rotate = false;
        bullet = new NoBullet();
        shootSound = sounds.krun;
    }

    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation) {
        super.shoot(unit, mount, shootX, shootY, rotation);
        float r = unit.angleTo(unit.aimX,unit.aimY);
        unit.rotation = r;
        TextureRegion s = Core.atlas.find("kmod-GodK-shadow");
        for (int i = 0; i < 1000; i++) {
            float l = 50;
            unit.x = unit.x() + Mathf.cos(r*Mathf.degRad)*l;
            unit.y = unit.y() + Mathf.sin(r*Mathf.degRad)*l;
            hit(unit.team,unit.rotation,unit.x,unit.y,100,100000,0.5f);
            new Effect(60,e -> {
                Draw.color(Color.white.a(e.fout()));
                Draw.rect(s,e.x,e.y,e.rotation-90);
                Draw.color();
            }).at(unit.x,unit.y,unit.rotation);
            if (unit.dst(unit.aimX,unit.aimY) < 100) {
                i = 1000;
                Core.camera.position.set(unit.aimX,unit.aimY);
            }
        }
    }

    @Override
    public void update(Unit unit, WeaponMount mount) {
        super.update(unit, mount);
    }

    public static void hit(Team t, float ro, float x, float y, float r, float d, float d1){
        Damage.damage(t,x,y,r,d);
        Units.nearbyEnemies(t,x,y,r,unit -> {
            unit.damage(d);
            EmpathyDamage.damageUnit(unit,unit.maxHealth/(d1*100),true,() -> {
                SpecialDeathEffects eff = SpecialDeathEffects.get(unit.type);
                if(!eff.solid){
                    eff.cutAlt(unit);
                }
                CutBatch batch = KMod.cutBatch;
                batch.explosionEffect = eff.explosionEffect != Fx.none ? eff.explosionEffect : null;
                batch.sound = eff.deathSound;
                batch.cutHandler = c -> {
                    c.vx += unit.vel.x+Mathf.cos(ro);
                    c.vy += unit.vel.y+Mathf.sin(ro);
                    c.cutWorld(unit.aimX, unit.aimY, unit.x, unit.y, null);
                };
                batch.switchBatch(unit::draw);
            });
        });
    }
}