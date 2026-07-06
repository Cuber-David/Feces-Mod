package K.content;

import mindustry.type.SectorPreset;

public class sector {
    public static SectorPreset zoneone,zonetwo,zonethree;

    public static void load() {
        zoneone = new SectorPreset("zoneone", planets.nonepro,0){{
            alwaysUnlocked = true;
            addStartingItems = false;
            captureWave = 20;
            difficulty = 1;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
            allowLaunchSchematics = true;
            allowLaunchLoadout = true;
        }};
        zonetwo = new SectorPreset("zonetwo", planets.nonepro,95){{
            alwaysUnlocked = false;
            addStartingItems = false;
            captureWave = 30;
            difficulty = 1.2f;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
            allowLaunchSchematics = true;
            allowLaunchLoadout = true;
        }};
        zonethree = new SectorPreset("zonethree", planets.nonepro,166){{
            alwaysUnlocked = false;
            addStartingItems = false;
            captureWave = 30;
            difficulty = 1.2f;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
            allowLaunchSchematics = true;
            allowLaunchLoadout = true;
        }};
    }
}
