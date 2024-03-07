package cn.com.twoke.game.tank.entity.tank;

import cn.com.twoke.game.tank.components.TankComponent;
import cn.com.twoke.game.tank.entity.GameEntity;

import java.awt.*;

public interface TankEntity{

    TankType getType();

    TankLevel getLevel();

    Image getCurrentFrame(int index);

    void setComponent(TankComponent tankComponent);

    float getMoveSpeed();
}
