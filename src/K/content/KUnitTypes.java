package K.content;

import K.content.unit.God.GodKUnitType;
import K.content.unit.God.RodyUnitType;
import K.content.unit.air.*;
import K.content.unit.ground.*;
import K.content.unit.others.*;
import K.content.unit.others.jujutsu.*;

public class KUnitTypes {
    public static FecaldroneUnitType Fecaldrone;public static BigdaggerUnitType Bigdagger;
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
    public static GodKUnitType GodK;
    public static Fecalwarcraft Fecalwarcraft;
    public static TacticalassaultmechaUnitType Tacticalassaultmecha;
    public static TacticalsuppressiontankUnitType Tacticalsuppressiontank;
    public static DomainInfUnitType Domaininf;
    public static BlueUnitType Blue;
    public static RedUnitType Red;
    public static PurpleUnitType Purple;
    public static BaU Ba;
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
        GodK = new GodKUnitType("GodK");
        Tacticalassaultmecha = new TacticalassaultmechaUnitType("Tacticalassaultmecha");
        Tacticalsuppressiontank = new TacticalsuppressiontankUnitType("Tacticalsuppressiontank");
        Domaininf = new DomainInfUnitType("Domaininf");
        Blue = new BlueUnitType("Blue");
        Red = new RedUnitType("Red");
        Purple = new PurpleUnitType("Purple");
        Ba = new BaU("Ba");
    }
}
