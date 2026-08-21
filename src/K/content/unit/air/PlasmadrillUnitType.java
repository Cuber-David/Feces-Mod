package K.content.unit.air;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.ai.UnitCommand;
import mindustry.content.UnitTypes;
import mindustry.core.Renderer;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;

import static mindustry.Vars.tilesize;

public class PlasmadrillUnitType extends UnitType {
    public PlasmadrillUnitType(String name) {
        super(name);

        constructor = UnitTypes.mono.constructor;
        defaultCommand = UnitCommand.mineCommand;
        hitSize = 8f;

        flying = true;
        drag = 0.06f;
        accel = 0.12f;
        speed = 2.5f;
        health = 200;
        engineSize = 2f;
        engineOffset = 8f;
        range = 50f;
        isEnemy = false;
        controlSelectGlobal = false;
        wreckSoundVolume = deathSoundVolume = 0.7f;
        researchCostMultiplier = 10f;

        mineTier = 1;
        mineSpeed = 3f;
        mineBeamOffset = 0f;
    }
    @Override
    public void drawMining(Unit unit){
        if(drawMineBeam){
            float focusLen = mineBeamOffset + Mathf.absin(Time.time, 1.1f, 0.5f);
            float px = unit.x + Angles.trnsx(unit.rotation, focusLen);
            float py = unit.y + Angles.trnsy(unit.rotation, focusLen);

            drawMiningBeam(unit, px, py);
        }
    }

    @Override
    public void drawMiningBeam(Unit unit, float px, float py){
        if(!unit.mining()) return;
        float swingScl = 12f, swingMag = tilesize / 8f;
        float flashScl = 0.3f;

        float ex = unit.mineTile.worldx() + Mathf.sin(Time.time + 48, swingScl, swingMag);
        float ey = unit.mineTile.worldy() + Mathf.sin(Time.time + 48, swingScl + 2f, swingMag);

        float r = (float) Math.atan((unit.mineTile.worldy()-unit.y)/(unit.mineTile.worldx()-unit.x));

        float mx;
        float my;

        if (unit.mineTile.worldx()-unit.x>=0) {
            final int M = 12;
             mx = unit.x + M * Mathf.cos(r);
             my = unit.y + M * Mathf.sin(r);
        }
        else {
            final int M = 8;
             mx = unit.x + (-1 * M * Mathf.cos(r));
             my = unit.y + (-1 * M * Mathf.sin(r));
        }

        final int L = 4;

        float lx = mx + L*Mathf.cos(r-90);
        float ly = my + L*Mathf.sin(r-90);
        float rx = mx + L*Mathf.cos(r+90);
        float ry = my + L*Mathf.sin(r+90);

        Draw.z(Layer.flyingUnit + 0.1f);

        Draw.color(Color.lightGray, Color.white, 1f - flashScl + Mathf.absin(Time.time, 0.5f, flashScl));

        Draw.alpha(Renderer.unitLaserOpacity);
        Drawf.laser(mineLaserRegion, mineLaserEndRegion, lx, ly, ex, ey, 0.75f);
        Drawf.laser(mineLaserRegion, mineLaserEndRegion, rx, ry, ex, ey, 0.75f);

        if(unit.isLocal()){
            Lines.stroke(1f, Pal.accent);
            Lines.poly(unit.mineTile.worldx(), unit.mineTile.worldy(), 4, tilesize / 2f * Mathf.sqrt2, Time.time);
        }

        Draw.color();
    }
}
