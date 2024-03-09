package cn.com.twoke.game.tank.util;

import cn.com.twoke.game.tank.config.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, BufferedImage> IMAGE_POOL = new HashMap<>();

    public static final BufferedImage TILE_SPRITE_IMG;
    public static final int SPRITE_SIZE = 5;

    public static final Font FONT_1 = new Font("微软雅黑", Font.BOLD, 40);

    static {
        TILE_SPRITE_IMG = AssetPool.loadTexture(Constant.TEXTURE_MAP_SPRITE);
    }

    public static BufferedImage loadTexture(String texturePath) {
        if (IMAGE_POOL.containsKey(texturePath)) {
            return IMAGE_POOL.get(texturePath);
        }
        BufferedImage textureImage = ResourceLoader.loadImage(texturePath);
        IMAGE_POOL.put(texturePath, textureImage);
        return textureImage;
    }
}
