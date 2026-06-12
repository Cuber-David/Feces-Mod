package K.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class items {
    public static Item
            //牢底
            Rody_proton,Rody_neutron,Rody_electron,Feces;

    public static void load(){
        Rody_proton = new Item("Rody_proton", Color.white){{
            explosiveness = 69.69f;
            hardness = 100;
            charge = 1.11f;
            alwaysUnlocked = true;
        }};
        Rody_neutron = new Item("Rody_neutron", Color.black){{
            explosiveness = 0;
            hardness = 100;
            alwaysUnlocked = true;
        }};
        Rody_electron = new Item("Rody_electron", Color.blue){{
            explosiveness = 96.96f;
            charge = -1.11f;
            hardness = 100;
            alwaysUnlocked = true;
        }};
        Feces = new Item("Feces", Color.brown){{
            flammability = 50;
            alwaysUnlocked = true;
        }};
    }
}
