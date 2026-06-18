package K.content;

import mindustry.type.SectorPreset;

public class sector {
    public static SectorPreset zoneone;

    public static void load() {
        zoneone = new SectorPreset("zoneone", planets.nonepro,0){{
            alwaysUnlocked = true;
            addStartingItems = true;
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
        }};
    }
}
