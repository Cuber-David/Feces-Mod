package K.content.unit.others.jujutsu;

import arc.math.Mathf;
import mindustry.gen.*;

public class DomainInfUnit extends UnitEntity {
    private static final float INNER_RADIUS_FACTOR = 0.6f;

    // 核心：设置特殊碰撞层，与所有默认层（地面=0，空中=2）都不匹配
    @Override
    public int collisionLayer() {
        return 3; // 任意非0/2的值，使该单位与所有正常单位不碰撞
    }

    @Override
    public boolean collides(Hitboxc other) {
        // 对单位返回 false（双重保险）
        if (other instanceof Unit) {
            return false;
        }
        // 子弹处理：内部无敌
        if (other instanceof Bullet) {
            float dist = Mathf.dst(this.x, this.y, other.getX(), other.getY());
            return !(dist < this.hitSize * INNER_RADIUS_FACTOR);
        }
        return super.collides(other);
    }

    // 物理半径归零（额外保险）
    @Override
    public float physicSize() {
        return 0f;
    }

}