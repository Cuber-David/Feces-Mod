package K.content.extend.Bullets;

import K.content.Fx.KFx;
import K.content.KUnitTypes;
import K.content.statuseffect;
import K.graphics.MainRenderer;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;

import static arc.graphics.g2d.Draw.reset;
import static mindustry.Vars.renderer;

public class DomainInf extends BulletType {
    private float[][] crackData;
    private final float t = 600;
    private final float endtime = 2*t-50;
    public DomainInf() {
        super(0,0);
        lifetime = t;
        despawnEffect = KFx.fee;
        drawSize = 4000;
    }
    private float life = lifetime-endtime;
    private Unit u;
    @Override
    public void init(Bullet b) {
        super.init(b);

        float[][] data = generateCrackData(48, 700f);
        b.data = data;
        u = KUnitTypes.Domaininf.spawn(b.team,b.x,b.y);
    }
    private float[][] generateCrackData(int count, float radius) {
        float[][] data = new float[count][5];

        for (int i = 0; i < count; i++) {
            data[i][0] = Mathf.random(360f);
            data[i][1] = Mathf.random(radius * 0.4f, radius);
            data[i][2] = Mathf.random(-25f, 25f);
            data[i][3] = Mathf.random(0.3f, 0.6f);
            data[i][4] = Mathf.random(0.3f, 0.6f);
        }

        return data;
    }

    public void draw(Bullet b){
        if(b.time<b.lifetime-120) {
            KFx.bp.at(b.x, b.y);
        }
        renderer.lights.add(b.x,b.y,Math.min(1000,(b.time)*20),Color.white,1);
        TextureRegion bs = Core.atlas.find("kmod-stars");
        float STsize = Math.min(1200,b.time*60);
        if(b.time>endtime){
            float[][] data = (float[][]) b.data;
            if (data == null) return;

            Draw.color(Color.white, 0.8f);
            Lines.stroke(1.5f);

            float x = b.x;
            float y = b.y;

            for (int i = 0; i < data.length; i++) {
                float angle = data[i][0];
                float length = data[i][1];
                float branchOffset = data[i][2];
                float branchLengthRatio = data[i][3];
                float branchStartRatio = data[i][4];

                float x1 = x + Mathf.cosDeg(angle) * 2f;
                float y1 = y + Mathf.sinDeg(angle) * 2f;
                float x2 = x + Mathf.cosDeg(angle + branchOffset * 0.3f) * length;
                float y2 = y + Mathf.sinDeg(angle + branchOffset * 0.3f) * length;
                Lines.line(x1, y1, x2, y2);
                if(b.time>endtime+30) {
                    float branchStart = length * branchStartRatio;
                    float branchLength = length * branchLengthRatio;
                    float branchAngle = angle + branchOffset;

                    float bx1 = x + Mathf.cosDeg(angle) * branchStart;
                    float by1 = y + Mathf.sinDeg(angle) * branchStart;
                    float bx2 = x + Mathf.cosDeg(branchAngle) * (branchStart + branchLength);
                    float by2 = y + Mathf.sinDeg(branchAngle) * (branchStart + branchLength);
                    Lines.line(bx1, by1, bx2, by2);
                }
            }
            Draw.color(Color.white, 0.5f);
            Fill.circle(x, y, 2.5f);

            Draw.color();
            Lines.stroke(1f);
        }
        else {
            if (b.time > 100) {
                TextureRegion ba = Core.atlas.find("kmod-domaininf");
                float size = Math.min(1000, (b.time - 100) * 80);
                Draw.rect(bs, b.x, b.y, STsize, STsize, b.time / -80);
                Draw.rect(ba, b.x, b.y, size, size, b.time / 80);
            }
            if (b.time > 60) {
                float bhsize = Math.min(24, (b.time - 60) * 0.6f);
                MainRenderer.addBlackHole(b.x, b.y, bhsize, bhsize * 6);
                Lines.stroke(10);
                Lines.circle(b.x, b.y, 30);
                Lines.stroke(20, Color.white.a(0.3f));
                Lines.circle(b.x, b.y, 30);
                Draw.color();
            }
        }
        reset();
    }

    public void update(Bullet b){
        Unit u = getU();
        if(b.time<endtime){
            if (u.health <= 0){
                b.time = endtime;
            }
        } else {
            if(u!=null) {
                u.health = 0;
            }
        }
        for (Team team : Team.all) {
            if(team!=b.team) {
                Units.nearby(team, b.x, b.y, 650, unit -> {
                    if(b.time>60) {
                        if (b.time < endtime) {
                            if (unit.team != b.team) {
                                unit.damage(0.01f);
                                unit.apply(statuseffect.infinitude, 600);
                            }
                        }
                    }
                });
            }
        }
    }

    public Unit getU() {
        return u;
    }
}
