package cn.com.twoke.game.tank.components.common;

import cn.com.twoke.game.tank.components.Component;
import cn.com.twoke.game.tank.config.Constant;
import cn.com.twoke.game.tank.config.Settings;
import cn.com.twoke.game.tank.util.AssetPool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileComponent extends Component {
    private final int type;
    private final BufferedImage tileImage;

    public int getType() {
        return type;
    }

    public TileComponent(int type) {
        this.type = type;
        if (type == 6) {
            tileImage = AssetPool.loadTexture(Constant.TEXTURE_MAP_FLAG);
        } else {
            tileImage = AssetPool.TILE_SPRITE_IMG.getSubimage( (type - 1) * Settings.DEFAULT_TILE_WIDTH,0,Settings.DEFAULT_TILE_WIDTH, Settings.DEFAULT_TILE_HEIGHT);
        }
    }

    public static TileComponent create(int index) {
        return new TileComponent(index);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(tileImage,
                (int) entity.getTransform().getPosition().x, (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height, null);
        debug(() -> {
            g.setColor(Color.red);
            g.drawRect((int) entity.getTransform().getPosition().x, (int)entity.getTransform().getPosition().y,
                    entity.getTransform().getSize().width,
                    entity.getTransform().getSize().height);
        });
    }

    public int getX() {
        return (int)(entity.getTransform().getPosition().x - Settings.PLAYGROUND_MARGIN_LEFT ) / Settings.TILE_WIDTH;
    }

    public int getY() {
        return (int)(entity.getTransform().getPosition().y - Settings.PLAYGROUND_MARGIN_TOP ) / Settings.TILE_HEIGHT;
    }

}
