package K.content.unit;

import K.content.entities.DeathblastAbility;
import K.content.fx;
import K.content.sounds;
import arc.graphics.Color;
import mindustry.ai.types.GroundAI;
import mindustry.ai.types.SuicideAI;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.SpawnDeathAbility;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static mindustry.content.UnitTypes.crawler;

public class BigcrawlerUnitType extends UnitType {
    public BigcrawlerUnitType(String name) {
        super(name);

        constructor = crawler.constructor;
        outlineColor = Color.gray;
        outlineRadius = 10;
        aiController = GroundAI::new;

        hitSize = 20f;
        speed = 1.5f;
        armor = 10f;
        hitSize = 80f;
        health = 10000;
        mechSideSway = 0.25f;
        range = 210;
        stepSound = Sounds.walkerStepTiny;
        stepSoundVolume = 4f;
        stepShake = 50f;
        deathShake = 100f;
        deathExplosionEffect = fx.collapserExplode;
        deathSound = sounds.hugeBlast;
        deathSoundVolume = 10f;

        abilities.add(new SpawnDeathAbility(crawler, 30, 20f));
        abilities.add(new DeathblastAbility());

        weapons.add(new Weapon(){{
            shootOnDeath = false;
            targetUnderBlocks = false;
            reload = 120f;
            shootCone = 180f;
            ejectEffect = fx.BigExplosion;
            shootSound = sounds.boom;
            shootSoundVolume = 0.4f;
            x = shootY = 0f;
            mirror = false;
            bullet = new BulletType(){{
                collidesTiles = false;
                collides = false;

                rangeOverride = 210f;
                hitEffect = Fx.pulverize;
                speed = 0f;
                splashDamageRadius = 210f;
                instantDisappear = true;
                splashDamage = 3000f;
                buildingDamageMultiplier = 0.68f;
                killShooter = false;
                hittable = false;
                collidesAir = true;
            }};
        }});
    }
}
