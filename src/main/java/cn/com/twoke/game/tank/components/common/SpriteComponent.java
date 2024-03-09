package cn.com.twoke.game.tank.components.common;


import cn.com.twoke.game.tank.components.Component;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.util.AssetPool;
import cn.com.twoke.game.tank.util.ImageUtil;
import cn.com.twoke.game.tank.util.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteComponent extends Component {

    public BufferedImage spriteImage;

    public SpriteComponent(String spriteImagePath) {
        super();
        spriteImage = AssetPool.loadTexture(spriteImagePath);
    }

    @Override
    public void setEntity(GameEntity entity) {
        super.setEntity(entity);
        entity.getTransform().setSize(spriteImage.getWidth(), spriteImage.getHeight());
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(ImageUtil.rotateImage(spriteImage, entity.getTransform().getRotate()), (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height, null);
    }

    @Override
    public void update(float dt) {

    }
}
