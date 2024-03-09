package cn.com.twoke.game.tank.entity.bullet;

import cn.com.twoke.game.tank.components.bullet.BulletComponent;
import cn.com.twoke.game.tank.components.tank.TankComponent;

public class BulletEntityBuilder {
    private final TankComponent component;
    public BulletEntityBuilder(TankComponent component) {
        this.component = component;
    }

    public BulletComponent createBullet() {
        return new BulletComponent(component);
    }
}
