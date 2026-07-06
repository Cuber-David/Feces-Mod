package K.content;

import arc.func.Prov;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Planet;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;


public class planets {
    public static Planet nonepro,littlepro;
    public Block defaultCore;

    public planets() {
        defaultCore = blocks.fecescore;
    }

    public static void load(){
        nonepro = new Planet("none", Planets.sun, 1.0f, 3){{
            this.generator = new KPlanetGenerator();


            this.meshLoader = new Prov<GenericMesh>() {
                @Override
                public GenericMesh get() {
                    return new HexMesh(nonepro, 6);
                }
            };
            alwaysUnlocked = true;
            defaultCore = blocks.fecescore;
            this.allowLaunchSchematics = true;
            this.iconColor = Color.valueOf("ff9899");
            this.atmosphereColor = Color.valueOf("46251c");
            allowLaunchSchematics = true;
            this.cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this,3,4.3f,0.13f,10, Color.valueOf("724034"),2,0.3f,0.7f,0.6f),
                    new HexSkyMesh(this,1,0.55f,0.23f,8, Color.valueOf("46251c"),1,0.21f,0.45f,0.3f)

            );
            this.allowLaunchLoadout = true;
            this.landCloudColor = Color.valueOf("ff9899");
            this.ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.showSpawns = true;
                r.lighting = false;
                r.disableWorldProcessors = false;
            };
        }};

        littlepro = new Planet("little", planets.nonepro, 0.5f,1){{
            this.generator = new KPlanetGenerator();
            alwaysUnlocked = true;
            this.iconColor = Color.valueOf("855f39");
            this.ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.showSpawns = true;
                r.lighting = false;
            };
            this.meshLoader = new Prov<GenericMesh>() {
                @Override
                public GenericMesh get() {
                    return new HexMesh(littlepro, 10);
                }

            };
        }};
    }
}
