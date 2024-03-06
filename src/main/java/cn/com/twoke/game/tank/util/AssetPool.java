package cn.com.twoke.game.tank.util;

import cn.com.twoke.game.tank.config.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AssetPool {

    public static final BufferedImage TILE_SPRITE_IMG;
    public static final int SPRITE_SIZE = 5;

    public static final Font FONT_1 = new Font("微软雅黑", Font.BOLD, 40);

    static {
        TILE_SPRITE_IMG = ResourceLoader.loadImage(Constant.TEXTURE_MAP_SPRITE);
    }

}
