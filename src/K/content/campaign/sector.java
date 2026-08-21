package K.content.campaign;

import mindustry.type.SectorPreset;

public class sector {
    public static SectorPreset zoneone,zonetwo,zonethree,zonefour,zonefive;

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
            noLighting = true;
            startWaveTimeMultiplier = 3f;
            allowLaunchSchematics = true;
            allowLaunchLoadout = true;
        }};
        zonefour = new SectorPreset("zonefour", planets.nonepro,165){{
            alwaysUnlocked = false;
            addStartingItems = false;
            captureWave = 30;
            difficulty = 1.2f;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
            allowLaunchSchematics = true;
            allowLaunchLoadout = true;
        }};
        zonefive = new SectorPreset("zonefive", planets.nonepro,37){{
            alwaysUnlocked = false;
            addStartingItems = false;
            captureWave = 30;
            difficulty = 1.2f;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
            allowLaunchSchematics = true;
            allowLaunchLoadout = true;
        }};
    }
}
