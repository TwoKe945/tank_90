package cn.com.twoke.game.tank.scenes;

import cn.com.twoke.game.tank.components.GridPlaygroundComponent;
import cn.com.twoke.game.tank.components.KeyCodeComponent;
import cn.com.twoke.game.tank.config.Settings;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.entity.Transform;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelScene extends Scene {

    public LevelScene() {
        GameEntity playground = new GameEntity("Playground", new Transform(
                new Point(Settings.PLAYGROUND_MARGIN_LEFT, Settings.PLAYGROUND_MARGIN_TOP),
                new Dimension(Settings.PLAYGROUND_WIDTH, Settings.PLAYGROUND_HEIGHT)));
        playground.add(new GridPlaygroundComponent());
        addToScene(playground);

        GameEntity levelScene = new GameEntity("LevelScene", new Transform(
                new Point(0, 0),
                new Dimension(Settings.WIDTH, Settings.HEIGHT)));
        levelScene.add(new KeyCodeComponent().onClick(KeyEvent.VK_ESCAPE, (e, entity) -> {
            game.changeScene(0);
        }));
        addToScene(levelScene);
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
