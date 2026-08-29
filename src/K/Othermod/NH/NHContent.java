package K.Othermod.NH;

import K.KMod;
import K.content.entities.UltFire;
import K.graphics.NHShaders;
import arc.Core;
import arc.func.Cons;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.gen.Icon;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Layer;
import mindustry.world.meta.Attribute;

public class NHContent extends Content {
    public static final float GRAVITY_TRAP_LAYER = Layer.light + 2.472f;
    public static final float HEX_SHIELD_LAYER = 76.172f;
    public static final float QUANTUM_LAYER = Layer.blockOver + 0.1919f;
    public static final float POWER_AREA = Layer.power + 0.114f;
    public static final float POWER_DYNAMIC = Layer.power + 0.514f;

    public static final int radioactive = 1 << 8;

    public static Texture smoothNoise, particleNoise, darkerNoise, noise;

    public static CacheLayer quantumLayer, armorLayer;

    public static TextureRegion
            crossRegion, sourceCenter, timeIcon, xenIcon,
            iconLevel, ammoInfo, arrowRegion, pointerRegion, icon, icon2, upgrade, upgrade2,danger,
            linkArrow, activeBoost,
            beamLaser, beamLaserEnd, beamLaserInner, beamLaserInnerEnd;

    public static TextureRegion //UI
            raid, objective, fleet, capture, adfsds;

    public static Attribute quantum, density;

    public static void loadPriority() {
        new NHContent().load();
    }

    public static void loadBeforeContentLoad() {
        CacheLayer.add(quantumLayer = new CacheLayer.ShaderLayer(NHShaders.quantum));
        quantum = Attribute.add("quantum");
        density = Attribute.add("density");
    }

    public static void loadLast() {

        //registerStatement("gravitywell", GravityWell::new, GravityWell::new);
        //registerStatement("linetarget", LineTarget::new, LineTarget::new);
        //registerStatement("randspawn", RandomSpawn::new, RandomSpawn::new);
        //registerStatement("randtarget", RandomTarget::new, RandomTarget::new);
        //registerStatement("teamthreat", TeamThreat::new, TeamThreat::new);
        //registerStatement("raidcontrol", RaidControl::new, RaidControl::new);
        //registerStatement("defaultraid", DefaultRaid::new, DefaultRaid::new);

    }

    @Override
    public ContentType getContentType() {
        return ContentType.error;
    }

    public void load() {
        if (Vars.headless) return;

        Icon.icons.put("midantha", new TextureRegionDrawable(Core.atlas.find(KMod.name("midantha"))));
        Icon.icons.put("nh", new TextureRegionDrawable(Core.atlas.find(KMod.name("icon-2"))));

        UltFire.load();

        crossRegion = Core.atlas.find("cross");
        sourceCenter = Core.atlas.find(KMod.name("source-center"));
        xenIcon = Core.atlas.find(KMod.name("xen-icon"));
        upgrade = Core.atlas.find(KMod.name("upgrade"));
        upgrade2 = Core.atlas.find(KMod.name("upgrade2"));
        arrowRegion = Core.atlas.find(KMod.name("jump-gate-arrow"));
        ammoInfo = Core.atlas.find(KMod.name("upgrade-info"));
        iconLevel = Core.atlas.find(KMod.name("level-up"));
        pointerRegion = Core.atlas.find(KMod.name("jump-gate-pointer"));
        icon = Core.atlas.find(KMod.name("icon-white"));
        icon2 = Core.atlas.find(KMod.name("icon-2"));

        raid = Core.atlas.find(KMod.name("event-default-raid-t1"));
        objective = Core.atlas.find(KMod.name("objective"));
        fleet = Core.atlas.find(KMod.name("fleet"));
        capture = Core.atlas.find(KMod.name("capture"));

        danger = Core.atlas.find(KMod.name("danger"));
        adfsds = Core.atlas.find(KMod.name("ADFSDS"));

        linkArrow = Core.atlas.find(KMod.name("linked-arrow"));
        activeBoost = Core.atlas.find(KMod.name("active-boost"));

        beamLaser = Core.atlas.find(KMod.name("stream-beam"));
        beamLaserEnd = Core.atlas.find(KMod.name("stream-beam-end"));
        beamLaserInner = Core.atlas.find(KMod.name("stream-beam-inner"));
        beamLaserInnerEnd = Core.atlas.find(KMod.name("stream-beam-inner-end"));

        smoothNoise = loadTex("smooth-noise", t -> {
            t.setFilter(Texture.TextureFilter.linear);
            t.setWrap(Texture.TextureWrap.repeat);
        });

        particleNoise = loadTex("particle-noise", t -> {
            t.setFilter(Texture.TextureFilter.linear);
            t.setWrap(Texture.TextureWrap.repeat);
        });

        darkerNoise = loadTex("darker-noise", t -> {
            t.setFilter(Texture.TextureFilter.linear);
            t.setWrap(Texture.TextureWrap.repeat);
        });

        noise = loadTex("noise", t -> {
            t.setFilter(Texture.TextureFilter.linear);
            t.setWrap(Texture.TextureWrap.repeat);
        });
    }

    Texture loadTex(String name, Cons<Texture> modifier) {
        Texture tex = new Texture(KMod.MOD.root.child("textures").child(name + (name.endsWith(".png") ? "" : ".png")));
        modifier.get(tex);

        return tex;
    }
}
