package K.content.unit.others.jujutsu;

import K.content.Fx.fx;
import K.content.extend.weapons.RedWeapon;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import static mindustry.Vars.renderer;

public class RedUnitType extends UnitType {
    public RedUnitType(String name) {
        super(name);
        flying = true;
        constructor = BlueUnit::new;
        aiController = DomainAI::new;
        hitSize = 0;
        speed = 10;
        range = 30;
        hitSize = 20;
        clipSize = 2000;
        hidden = true;

        weapons.add(new RedWeapon(){{
            x=y=shootY=0;
        }});
    }

    private boolean b;
    private float time;
    private final float lifetime = 60;
    private Color incolor = Color.valueOf("f80c19").a(0.8f);
    private Color outcolor = Color.valueOf("ffa2bd").a(0.8f);
    private Color lightcolor = Color.valueOf("fa1e31").a(0.2f);

    @Override
    public void draw(Unit u) {
        float c = 80;
        renderer.lights.add(u.x,u.y,u.hitSize*c/10,incolor,1);
        for(int i=0;i<c;i++){
            Draw.color(lightcolor,(float)i/(60*c));
            Fill.circle(u.x,u.y,u.hitSize*((16*c)/i));
        }
        drawtrail(u);
        Draw.color(outcolor);
        Fill.circle(u.x,u.y,u.hitSize*1.0f*breath(time));
        Draw.color(incolor);
        Fill.circle(u.x,u.y,u.hitSize*0.8f*breath(time));
        Draw.color();
        fx.orbitred.at(u.x,u.y,incolor);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update(Unit u) {
        if(u.hitSize<=1.21f)u.remove();
        if (time>lifetime) {
            time=0;
            b = !b;
        }
        kickoff(u);
        hit(u);
        time++;
    }

    private float fin(float t) {
        return t / 60.0F;
    }

    private float fout(float t) {
        return 1.0F - this.fin(t);
    }

    private float breath(float t) {
        return this.b ? this.fin(t) * 0.2F + 0.9F : this.fout(t) * 0.2F + 0.9F;
    }

    private void hit(Unit u) {
        Damage.damage(u.team, u.x, u.y, u.hitSize * 2.0F, 1.0F);
    }

    private void drawtrail(Unit u) {
        (new Effect(30.0F, (e) -> {
            Draw.color(this.incolor);
            renderer.lights.add(u.x, u.y, u.hitSize * 2.0F * e.fout(), this.incolor, 2.0F);
            Fill.circle(e.x, e.y, u.hitSize * 0.8f * e.fout());
        })).at(u.x, u.y);
    }

    private void kickoff(Unit u){
        float l = 30;
        Units.nearbyEnemies(u.team,u.x,u.y,range,unit -> {
            float r = 180+unit.angleTo(u.x,u.y);
            unit.vel().add(Mathf.cos(r)*l,Mathf.sin(r)*l);
        });
        Groups.bullet.each(b ->
        {
            if (b.hitSize>=1600) {
                return;
            }
            float bd = b.dst(u.x, u.y);
            float br = 180 + b.angleTo(u.x, u.y);
            if (bd < range) b.vel().add(Mathf.cos(br) * l, Mathf.sin(br) * l);
        });
    }
}
