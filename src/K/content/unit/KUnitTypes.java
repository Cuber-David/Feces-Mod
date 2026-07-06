package K.content.unit;


import K.content.planets;
import arc.struct.ObjectSet;

import static K.content.planets.nonepro;

public class KUnitTypes {
    public static FecaldroneUnitType Fecaldrone;
    public static BigdaggerUnitType Bigdagger;
    public static BignovaUnitType Bignova;
    public static BigcrawlerUnitType Bigcrawler;
    public static void load(){
        Fecaldrone = new FecaldroneUnitType("Fecaldrone"){{
            alwaysUnlocked = false;
        }};
        Bigdagger = new BigdaggerUnitType("Bigdagger"){{
            isHidden();
        }};
        Bignova = new BignovaUnitType("Bignova"){{
            isHidden();
        }};
        Bigcrawler = new BigcrawlerUnitType("Bigcrawler"){{
            isHidden();
        }};
    }
}
