package K.content;

import K.content.Fx.KFx;
import K.content.extend.*;
import K.content.extend.Bullets.AcceledBulletType;
import K.content.extend.Bullets.DelayAcceledBulletType;
import K.content.extend.Bullets.DelayAccelBulletTypepro;
import K.content.extend.blocks.*;
import K.content.extend.util.EffectWrapper;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.DrawPart;
import mindustry.entities.part.HaloPart;
import mindustry.entities.part.RegionPart;
import mindustry.entities.part.ShapePart;
import mindustry.game.Team;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.liquid.LiquidBridge;
import mindustry.world.blocks.liquid.LiquidJunction;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.power.Battery;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.power.NuclearReactor;
import mindustry.world.blocks.power.SolarGenerator;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.blocks.storage.Unloader;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.ConsumeItemExplode;
import mindustry.world.consumers.ConsumeItemFlammable;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

import static arc.graphics.g2d.Draw.alpha;
import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;
import static mindustry.type.ItemStack.with;

public class blocks {
    public static  Block
            //炮塔
            fecesprojector,ionicpulsecannon,fecesrain,protonbeam,
            //牢底工厂
            Rody_neutron_centrifuge,fecespress,Ionic_liquid_factory,fecespulverizer,fecessiliconfactory,
                    Atomic_aggregation_device,Particle_diverter_device,hugefecespress,
                    Ionic_liquid_factory_big,fecalwatermixer,largecentrifuge,
            //地表
            groundfeces,groundurine,groundfecalwater,glowmetalfloor,
            //钻头
            fecesdrill,ionicdrill,
            //墙
            feceswall,feceswallbig,Rody_neutronwall,Rody_wall,
            //运输
            fecesconveyor,fecesrouter,fecesjunction,fecesbridgeconveyor,
                    fecessorter,fecesinvertedSorter,fecesoverflowGate,fecesunderflowGate,
                    fecesunloader,neutronconveyor,neutronrouter,neutronjunction,
                    neutronbridgeconveyor,
            //液体
            fecespump,fecesconduit,fecesconduitrouter,fecesconduitjunction,fecesconduitbridge,
                    fecesconduitsorter,fecestank,
            //电
            Simple_fecal_incinerator,fecespole,fecesbattery,Rody_reactor,fecessolarpenal,
                    fecalsteamgenerator,
            //功能
            fecescore,fecesmend,fecesvault,booth,bigbooth,Rodycore,Rodycristal,fecesbasion,
                    protectshield,
            //单位
            biggroundFactory,HelpFactory,testfactory,TacticalFactory,
            //环境墙
            fecesstonewall;
    public static void load() {

        //建筑
        //炮塔
        fecesprojector = new ItemTurret("fecesprojector"){{
            requirements(Category.turret, with(items.Feces, 20, Items.lead, 20, items.Constipated_feces, 20));
            ammo(
                    items.Feces,  new BasicBulletType(2.5f, 20){{
                        width = 7f;
                        height = 9f;
                        lifetime = 120f;
                        ammoMultiplier = 1;
                        knockback = 1f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Color.valueOf("673931");
                        frontColor = Color.valueOf("debfba");
                    }},
                    items.Constipated_feces, new BasicBulletType(3.5f, 25){{
                        width = 9f;
                        height = 12f;
                        ammoMultiplier = 4;
                        lifetime = 120f;
                        reloadMultiplier = 0.6f;
                        rangeChange = 40f;
                        knockback = 2f;
                        splashDamageRadius = 40f * 0.75f;
                        splashDamage = 50f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Color.valueOf("442800");
                        frontColor = Color.valueOf("ffcc85");
                    }},
                    Items.silicon, new BasicBulletType(3f, 36){{
                        width = 7f;
                        height = 9f;
                        homingPower = 0.2f;
                        reloadMultiplier = 2.0f;
                        ammoMultiplier = 5;
                        lifetime = 120f;

                        trailLength = 5;
                        trailWidth = 1.5f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.siliconAmmoFront;
                    }}
            );

            shoot.shots = 1;

            recoils = 1;

            drawer = new DrawTurret("reinforced-");
            size = 2;
            targetAir = false;
            shootSound = Sounds.shootRipple;
            recoil = 5f;
            reload = 40f;
            range = 320;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 800;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            coolantMultiplier = 10f;
            researchCostMultiplier = 0.1f;
            depositCooldown = 2.0f;

            limitRange(5f);
        }};
        ionicpulsecannon = new PowerTurret("ionicpulsecannon"){{
            requirements(Category.turret, with(Items.lead, 60, items.Feces, 70, items.Constipated_feces, 60));
            researchCost = with(items.Constipated_feces, 100, Items.lead, 160, items.Feces, 170);
            range = 325f;

            shoot.firstShotDelay = 40f;

            recoil = 4f;
            reload = 120f;
            shake = 8f;
            shootEffect = Fx.lancerLaserShoot;
            smokeEffect = Fx.none;
            heatColor = Color.red;
            size = 4;
            drawer = new DrawTurret("reinforced-");
            health = 1680;
            targetAir = true;
            moveWhileCharging = true;
            accurateDelay = true;
            shootSound = Sounds.shootLancer;
            coolant = consumeCoolant(0.2f);
            chargeSound = Sounds.chargeLancer;
            shootEffect = KFx.PulseShoot;

            consumePower(100 / 6f);
            consumeLiquid(liquids.ionic_liquid,1f);

            shootType = new LaserBulletType(360){{
                colors = new Color[]{Color.valueOf("673931"), Color.valueOf("855f39"), Color.black};
                //TODO merge
                chargeEffect = new MultiEffect(KFx.PulseCharge, KFx.PulseChargeBegin);
                shootEffect = KFx.PulseShoot;

                buildingDamageMultiplier = 0.25f;
                armorMultiplier = 0.5f;
                hitEffect = Fx.hitLancer;
                hitSize = 8;
                lifetime = 24f;
                drawSize = 1000f;
                collidesAir = true;
                length = 320f;
                ammoMultiplier = 1f;
                pierceCap = -1;
            }};
        }};
        fecesrain = new ItemTurret("fecesrain"){{
            requirements(Category.turret, with(Items.lead, 260, items.Feces, 170, items.Constipated_feces, 160, Items.silicon, 360));
            researchCost = with(items.Constipated_feces, 200, Items.lead, 320, items.Feces, 340, Items.silicon, 360);

            size = 4;
            health = 2400;
            armor = 10f;

            range = 200f;
            trackingRange = range * 1.4f;

            warmupMaintainTime = 22f;
            shootWarmupSpeed /= 2f;
            minWarmup = 0.9f;

            drawer = new DrawTurret("reinforced-") {{
                parts.add(new RegionPart("-barrel-main") {{
                    mirror = true;
                    moveX = 3.2f;
                    heatColor = Color.orange;
                    heatLightOpacity = 0.66f;
                }});

                parts.add(new RegionPart("-charger") {{
                    mirror = true;
                    moveX = 3.2f;

                    moves.add(new PartMove() {{
                        y = -1.25f;
                        x = 1.25f;
                        progress = PartProgress.warmup.compress(0.3f, 0.86f);
                    }});
                }});

                parts.add(new RegionPart("-back") {{
                    mirror = true;

                    moveX = 3.2f;

                    moves.add(new PartMove() {{
                        y = -1.25f;
                        x = 2f;
                        progress = PartProgress.warmup.compress(0.4f, 0.95f);
                    }});
                }});
            }};

            reload = 45;
            range = 8 * 70;
            trackingRange = range * 1.4f;

            shoot = new AdaptedShootHelix() {{
                flip = true;
                shots = 10;
                mag = 1.65f;
                scl = 6f;
                shotDelay = 3.5f;
                offset = 9.75f * Mathf.PI2;
                rotSpeedOffset = 0.015f;
                rotSpeedBegin = 0.925f;
                targetGround = true;
            }};

            canOverdrive = true;

            ammo(Items.silicon, new AcceledBulletType(5.2f, 15, "kmod-s") {{
                        width = 7f;
                        height = 13f;
                        shrinkY = 0f;

                        collidesAir = collidesGround = collidesTiles = true;
                        ammoMultiplier = 1f;
                        backColor = lightningColor = hitColor = Pal.siliconAmmoFront;
                        lightColor = frontColor = Pal.siliconAmmoFront;
                        splashDamageRadius = 32f;
                        splashDamage = 30;
                        knockback = 12f;
                        despawnEffect = Fx.smoke;
                        hitEffect = Fx.hitLancer;
                        lifetime = 100f;

                        status = StatusEffects.freezing;
                        statusDuration = 180f;

                        hitSound = despawnSound = Sounds.explosionDull;
                        hitSoundVolume = 0.6f;
                        hitSoundPitch -= 0.11f;
                        hitShake = 1.1f;
                        hitEffect = Fx.flakExplosion;

                        shootEffect = EffectWrapper.wrap(Fx.shootBigSmoke2, 0, true);
                        smokeEffect = Fx.missileTrailSmokeSmall;

                        inaccuracy = 0.3f;

                        weaveMag = 3f;
                        weaveScale = 3.55f;
                        homingDelay = 5f;
                        homingPower = 0.25f;
                        homingRange = 160f;

                        velocityBegin = 1.4f;
                        velocityIncrease = 8f;
                        accelerateBegin = 0.005f;
                        accelerateEnd = 0.75f;

                        trailColor = Pal.siliconAmmoBack;
                        trailWidth = 1f;
                        trailLength = 15;
                    }});
            ammoPerShot = 5;
            maxAmmo = 12;

            AdaptedShootHelix shootS = (AdaptedShootHelix) shoot.copy();
            shootS.flip = false;
            shootS.shots = 4;
            shootS.shotDelay = 6;
            shootS.mag /= 2;
            shootS.scl *= 2;
            shootS.offset = shootS.scl * Mathf.PI2;
            consumeLiquid(liquids.ionic_liquid, 1f);

        }};
        protonbeam = new ItemTurret("protonbeam"){{
            requirements(Category.turret, with(Items.lead, 260, items.Feces, 170, items.Rody_neutron, 300, Items.silicon, 300));
            researchCost = with(items.Rody_neutron, 300, Items.lead, 300, items.Feces, 300, Items.silicon, 300);

            size = 4;
            health = 3200;
            armor = 12f;
            range = 400f;
            warmupMaintainTime = 22f;
            shootWarmupSpeed /= 2f;
            minWarmup = 0.9f;
            shootSound = sounds.shootbeam;
            consumeLiquid(liquids.ionic_liquid,24/60f);
            ammo(items.Rody_proton, new DelayAcceledBulletType(),items.Rody_atom, new DelayAccelBulletTypepro(){{reloadMultiplier = 2;rangeChange = 188;}});
            shoot = new SalvoShoot(){{
                dx = 8;
                shots = 1;
                shotDelay = 0f;
                reload = 120;
            }};
            drawer = new DrawTurret("reinforced-") {{
                parts.add(
                        new RegionPart("-main"){{
                            mirror = false;
                            moveY = 2f;
                            heatColor = Color.white;
                            heatLightOpacity = 0.66f;
                        }}
                );
                parts.add(new RegionPart("-side") {{
                    mirror = true;

                    x=-20;
                    y=-12;
                    moveX = 3.2f;
                    moveRot = -35;

                    moves.add(new PartMove() {{
                        y = 3f;
                        x = -5f;
                        progress = PartProgress.warmup.compress(0.4f, 0.95f);
                    }});
                }});
            }};

        }};
        //工厂
        fecespress = new GenericCrafter("fecespress"){{
            requirements(Category.crafting, with(items.Feces, 45, Items.lead, 20));

            craftEffect = Fx.muddy;
            outputItem = new ItemStack(items.Constipated_feces, 1);
            craftTime = 200f;
            itemCapacity = 10;
            size = 2;
            hasItems = true;
            researchCost = with(items.Feces, 15, Items.lead, 15);

            consumeItem(items.Feces, 3);
        }};
        hugefecespress = new GenericCrafter("hugefecespress"){{
            requirements(Category.crafting, with(items.Constipated_feces, 500, Items.lead, 1500,Items.silicon, 300, items.Feces, 800));

            craftEffect = new Effect(110f, e -> {
                color(Color.brown);
                alpha(e.fout());

                randLenVectors(e.id, 3, 2f + e.finpow() * 11f, (x, y) -> {
                    Fill.circle(e.x + x, e.y + y, 0.6f + e.fin() * 5f);
                });
            });
            outputItem = new ItemStack(items.Constipated_feces, 20);
            craftTime = 90f;
            hasLiquids = true;
            itemCapacity = 300;
            liquidCapacity = 10f;
            size = 6;
            hasItems = true;
            researchCost = with(items.Constipated_feces, 500, Items.lead, 1500,Items.silicon, 300, items.Feces, 800);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawDefault(), new DrawLiquidRegion(), new DrawFrames(), new DrawFade());

            consumeItem(items.Feces, 30);
            consumePower(50);
            consumeLiquid(liquids.ionic_liquid, 0.1f);
        }};
        Ionic_liquid_factory = new GenericCrafter("Ionic_liquid_factory"){{
            requirements(Category.crafting, with(items.Feces, 50, Items.lead, 25, items.Constipated_feces, 10));
            researchCost = with(items.Feces, 150, Items.lead, 150, items.Constipated_feces, 150);
            craftTime = 120f;
            size = 3;
            hasItems = true;
            itemCapacity = 10;
            outputLiquid = new LiquidStack(liquids.ionic_liquid, 12f / 60f);
            hasPower = true;
            hasLiquids = true;
            outputsLiquid = true;
            rotate = false;
            liquidCapacity = 60f;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(liquids.urine), new DrawLiquidTile(liquids.ionic_liquid){{drawLiquidLight = true;}}, new DrawDefault());
            lightLiquid = liquids.ionic_liquid;

            consumePower(1f);
            consumeItem(Items.lead, 1);
            consumeLiquid(liquids.urine, 12f / 60f);
        }};
        Ionic_liquid_factory_big = new GenericCrafter("Ionic_liquid_factory_big"){{
            requirements(Category.crafting, with(items.Feces, 850, Items.lead, 625, Items.silicon, 100));
            researchCost = with(items.Feces, 1500, Items.lead, 1000, Items.silicon, 350);
            craftTime = 60f;
            size = 6;
            outputLiquid = new LiquidStack(liquids.ionic_liquid, 1.6f);
            hasPower = true;
            hasLiquids = true;
            outputsLiquid = true;
            rotate = false;
            liquidCapacity = 60f;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(liquids.urine), new DrawLiquidTile(liquids.ionic_liquid){{drawLiquidLight = true;}}, new DrawRegion("-rotator"){{
                spinSprite = true;
                rotateSpeed = 4f;
            }}, new DrawDefault());

            lightLiquid = liquids.ionic_liquid;

            consumePower(1000 / 60f);
            consumeLiquid(liquids.urine, 0.8f);
            consumeItems(with(Items.lead, 3,items.Feces, 3));
        }};
        fecespulverizer = new GenericCrafter("fecespulverizer"){{
            requirements(Category.crafting, with(items.Feces, 90, Items.lead, 85));
            outputItem = new ItemStack(Items.sand, 12);
            size = 2;
            itemCapacity = 20;
            liquidCapacity = 0.5f;
            craftEffect = Fx.pulverize;
            craftTime = 30f;
            updateEffect = Fx.pulverizeSmall;
            hasItems = hasPower = true;
            drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator"){{
                spinSprite = true;
                rotateSpeed = 8f;
            }}, new DrawRegion("-top"));
            ambientSound = Sounds.loopGrind;
            ambientSoundVolume = 0.5f;

            consumeItem(Items.scrap, 12);
            consumeLiquid(liquids.ionic_liquid, 0.1f);
            consumePower(3f);
        }};
        fecessiliconfactory = new AttributeCrafter("fecessiliconfactory"){{
            requirements(Category.crafting, with(items.Constipated_feces, 120, Items.lead, 80, items.Feces, 100));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(Items.silicon, 20);
            craftTime = 180f;
            size = 3;
            hasPower = true;
            hasLiquids = true;
            itemCapacity = 40;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.2f;

            consumeItems(with(Items.coal, 8, Items.sand, 12));
            consumeLiquid(liquids.ionic_liquid, 0.05f);
            consumePower(100 / 6f);
        }};
        fecalwatermixer = new GenericCrafter("fecalwatermixer"){{
            requirements(Category.crafting, with(items.Feces, 60, Items.lead, 50, items.Constipated_feces, 10));
            researchCost = with(items.Feces, 150, Items.lead, 150, items.Constipated_feces, 150);
            craftTime = 60;
            size = 3;
            hasItems = true;
            itemCapacity = 10;
            outputLiquid = new LiquidStack(liquids.fecalwater, 24f / 60f);
            hasPower = true;
            hasLiquids = true;
            outputsLiquid = true;
            rotate = false;
            liquidCapacity = 60f;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(liquids.urine), new DrawLiquidTile(liquids.ionic_liquid){{drawLiquidLight = true;}}, new DrawDefault());
            lightLiquid = liquids.fecalwater;

            consumePower(2f);
            consumeItem(items.Constipated_feces, 5);
            consumeLiquid(liquids.urine, 24f / 60f);
        }};
        Rody_neutron_centrifuge = new Separator("Rody_neutron_centrifuge"){{
            size = 3;
            requirements(Category.crafting, with(Items.silicon, 30, Items.lead, 25, items.Feces, 30));
            hasItems = true;
            itemCapacity = 100;

            results = with(
                    items.Rody_neutron, 4,
                    Items.scrap, 1
            );
            hasPower = true;
            craftTime = 3f;
            researchCost = with(items.Feces, 60, Items.lead, 60, Items.silicon, 60);

            consumeItem(items.Feces, 3);
            consumePower(1.5f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawRegion("-spinner", 5, true), new DrawDefault());
        }};
        largecentrifuge = new GenericCrafter("largecentrifuge"){{
            size = 6;
            requirements(Category.crafting, with(Items.silicon, 300, Items.lead, 350, items.Feces, 640,items.Rody_neutron, 100));
            hasItems = true;
            itemCapacity = 300;

            outputItems = ItemStack.with(items.Rody_neutron,35);
            hasPower = true;
            hasLiquids = true;
            craftTime = 60f;
            researchCost = with(items.Feces, 160, Items.lead, 160, Items.silicon, 160,items.Rody_neutron, 100);

            consumeLiquid(liquids.fecalwater, 0.8f);
            consumePower(2000/60f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawRegion("-spinner", 5, true), new DrawDefault());
        }};
        Particle_diverter_device = new GenericCrafter("Particle_diverter_device"){{
            requirements(Category.crafting, with(Items.silicon, 50, Items.lead, 40, items.Constipated_feces, 130, items.Rody_neutron, 20));
            size = 3;

            researchCostMultiplier = 0.1f;
            craftTime = 60f;
            itemCapacity = 100;

            liquidCapacity = 50f;

            consumeLiquid(liquids.ionic_liquid, 0.1f);
            consumePower(20f);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(liquids.ionic_liquid, 2f),
                    new DrawBubbles(Color.white){{
                        sides = 10;
                        recurrence = 3f;
                        spread = 6;
                        radius = 1.5f;
                        amount = 20;
                    }},
                    new DrawRegion(),
                    new DrawLiquidOutputs(),
                    new DrawGlowRegion(){{
                        alpha = 0.7f;
                        color = Color.valueOf("c4bdf3");
                        glowIntensity = 0.3f;
                        glowScale = 6f;
                    }}
            );

            ambientSound = Sounds.loopElectricHum;
            ambientSoundVolume = 0.08f;

            regionRotated1 = 3;
            outputItems = ItemStack.with(items.Rody_proton, 10, items.Rody_electron, 10);
        }};
        Atomic_aggregation_device = new GenericCrafter("Atomic_aggregation_device"){{
            requirements(Category.crafting, with(items.Constipated_feces, 520, Items.lead, 480, Items.silicon, 500, items.Rody_neutron, 300));
            craftEffect = Fx.flakExplosionBig;
            outputItem = new ItemStack(items.Rody_atom, 1);
            craftTime = 180f;
            size = 6;
            hasPower = true;
            itemCapacity = 200;
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.2f;

            drawer = new DrawMulti(
                    new DrawBaseRegion("-6x6"),
                    new DrawParticles() {{
                        particles = 5;
                        particleRad = 1f;
                        particleSize = 1f;
                        color = Pal.surgeAmmoFront;
                    }},
                    new DrawArcSmelt() {{
                        flameRad = 1.7f;
                        circleSpace = 3f;
                    }},
                    new DrawRegion(),
                    new DrawGlowRegion() {{
                        suffix = "-glow";
                        rotate = true;
                        color = Color.orange;
                    }}
            );

            consumeItems(with(items.Rody_neutron, 101, items.Rody_proton, 67, items.Rody_electron, 67));
            consumePower(100 / 6f);
        }};

        //钻头
        fecesdrill = new Drill("fecesdrill"){{
            requirements(Category.production, with(items.Feces, 10));
            tier = 2;
            drillTime = 600;
            size = 2;
            //mechanical drill doesn't work in space
            envEnabled ^= Env.space;
            researchCost = with(items.Feces, 15);

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
        ionicdrill = new Drill("ionicdrill"){{
            requirements(Category.production, with(items.Feces, 150, Items.silicon, 60, items.Constipated_feces, 50));
            drillTime = 200;
            size = 4;
            drawRim = true;
            hasPower = true;
            tier = 5;
            updateEffect = Fx.pulverizeRed;
            updateEffectChance = 0.03f;
            drillEffect = Fx.mineHuge;
            rotateSpeed = 15f;
            warmupSpeed = 0.01f;
            itemCapacity = 30;

            liquidBoostIntensity = 1.8f;

            consumePower(25f);
            consumeLiquid(liquids.ionic_liquid, 0.05f).boost();
            researchCostMultiplier = 0.1f;
        }};
        //运输
        fecesjunction = new Junction("fecesjunction"){{
            requirements(Category.distribution, with(items.Feces, 2));
            health = 150;
            buildCostMultiplier = 2f;
            capacity = 1;
            speed = 1;
            researchCost = with(items.Feces, 20);
        }};
        fecesrouter = new Router("fecesrouter"){{
            requirements(Category.distribution, with(items.Feces, 3));
            buildCostMultiplier = 2f;
            researchCost = with(items.Feces, 30);
            alwaysUnlocked = false;
            speed = 2f;
        }};
        fecesbridgeconveyor = new BufferedItemBridge("fecesbridge-conveyor"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 4));
            fadeIn = moveArrows = false;
            range = 6;
            speed = 90f;
            arrowSpacing = 8f;
            bufferCapacity = 20;
            researchCost = with(items.Feces, 50, Items.lead, 20);
            alwaysUnlocked = false;
            crushFragile = true;
        }};
        fecesconveyor = new Conveyor("fecesconveyor"){{
            requirements(Category.distribution, with(items.Feces, 1));
            health = 40;
            speed = 0.05f;
            displayedSpeed = 7f;
            buildCostMultiplier = 2f;
            junctionReplacement = blocks.fecesjunction;
            bridgeReplacement = blocks.fecesbridgeconveyor;
            researchCost = with(items.Feces, 20);
        }};
        neutronjunction = new Junction("neutronjunction"){{
            requirements(Category.distribution, with(items.Rody_neutron, 2));
            buildCostMultiplier = 2f;
            health = 300;
            capacity = 1;
            speed = 10;
            researchCost = with(items.Rody_neutron, 20);
        }};
        neutronrouter = new Router("neutronrouter"){{
            requirements(Category.distribution, with(items.Rody_neutron, 3));
            health = 400;
            buildCostMultiplier = 2f;
            researchCost = with(items.Rody_neutron, 30);
            alwaysUnlocked = false;
            speed = 20f;
        }};
        neutronbridgeconveyor = new BufferedItemBridge("neutronbridge-conveyor"){{
            requirements(Category.distribution, with(Items.silicon, 2, items.Rody_neutron, 4));
            fadeIn = moveArrows = false;
            range = 10;
            speed = 90f;
            displayedSpeed = 90;
            arrowSpacing = 16f;
            bufferCapacity = 20;
            researchCost = with(items.Rody_neutron, 50, Items.silicon, 20);
            alwaysUnlocked = false;
            crushFragile = true;
        }};
        neutronconveyor = new Conveyor("neutronconveyor"){{
            requirements(Category.distribution, with(items.Rody_neutron, 3, Items.silicon, 1));
            health = 400;
            speed = 0.5f;
            displayedSpeed = 67f;
            buildCostMultiplier = 2f;
            junctionReplacement = blocks.neutronjunction;
            bridgeReplacement = blocks.neutronbridgeconveyor;
            researchCost = with(items.Rody_neutron, 60, Items.silicon, 20);
        }};
        fecessorter = new Sorter("fecessorter"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            buildCostMultiplier = 1f;
            researchCost = with(items.Feces, 20, Items.lead, 20);
            alwaysUnlocked = false;
        }};
        fecesinvertedSorter = new Sorter("fecesinverted-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            buildCostMultiplier = 1f;
            researchCost = with(items.Feces, 20, Items.lead, 20);
            alwaysUnlocked = false;
            invert = true;
        }};
        fecesoverflowGate = new OverflowGate("fecesoverflow-gate"){{
                requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            researchCost = with(items.Feces, 20, Items.lead, 20);
            alwaysUnlocked = false;
            buildCostMultiplier = 1f;
        }};
        fecesunderflowGate = new OverflowGate("fecesunderflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            buildCostMultiplier = 1f;
            researchCost = with(items.Feces, 20, Items.lead, 20);
            alwaysUnlocked = false;
            invert = true;
        }};
        fecesunloader = new Unloader("fecesunloader"){{
            requirements(Category.distribution, with(items.Constipated_feces, 25, Items.silicon, 10));
            speed = 60f / 20f;
            group = BlockGroup.transportation;
        }};
        //液体
        fecespump = new Pump("fecespump"){{
            requirements(Category.liquid, with(items.Constipated_feces, 10,Items.lead, 5));
            consumePower(20);
            size = 2;
            researchCost = with(items.Feces, 30, Items.lead, 30);
        }};
        fecesconduitrouter = new LiquidRouter("fecesconduitrouter"){{
            requirements(Category.liquid, with(items.Feces, 1,Items.lead, 1));
            researchCostMultiplier = 0.5f;
        }};
        fecesconduitjunction = new LiquidJunction("fecesconduitjunction"){{
            requirements(Category.liquid, with(items.Feces, 10,Items.lead, 20));
            researchCostMultiplier = 0.5f;
        }};
        fecesconduitbridge = new LiquidBridge("fecesconduitbridge"){{
            requirements(Category.liquid, with(items.Feces, 40,Items.lead, 40));
            researchCostMultiplier = 0.15f;
            range = 6;
        }};
        fecesconduit = new Conduit("fecesconduit"){{
            requirements(Category.liquid, with(items.Feces, 1,Items.lead, 1));
            buildCostMultiplier = 0.2f;
            bridgeReplacement = fecesconduitbridge;
            junctionReplacement = fecesconduitjunction;
        }};
        fecesconduitsorter = new SortLiquidRouter("fecesconduitsorter"){{
            requirements(Category.liquid, with(items.Feces, 10,Items.lead, 20));
            researchCostMultiplier = 0.12f;
            isHidden();
        }};
        fecestank = new LiquidRouter("fecestank"){{
            requirements(Category.liquid, with(items.Constipated_feces, 120, Items.lead, 140));
            size = 3;
            solid = true;
            liquidCapacity = 2000f;
            health = 600;
            researchCostMultiplier = 0.1f;
        }};
        //电力
        fecespole = new PowerPole("fecespole"){{
            requirements(Category.power, with(items.Feces, 10, Items.lead, 10));
            size = 2;
            areaRange = 12;
            lineCount = 1;
            lineRadius = 20;
            laserRange = 12;
            maxNodes = 20;
            researchCost = with(items.Feces, 20);
        }};
        Simple_fecal_incinerator = new ConsumeGenerator("Simple_fecal_incinerator"){{
            requirements(Category.power, with(items.Feces, 20, Items.lead, 10));
            powerProduction = 5f;
            itemDuration = 200f;
            size = 3;

            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.03f;
            generateEffect = Fx.generatespark;

            consume(new ConsumeItemFlammable());
            consume(new ConsumeItemExplode());
            itemDurationMultipliers.put(items.Feces, 3f);
            researchCost = with(items.Feces, 640, Items.lead, 320);

            drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion());
        }};
        fecalsteamgenerator = new ConsumeGenerator("fecalsteamgenerator"){{
            requirements(Category.power, with(items.Feces, 200, Items.lead, 100,Items.silicon,50));
            researchCost = with(items.Feces, 640, Items.lead, 320, Items.silicon,250);
            powerProduction = 15f;
            itemDuration = 120f;
            health = 1200;
            size = 3;
            hasLiquids = true;
            consumeLiquid(liquids.urine,0.1f);
            consume(new ConsumeItemFlammable());

            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.03f;
            generateEffect = Fx.heatReactorSmoke;

            drawer = new DrawMulti(new DrawRegion("-bottom"),new DrawLiquidTile(),new DrawRegion("-spinner", 5, true),new DrawDefault(), new DrawWarmupRegion());
        }};
        fecesbattery = new Battery("fecesbattery"){{
            requirements(Category.power, with(items.Feces, 50, Items.lead, 80));
            consumePowerBuffered(40000f);
            baseExplosiveness = 10f;
            health = 200;
            size = 2;
            researchCostMultiplier = 0.1f;
        }};
        fecessolarpenal = new SolarGenerator("fecessolarpenal"){{
            requirements(Category.power, with(Items.lead, 100, Items.silicon, 80, items.Feces, 40));
            health = 500;
            size = 3;
            powerProduction = 10;
            researchCostMultiplier = 0.1f;
        }};
        Rody_reactor = new NuclearReactor("Rody_reactor"){{
            requirements(Category.power, with(Items.lead, 1000, Items.silicon, 600, items.Rody_neutron, 350, items.Constipated_feces, 450));
            ambientSound = Sounds.loopThoriumReactor;
            ambientSoundVolume = 0.31f;
            outputsPower = true;
            itemCapacity = 60;
            liquidCapacity = 120f;
            size = 5;
            health = 7000;
            itemDuration = 360f;
            powerProduction = 5000 / 3f;
            heating = 0.02f;
            fuelItem = items.Rody_atom;

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPlasma(), new DrawDefault());
            explosionShake = 100f;
            explosionShakeDuration = 120f;
            explosionDamage = 9000 * 5;
            explosionRadius = 40;
            explosionMinWarmup = 0.3f;
            explodeEffect = KFx.ReactorExplosion;
            explodeSound = Sounds.explosionReactor2;
            researchCostMultiplier = 0.1f;

            consumeItem(items.Rody_atom);
            consumeLiquid(liquids.ionic_liquid, 0.4f).update(false);
        }};
        //墙
        feceswall = new Wall("feceswall"){{
            requirements(Category.defense, with(items.Constipated_feces, 5));
            health = 400;
            size = 1;
            researchCost = with(items.Constipated_feces, 20);
        }};
        feceswallbig = new Wall("feceswallbig"){{
            requirements(Category.defense, with(items.Constipated_feces, 20));
            health = 1680;
            size = 2;
            researchCostMultiplier = 0.25f;
        }};
        Rody_neutronwall = new AdaptWall("Rody_neutronwall"){{
            requirements(Category.defense, with(items.Rody_neutron, 20));
            health = 800;
            size = 1;
            researchCostMultiplier = 0.25f;
        }};
        Rody_wall = new Wall("Rody_wall"){{
            requirements(Category.defense, with(items.Rody_atom, 1000));
            health = 666666;
            size = 5;
            armor = 67;
            researchCost = with(items.Rody_atom, 20000);
            placeSound = sounds.watching;
            breakSound = sounds.dabian;
            destroySound = sounds.dogshit;
        }};
        //功能
        fecescore = new CoreBlock("fecescore"){{
            requirements(Category.effect, BuildVisibility.coreZoneOnly, with(items.Feces, 1000, items.Constipated_feces, 100, Items.lead, 800));
            alwaysUnlocked = false;
            researchCostMultiplier = 0.02f;

            health = 1500;
            itemCapacity = 5000;
            size = 3;
            buildCostMultiplier = 1f;
            unitType = KUnitTypes.Fecaldrone;

            unitCapModifier = 10;
        }};
        fecesbasion = new CoreBlock("fecesbasion"){{
            requirements(Category.effect, with(items.Feces, 3000, items.Constipated_feces, 500, Items.lead, 2000, Items.silicon, 1000));
            alwaysUnlocked = false;
            researchCostMultiplier = 0.02f;

            health = 4500;
            itemCapacity = 12000;
            size = 4;
            buildCostMultiplier = 1f;
            unitType = KUnitTypes.Fecalwarcraft;

            unitCapModifier = 25;
        }};
        fecesmend = new MendProjector("fecesmend"){{
            requirements(Category.effect, with(Items.lead, 200, items.Constipated_feces, 80, Items.silicon, 40, items.Feces, 250));
            consumePower(50 / 6f);
            size = 3;
            reload = 120;
            range = 160f;
            healPercent = 8f;
            phaseBoost = 30f;
            phaseColor = Color.black;
            health = 1500;
            consumeLiquid(liquids.ionic_liquid, 0.05f).boost();
            researchCostMultiplier = 0.1f;
        }};
        fecesvault = new StorageBlock("fecesvault"){{
            requirements(Category.effect, with(items.Constipated_feces, 200, Items.lead, 150));
            size = 3;
            itemCapacity = 1500;
            health = 550;
            researchCostMultiplier = 0.1f;
        }};
        booth = new Booth("booth"){{
            requirements(Category.effect, with());
            itemCapacity = 1;
            this.buildVisibility = BuildVisibility.hidden;
        }};
        bigbooth = new Booth("bigbooth"){{
            requirements(Category.effect, with());
            itemCapacity = 1;
            size = 2;
            this.buildVisibility = BuildVisibility.hidden;
        }};
        Rodycore = new PowerTurret("Rodycore"){{
            requirements(Category.effect, with());
            health = 2111114514;
            armor = 2111451419;
            this.buildVisibility = BuildVisibility.hidden;

            var heatProgress = DrawPart.PartProgress.warmup.delay(9.9f);
            float circleRad = 180f, circleRotSpeed = 1.5f, circleStroke = 8f;
            Color circleColor = Color.red;

            hasShadow = false;
            warmupMaintainTime = 180f;
            recoil = 0f;
            reload = 1f;
            shootEffect = Fx.none;
            smokeEffect = Fx.none;
            shake = 0;
            size = 2;
            breakable = false;
            solid = true;
            targetAir = false;
            moveWhileCharging = true;
            accurateDelay = true;
            shootSound = Sounds.none;
            range = 0;
            autoResetEnabled = false;
            drawLiquidLight = emitLight = obstructsLight = true;
            lightRadius = 1000f;
            shootCone = 360;

            shootType = new BulletType(){{
                shootY = -8;
                lifetime = 2f;
                speed = 0f;
                collidesAir = false;
                collidesGround = false;
                shootEffect = Fx.none;
                hitEffect = Fx.none;
                smokeEffect = Fx.none;
                lightRadius = 1000f;
                lightOpacity = 1;
                lightColor = Color.red;
                hasShadow = false;
                lightClipSize = 800f;
                despawnEffect = Fx.none;
            }};

            drawer = new DrawTurret(""){{
                parts.add(new RegionPart("-ring1"){{
                    heatColor = Color.red;
                    moveRot = 22.5f;
                    rotateSpeed = 0.1f;
                    heatProgress = PartProgress.warmup;
                    hasShadow = false;
                }});
                parts.add(new RegionPart("-ring2"){{
                    heatColor = Color.red;
                    moveRot = -2*22.5f;
                    rotateSpeed = 0.2f;
                    heatProgress = PartProgress.warmup;
                    hasShadow = false;
                }});
                parts.add(new RegionPart("-ring3"){{
                    heatColor = Color.red;
                    moveRot = 3*22.5f;
                    colorTo = Color.white;
                    rotateSpeed = 0.5f;
                    heatProgress = PartProgress.warmup;
                    hasShadow = false;
                }});
                parts.addAll(
                new ShapePart(){{
                    progress = PartProgress.warmup;
                    color = circleColor;
                    circle = true;
                    hollow = true;
                    stroke = 0f;
                    strokeTo = circleStroke;
                    radius = circleRad;
                    layer = Layer.effect;
                }},
                new ShapePart(){{
                    progress = PartProgress.warmup;
                    rotateSpeed = -circleRotSpeed;
                    color = circleColor;
                    sides = 4;
                    hollow = true;
                    stroke = 0f;
                    strokeTo = circleStroke;
                    radius = circleRad - 1f;
                    layer = Layer.effect;
                }},
                new ShapePart(){{
                    progress = PartProgress.warmup;
                    rotateSpeed = -circleRotSpeed;
                    color = circleColor;
                    sides = 4;
                    hollow = true;
                    stroke = 0f;
                    strokeTo = circleStroke;
                    radius = circleRad / 1.414f;
                    layer = Layer.effect;
                    rotation = 45;
                }},
                new HaloPart(){{
                    progress = PartProgress.warmup;
                    color = circleColor;
                    tri = true;
                    shapes = 3;
                    triLength = 0f;
                    triLengthTo = 50f;
                    radius = 40f;
                    haloRadius = circleRad;
                    haloRotateSpeed = circleRotSpeed / -2f;
                    shapeRotation = 180f;
                    haloRotation = 180f;
                    layer = Layer.effect;
                        }}
                );


            }};

        }};
        Rodycristal = new PowerTurret1("Rodycristal"){{
            requirements(Category.effect, with());
            this.buildVisibility = BuildVisibility.hidden;
            health = 100;
            size = 2;
            shootSound = Sounds.none;
            range = 0;
            drawLiquidLight = emitLight = true;
            lightRadius = 100f;
            lightColor = Color.red;
            hasShadow = false;
            shootType = new BulletType(){{
                shootY = -8;
                lifetime = 2f;
                speed = 0f;
                shootEffect = Fx.none;
                hitEffect = Fx.none;
                smokeEffect = Fx.none;
                lightColor = Color.red;
                hasShadow = false;
                lightClipSize = 160f;
                despawnEffect = Fx.none;
            }};
            float circleRad = 18f, circleRotSpeed = 5f, circleStroke = 3f;
            Color circleColor = Color.red;
            drawer = new DrawTurret(""){{
                parts.addAll(
                        new ShapePart(){{
                            progress = PartProgress.warmup;
                            rotateSpeed = -circleRotSpeed;
                            color = circleColor;
                            sides = 4;
                            hollow = true;
                            stroke = circleStroke;
                            radius = circleRad - 1f;
                            layer = Layer.effect;
                        }},
                        new ShapePart(){{
                            progress = PartProgress.warmup;
                            rotateSpeed = -circleRotSpeed;
                            color = circleColor;
                            sides = 4;
                            hollow = true;
                            stroke = circleStroke;
                            radius = circleRad / 1.414f;
                            layer = Layer.effect;
                            rotation = 45;
                        }},
                        new HaloPart(){{
                            progress = PartProgress.warmup;
                            color = circleColor;
                            tri = true;
                            shapes = 3;
                            triLength = 0f;
                            triLengthTo = 5f;
                            radius = 10f;
                            haloRadius = circleRad / 2;
                            haloRotateSpeed = circleRotSpeed / -2f;
                            shapeRotation = 180f;
                            haloRotation = 180f;
                            layer = Layer.effect;
                        }}
                );


            }};
        }};
        protectshield = new ShieldGenerator("protectshield"){{
            requirements(Category.effect, with(Items.silicon,350,Items.lead,280,items.Rody_neutron,200,items.Constipated_feces,100));
            health = 1600;
            size = 4;
        }};
        //单位
        HelpFactory = new UnitFactory("HelpFactory"){{
            requirements(Category.units, with(items.Feces, 120, Items.lead, 70, Items.silicon, 60));
            plans = Seq.with(
                    new UnitPlan(KUnitTypes.Plasmadrill, 60f * 30, with(items.Constipated_feces, 25,Items.lead, 30)),
                    new UnitPlan(KUnitTypes.Helperdrone, 60f * 30, with(items.Constipated_feces, 40,Items.lead, 50,Items.silicon, 60))
            );
            size = 3;
            consumePower(12f);
            researchCostMultiplier = 0.5f;
        }};
        biggroundFactory = new UnitFactory("bigground-factory"){{
            requirements(Category.units, with(items.Feces, 1250, Items.lead, 3000, Items.silicon, 2000));
            plans = Seq.with(
                    new UnitPlan(KUnitTypes.Bigdagger, 60f * 15, with(Items.silicon, 1000, Items.lead, 1000)),
                    new UnitPlan(KUnitTypes.Bigcrawler, 60f * 10, with(Items.silicon, 800, Items.coal, 1000)),
                    new UnitPlan(KUnitTypes.Bignova, 60f * 40, with(Items.silicon, 3000, Items.lead, 2000, Items.titanium, 2000))
            );
            size = 15;
            consumePower(200f);
            researchCostMultiplier = 0.5f;
        }};
        testfactory = new TF("testfactory"){{
            requirements(Category.units, with(items.Feces, 325, Items.lead, 420, Items.silicon, 250));
            plans = Seq.with(
                    new UnitFactory.UnitPlan(KUnitTypes.Combatengineer, 60f * 50, with(Items.silicon, 30,items.Feces, 10, Items.lead, 40)),
                    new UnitFactory.UnitPlan(KUnitTypes.Firebeedrone, 60f * 80, with(Items.silicon, 80,items.Constipated_feces, 30, Items.lead, 30)),
                    new UnitFactory.UnitPlan(KUnitTypes.Testspider, 60f * 180, with(Items.silicon, 380,items.Constipated_feces, 150, Items.lead, 340)),
                    new UnitFactory.UnitPlan(KUnitTypes.Conceptualhovertank, 60f * 100, with(Items.silicon, 180,items.Feces, 130, Items.lead, 240)),
                    new UnitFactory.UnitPlan(KUnitTypes.Testtank, 60f * 100, with(Items.silicon, 180,items.Constipated_feces, 16, Items.lead, 340)),
                    new UnitFactory.UnitPlan(KUnitTypes.Flyingfortress, 60f * 90, with(Items.silicon, 280,items.Feces, 130, Items.lead, 340))
                    );
            size = 6;
            consumePower(20f);
            consumeLiquid(liquids.ionic_liquid,0.4f);
            researchCostMultiplier = 0.02f;
            health = 2000;
        }};
        TacticalFactory = new UnitFactory("TacticalFactory"){{
            requirements(Category.units, with(items.Constipated_feces, 120, Items.lead, 120, Items.silicon, 80));
            plans = Seq.with(
                    new UnitPlan(KUnitTypes.Tacticalassaultmecha, 60f * 60, with(items.Constipated_feces, 45,Items.lead, 80,Items.silicon,80)),
                    new UnitPlan(KUnitTypes.Tacticalsuppressiontank, 60f * 90, with(items.Constipated_feces, 85,Items.lead, 100,Items.silicon,120))
            );
            size = 4;
            consumePower(12f);
            researchCostMultiplier = 0.5f;
        }};
        //地板
        groundfeces = new OreBlock(items.Feces){{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};
        groundurine = new Floor("groundurine"){{
            speedMultiplier = 0.5f;
            variants = 0;
            status = StatusEffects.wet;
            statusDuration = 90f;
            liquidDrop = liquids.urine;
            isLiquid = true;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
        }};
        groundfecalwater = new Floor("groundfecalwater"){{
            speedMultiplier = 0.2f;
            variants = 0;
            liquidDrop = liquids.fecalwater;
            liquidMultiplier = 1.5f;
            isLiquid = true;
            status = StatusEffects.wet;
            statusDuration = 120f;
            status = statuseffect.sick;
            statusDuration = 100f;
            drownTime = 200f;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
        }};
        glowmetalfloor = new Floor("glowmetalfloor"){{
            speedMultiplier = 0.2f;
            variants = 0;
            albedo = 0.9f;
            supportsOverlay = true;
            emitLight = true;
            lightRadius = 30f;
            lightColor = Team.crux.color.cpy().a(0.3f);
        }};

        //环境墙
        fecesstonewall = new StaticWall("feces-stone-wall");
    }

    }

