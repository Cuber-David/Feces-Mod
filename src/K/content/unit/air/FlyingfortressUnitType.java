package K.content.unit.air;

import arc.graphics.Color;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class FlyingfortressUnitType extends UnitType {
    public FlyingfortressUnitType(String name) {
        super(name);
        constructor = UnitTypes.mega.constructor;
        health = 3000;
        armor = 4;
        hitSize = 50;
        speed = 1.2f;
        flying = true;
        payloadCapacity = 1600;
        buildRange = 120f;
        buildSpeed = 4f;
        buildBeamOffset = -30;
        itemCapacity = 300;
        mineRange = 80;
        mineSpeed = 9;
        mineTier = 8;
        mineFloor = true;
        researchCostMultiplier = 0.1f;

        setEnginesMirror(
                new UnitEngine(16f, -35f, 8f, -90f)
        );

        abilities.add(new RepairFieldAbility(200,300,144));

        weapons.add(
                new Weapon("kmod-healcannon"){{
                    shootSound = Sounds.shootLaser;
                    shootY = 14;
                    reload = 24f;
                    x = 18f;
                    y = -8f;
                    rotate = true;
                    bullet = new LaserBoltBulletType(5.2f, 30){{
                        lifetime = 55f;
                        healPercent = 9.5f;
                        collidesTeam = true;
                        backColor = Pal.heal;
                        frontColor = Color.white;
                        layer = 160;
                    }};
                }},
                new Weapon("kmod-healcannon"){{
                    shootSound = Sounds.shootLaser;
                    shootY = 14;
                    reload = 15f;
                    x = 24f;
                    y = -12f;
                    rotate = true;
                    bullet = new LaserBoltBulletType(5.2f, 30){{
                        lifetime = 55f;
                        healPercent = 9.5f;
                        collidesTeam = true;
                        backColor = Pal.heal;
                        frontColor = Color.white;
                        layer = 160;
                    }};
                }}
        );
    }
}
