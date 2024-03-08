package cn.com.twoke.game.tank.entity.tank;

import cn.com.twoke.game.tank.components.tank.TankComponent;

import java.awt.*;

public interface TankEntity{

    TankType getType();

    TankLevel getLevel();

    Image getCurrentFrame(int index);

    void setComponent(TankComponent tankComponent);

    float getMoveSpeed();
}
