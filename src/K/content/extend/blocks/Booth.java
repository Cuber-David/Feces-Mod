package K.content.extend.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Nullable;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.type.Item;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.storage.CoreBlock;

import static mindustry.Vars.itemSize;
import static mindustry.world.blocks.storage.StorageBlock.incinerateEffect;

public class Booth extends Conveyor {

    public Booth(String name) {
        super(name);
        breakable = alwaysReplace = unitMoveBreakable = false;
        solid = true;
    }
    public class BoothBuild extends Building{
        public  Item lastItem;
        public @Nullable Building next;
        public @Nullable Duct.DuctBuild nextc;
        public @Nullable Building linkedCore;



        public Color transparentColor = new Color(0.4f, 0.4f, 0.4f, 0.1f);

        @Override
        public boolean acceptItem(Building source, Item item){
            return linkedCore != null ? linkedCore.acceptItem(source, item) : items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public void handleItem(Building source, Item item){
            if(linkedCore != null){
                if(linkedCore.items.get(item) >= ((CoreBlock.CoreBuild)linkedCore).storageCapacity){
                    incinerateEffect(this, source);
                }
                ((CoreBlock.CoreBuild)linkedCore).noEffect = true;
                linkedCore.handleItem(source, item);
            }else{
                super.handleItem(source, item);
            }
        }

        protected void drawAt(float x, float y, int bits, float rotation, SliceMode slice){
            Draw.z(Layer.blockUnder);

            Draw.z(Layer.blockUnder + 0.2f);
            Draw.color(transparentColor);
            Draw.color();
        }

        @Override
        public void itemTaken(Item item){
            if(linkedCore != null){
                linkedCore.itemTaken(item);
            }
        }

        @Override
        public int getMaximumAccepted(Item item){
            return linkedCore != null ? linkedCore.getMaximumAccepted(item) : itemCapacity;
        }

        @Override
        public void drawSelect(){
            if(linkedCore != null){
                linkedCore.drawSelect();
            }
        }

        @Override
        public boolean allowDeposit(){
            return linkedCore != null || super.allowDeposit();
        }

        @Override
        public void updateTile(){
                this.lastItem = items.first();
        }

        @Override
        public void draw(){
            Draw.rect(this.block.region, this.x, this.y, this.drawrot());
            //draw item
            if(lastItem != null) {
                float size = itemSize * Mathf.lerp(Math.min((float) items.total() / itemCapacity, 1), 1f, 0.4f);
                Draw.rect(lastItem.fullIcon, x, y, size, size, 0);
            }
    }
    }
}
