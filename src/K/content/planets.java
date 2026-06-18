package K.content;

import arc.func.Prov;
import arc.graphics.Color;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Planet;


public class planets {
    public static Planet nonepro,littlepro;

    public static void load(){
        nonepro = new Planet("none", Planets.sun, 1.0f, 3){{
            this.generator = new KPlanetGenerator();


            this.meshLoader = new Prov<GenericMesh>() {
                @Override
                public GenericMesh get() {
                    return new HexMesh(nonepro, 6);
                }
            };

            this.iconColor = Color.valueOf("ff9899");
            this.atmosphereColor = Color.valueOf("46251c");

            this.cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this,3,4.3f,0.13f,10, Color.valueOf("724034"),2,0.3f,0.7f,0.6f),
                    new HexSkyMesh(this,1,0.55f,0.23f,8, Color.valueOf("46251c"),1,0.21f,0.45f,0.3f)

            );

            this.ruleSetter = r -> {
                r.waveTeam = Team.sharded;
                r.placeRangeCheck = false;
                r.showSpawns = true;
                r.lighting = false;
            };

            alwaysUnlocked = true;
        }};

        littlepro = new Planet("little", planets.nonepro, 0.2f,1){{
            this.generator = new SerpuloPlanetGenerator();
            alwaysUnlocked = true;
            this.iconColor = Color.valueOf("855f39");
            this.meshLoader = new Prov<GenericMesh>() {
                @Override
                public GenericMesh get() {
                    return new HexMesh(littlepro, 10);
                }
            };
        }};
    }
}
