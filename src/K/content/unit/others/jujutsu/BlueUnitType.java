package K.content.unit.others.jujutsu;

import K.content.KUnitTypes;
import K.content.extend.Bullets.NoBullet;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import java.util.HashMap;
import java.util.Objects;

import mindustry.Vars;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class BlueUnitType extends UnitType {
    private float time;
    private final float lifetime = 60.0F;
    private final float baserad = hitSize*40;
    private boolean b;
    private Color outerBlue = Color.valueOf("005eff").a(0.5F);
    private Color innerBlue = Color.valueOf("00ffff").a(0.7F);
    private int particleCount = 40;
    private final float baseradius = 28.0F;
    private float radius;
    private float baseLife = 60.0F;
    private HashMap<Integer, Particle[]> unitParticleData = new HashMap();

    public BlueUnitType(String name) {
        super(name);
        hidden = true;
        this.hittable = false;
        this.hitSize = 20.0F;
        this.speed = 8.0F;
        this.constructor = BlueUnit::new;
        this.aiController = DomainAI::new;
        this.range = this.hitSize * 20.0F;
        this.flying = true;
        this.weapons.add(new Weapon() {{
            shootSound = Sounds.none;
            shootSoundVolume = 0;
            shootCone = 360;
            bullet = new NoBullet();
        }});
    }

    private float tr;

    @Override
    public void init() {
        super.init();
        tr = 0;
        radius = 0;
    }

    private Particle[] createParticles() {
        Particle[] particles = new Particle[this.particleCount];

        for(int i = 0; i < this.particleCount; ++i) {
            Particle p = new Particle();
            this.resetParticle(p, i);
            particles[i] = p;
        }

        return particles;
    }

    private void resetParticle(Particle p, int index) {
        p.angle = 90.0F + Mathf.random(-80.0F, 80.0F);
        p.startDist = this.radius * Mathf.random(0.85F, 1.0F);
        p.endDist = this.radius * Mathf.random(1.5F, 2.5F);
        p.timer = Mathf.random(0.0F, this.baseLife);
        p.maxTimer = this.baseLife * Mathf.random(0.8F, 1.2F);
        p.isDark = Mathf.randomBoolean();
    }

    public void update(Unit unit) {
        tr=tr+0.1f;
        radius = Math.min(baseradius,tr);
        if (this.time > 60.0F) {
            this.time = 0.0F;
            this.b = !this.b;
        }
        this.hit(unit);
        findred(unit);
        Particle[] particles = (Particle[])this.unitParticleData.get(unit.id);
        if (particles == null) {
            particles = this.createParticles();
            this.unitParticleData.put(unit.id, particles);
        }

        for(Particle p : particles) {
            ++p.timer;
            if (p.timer >= p.maxTimer) {
                p.angle = 90.0F + Mathf.random(-80.0F, 80.0F);
                p.startDist = this.radius * 0.6F * Mathf.random(0.85F, 1.0F);
                p.endDist = this.radius * Mathf.random(1.5F, 2.5F);
                p.timer = 0.0F;
                p.maxTimer = this.baseLife * Mathf.random(0.8F, 1.2F);
                p.isDark = Mathf.randomBoolean();
            }
        }

        if (unit.isShooting()) {
            range = baserad*2;
        } else {
                range = baserad;
        }
        this.attarct(unit);
        ++this.time;
    }

    public void draw(Unit unit) {
        this.drawtrail(unit);
        Particle[] particles = (Particle[])this.unitParticleData.get(unit.id);
        if (particles == null) {
            particles = this.createParticles();
            this.unitParticleData.put(unit.id, particles);
        }

        float x = unit.x;
        float y = unit.y;
        Draw.color(this.outerBlue, 0.15F);
        Fill.circle(x, y, this.radius * 1.08F * this.breath(this.time));
        Draw.color(this.outerBlue);
        Fill.circle(x, y, this.radius * this.breath(this.time));

        for(Particle p : particles) {
            float progress = p.timer / p.maxTimer;
            float currentAngle;
            if (progress < 0.5F) {
                currentAngle = p.angle;
            } else {
                float t = (progress - 0.5F) * 2.0F;
                currentAngle = Mathf.lerp(p.angle, 90.0F, t);
            }

            float currentDist = Mathf.lerp(p.startDist, p.endDist, progress);
            float px = x + Mathf.cosDeg(currentAngle) * currentDist;
            float py = y + Mathf.sinDeg(currentAngle) * currentDist;
            float size;
            if (progress < 0.5F) {
                size = 4.5F;
            } else {
                float t = (progress - 0.5F) * 2.0F;
                size = 4.5F * (1.0F - t);
            }

            size = Math.max(size, 0.3F);
            float alpha;
            if (progress < 0.5F) {
                alpha = 0.7F;
            } else {
                float t = (progress - 0.5F) * 2.0F;
                alpha = 0.7F * (1.0F - t);
            }

            alpha = Mathf.clamp(alpha, 0.0F, 0.7F);
            Color color = p.isDark ? this.outerBlue : this.innerBlue;
            Draw.color(color, alpha);
            Fill.circle(px, py, size);
        }

        Draw.color(this.innerBlue);
        Fill.circle(x, y, this.radius * 0.65F * this.breath(this.time));
        Draw.color(Color.white, 0.15F);
        Fill.circle(x, y, this.radius * 0.1F * this.breath(this.time));
        Draw.color();
    }

    private void attarct(Unit u) {
        float maxSpeed = 0.2F;
        float x = u.x;
        float y = u.y;
        Units.nearbyEnemies(u.team, x, y, range, (unit) -> {
            float udx = x - unit.x;
            float udy = y - unit.y;
            float dst = Mathf.len(udx, udy);
            if (dst < this.range && dst > 2.0F) {
                float uspeedMultiplier = 1.0F - dst / range;
                float uattractSpeed = maxSpeed * uspeedMultiplier;
                uattractSpeed = Math.max(uattractSpeed, 0.2F);
                float unormDx = udx / dst;
                float unormDy = udy / dst;
                unit.vel().add(unormDx * uattractSpeed, unormDy * uattractSpeed);
                float maxTotalSpeed = 10.0F;
                if (unit.vel().len() > maxTotalSpeed) {
                    unit.vel().setLength(maxTotalSpeed);
                }
            }

        });
        Groups.bullet.each((bullet) -> {
            float dx = x - bullet.x;
            float dy = y - bullet.y;
            float dist = Mathf.len(dx, dy);
            if (dist < this.range && dist > 2.0F) {
                float speedMultiplier = 1.0F - dist / this.range;
                float attractSpeed = maxSpeed * speedMultiplier;
                attractSpeed = Math.max(attractSpeed, 0.2F);
                float normDx = dx / dist;
                float normDy = dy / dist;
                bullet.vel().add(normDx * attractSpeed, normDy * attractSpeed);
                float maxTotalSpeed = 10.0F;
                if (bullet.vel().len() > maxTotalSpeed) {
                    bullet.vel().setLength(maxTotalSpeed);
                }
            }

        });
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
            Draw.color(this.innerBlue);
            Vars.renderer.lights.add(u.x, u.y, u.hitSize * 2.0F * e.fout(), this.innerBlue, 2.0F);
            Fill.circle(e.x, e.y, u.hitSize * e.fout());
        })).at(u.x, u.y);
    }

    private static class Particle {
        float angle;
        float timer;
        float maxTimer;
        float startDist;
        float endDist;
        boolean isDark;
    }

    private void findred(Unit u){
        Units.nearby(u.team,u.x,u.y,20,unit -> {
            if (Objects.equals(unit.type, KUnitTypes.Red)){
                Unit unit1 = KUnitTypes.Purple.spawn(u.team,u.x,u.y);
                unit.remove();u.remove();
            }
        });
    }
}
