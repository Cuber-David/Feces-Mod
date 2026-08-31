package K.content.unit.Rody;

import K.content.extend.Bullets.EndCreepLaserBulletType;
import K.content.extend.Bullets.EndNukeBulletType;
import K.content.extend.weapons.LaserWeapon;
import K.content.sounds;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.ai.UnitCommand;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static mindustry.Vars.renderer;

public class RodyUnitType extends UnitType {
    public RodyUnitType(String name) {
        super(name);

        hitSize = 90;
        armor = 6767;
        constructor = RodyUnit::new;
        aiController = RodyAI::new;
        defaultCommand = UnitCommand.mineCommand;
        mineFloor = true;
        mineSpeed = 99;
        drawMineBeam = false;
        mineRange = 8;
        mineTier = 99;
        buildRange = 800;
        buildSpeed = 99;
        drawBuildBeam = false;
        flying = true;
        health = 67676767;
        drawCell = false;
        range = 8;
        speed = 10;
        rotateSpeed = 3;
        autoFindTarget = false;
        circleTargetRadius = 0;
        targetBuildingsMobile = false;
        circleTarget = false;
        targetAir = false;
        targetGround = false;
        hidden = true;
        weapons.add(new LaserWeapon(""){{
            x = 27.5f;
            y = -7f;
            mirror = true;
            targetAir = false;
            targetGround = false;

            continuous = true;
            rotate = true;
            rotateSpeed = 360;
            alternate = false;
            reload = 40f;

            //rotateSpeed = 0.5f;

            shootCone = 2f;
            range = 8;
            circleTargetRadius = 0;
            targetBuildingsMobile = false;
            circleTarget = false;
            shootSound = sounds.deslaser;
            laserShootSound = sounds.deslasershoot;

            bullet = new EndCreepLaserBulletType(){{
                layer = 180;
            }};
        }});
//        weapons.add( new Weapon(""){{
//            x = 0f;
//            y = -80.75f;
//            shootY = 0f;
//            mirror = false;
//            targetAir = false;
//            targetGround = false;
//            range = 8;
//
//            rotate = true;
//            alternate = true;
//            reload = 360;
//
//            rotateSpeed = 12f;
//
//            shootCone = 360f;
//            shootSound = sounds.watching;
//            layerOffset = 100;
//
//            bullet = new EndNukeBulletType();
//        }});
    }
    @Override
    public void applyColor(Unit unit){
        Draw.color();
        if(healFlash){
            Tmp.c1.set(Color.red.a(0.3f)).lerp(healColor, Mathf.clamp(unit.healTime - unit.hitTime));
        }
        Draw.mixcol(Tmp.c1, Math.max(unit.hitTime, !healFlash ? 0f : Mathf.clamp(unit.healTime)));

        if(unit.drownTime > 0 && unit.lastDrownFloor != null){
            Draw.mixcol(Tmp.c1.set(unit.lastDrownFloor.mapColor).mul(0.83f), unit.drownTime * 0.9f);
        }
        //this is horribly scuffed.
        if(renderer != null && renderer.overlays != null){
            renderer.overlays.checkApplySelection(unit);
        }
    }
}
