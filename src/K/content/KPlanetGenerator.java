package K.content;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.struct.ObjectMap;
import arc.util.Nullable;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.gen.Iconc;
import mindustry.maps.generators.BaseGenerator;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;

import static arc.graphics.g2d.Draw.scl;
import static mindustry.Vars.state;

public class KPlanetGenerator extends SerpuloPlanetGenerator {

    public static void load() {

    }

    float rawHeight(Vec3 position) {
        return (Mathf.pow(Simplex.noise3d(
                        seed,
                        7,
                        0.5f,
                        1f / 3f,
                        position.x,
                        position.y,
                        position.z),
                2.3f));
    }


    public class SerpuloPlanetGenerator extends PlanetGenerator {
        //alternate, less direct generation
        public static boolean indirectPaths = false;
        //random water patches
        public static boolean genLakes = false;

        BaseGenerator basegen = new BaseGenerator();
        float heightYOffset = 42.7f;
        float scl = 5f;
        float waterOffset = 0.04f;
        float heightScl = 1.01f;

        Block[][] arr =
                {
                        {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.darksandTaintedWater, Blocks.stone, Blocks.stone},
                        {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.darksandTaintedWater, Blocks.stone, Blocks.stone, Blocks.stone},
                        {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.sand, Blocks.salt, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.darksandTaintedWater, Blocks.stone, Blocks.stone, Blocks.stone},
                        {Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.salt, Blocks.salt, Blocks.salt, Blocks.sand, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.snow, Blocks.iceSnow, Blocks.ice},
                        {Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.salt, Blocks.sand, Blocks.sand, Blocks.basalt, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice},
                        {Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.moss, Blocks.iceSnow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.snow, Blocks.ice},
                        {Blocks.deepwater, Blocks.sandWater, Blocks.sand, Blocks.sand, Blocks.moss, Blocks.moss, Blocks.snow, Blocks.basalt, Blocks.basalt, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice},
                        {Blocks.deepTaintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.darksand, Blocks.basalt, Blocks.moss, Blocks.basalt, Blocks.hotrock, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice, Blocks.ice},
                        {Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.moss, Blocks.sporeMoss, Blocks.snow, Blocks.basalt, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice, Blocks.ice},
                        {Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.sporeMoss, Blocks.ice, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice},
                        {Blocks.deepTaintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.sporeMoss, Blocks.sporeMoss, Blocks.ice, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice},
                        {Blocks.taintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.sporeMoss, Blocks.moss, Blocks.sporeMoss, Blocks.iceSnow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice},
                        {Blocks.darksandWater, Blocks.darksand, Blocks.snow, Blocks.ice, Blocks.iceSnow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice}
                };

        ObjectMap<Block, Block> dec = ObjectMap.of(
                Blocks.sporeMoss, Blocks.sporeCluster,
                Blocks.moss, Blocks.sporeCluster,
                Blocks.taintedWater, Blocks.water,
                Blocks.darksandTaintedWater, Blocks.darksandWater
        );

        ObjectMap<Block, Block> tars = ObjectMap.of(
                Blocks.sporeMoss, Blocks.shale,
                Blocks.moss, Blocks.shale
        );

        float water = 2f / arr[0].length;
        //megabase position
        Vec3 basePos = new Vec3(0.9341721, 0.0, 0.3568221);


        @Override
        public void onSectorCaptured(Sector sector) {
            sector.planet.reloadMeshAsync();
        }

        @Override
        public void onSectorLost(Sector sector) {
            sector.planet.reloadMeshAsync();
        }

        @Override
        public void beforeSaveWrite(Sector sector) {
            sector.planet.reloadMeshAsync();
        }

        @Override
        public boolean isEmissive() {
            return true;
        }

        public boolean allowNumberedLaunch(Sector s) {
            return s.hasBase() && !s.isAttacked() && (s.info.bestCoreType.size >= 4 || s.isBeingPlayed() && state.rules.defaultTeam.cores().contains(b -> b.block.size >= 4));
        }

        @Override
        public boolean allowLanding(Sector sector) {
            return sector.planet.allowLaunchToNumbered && (sector.hasBase() || sector.near().contains(this::allowNumberedLaunch));
        }

        @Override
        public @Nullable Sector findLaunchCandidate(Sector destination, @Nullable Sector selected) {
            if (destination.preset == null || !destination.preset.requireUnlock) {
                if (selected != null && selected.isNear(destination) && allowNumberedLaunch(selected)) {
                    return selected;
                } else {
                    return destination.near().find(this::allowNumberedLaunch);
                }
            } else {
                return super.findLaunchCandidate(destination, selected);
            }
        }

        @Override
        public void getLockedText(Sector hovered, StringBuilder out) {
            if ((hovered.preset == null || !hovered.preset.requireUnlock) && hovered.near().contains(Sector::hasBase)) {
                if (hovered.isShielded()) {
                    out.append("[red]").append(Iconc.defense).append("[]").append(Core.bundle.get("sector.shielded"));
                } else {
                    out.append("[red]").append(Iconc.cancel).append("[]").append(Blocks.coreFoundation.emoji()).append(Core.bundle.get("sector.foundationrequired"));
                }
            } else {
                super.getLockedText(hovered, out);
            }
        }

        @Override
        public float getHeight(Vec3 position) {
            float height = rawHeight(position);
            return Math.max(height, water);
        }

        @Override
        public void getColor(Vec3 position, Color out) {
            Block block = getBlock(position, true);
            //replace salt with sand color
            if (block == Blocks.salt) block = Blocks.sand;
            out.set(block.mapColor).a(1f - block.albedo);
        }

        Block getBlock(Vec3 position, boolean visualOnly){
            float height = rawHeight(position);
            float px = position.x * scl, py = position.y * scl, pz = position.z * scl;

            float rad = scl;
            float temp = Mathf.clamp(Math.abs(py * 2f) / (rad));
            float tnoise = Simplex.noise3d(seed, 7, 0.56, 1f/3f, px, py + 999f - 0.1f, pz);
            temp = Mathf.lerp(temp, tnoise, 0.5f);
            height *= 1.2f;
            height = Mathf.clamp(height);

            float tar = Simplex.noise3d(seed, 4, 0.55f, 1f/2f, px, py + 999f, pz) * 0.3f + position.dst(0, 0, 1f) * 0.2f;

            Block res = arr[Mathf.clamp((int)(temp * arr.length), 0, arr[0].length - 1)][Mathf.clamp((int)(height * arr[0].length), 0, arr[0].length - 1)];
            if(tar > 0.5f){
                return tars.get(res, res);
            }else{
                if(visualOnly && position.within(basePos, 0.65f)){

                    float dst = 999f;

                    Object[] sectors = Planets.serpulo.sectors.items;
                    int size = Planets.serpulo.sectors.size;

                    for(int i = 0; i < size; i ++){
                        var sector = (Sector)sectors[i];

                        if(sector.hasEnemyBase()){
                            dst = Math.min(dst, position.dst(sector.tile.v));
                        }
                    }

                    float freq = 0.05f, freq2 = 0.07f;

                    if(dst*0.85f + Simplex.noise3d(seed, 3, 0.4, 5.5f, position.x, position.y + 200f, position.z)*0.015f + ((basePos.dst(position) + 0.00f) % freq < freq/2f ? 1f : 0f) * 0.07f < 0.15f){
                        return ((basePos.dst(position) + 0.01f) % freq2 < freq2*0.65f) ? Blocks.metalFloor : Blocks.darkPanel6;
                    }
                }
                return res;
            }
        }
    }
}
