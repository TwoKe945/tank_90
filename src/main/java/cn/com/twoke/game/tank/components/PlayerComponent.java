package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.config.EnemyLevel;
import cn.com.twoke.game.tank.config.EnemyType;

public class PlayerComponent extends TankComponent  {


    public PlayerComponent(int[][] grid, EnemyLevel level, EnemyType type) {
        super(grid, level, type);
    }
}
