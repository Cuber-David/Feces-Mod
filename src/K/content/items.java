package K.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.type.Item;

public class items {
    public static Item
            //牢底
            Rody_proton,Rody_neutron,Rody_electron,Rody_atom,
            //大便
            Feces,Constipated_feces,
            //其他材料
            Fecium;

    public static void load(){
        Rody_proton = new Item("Rody_proton", Color.white){{
            explosiveness = 69.69f;
            hardness = 100;
            charge = 1.11f;
            alwaysUnlocked = false;
        }};
        Rody_neutron = new Item("Rody_neutron", Color.black){{
            explosiveness = 0;
            hardness = 100;
            alwaysUnlocked = false;
        }};
        Rody_electron = new Item("Rody_electron", Color.blue){{
            explosiveness = 96.96f;
            charge = -1.11f;
            hardness = 100;
            alwaysUnlocked = false;
        }};
        Rody_atom = new Item("Rody_atom", Color.white){{
            alwaysUnlocked = false;
            radioactivity = 3;
            explosiveness = 888.888f;
            frames = 6;
            hardness = 6767;
        }};
        Feces = new Item("Feces", Color.brown){{
            flammability = 0.1f;
            alwaysUnlocked = false;
        }};
        Constipated_feces = new Item("Constipated_feces", Color.brown){{
            flammability = 5.0f;
            alwaysUnlocked = false;
        }};
        Fecium = new Item("Fecium", Color.valueOf("663931")){{
            flammability = 0.15f;
            hardness = 4;
            healthScaling = 1.4f;
        }};
    }
}
