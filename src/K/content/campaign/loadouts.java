package K.content.campaign;

import mindustry.game.*;

public class loadouts {
    public static Schematic
    basiccore,basioncore;

    public static void load(){
        basiccore = Schematics.readBase64("bXNjaAF4nBWLyQmAMBQFXxZEFOzEUqxAPGT5QjAbSW5i7yYwzGkGAkJCRhUIs1aVTCqExaTYKLZDZfD3w2qpmuJycykCmLzS5Cv4eTFsT0h2v8n0ZLwAG3T9fe8XlA==");
        basioncore = Schematics.readBase64("bXNjaAF4nGNgYWABorzE3FQGrqTE4sz8vOT8IiA7OT+vJDWvxDexgIGpupaBOyW1OLkos6AEqICBgYEtJzEpNaeYgSk6lpFBIDs3P0U3LTU5tRhiAlABIwgBCQAEiBlO");
    }
}
