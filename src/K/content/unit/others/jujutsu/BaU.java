package K.content.unit.others.jujutsu;

import K.content.extend.Bullets.jujutsu.SlashBulletType;
import arc.math.Mathf;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class BaU extends UnitType {
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
    public void update(Unit unit) {
        for (int i = 0; i < 16; i++) {
            SlashBulletType.createBullet(new SlashBulletType(),unit.team,unit.x+Mathf.random(64)-32,unit.y+Mathf.random(64)-32,Mathf.random(360),5000,0.1f,0.1f);
        }
        unit.remove();
    }
}
