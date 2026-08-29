package K.content.extend.Bullets;

import K.content.statuseffect;
import K.content.Fx.KPal;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.world.Tile;

public class DomainCook extends BulletType {
    public DomainCook(){
        super(0,0);
        lifetime = 600;
        collides = false;
        drawSize = 4000;
    }
    @Override
    public void draw(Bullet b) {
        Draw.z(21);
        TextureRegion sk = Core.atlas.find("kmod-domaincook");
        Draw.color(KPal.darkRed.a(0.7f));
        Fill.circle(b.x,b.y,Math.min(1200,b.time*100));
        Draw.z(55);
        Draw.color();
        Draw.rect(sk,b.x,b.y);
    }
    @Override
    public void init(Bullet b) {
        super.init(b);
        b.data = new Object[]{0f};
    }
    @Override
    public void update(Bullet b){
        for (int i = 0; i < 100; i++){
            float r = Mathf.random(360);
            float l = Mathf.random(90)/100f+0.1f;
            SlashBulletType.createBullet(new SlashBulletType(), b.team, b.x, b.y, r, 1, 0.4f, l);
        }
        super.update(b);
        Object[] data = (Object[]) b.data;
        float counter = (float) data[0];
        counter += 1f;
        if (counter >= 1) {
            counter = 0f;
            scanAndSpawn(b);
        }
        data[0] = counter;
    }

    private void scanAndSpawn(Bullet b) {
        float x = b.x;
        float y = b.y;
        float radius = 1200;
        Team team = b.team;
        Units.nearbyEnemies(team,x,y,radius,unit -> {
            if (unit.dst(x, y) <= radius) {
                if(!unit.hasEffect(statuseffect.domainopen))
                {spawnChildBullet(b, unit.x, unit.y);}
            }
        });
        int tileRadius = (int)(radius / Vars.tilesize) + 1;
        int cx = (int)(x / Vars.tilesize);
        int cy = (int)(y / Vars.tilesize);
        for (int dx = -tileRadius; dx <= tileRadius; dx++) {
            for (int dy = -tileRadius; dy <= tileRadius; dy++) {
                Tile tile = Vars.world.tile(cx + dx, cy + dy);
                if (tile == null) continue;
                Building building = tile.build;
                if (building != null && building.team != b.team) {
                    float dist = building.dst(x, y);
                    if (dist <= radius) {
                        spawnChildBullet(b,building.x,building.y);
                    }
                }
            }
        }
    }
    private void spawnChildBullet(Bullet parent, float tx, float ty) {
        BulletType childBullet = new SlashBulletType();
        float angle = parent.angleTo(tx, ty);  // 从主子弹指向目标的角度
        SlashBulletType.createBullet(childBullet,parent.team,tx,ty,angle,1000,0,1);
    }
}
