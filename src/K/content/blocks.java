package K.content;

import arc.graphics.Color;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.Separator;
import mindustry.world.consumers.ConsumeItemExplode;
import mindustry.world.consumers.ConsumeItemFlammable;
import mindustry.world.draw.*;
import mindustry.world.meta.Env;

import static mindustry.type.ItemStack.with;

public class blocks {
    public static  Block
            //炮塔
            fecesprojector,
            //牢底工厂
            Rody_neutron_centrifuge,fecespress,
            //地表
            groundfeces,
            //钻头
            fecesdrill,
            //运输
            fecesconveyor,fecesrouter,fecesjunction,fecesbridgeconveyor,
                    fecessorter,fecesinvertedSorter,fecesoverflowGate,fecesunderflowGate,
            //电
    Simple_fecal_incinerator;
    public static void load() {

        //建筑
        fecesprojector = new ItemTurret("fecesprojector"){{
            requirements(Category.turret, with(items.Feces, 90, Items.lead, 20, items.Constipated_feces, 20));
            ammo(
                    items.Feces,  new BasicBulletType(2.5f, 20){{
                        width = 7f;
                        height = 9f;
                        lifetime = 60f;
                        ammoMultiplier = 1;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.copperAmmoBack;
                        frontColor = Color.brown;
                    }},
                    items.Constipated_feces, new BasicBulletType(3.5f, 45){{
                        width = 9f;
                        height = 12f;
                        ammoMultiplier = 4;
                        lifetime = 60f;
                        reloadMultiplier = 0.8f;
                        rangeChange = 16f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Color.brown;
                    }},
                    Items.silicon, new BasicBulletType(3f, 36){{
                        width = 7f;
                        height = 9f;
                        homingPower = 0.2f;
                        reloadMultiplier = 1.5f;
                        ammoMultiplier = 5;
                        lifetime = 60f;

                        trailLength = 5;
                        trailWidth = 1.5f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.siliconAmmoBack;
                        frontColor = Pal.siliconAmmoFront;
                    }}
            );

            shoot = new ShootAlternate(3.5f);

            recoils = 2;

            size = 3;
            targetAir = false;
            shootSound = Sounds.shootRipple;
            recoil = 0.5f;
            shootY = 3f;
            reload = 20f;
            range = 160;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 800;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            coolantMultiplier = 10f;
            researchCostMultiplier = 0.05f;
            depositCooldown = 2.0f;

            limitRange(5f);
        }};

        Rody_neutron_centrifuge = new Separator("Rody_neutron_centrifuge"){{
            size = 3;
            requirements(Category.crafting, with(Items.silicon, 30, Items.lead, 25, items.Feces, 30));
            hasItems = true;
            itemCapacity = 100;

            results = with(
                    items.Rody_neutron, 3,
                    Items.scrap, 2
            );
            hasPower = true;
            craftTime = 60f;
            researchCost = with(items.Feces, 30, Items.lead, 30, Items.silicon, 30);

            consumeItem(items.Feces, 10);
            consumePower(1.5f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawRegion("-spinner", 5, true), new DrawDefault());
        }};
        fecespress = new GenericCrafter("fecespress"){{
            requirements(Category.crafting, with(items.Feces, 45, Items.lead, 20));

            craftEffect = Fx.muddy;
            outputItem = new ItemStack(items.Constipated_feces, 1);
            craftTime = 200f;
            itemCapacity = 10;
            size = 2;
            hasItems = true;
            researchCost = with(items.Feces, 10, Items.lead, 5);

            consumeItem(items.Feces, 3);
        }};

        fecesdrill = new Drill("fecesdrill"){{
            requirements(Category.production, with(items.Feces, 10));
            tier = 2;
            drillTime = 600;
            size = 2;
            //mechanical drill doesn't work in space
            envEnabled ^= Env.space;
            researchCost = with(items.Feces, 10);

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};

        fecesjunction = new Junction("fecesjunction"){{
            requirements(Category.distribution, with(items.Feces, 2));
            buildCostMultiplier = 2f;
            capacity = 1;
            speed = 1;
            researchCost = with(items.Feces, 5);
        }};
        fecesconveyor = new Conveyor("fecesconveyor"){{
            requirements(Category.distribution, with(items.Feces, 1));
            health = 40;
            speed = 0.05f;
            displayedSpeed = 7f;
            buildCostMultiplier = 2f;
            junctionReplacement = blocks.fecesjunction;
            researchCost = with(items.Feces, 5);
        }};
        fecesrouter = new Router("fecesrouter"){{
            requirements(Category.distribution, with(items.Feces, 3));
            buildCostMultiplier = 2f;
            researchCost = with(items.Feces, 5);
            alwaysUnlocked = false;
            speed = 2f;
        }};
        fecesbridgeconveyor = new BufferedItemBridge("fecesbridge-conveyor"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 4));
            fadeIn = moveArrows = false;
            range = 6;
            speed = 90f;
            arrowSpacing = 8f;
            bufferCapacity = 3;
            researchCost = with(items.Feces, 5, Items.lead, 5);
            alwaysUnlocked = false;
            crushFragile = true;
        }};
        fecessorter = new Sorter("fecessorter"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            buildCostMultiplier = 1f;
            researchCost = with(items.Feces, 5, Items.lead, 5);
            alwaysUnlocked = false;
        }};
        fecesinvertedSorter = new Sorter("fecesinverted-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            buildCostMultiplier = 1f;
            researchCost = with(items.Feces, 5, Items.lead, 5);
            alwaysUnlocked = false;
            invert = true;
        }};
        fecesoverflowGate = new OverflowGate("fecesoverflow-gate"){{
                requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            researchCost = with(items.Feces, 5, Items.lead, 5);
            alwaysUnlocked = false;
            buildCostMultiplier = 1f;
        }};
        fecesunderflowGate = new OverflowGate("fecesunderflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, items.Feces, 2));
            buildCostMultiplier = 1f;
            researchCost = with(items.Feces, 5, Items.lead, 5);
            alwaysUnlocked = false;
            invert = true;
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

            drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion());
        }};
        //地板
        groundfeces = new OreBlock(items.Feces){{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
        }};
    }

    }

