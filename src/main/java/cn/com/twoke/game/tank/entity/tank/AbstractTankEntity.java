package cn.com.twoke.game.tank.entity.tank;

import cn.com.twoke.game.tank.components.tank.TankComponent;

public abstract class AbstractTankEntity implements TankEntity {
    protected TankComponent component;
    protected TankType type;
    protected TankLevel level;
    protected float movingSpeed = 0.5f;

    @Override
    public TankType getType() {
        return type;
    }

    @Override
    public TankLevel getLevel() {
        return level;
    }

    @Override
    public float getMoveSpeed() {
        return movingSpeed;
    }

    @Override
    public void setComponent(TankComponent tankComponent) {
        this.component = tankComponent;
    }
}
