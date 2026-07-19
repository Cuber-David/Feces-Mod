package K.content.unit;

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
    public static void load(){
        Thunder = new ThunderUnitType("Thunder");
        Sound = new SoundUnitType("Sound");
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
        Plasmadrill = new PlasmadrillUnitType("Plasmadrill");
        Testspider = new TestspiderUnitType("Testspider");
        Helperdrone = new HelperdroneUnitType("Helperdrone");
        Flyingfortress = new FlyingfortressUnitType("Flyingfortress");
    }
}
