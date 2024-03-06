package cn.com.twoke.game.tank.scenes;

import cn.com.twoke.game.tank.components.GridPlaygroundComponent;
import cn.com.twoke.game.tank.components.KeyCodeComponent;
import cn.com.twoke.game.tank.components.TankComponent;
import cn.com.twoke.game.tank.config.EnemyLevel;
import cn.com.twoke.game.tank.config.EnemyType;
import cn.com.twoke.game.tank.config.Settings;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.entity.Transform;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class LevelScene extends Scene {
    GridPlaygroundComponent gridPlaygroundComponent;

    List<TankComponent> tanks;
    public LevelScene() {
        tanks = new ArrayList<>();
        GameEntity playground = new GameEntity("Playground", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT, Settings.PLAYGROUND_MARGIN_TOP),
                new Dimension(Settings.PLAYGROUND_WIDTH, Settings.PLAYGROUND_HEIGHT)));
        gridPlaygroundComponent  = new GridPlaygroundComponent();
        playground.add(gridPlaygroundComponent);
        addToScene(playground);

        GameEntity levelScene = new GameEntity("LevelScene", new Transform(
                new Vec2f(0, 0),
                new Dimension(Settings.WIDTH, Settings.HEIGHT)));
        levelScene.add(new KeyCodeComponent().onClick(KeyEvent.VK_ESCAPE, (e, entity) -> {
            game.changeScene(0);
        }));
        addToScene(levelScene);





    }

    public boolean traversalFourPoints(float x, float y, Function<float[], Boolean> handler) {
        float tempX = x, tempY = y;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                tempX += 28;
            }
            if (i == 2) {
                tempY += 28;
            }
            if (i == 3) {
                tempX -= 28;
            }
            if (!handler.apply(new float[]{tempX, tempY})) {
                return false;
            }
        }
        return true;
    }

    public void setGrid(int[][] grid) {
        gridPlaygroundComponent.setGrid(grid);
        Random random = new Random();
        for (int i = 0; i < 20; ) {
            float x =  Settings.PLAYGROUND_MARGIN_LEFT + random.nextInt(Settings.PLAYGROUND_WIDTH - 28);
            float y =  Settings.PLAYGROUND_MARGIN_TOP + random.nextInt(Settings.PLAYGROUND_HEIGHT - 28);
            if (traversalFourPoints(x, y, this::check)) {
                GameEntity entity = new GameEntity("EnemyTank" + i, new Transform(
                        new Vec2f(x,y),
                        new Dimension(28, 28)
                ));
                TankComponent tankComponent = new TankComponent(gridPlaygroundComponent.getGridData(),
                        EnemyLevel.values()[random.nextInt(EnemyLevel.values().length)],
                        EnemyType.values()[random.nextInt(EnemyType.values().length)]);
                entity.add(tankComponent);
                addToScene(entity);
                i++;
            }
        }
    }

    private Boolean check(float[] point) {
        int tileX = (int) (point[0] - Settings.PLAYGROUND_MARGIN_LEFT) / Settings.TILE_WIDTH;
        int tileY = (int) (point[1] - Settings.PLAYGROUND_MARGIN_TOP) / Settings.TILE_HEIGHT;
        if (tileY >= Settings.PLAYGROUND_ROW ||
                tileX >= Settings.PLAYGROUND_COL ||
                tileX < 0 || tileY < 0
        ) return false;
        return gridPlaygroundComponent.getGridData()[tileY][tileX] != 1 && gridPlaygroundComponent.getGridData()[tileY][tileX] != 2;
    }

    @Override
    protected void doRender(Graphics g) {
        g.setColor(new Color(0x7F7F7F));
        g.fillRect(0,0, Settings.WIDTH, Settings.HEIGHT);
    }

    private static final Scene levelScene = new LevelScene();

    public static Scene get() {
        return levelScene;
    }
}
