package K.content;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;

import static mindustry.type.ItemStack.with;

public class blocks {
    public static Block
            //牢底
            Rody_neutron_capture_agent;

    public static void load(){
        Rody_neutron_capture_agent = new GenericCrafter("Rody_neutron_capture_agent"){{
            requirements(Category.crafting, with(Items.titanium, 125, Items.silicon, 80, Items.lead, 80));

            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(items.Rody_neutron, 1);
            craftTime = 60f;
            size = 3;
            hasItems = true;

            consumeItem(items.Feces, 1);
            alwaysUnlocked = false;
        }};
    }
}
