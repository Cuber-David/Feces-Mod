package K.content.extend;

import arc.math.Mathf;
import arc.util.Nullable;
import mindustry.entities.pattern.ShootHelix;

public class SalvoShoot extends ShootHelix {
    public boolean flip = false;
    public int Delay2 = 10;
    public float dx = 0;

    @Override
    public void flip(){
    }

    @Override
    public void shoot(int totalShots, BulletHandler handler, @Nullable Runnable barrelIncrementer) {
        for (int i = -1;i<2;i++) {
            handler.shoot(i*dx, 0, 0, firstShotDelay + shotDelay);
            handler.shoot(i*dx, 0, 0, firstShotDelay + shotDelay + Delay2);
            handler.shoot(i*dx, 0, 0, firstShotDelay + shotDelay + Delay2 * 2);
        }
    }
}
