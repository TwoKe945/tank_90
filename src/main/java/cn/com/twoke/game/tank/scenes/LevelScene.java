package cn.com.twoke.game.tank.scenes;

import cn.com.twoke.game.tank.components.Component;
import cn.com.twoke.game.tank.components.common.GridPlaygroundComponent;
import cn.com.twoke.game.tank.components.common.TileComponent;
import cn.com.twoke.game.tank.components.input.KeyCodeComponent;
import cn.com.twoke.game.tank.components.tank.TankComponent;
import cn.com.twoke.game.tank.config.Settings;
import cn.com.twoke.game.tank.config.Dir;
import cn.com.twoke.game.tank.config.tank.PlayerType;
import cn.com.twoke.game.tank.entity.GameObjectType;
import cn.com.twoke.game.tank.entity.GameType;
import cn.com.twoke.game.tank.entity.tank.EnemyTank;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.entity.Transform;
import cn.com.twoke.game.tank.entity.tank.PlayerTank;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class LevelScene extends Scene {
    List<TankComponent> tanks;

    public LevelScene() {
        tanks = new ArrayList<>();
        GameEntity playground = new GameEntity("Playground", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT, Settings.PLAYGROUND_MARGIN_TOP),
                new Dimension(Settings.PLAYGROUND_WIDTH, Settings.PLAYGROUND_HEIGHT)));
        addToScene(playground);

        GameEntity levelScene = new GameEntity("LevelScene", new Transform(
                new Vec2f(0, 0),
                new Dimension(Settings.WIDTH, Settings.HEIGHT)));
        levelScene.add(new KeyCodeComponent().onClick(KeyEvent.VK_ESCAPE, (e, entity) -> {
            game.changeScene(0);
        }));
        addToScene(levelScene);


        Random random = new Random();
        for (int i = 0; i < 1; i++) {
            GameEntity entity = new GameEntity("EnemyTank" + 0, new Transform(
                    new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + random.nextInt(Settings.PLAYGROUND_WIDTH - 28),
                            Settings.PLAYGROUND_MARGIN_TOP + random.nextInt(Settings.PLAYGROUND_HEIGHT - 28) ),
                    new Dimension(28, 28)
            ), 1);
            TankComponent tankComponent = new TankComponent(new EnemyTank());
            entity.add(tankComponent);
            addToScene(entity);
        }

        // 玩家1
        GameEntity playerTank = new GameEntity("Player1",
                new Transform(
                        new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT,Settings.PLAYGROUND_MARGIN_TOP),
                        new Dimension(28, 28)
                ), 1);
        playerTank.add(new TankComponent(new PlayerTank(PlayerType.PLAYER_1)));
        playerTank.add(createPlayer1KeyCodeComponent(playerTank));
        addToScene(playerTank);
    }

    private Component createPlayer1KeyCodeComponent(GameEntity playerTank) {
        TankComponent tankComponent = playerTank.get(TankComponent.class);
        return new KeyCodeComponent()
          .onClick(KeyEvent.VK_W, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.UP)))
          .onClick(KeyEvent.VK_S, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.DOWN)))
          .onClick(KeyEvent.VK_A, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.LEFT)))
          .onClick(KeyEvent.VK_D, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.RIGHT)))
          .onClick(KeyEvent.VK_SPACE, (e, entity) -> {
                if (tankComponent.bulletCountNumber() == 0) {
                    tankComponent.fire();
                }

          });
    }

    private KeyCodeComponent.KeyCodeHandler createHandler(TankComponent tankComponent, Runnable runnable) {

        return new KeyCodeComponent.KeyCodeHandler() {
            @Override
            public void handle(KeyEvent e, GameEntity entity) {
                tankComponent.setMoving(false);
            }

            @Override
            public void pressed(KeyEvent e, GameEntity entity) {
                runnable.run();
                tankComponent.setMoving(true);
            }
        };
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
        GameEntity tileItem;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 0) continue;
                tileItem = new GameEntity("tileItem" + x + "_" + y, new Transform(
                        new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                                Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT),
                        new Dimension( grid[y][x] == 6 ? Settings.TILE_WIDTH * 2 : Settings.TILE_WIDTH,
                                grid[y][x] == 6 ? Settings.TILE_HEIGHT * 2 : Settings.TILE_HEIGHT)),
                        grid[y][x] == 5 ? 2: 0,
                        getTileType(grid[y][x])
                );
                tileItem.add(TileComponent.create(grid[y][x]));
                addToScene(tileItem);
            }
        }
    }

    private GameObjectType[] getTileType(int type) {
        switch (type) {
            case 1:
                return new GameObjectType[] {GameType.TILE, GameType.BRICK , GameType.FIRE_BRICK};
            case 2:
                return new GameObjectType[] {GameType.TILE, GameType.WATER};
            case 3:
                return new GameObjectType[] {GameType.TILE, GameType.SNOW};
            case 4:
                return new GameObjectType[] {GameType.TILE,  GameType.BRICK ,GameType.CERAMIC_BRICK};
            case 5:
                return new GameObjectType[] {GameType.TILE, GameType.GRASS};
            case 6:
                return new GameObjectType[] {GameType.FLAG};
            default:
                return new GameObjectType[] {GameType.TILE};
        }

    }

    @Override
    protected void doRender(Graphics g) {
        g.setColor(new Color(0x7F7F7F));
        g.fillRect(0,0, Settings.WIDTH, Settings.HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(Settings.PLAYGROUND_MARGIN_LEFT,
                Settings.PLAYGROUND_MARGIN_TOP,
                Settings.PLAYGROUND_WIDTH,
                Settings.PLAYGROUND_HEIGHT);
    }

    private static final Scene levelScene = new LevelScene();

    public static Scene get() {
        return levelScene;
    }
}
