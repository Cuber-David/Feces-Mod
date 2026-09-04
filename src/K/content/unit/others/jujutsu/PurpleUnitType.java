package K.content.unit.others.jujutsu;

import K.content.extend.Bullets.jujutsu.PurpleBulletType;
import K.content.extend.util.DrawPurple;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.game.Team;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
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
        drag = 0;

        weapons.add(new Weapon(){{
            x=0;
            y=0;
            shootY=0;
            mirror = false;
            reload = 300;
            bullet = new PurpleBulletType(hitSize);
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
        DrawPurple.drawp(u.x,u.y,u.hitSize*4);
        drawtrail(u);
        Lightning.create(u.team,Color.valueOf("f1ccf7").a(0.9f),1f,u.x,u.y, (float) Mathf.random(360), (int) Mathf.random(hitSize));
    }

    private void drawtrail(Unit u) {
        (new Effect(30.0F, (e) -> {
            Draw.color(Color.valueOf("b853e1").a(0.8f));
            renderer.lights.add(u.x, u.y, u.hitSize * 2.0F * e.fout(), Color.valueOf("b853e1").a(0.8f), 2.0F);
            Fill.circle(e.x, e.y, u.hitSize * e.fout());
        })).at(u.x, u.y);
    }

    private void hit(Unit u) {
        Damage.damage(u.team, u.x, u.y, u.hitSize * 2.0F, 100.0F);
    }
}
