package K.content;

import K.content.unit.*;
import mindustry.content.Items;
import mindustry.type.ItemStack;

import static mindustry.type.ItemStack.with;

public class KUnitTypes {
    public static FecaldroneUnitType Fecaldrone;
    public static BigdaggerUnitType Bigdagger;
    public static BignovaUnitType Bignova;
    public static BigcrawlerUnitType Bigcrawler;
    public static PlasmadrillUnitType Plasmadrill;
    public static TestspiderUnitType Testspider;
    public static HelperdroneUnitType Helperdrone;
    public static ThunderUnitType Thunder;
    public static SoundUnitType Sound;
    public static FlyingfortressUnitType Flyingfortress;
    public static ConceptualhovertankUnitType Conceptualhovertank;
    public static TesttankUnitType Testtank;
    public static FirebeedroneUnitType Firebeedrone;
    public static CombatengineerUnitType Combatengineer;
    public static RodyUnitType Rody;
    public static Fecalwarcraft Fecalwarcraft;
    public static void load(){
        Thunder = new ThunderUnitType("Thunder");
        Sound = new SoundUnitType("Sound");
        Fecaldrone = new FecaldroneUnitType("Fecaldrone"){{
            alwaysUnlocked = false;
        }};
        Fecalwarcraft = new Fecalwarcraft("Fecalwarcraft");
        Bigdagger = new BigdaggerUnitType("Bigdagger"){{
            isHidden();
        }};
        Bignova = new BignovaUnitType("Bignova"){{
            isHidden();
        }};
        Bigcrawler = new BigcrawlerUnitType("Bigcrawler"){{
            isHidden();
        }};
        Plasmadrill = new PlasmadrillUnitType("Plasmadrill"){{
            researchCostMultiplier = 10f;
        }};
        Testspider = new TestspiderUnitType("Testspider"){{
        }};
        Helperdrone = new HelperdroneUnitType("Helperdrone"){{
            researchCostMultiplier = 10f;
        }};
        Flyingfortress = new FlyingfortressUnitType("Flyingfortress"){{
            researchCostMultiplier = 0.1f;
        }};
        Conceptualhovertank = new ConceptualhovertankUnitType("Conceptualhovertank");
        Testtank = new TesttankUnitType("Testtank");
        Firebeedrone = new FirebeedroneUnitType("Firebeedrone");
        Combatengineer = new CombatengineerUnitType("Combatengineer");
        Rody = new RodyUnitType("Rody");
    }
}
