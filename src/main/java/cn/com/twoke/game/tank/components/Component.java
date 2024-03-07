package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.entity.GameEntity;

import java.awt.*;

/**
 * 游戏组件
 */
public abstract class Component {

    protected GameEntity entity;

    public void setEntity(GameEntity entity) {
        this.entity = entity;
    }

    /**
     * 界面渲染
     */
    public void render(Graphics g) {}

    /**
     *
     * @param dt 每帧花费的时间
     */
    public void update(float dt) {}

    public GameEntity getEntity() {
        return entity;
    }
}
