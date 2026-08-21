package K.content;

import K.content.unit.Rody.RodyUnitType;
import K.content.unit.air.*;
import K.content.unit.ground.*;
import K.content.unit.others.DomainUnitType;
import K.content.unit.others.SoundUnitType;
import K.content.unit.others.ThunderUnitType;

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
    public static TacticalassaultmechaUnitType Tacticalassaultmecha;
    public static TacticalsuppressiontankUnitType Tacticalsuppressiontank;
    public static DomainUnitType Domain;
    public static void load(){
        Thunder = new ThunderUnitType("Thunder");
        Sound = new SoundUnitType("Sound");
        Fecaldrone = new FecaldroneUnitType("Fecaldrone");
        Fecalwarcraft = new Fecalwarcraft("Fecalwarcraft");
        Bigdagger = new BigdaggerUnitType("Bigdagger");
        Bignova = new BignovaUnitType("Bignova");
        Bigcrawler = new BigcrawlerUnitType("Bigcrawler");
        Plasmadrill = new PlasmadrillUnitType("Plasmadrill");
        Testspider = new TestspiderUnitType("Testspider");
        Helperdrone = new HelperdroneUnitType("Helperdrone");
        Flyingfortress = new FlyingfortressUnitType("Flyingfortress");
        Conceptualhovertank = new ConceptualhovertankUnitType("Conceptualhovertank");
        Testtank = new TesttankUnitType("Testtank");
        Firebeedrone = new FirebeedroneUnitType("Firebeedrone");
        Combatengineer = new CombatengineerUnitType("Combatengineer");
        Rody = new RodyUnitType("Rody");
        Tacticalassaultmecha = new TacticalassaultmechaUnitType("Tacticalassaultmecha");
        Tacticalsuppressiontank = new TacticalsuppressiontankUnitType("Tacticalsuppressiontank");
        Domain = new DomainUnitType("Domain");
    }
}
