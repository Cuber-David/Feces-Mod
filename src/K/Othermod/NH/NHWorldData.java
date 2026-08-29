package K.Othermod.NH;

import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;

import static K.Othermod.NH.NHVars.worldData;

public class NHWorldData {
    public static short CURRENT_VER = 2;


    public NHWorldData() {
        SaveVersion.addCustomChunk("nh-world-data", (SaveFileReader.CustomChunk) worldData);
    }
}
