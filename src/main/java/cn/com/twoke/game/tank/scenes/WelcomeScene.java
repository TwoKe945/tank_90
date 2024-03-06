package cn.com.twoke.game.tank.scenes;

import cn.com.twoke.game.tank.components.MouseMotionComponent;
import cn.com.twoke.game.tank.components.TextButtonComponent;
import cn.com.twoke.game.tank.config.Settings;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.entity.Transform;
import cn.com.twoke.game.tank.util.AssetPool;
import cn.com.twoke.game.tank.util.ResourceLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class WelcomeScene extends Scene {

    private BufferedImage logoImage;
    private BufferedImage loadingImage;
    private final GameEntity startGameButton;
    private final GameEntity editLevelButton;

    public WelcomeScene() {
        logoImage = ResourceLoader.loadImage("/logo.png");
        loadingImage = ResourceLoader.loadImage("/tankLoading.png");

        startGameButton = new GameEntity("StartGameButton", new Transform(
                new Point(0,(int)(190 * Settings.SCALE) +  (int)(logoImage.getHeight() * Settings.SCALE)
                                +  (int)(loadingImage.getHeight() * Settings.SCALE)),
                new Dimension(Settings.WIDTH, 60)
        ));
        TextButtonComponent component = new TextButtonComponent("开始游戏");
        component.setFont(AssetPool.FONT_1);
        component.setColor(Color.WHITE);
        startGameButton.add(component);
        addToScene(startGameButton);

        editLevelButton = new GameEntity("EditLevelButton", new Transform(
                new Point(0,(int)(250 * Settings.SCALE) +  (int)(logoImage.getHeight() * Settings.SCALE)
                        +  (int)(loadingImage.getHeight() * Settings.SCALE)),
                new Dimension(Settings.WIDTH, 60)
        ));
        component = new TextButtonComponent("编辑关卡");
        component.setFont(AssetPool.FONT_1);
        component.setColor(Color.WHITE);
        editLevelButton.add(component);
        editLevelButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, (e, entity) -> {
            game.changeScene(2);
        }));
        addToScene(editLevelButton);
    }

    private static final Scene welcomeScene = new WelcomeScene();

    public static Scene get() {
        return welcomeScene;
    }


    @Override
    protected void doRender(Graphics g) {
        g.setColor(new Color(0x000));
        g.fillRect(0,0, Settings.WIDTH, Settings.HEIGHT);

        g.drawImage(logoImage,  (Settings.WIDTH - (int)(logoImage.getWidth() * Settings.SCALE)) / 2 ,(int)(100 * Settings.SCALE),
                (int)(logoImage.getWidth() * Settings.SCALE),
                (int)(logoImage.getHeight() * Settings.SCALE), null);

        g.drawImage(loadingImage,  (Settings.WIDTH - (int)(loadingImage.getWidth() * Settings.SCALE)) / 2 ,
                (int)(150 * Settings.SCALE) +  (int)(logoImage.getHeight() * Settings.SCALE),
                (int)(loadingImage.getWidth() * Settings.SCALE),
                (int)(loadingImage.getHeight() * Settings.SCALE), null);
    }



}
