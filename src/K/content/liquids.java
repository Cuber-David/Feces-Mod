package K.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class liquids {
    public static Liquid
            urine,fecalwater,ionic_liquid;
    public static void load(){
        urine = new Liquid("urine", Color.valueOf("986d41"))
        {{
            temperature = 1f;
        }};
        fecalwater = new Liquid("fecalwater",Color.valueOf("7a433a")){{
            viscosity = 0.9f;
            flammability = 1f;
        }};
        ionic_liquid = new Liquid("ionic_liquid",Color.black){{
            viscosity = 0.9f;
            temperature = 0.05f;
            heatCapacity = 0.1f;
        }};
    }

}
