package cn.com.twoke.game.tank.entity.tank;

import cn.com.twoke.game.tank.components.TankComponent;
import cn.com.twoke.game.tank.entity.GameEntity;

import java.awt.*;

public abstract class AbstractTankEntity implements TankEntity {
    protected TankComponent component;
    protected TankType type;
    protected TankLevel level;

    @Override
    public TankType getType() {
        return type;
    }

    @Override
    public TankLevel getLevel() {
        return level;
    }


    @Override
    public void setComponent(TankComponent tankComponent) {
        this.component = tankComponent;
    }
}
