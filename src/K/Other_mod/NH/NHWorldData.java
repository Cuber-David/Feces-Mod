package K.Other_mod.NH;

import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;

import static K.Other_mod.NH.NHVars.worldData;

public class NHWorldData {
    public static short CURRENT_VER = 2;


    public NHWorldData() {
        SaveVersion.addCustomChunk("nh-world-data", (SaveFileReader.CustomChunk) worldData);
    }
}
