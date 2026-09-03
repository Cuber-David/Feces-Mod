package K.content.unit.others.jujutsu;

import K.content.Fx.KFx;
import K.content.extend.Bullets.jujutsu.PurpleBulletType;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static mindustry.Vars.renderer;

public class PurpleUnitType extends UnitType {
    public PurpleUnitType(String name) {
        super(name);
        constructor = BlueUnit::new;
        hitSize = 40;
        speed = 15;
        flying = true;
        hittable = false;
        clipSize = 1000;
        targetable = false;
        hidden = true;

        weapons.add(new Weapon(){{
            x=0;
            y=0;
            shootY=0;
            mirror = false;
            reload = 300;
            bullet = new PurpleBulletType();
            shake = 100;
            shootCone = 360;
            shootSound = Sounds.none;
            shootOnDeath=true;
        }});
    }
    private boolean b;
    private float time;
    private final float lifetime = 180;
    private float r;

    @Override
    public void update(Unit u) {
        hit(u);
        if(time>lifetime) {
            time = -1;
            b = !b;
        }
        r++;
        time++;
    }

    @Override
    public void draw(Unit u) {
        float c = 160;
        drawtrail(u);
        for(int i=0;i<c;i++){
            Draw.color(Color.valueOf("d076f1").a(0.2f),(float)i/(80*c));
            Fill.circle(u.x,u.y,u.hitSize*((16*c)/i));
        }
        Draw.z(160);
        Draw.color(Color.valueOf("f1ccf7").a(0.08f));
        for (int i = 0; i < 4; i++) {
            Drawf.tri(u.x,u.y,hitSize*2f,hitSize*12f,r/16+i*90);
        }
        Draw.color(Color.valueOf("9c5ad5").a(0.4f));
        Fill.circle(u.x,u.y,hitSize*1.4f*breath(time));
        Draw.color(Color.valueOf("d076f1").a(0.8f));
        Fill.circle(u.x,u.y,hitSize*breath(time));
        Draw.color(Color.valueOf("f1ccf7").a(0.9f));
        Fill.circle(u.x,u.y,hitSize*0.6f);
        Draw.reset();
        KFx.orbitpurple.at(u.x,u.y);
        KFx.orbitpurpleout.at(u.x,u.y);
        Lightning.create(u.team,Color.valueOf("f1ccf7").a(0.9f),1f,u.x,u.y, Mathf.random(360), Mathf.random(32));
    }

    private float fin(float t) {
        return t / lifetime;
    }

    private float fout(float t) {
        return 1.0F - this.fin(t);
    }

    private float breath(float t){
        if (b) {
            return fin(t) * 0.05f + 1;
        }else {
            return fout(t) * 0.05f + 1f;
        }
    }

    private void drawtrail(Unit u) {
        (new Effect(30.0F, (e) -> {
            Draw.color(Color.valueOf("d076f1").a(0.8f));
            renderer.lights.add(u.x, u.y, u.hitSize * 2.0F * e.fout(), Color.valueOf("d076f1").a(0.8f), 2.0F);
            Fill.circle(e.x, e.y, u.hitSize * e.fout());
        })).at(u.x, u.y);
    }

    private void hit(Unit u) {
        Damage.damage(u.team, u.x, u.y, u.hitSize * 2.0F, 100.0F);
    }
}
